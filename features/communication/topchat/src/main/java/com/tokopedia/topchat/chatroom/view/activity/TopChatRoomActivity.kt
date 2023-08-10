package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.MODE_DEFAULT_GET_CHAT
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stories.common.StoriesAvatarView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.view.fragment.ChatListInboxFragment
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomFlexModeListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatRoomWebSocketViewModel
import com.tokopedia.topchat.chattemplate.view.customview.TopChatTemplateSeparatedView
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.topchat.common.util.ViewUtil.FLAT_STATE
import com.tokopedia.topchat.common.util.ViewUtil.HALF_OPEN_STATE
import com.tokopedia.topchat.common.util.ViewUtil.getFoldingFeatureState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

open class TopChatRoomActivity :
    BaseChatToolbarActivity(),
    HasComponent<ChatComponent>,
    StickerFragment.Listener,
    TopChatRoomFlexModeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TopChatRoomWebSocketViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TopChatRoomWebSocketViewModel::class.java]
    }

    private var chatComponent: ChatComponent? = null

    private lateinit var windowInfoRepo: WindowInfoTracker
    private lateinit var chatRoomFragment: TopChatRoomFragment
    private lateinit var chatListFragment: ChatListInboxFragment

    var remoteConfig: RemoteConfig? = null

    private var constraintLayoutParent: ConstraintLayout? = null
    private var frameLayoutChatRoom: FrameLayout? = null
    private var frameLayoutChatList: FrameLayout? = null
    private var chatRoomToolbarTitle: TextView? = null
    private var chatRoomToolbarLabel: TextView? = null
    private var chatRoomToolbarSubtitle: TextView? = null
    private var chatRoomToolbarAvatar: StoriesAvatarView? = null

    private var layoutUpdatesJob: Job? = null
    private var displayState: Int = 0

    /**
     * Flag for determine if the device is in flex mode
     * True if in flex mode and only for fold (book) device
     */
    private var currentlyInFlexMode: Boolean = false

    // messageId for chatroom fragment intent, replaced to applink's parameter when intent doesn't have the extra
    private var messageId: String = "0"

    private var chatTemplateSeparatedView: TopChatTemplateSeparatedView? = null
    private var toolbarChatList: Toolbar? = null
    private var chatSavedInstance: Bundle? = null

    override fun getScreenName(): String {
        return "/${TopChatAnalytics.Category.CHAT_DETAIL}"
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (!currentActiveChat.isNullOrEmpty() && isAllowedFlexMode()) {
            handleIntentChatRoomWithMessageId()
        }
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)

            if (role == null) {
                role = intent.getIntExtra(Constant.CHAT_USER_ROLE_KEY, RoleType.BUYER)
            }
            if (currentActiveChat == null && isAllowedFlexMode()) {
                currentActiveChat = intent.getStringExtra(Constant.CHAT_CURRENT_ACTIVE)
            } else {
                // open another chatroom, remove attachment & reset web socket
                bundle.putBoolean(Constant.CHAT_REMOVE_ATTACHMENT, true)
            }
        }

        return createChatRoomFragment(bundle)
    }

    protected open fun createChatRoomFragment(bundle: Bundle): Fragment {
        return TopChatRoomFragment.createInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        initInjector()
        bindViews()
        setupLifeCycleObserver()
        setupFragments(savedInstanceState)
        initWindowBackground()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getComponent(): ChatComponent {
        return chatComponent ?: initializeChatComponent()
    }

    protected open fun initializeChatComponent(): ChatComponent {
        return DaggerChatComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .chatRoomContextModule(ChatRoomContextModule(this))
            .build().also {
                chatComponent = it
            }
    }

    private fun setupLifeCycleObserver() {
        this.lifecycle.addObserver(viewModel)
    }

    override fun getTagFragment(): String = ""

    override fun getStickerViewHolderListener(): StickerViewHolder.Listener? {
        val fragment = getChatFragment(R.id.chatroom_fragment)
        if (fragment is StickerViewHolder.Listener) {
            return fragment
        }
        return null
    }

    private fun initWindowBackground() {
        val color = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        window.decorView.setBackgroundColor(color)
    }

    private fun decreaseToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val pathSegments = it.pathSegments
            when {
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_SELLER) -> {
                    val toShopId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName =
                        it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage =
                        it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar =
                        it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderUiModel()
                    model.name = shopName
                    model.label = LABEL_SELLER
                    model.senderId = toShopId
                    model.role = ROLE_SELLER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_SHOP_ID, toShopId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toShopId)
                    role = RoleType.BUYER
                }
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_BUYER) -> {
                    val toUserId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName = it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage = it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar = it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: SOURCE_ASK_BUYER
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderUiModel()
                    model.name = shopName
                    model.label = LABEL_USER
                    model.senderId = toUserId
                    model.role = ROLE_USER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_USER_ID, toUserId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toUserId)
                    role = RoleType.SELLER
                }
                else -> {
                    handleIntentChatRoomWithMessageId()
                }
            }
        }
    }

    private fun handleIntentChatRoomWithMessageId() {
        if (messageId == ZER0_MESSAGE_ID) {
            messageId = intent?.data?.lastPathSegment.toZeroStringIfNull()
        }
        intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun setupToolbar() {
        scanPathQuery(intent.data)
        clearToolbar()
        bindToolbarViews()
        setupDummyToolbar()
    }

    private fun clearToolbar() {
        val mInflater = LayoutInflater.from(this)
        val mCustomView = mInflater.inflate(getChatHeaderLayout(), null)
        toolbar.removeAllViews()
        toolbar.addView(mCustomView)
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.contentInsetEndWithActions = 0
    }

    private fun bindToolbarViews() {
        chatRoomToolbarTitle = findViewById(com.tokopedia.chat_common.R.id.title)
        chatRoomToolbarLabel = findViewById(com.tokopedia.chat_common.R.id.label)
        chatRoomToolbarSubtitle = findViewById(com.tokopedia.chat_common.R.id.subtitle)
        chatRoomToolbarAvatar = findViewById(com.tokopedia.chat_common.R.id.user_avatar)
    }

    private fun setupDummyToolbar() {
        intent.getParcelableExtra<ChatRoomHeaderUiModel>(ApplinkConst.Chat.PARAM_HEADER)?.let { header ->
            chatRoomToolbarAvatar?.setImageUrl(header.image)
            chatRoomToolbarTitle?.text = header.name
            chatRoomToolbarLabel?.hide()
            chatRoomToolbarSubtitle?.hide()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_chat_room
    }

    override fun setupFragment(savedInstance: Bundle?) {
        // Do nothing
    }

    override fun onStart() {
        super.onStart()
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                windowInfoRepo.windowLayoutInfo(this@TopChatRoomActivity)
                    .collect { newLayoutInfo ->
                        changeLayout(newLayoutInfo)
                    }
            } catch (throwable: Throwable) {
                if (throwable !is CancellationException) {
                    onErrorGetWindowLayoutInfo()
                }
            }
        }
    }

    private fun onErrorGetWindowLayoutInfo() {
        setupChatRoomOnlyToolbar()
        handleNonFlexModeView()
        currentlyInFlexMode = false
    }

    override fun onStop() {
        super.onStop()
        layoutUpdatesJob?.cancel()
    }

    private fun bindViews() {
        constraintLayoutParent = findViewById(R.id.cl_parent)
        frameLayoutChatRoom = findViewById(R.id.chatroom_fragment)
        frameLayoutChatList = findViewById(R.id.chatlist_fragment)
        chatTemplateSeparatedView = findViewById(R.id.separated_chat_template)

        // variable toolbar is chatroom's toolbar
        toolbarChatList = findViewById(R.id.toolbar_chatlist)

        windowInfoRepo = WindowInfoTracker.getOrCreate(this@TopChatRoomActivity)
    }

    private fun setupFragments(savedInstanceState: Bundle?) {
        if (isAllowedFlexMode()) {
            chatSavedInstance = savedInstanceState
            setupFlexModeFragments(savedInstanceState)
        } else {
            setupChatRoomOnlyToolbar()
            decreaseToolbarElevation()
            chatRoomFragment = newFragment as TopChatRoomFragment
            handleNonFlexModeView()
        }
    }

    private fun setupFlexModeFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            role = null
            currentActiveChat = null
        } else {
            messageId = currentActiveChat ?: ZER0_MESSAGE_ID
        }
        chatRoomFragment = newFragment as TopChatRoomFragment
        chatListFragment = ChatListInboxFragment.createFragment(role, currentActiveChat)
        chatTemplateSeparatedView?.setupSeparatedChatTemplate(chatRoomFragment)
        chatListFragment.chatRoomFlexModeListener = this
        chatRoomFragment.chatRoomFlexModeListener = this
    }

    private fun getChatFragment(@IdRes id: Int): Fragment? {
        return supportFragmentManager.findFragmentById(id)
    }

    private fun attachChatListFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.chatlist_fragment, chatListFragment)
        ft.commitAllowingStateLoss()
        frameLayoutChatList?.show()
    }

    private fun attachChatRoomFragment(isFromAnotherChat: Boolean = false) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (isFromAnotherChat) {
            chatRoomFragment.arguments?.putBoolean(IS_FROM_ANOTHER_CALL, isFromAnotherChat)
        }
        ft.replace(R.id.chatroom_fragment, chatRoomFragment)
        ft.commitAllowingStateLoss()
        frameLayoutChatRoom?.show()
    }

    private fun changeLayout(windowLayoutInfo: WindowLayoutInfo) {
        if (isAllowedFlexMode()) {
            val foldingFeature = getFoldingFeature(windowLayoutInfo.displayFeatures)
            displayState = getFoldingFeatureState(foldingFeature?.state)
            currentlyInFlexMode = if (windowLayoutInfo.displayFeatures.isNotEmpty() &&
                !isTableTop(foldingFeature)
            ) {
                val isSuccess = ViewUtil.alignViewToDeviceFeatureBoundaries(
                    windowLayoutInfo,
                    constraintLayoutParent,
                    R.id.chatlist_fragment,
                    R.id.chatroom_fragment,
                    R.id.toolbar,
                    R.id.device_feature
                )
                attachChatListFragment()
                attachChatRoomFragment()
                chatRoomFragment.toggleTemplateChatWhenFlex(true)
                isSuccess
            } else {
                handleNonFlexModeView()
                false
            }
            setupToolbarWithFlex()
        }
    }

    private fun handleNonFlexModeView() {
        frameLayoutChatList?.hide()
        attachChatRoomFragment()
        chatTemplateSeparatedView?.hide()
        chatRoomFragment.toggleTemplateChatWhenFlex(false)
    }

    private fun getFoldingFeature(displayFeatures: List<DisplayFeature>): FoldingFeature? {
        return if (displayFeatures.isNotEmpty() && displayFeatures.first() is FoldingFeature) {
            displayFeatures.first() as FoldingFeature
        } else {
            null
        }
    }

    override fun onClickAnotherChat(msg: ItemChatListPojo) {
        hideKeyboard()
        if (isFlexMode()) {
            messageId = msg.msgId
            currentActiveChat = msg.msgId
            chatRoomFragment = newFragment as TopChatRoomFragment
            chatRoomFragment.chatRoomFlexModeListener = this
            chatTemplateSeparatedView?.setupSeparatedChatTemplate(chatRoomFragment)
            attachChatRoomFragment(isFromAnotherChat = true)
        }
    }

    override fun onSuccessGetMessageId(msgId: String) {
        if (isFlexMode()) {
            messageId = msgId
            currentActiveChat = msgId
            checkPeriodicallyUntilListRendered(msgId)
        }
    }

    private fun checkPeriodicallyUntilListRendered(msgId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var tryCounter = 0
            while (!chatListFragment.stopTryingIndicator && tryCounter <= COUNTER_THRESHOLD) {
                if (!chatListFragment.adapter?.list.isNullOrEmpty() &&
                    chatListFragment.adapter?.activeChat != null
                ) {
                    try {
                        chatListFragment.setIndicatorCurrentActiveChat(msgId)
                        // to prevent: Cannot call this method while RecyclerView is computing a layout or scrolling
                    } catch (ignored: Exception) {
                        ignored.printStackTrace()
                    }
                }
                tryCounter++
                delay(DELAY)
            }
        }
    }

    override fun getSeparatedTemplateChat(): TopChatTemplateSeparatedView? {
        return chatTemplateSeparatedView
    }

    override fun isFlexMode(): Boolean {
        return (
            displayState == FLAT_STATE ||
                displayState == HALF_OPEN_STATE
            ) &&
            isAllowedFlexMode() && currentlyInFlexMode
    }

    private fun hideKeyboard() {
        try {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            chatTemplateSeparatedView?.hide()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    private fun setupToolbarWithFlex() {
        if (isFlexMode()) {
            setupDoubleToolbar()
        } else {
            setupChatRoomOnlyToolbar()
        }
        decreaseToolbarElevation()
    }

    private fun setupDoubleToolbar() {
        toolbarChatList?.show()
        setupToolbarFlexChatlist()
        setupToolbarFlexChatroom()
    }

    private fun setupChatRoomOnlyToolbar() {
        toolbarChatList?.hide()
        setSupportActionBar(toolbar)
        setupTopChatSupportActionBar()
    }

    private fun setupToolbarFlexChatroom() {
        toolbar.contentInsetStartWithNavigation = ViewUtil.convertToPx(SIXTEEN_DP)
        toolbar.contentInsetEndWithActions = 0
    }

    private fun setupToolbarFlexChatlist() {
        setSupportActionBar(toolbarChatList)
        setupTopChatSupportActionBar()
    }

    private fun setupTopChatSupportActionBar() {
        supportActionBar?.run {
            setBackgroundDrawable(
                ColorDrawable(
                    MethodChecker.getColor(
                        this@TopChatRoomActivity,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
            )
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, com.tokopedia.chat_common.R.drawable.ic_action_back)
            if (upArrow != null) {
                upArrow.setColorFilter(
                    MethodChecker.getColor(this@TopChatRoomActivity, com.tokopedia.unifyprinciples.R.color.Unify_NN600),
                    PorterDuff.Mode.SRC_ATOP
                )
                this.setHomeAsUpIndicator(upArrow)
            }
            this.title = TITLE_CHAT
            toolbar.contentInsetStartWithNavigation = 0
            toolbar.contentInsetEndWithActions = 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (isFlexMode()) {
            menu?.clear()
            if (GlobalConfig.isSellerApp()) {
                menuInflater.inflate(R.menu.chat_options_menu_sellerapp, menu)
            } else {
                menuInflater.inflate(R.menu.chat_options_menu, menu)
            }
            hideAndDisableDuplicateMenu(menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    private fun hideAndDisableDuplicateMenu(menu: Menu?) {
        menu?.findItem(R.id.menu_chat_search)?.isVisible = true
        menu?.findItem(R.id.menu_chat_filter)?.isVisible = false
        menu?.findItem(R.id.menu_chat_filter)?.isEnabled = false
        menu?.findItem(R.id.menu_chat_setting)?.isVisible = false
        menu?.findItem(R.id.menu_chat_setting)?.isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (isFlexMode()) {
            when (item.itemId) {
                R.id.menu_chat_search -> {
                    RouteManager.route(this, ApplinkConstInternalMarketplace.CHAT_SEARCH)
                    finish()
                    true
                }
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun isAllowedFlexMode(): Boolean {
        if (remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
        return remoteConfig?.getBoolean(Constant.TOPCHAT_ALLOWED_FLEX_MODE, true) ?: true
    }

    private fun isTableTop(foldFeature: FoldingFeature?) =
        foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL

    override fun onBackPressed() {
        if (::chatRoomFragment.isInitialized && chatRoomFragment.onBackPressed()) {
            return
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val SOURCE_ASK_BUYER = "tx_ask_buyer"
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"
        val TAG = TopChatRoomActivity::class.java.name
        private const val TITLE_CHAT = "Chat"

        private const val DELAY = 1000L
        private const val COUNTER_THRESHOLD = 10
        private const val ZER0_MESSAGE_ID = "0"
        private const val SIXTEEN_DP = 16
        private var role: Int? = null
        private var currentActiveChat: String? = null

        const val IS_FROM_ANOTHER_CALL = "is_from_another_chat"
    }
}
