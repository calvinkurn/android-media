package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
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
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.window.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.fragment.ChatListInboxFragment
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomFlexModeListener
import com.tokopedia.topchat.chatsetting.view.activity.ChatSettingActivity
import com.tokopedia.topchat.chattemplate.view.customview.TopChatTemplateSeparatedView
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.util.ViewUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
open class TopChatRoomActivity : BaseChatToolbarActivity(), HasComponent<ChatComponent>,
    StickerFragment.Listener, TopChatRoomFlexModeListener {

    private var chatComponent: ChatComponent? = null

    private lateinit var windowInfoRepo: WindowInfoRepo
    private lateinit var chatRoomFragment: TopChatRoomFragment
    private lateinit var chatListFragment: ChatListInboxFragment

    private var constraintLayoutParent: ConstraintLayout? = null
    private var frameLayoutChatRoom: FrameLayout? = null
    private var frameLayoutChatList: FrameLayout? = null

    private var layoutUpdatesJob: Job? = null
    private var role: Int = RoleType.BUYER
    private var currentActiveChatId: String = ""
    private var displayState: Int = 0
    private var messageId: String = "0"
    private var chatTemplateSeparatedView: TopChatTemplateSeparatedView? = null
    private var toolbarChatList: Toolbar? = null

    override fun getScreenName(): String {
        return "/${TopChatAnalytics.Category.CHAT_DETAIL}"
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)

        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
            role = intent.getIntExtra(Constant.CHAT_USER_ROLE_KEY, RoleType.BUYER)
            currentActiveChatId = intent.getStringExtra(Constant.CHAT_CURRENT_ACTIVE_ID)?: ""
        }

        return createChatRoomFragment(bundle)
    }

    protected open fun createChatRoomFragment(bundle: Bundle): Fragment {
        return TopChatRoomFragment.createInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setupFoldableSupport(savedInstanceState)
        initWindowBackground()
        chatTemplateSeparatedView = findViewById(R.id.separated_chat_template)
        chatTemplateSeparatedView?.setupSeparatedChatTemplate(chatRoomFragment)

        //variable toolbar is chatroom's toolbar
        toolbarChatList = findViewById(R.id.toolbar_chatlist)
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

    override fun getTagFragment(): String = ""

    override fun getStickerViewHolderListener(): StickerViewHolder.Listener? {
        val fragment = getChatFragment(R.id.chatroom_fragment)
        if (fragment is StickerViewHolder.Listener) {
            return fragment
        }
        return null
    }

    private fun initWindowBackground() {
        val color = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        window.decorView.setBackgroundColor(color)
    }

    override fun setupToolbar() {
        //Do Nothing
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

                    val model = ChatRoomHeaderViewModel()
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
                }
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_BUYER) -> {
                    val toUserId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName =
                        it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage =
                        it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar =
                        it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderViewModel()
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
                }
                else -> {
                    if (messageId == ZER0_MESSAGE_ID) {
                        messageId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    }
                    intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
                }
            }
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_chat_room
    }

    override fun setupFragment(savedInstance: Bundle?) {
        //Do nothing
    }

    override fun onStart() {
        super.onStart()
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            windowInfoRepo.windowLayoutInfo()
                .collect { newLayoutInfo ->
                    changeLayout(newLayoutInfo)
                }
        }
    }

    override fun onStop() {
        super.onStop()
        layoutUpdatesJob?.cancel()
    }

    private fun setupFoldableSupport(savedInstanceState: Bundle?) {
        constraintLayoutParent = findViewById(R.id.cl_parent)
        frameLayoutChatRoom = findViewById(R.id.chatroom_fragment)
        frameLayoutChatList = findViewById(R.id.chatlist_fragment)
        windowInfoRepo = windowInfoRepository()
        setupFragments(savedInstanceState)
    }

    private fun setupFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            chatRoomFragment = newFragment as TopChatRoomFragment
            chatListFragment = ChatListInboxFragment.createFragment(role, currentActiveChatId)
        } else {
            chatRoomFragment = (getChatFragment(R.id.chatroom_fragment) as TopChatRoomFragment?)
                ?: newFragment as TopChatRoomFragment
            chatListFragment = (getChatFragment(R.id.chatlist_fragment) as ChatListInboxFragment?)
                ?: ChatListInboxFragment.createFragment(role, currentActiveChatId)
        }
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

    private fun attachChatRoomFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.chatroom_fragment, chatRoomFragment)
        ft.commitAllowingStateLoss()
        frameLayoutChatRoom?.show()
    }

    private fun changeLayout(windowLayoutInfo: WindowLayoutInfo) {
        saveDisplayState(windowLayoutInfo.displayFeatures)
        if (windowLayoutInfo.displayFeatures.isNotEmpty()) {
            ViewUtil.alignViewToDeviceFeatureBoundaries(
                resources, theme, window, windowLayoutInfo,
                constraintLayoutParent,
                R.id.chatlist_fragment, R.id.chatroom_fragment,
                R.id.toolbar, R.id.device_feature
            )
            attachChatListFragment()
            attachChatRoomFragment()
            chatRoomFragment.toggleTemplateChatWhenFlex(true)
        } else {
            setupToolbar()
            frameLayoutChatList?.hide()
            attachChatRoomFragment()
            chatTemplateSeparatedView?.hideSeparatedChatTemplate()
            chatRoomFragment.toggleTemplateChatWhenFlex(false)
        }
        setupToolbarWithFlex()
    }

    private fun saveDisplayState(displayFeatures: List<DisplayFeature>) {
        if (displayFeatures.isEmpty()) {
            displayState = EMPTY_STATE
        } else if (displayFeatures.first() is FoldingFeature) {
            val foldingFeature = displayFeatures.first() as FoldingFeature
            displayState = foldingFeature.state
        }
    }

    override fun onClickAnotherChat(msgId: String) {
        hideKeyboard()
        if (isFlexMode()) {
            messageId = msgId
            chatRoomFragment = newFragment as TopChatRoomFragment
            chatRoomFragment.chatRoomFlexModeListener = this
            chatTemplateSeparatedView?.setupSeparatedChatTemplate(chatRoomFragment)
            attachChatRoomFragment()
        }
    }

    override fun getSeparatedTemplateChat(): TopChatTemplateSeparatedView? {
        return chatTemplateSeparatedView
    }

    override fun isFlexMode(): Boolean {
        return displayState == FLAT_STATE || displayState == HALF_OPEN_STATE
    }

    private fun hideKeyboard() {
        try {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            chatTemplateSeparatedView?.hideSeparatedChatTemplate()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    private fun setupToolbarWithFlex() {
        if(isFlexMode()) {
            toolbarChatList?.show()
            setupToolbarFlexChatlist()
            setupToolbarFlexChatroom()
        } else {
            toolbarChatList?.hide()
            super.setupToolbar()
        }
        decreaseToolbarElevation()
    }

    private fun setupToolbarFlexChatroom() {
        val mInflater = LayoutInflater.from(this)
        val mCustomView = mInflater.inflate(getChatHeaderLayout(), null)
        toolbar.removeAllViews()
        toolbar.addView(mCustomView)
        toolbar.contentInsetStartWithNavigation = ViewUtil.convertToPx(16)
        toolbar.contentInsetEndWithActions = 0
    }

    private fun setupToolbarFlexChatlist() {
        setSupportActionBar(toolbarChatList)
        supportActionBar?.run {
            setBackgroundDrawable(
                ColorDrawable(
                    MethodChecker.getColor(
                        this@TopChatRoomActivity,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0)
                )
            )
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, com.tokopedia.chat_common.R.drawable.ic_action_back)
            if (upArrow != null) {
                upArrow.setColorFilter(
                    MethodChecker.getColor(this@TopChatRoomActivity, com.tokopedia.unifyprinciples.R.color.Unify_N500),
                    PorterDuff.Mode.SRC_ATOP
                )
                this.setHomeAsUpIndicator(upArrow)
            }
            this.title = "Chat"
            toolbar.contentInsetStartWithNavigation = 0
            toolbar.contentInsetEndWithActions = 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if(isFlexMode()) {
            menu?.clear()
            if (GlobalConfig.isSellerApp()) {
                menuInflater.inflate(R.menu.chat_options_menu_sellerapp, menu)
            } else {
                menuInflater.inflate(R.menu.chat_options_menu, menu)
            }
            menu?.findItem(R.id.menu_chat_search)?.isVisible = true
            menu?.findItem(R.id.menu_chat_filter)?.isVisible = true
            menu?.findItem(R.id.menu_chat_setting)?.isVisible = true
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(isFlexMode()) {
            return when (item.itemId) {
                R.id.menu_chat_filter -> {
                    true
                }
                R.id.menu_chat_setting -> {
                    val intent = ChatSettingActivity.getIntent(this, (role == RoleType.SELLER))
                    startActivity(intent)
                    true
                }
                R.id.menu_chat_search -> {
                    RouteManager.route(this, ApplinkConstInternalMarketplace.CHAT_SEARCH)
                    true
                }
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val REQUEST_CODE_CHAT_IMAGE = 2325
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"
        val TAG = TopChatRoomActivity::class.java.name

        private const val EMPTY_STATE = 0
        private const val FLAT_STATE = 1
        private const val HALF_OPEN_STATE = 2
        private const val ZER0_MESSAGE_ID = "0"
    }

}
