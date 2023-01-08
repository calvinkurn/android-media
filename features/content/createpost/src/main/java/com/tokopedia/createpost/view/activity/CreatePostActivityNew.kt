package com.tokopedia.createpost.view.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.SHOP_ID_PARAM
import com.tokopedia.createpost.common.USER_ID_PARAM
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.service.SubmitPostService
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.view.fragment.ContentCreateCaptionFragment
import com.tokopedia.createpost.view.fragment.CreatePostPreviewFragmentNew
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.ui.bottomsheet.ContentAccountTypeBottomSheet
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.toolbar.ContentAccountToolbar
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CreatePostActivityNew : BaseSimpleActivity(), CreateContentPostCommonListener {

    /** View */
    private lateinit var toolbarCommon: ContentAccountToolbar

    @Inject
    lateinit var createPostAnalytics: CreatePostAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var feedAccountAnalytic: FeedAccountTypeAnalytic

    var selectedContentAccount: ContentAccountUiModel = ContentAccountUiModel.Empty
    var isOpenFrom: String = ""

    protected val mFeedAccountList = mutableListOf<ContentAccountUiModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when(fragment) {
            is ContentAccountTypeBottomSheet -> {
                fragment.setData(mFeedAccountList)
                fragment.setAnalytic(feedAccountAnalytic)
                fragment.setOnAccountClickListener(object : ContentAccountTypeBottomSheet.Listener {
                    override fun onAccountClick(contentAccount: ContentAccountUiModel) {
                        if(contentAccount.type != selectedContentAccount.type && contentAccount.isShop) {
                            showSwitchAccountDialog(contentAccount)
                            return
                        }

                        changeSelectedFeedAccount(contentAccount)
                    }
                })
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun inflateFragment() {
        val newFragment = newFragment

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(parentViewResourceID, newFragment, tagFragment)
            .commit()
    }

    override fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String,
    ) {
    }

    override fun setContentAccountList(contentAccountList: List<ContentAccountUiModel>) {
        setupToolbar(contentAccountList)
    }

    override fun openProductTaggingPageOnPreviewMediaClick(position: Int) {
        KeyboardHandler.hideSoftKeyboard(this)
        (fragment as BaseCreatePostFragmentNew).getLatestCreatePostData().currentCorouselIndex =
            position
        isOpenedFromPreview = true
        intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)
        inflateFragment()
    }

    override fun clickProductTagBubbleAnalytics(mediaType: String, productId: String) {
        //DO nothing
    }

    override fun updateTaggingInfoInViewModel(feedXMediaTagging: FeedXMediaTagging) {
        //DO nothing
    }

    private fun isVideoFile(uri: Uri): Boolean {
        val mimetype = uri.getMimeType(this);
        return mimetype?.contains(MediaType.VIDEO) ?: false
    }

    private fun Uri.getMimeType(context: Context): String? {
        return when (scheme) {
            ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(this)
            else -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(toString()).toLowerCase(Locale.US)
            )
        }
    }

    private fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(applicationContext))
            .createPostModule(CreatePostModule(applicationContext)).build()
            .inject(this)
    }

    companion object {
        const val TYPE_CONTENT_TAGGING_PAGE = "content-tagging-page"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"
        const val PARAM_SHOW_PROGRESS_BAR = "show_posting_progress_bar"
        const val PARAM_IS_EDIT_STATE = "is_edit_state"
        const val PARAM_MEDIA_PREVIEW = "media_preview"
        const val EXTRA_SELECTED_FEED_ACCOUNT_ID = "EXTRA_SELECTED_FEED_ACCOUNT_ID"
        private const val DEFAULT_CACHE_DURATION = 7L

        const val PARAM_POST_ID = "post_id"
        const val PARAM_TYPE = "author_type"

        var isEditState: Boolean = false
        var isOpenedFromPreview: Boolean = false
        fun createIntent(
            context: Context,
            createPostViewModel: CreatePostViewModel,
            param_type: String
        ): Intent {
            isEditState = createPostViewModel.isEditState
            val intent = Intent(context, CreatePostActivityNew::class.java)
            intent.putExtra(PARAM_TYPE, param_type)
            intent.putExtra(CreatePostViewModel.TAG, createPostViewModel)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent.data?.let {
            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
        }
        if (intent.getStringExtra(PARAM_TYPE) == null) {
            val uris = bundle.get(BundleData.URIS)
            val finalUri =
                uris?.let {
                    if (it.toString().endsWith(","))
                        (it as CharSequence).subSequence(0, it.length - 1)
                    else
                        it as CharSequence
                }

            val list = (finalUri)?.split(",")
            val createPostViewModel = CreatePostViewModel()
            list?.let {
                it.forEach { uri ->
                    val type = if (isVideoFile(Uri.parse(uri))) MediaType.VIDEO else MediaType.IMAGE
                    val mediaModel = MediaModel(path = uri, type = type)
                    createPostViewModel.fileImageList.add(mediaModel)
                }
            }
            intent.putExtra(CreatePostViewModel.TAG, createPostViewModel)
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)

        }

        if(selectedContentAccount.id.isNotEmpty())
            intent.putExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID, selectedContentAccount.id)

        return when (intent.extras?.get(PARAM_TYPE)) {
            TYPE_CONTENT_TAGGING_PAGE -> CreatePostPreviewFragmentNew.createInstance(intent.extras
                ?: Bundle())
            TYPE_CONTENT_PREVIEW_PAGE -> ContentCreateCaptionFragment.createInstance(intent.extras
                ?: Bundle())
            else -> {
                return CreatePostPreviewFragmentNew.createInstance(intent.extras ?: Bundle())
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_create_post_new
    }

    override fun getParentViewResourceID(): Int {
        return R.id.content_parent_view
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        setupView()
    }

    override fun clickContinueOnTaggingPage(){
        createPostAnalytics.eventNextOnProductTaggingPage((fragment as BaseCreatePostFragmentNew).getLatestCreatePostData().completeImageList.size)
        intent.putExtra(PARAM_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
        inflateFragment()
    }

    override fun onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this)
        val model =  (intent.extras?.get(CreatePostViewModel.TAG) as CreatePostViewModel?)
        isEditState = model?.isEditState ?: false

        if (intent.extras?.get(PARAM_TYPE) == TYPE_CONTENT_PREVIEW_PAGE && isEditState) {
            createPostAnalytics.eventClickBackOnPreviewPage()
            finish()
        } else if (intent.extras?.get(PARAM_TYPE) == TYPE_CONTENT_TAGGING_PAGE && isOpenedFromPreview) {
            createPostAnalytics.eventClickBackOnProductTaggingPage()
            clickContinueOnTaggingPage()
            isOpenedFromPreview = false
        }
        else if (intent.extras?.get(PARAM_TYPE) == TYPE_CONTENT_TAGGING_PAGE) {

            createPostAnalytics.eventClickBackOnProductTaggingPage()

            val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(com.tokopedia.content.common.R.string.feed_content_dialog_title))
            dialog.setDescription(getString(com.tokopedia.content.common.R.string.feed_content_dialog_desc))
            dialog.setPrimaryCTAText(getString(com.tokopedia.content.common.R.string.feed_content_primary_cta_text))
            dialog.setSecondaryCTAText(getString(com.tokopedia.content.common.R.string.feed_content_sec_cta_text))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                createPostAnalytics.eventClickContinueOnConfirmationPopup()

            }
            dialog.setSecondaryCTAClickListener {
                createPostAnalytics.eventClickExitOnConfirmationPopup()
                dialog.dismiss()
                backWithActionResult()
            }
            dialog.show()
        } else {
            createPostAnalytics.eventClickBackOnPreviewPage()
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)
            inflateFragment()

        }

    }

    override fun postFeed() {
        createPostAnalytics.eventClickPostOnPreviewPage()
        KeyboardHandler.hideSoftKeyboard(this)
        val cacheManager = SaveInstanceCacheManager(this, true)
        val createPostViewModel = (fragment as? BaseCreatePostFragmentNew)?.getLatestCreatePostData()
        createPostViewModel?.authorType = selectedContentAccount.type
        cacheManager.put(
            CreatePostViewModel.TAG,
            createPostViewModel,
            TimeUnit.DAYS.toMillis(DEFAULT_CACHE_DURATION)
        )
        cacheManager.id?.let { draftId ->
            SubmitPostService.startService(applicationContext, draftId)
        }

        when (isOpenFrom) {
            BundleData.VALUE_IS_OPEN_FROM_USER_PROFILE -> goToUserProfile()
            BundleData.VALUE_IS_OPEN_FROM_SHOP_PAGE -> goToShopPage()
            else -> createPostViewModel?.let { goToFeed(it) }
        }
    }

    private fun goToFeed(createPostViewModel: CreatePostViewModel) {
        this.let {
            val applink = ApplinkConst.HOME_FEED
            val intent = RouteManager.getIntent(it, applink)
            intent.putExtra(PARAM_SHOW_PROGRESS_BAR, true)
            val isEditState = createPostViewModel.isEditState
            intent.putExtra(PARAM_IS_EDIT_STATE, isEditState)
            intent.putExtra(PARAM_MEDIA_PREVIEW,
                if (!isEditState) createPostViewModel.completeImageList.first().path else "")
            startActivity(intent)
            finish()
        }
    }

    private fun goToUserProfile() {
        val appLink = ApplinkConst.PROFILE_SUCCESS_POST.replace(USER_ID_PARAM, userSession.userId)
        val intent = RouteManager.getIntent(this, appLink)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun goToShopPage() {
        val appLink = ApplinkConst.SHOP.replace(SHOP_ID_PARAM, userSession.shopId)
        val intent = RouteManager.getIntent(this, appLink)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun setupView() {
        toolbarCommon = findViewById(R.id.toolbar_common)
    }

    private fun setupToolbar(contentAccountList: List<ContentAccountUiModel>) {
        mFeedAccountList.clear()
        mFeedAccountList.addAll(contentAccountList)

        val selectedFeedAccountId = intent.getStringExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID) ?: ""
        isOpenFrom = intent.extras?.getString(BundleData.KEY_IS_OPEN_FROM, "") ?: ""
        selectedContentAccount = if (mFeedAccountList.isEmpty()) ContentAccountUiModel.Empty
        else mFeedAccountList.firstOrNull { it.id == selectedFeedAccountId }
            ?: mFeedAccountList.first()

        val createPostViewModel = (intent?.extras?.get(CreatePostViewModel.TAG) as CreatePostViewModel?)
        createPostViewModel?.let {
               if (it.isEditState){
                   selectedContentAccount = findAccountByAuthorIdOfPost(mFeedAccountList, it.editAuthorId)
            }
        }

        toolbarCommon.apply {
            icon = selectedContentAccount.iconUrl
            title = getString(com.tokopedia.content.common.R.string.feed_content_post_sebagai)
            subtitle = selectedContentAccount.name
            createPostViewModel?.let {
                val isAllowSwitchAccount = mFeedAccountList.size > 1 && mFeedAccountList.find { acc -> acc.isUserPostEligible } != null

                if (!it.isEditState && isAllowSwitchAccount)
                    setOnAccountClickListener {
                        feedAccountAnalytic.clickAccountInfo()
                        ContentAccountTypeBottomSheet
                            .getFragment(supportFragmentManager, classLoader)
                            .show(supportFragmentManager)
                    }
                else setOnAccountClickListener(null)
            }


            setOnBackClickListener {
                onBackPressed()
            }

            visibility = View.VISIBLE
        }
        setSupportActionBar(toolbarCommon)
    }

    private fun findAccountByAuthorIdOfPost(
        contentAccountList: List<ContentAccountUiModel>,
        authorId: String
    ): ContentAccountUiModel {
        val acc = contentAccountList.find {
            it.id == authorId
        }
        return acc ?: ContentAccountUiModel.Empty
    }

    private fun backWithActionResult() {
        val intent = Intent().apply {
            putExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID, selectedContentAccount.id)
            putExtra(BundleData.KEY_IS_OPEN_FROM, isOpenFrom)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun showSwitchAccountDialog(contentAccount: ContentAccountUiModel) {
        DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.feed_cc_dialog_switch_account_buyer_to_seller_title))
            setDescription(getString(R.string.feed_cc_dialog_switch_account_buyer_to_seller_desc))
            setPrimaryCTAText(getString(R.string.feed_cc_dialog_switch_account_buyer_to_seller_primary_button))
            setSecondaryCTAText(getString(R.string.feed_cc_dialog_switch_account_buyer_to_seller_secondary_button))

            setPrimaryCTAClickListener { dismiss() }

            setSecondaryCTAClickListener {
                feedAccountAnalytic.clickChangeAccountToSeller()
                dismiss()
                when(intent.extras?.get(PARAM_TYPE)) {
                    TYPE_CONTENT_TAGGING_PAGE -> {
                        (fragment as CreatePostPreviewFragmentNew).deleteAllProducts()
                    }
                    TYPE_CONTENT_PREVIEW_PAGE -> {
                        (fragment as ContentCreateCaptionFragment).deleteAllProducts()
                    }
                }

                changeSelectedFeedAccount(contentAccount)
            }
        }.show()
    }

    private fun changeSelectedFeedAccount(contentAccount: ContentAccountUiModel) {
        selectedContentAccount = contentAccount

        toolbarCommon.apply {
            subtitle = selectedContentAccount.name
            icon = selectedContentAccount.iconUrl
        }
    }
}
