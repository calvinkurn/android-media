package com.tokopedia.createpost.view.activity

import android.app.Activity
import android.app.Application
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.view.service.SubmitPostServiceNew
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.domain.usecase.UploadMultipleImageUsecaseNew
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.view.fragment.ContentCreateCaptionFragment
import com.tokopedia.createpost.view.fragment.CreatePostPreviewFragmentNew
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker_insta.common.BundleData
import com.tokopedia.imagepicker_insta.common.ui.bottomsheet.FeedAccountTypeBottomSheet
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_create_post_new.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CreatePostActivityNew : BaseSimpleActivity(), CreateContentPostCommonListener {

    @Inject
    lateinit var createPostAnalytics: CreatePostAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var selectedFeedAccount: FeedAccountUiModel

    private val feedAccountBottomSheet: FeedAccountTypeBottomSheet by lazy(mode = LazyThreadSafetyMode.NONE) {
        val fragment = FeedAccountTypeBottomSheet.getFragment(supportFragmentManager, classLoader)
        fragment.setOnAccountClickListener(object : FeedAccountTypeBottomSheet.Listener {
            override fun onAccountClick(feedAccount: FeedAccountUiModel) {
                /** TODO: show confirmation popup first */
                selectedFeedAccount = feedAccount
                toolbar_common.apply {
                    subtitle = selectedFeedAccount.name
                    icon = selectedFeedAccount.iconUrl
                }
            }
        })
        fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        UploadMultipleImageUsecaseNew.mContext = applicationContext as Application?
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
        const val EXTRA_SELECTED_FEED_ACCOUNT = "EXTRA_SELECTED_FEED_ACCOUNT"

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
                if (uris.toString().endsWith(","))
                    (uris as CharSequence).subSequence(0, uris.length - 1)
                else
                    uris as CharSequence

            val list = (finalUri).split(",")
            val createPostViewModel = CreatePostViewModel()
            list.forEach { uri ->
                val type = if (isVideoFile(Uri.parse(uri))) MediaType.VIDEO else MediaType.IMAGE
                val mediaModel = MediaModel(path = uri, type = type)
                createPostViewModel.fileImageList.add(mediaModel)
            }
            intent.putExtra(CreatePostViewModel.TAG, createPostViewModel)
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)

        }
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
        setupToolbar()
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
            dialog.setTitle(getString(R.string.feed_content_dialog_title))
            dialog.setDescription(getString(R.string.feed_content_dialog_desc))
            dialog.setPrimaryCTAText(getString(R.string.feed_content_primary_cta_text))
            dialog.setSecondaryCTAText(getString(R.string.feed_content_sec_cta_text))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                createPostAnalytics.eventClickContinueOnConfirmationPopup()

            }
            dialog.setSecondaryCTAClickListener {
                createPostAnalytics.eventClickExitOnConfirmationPopup()
                dialog.dismiss()

                val intent = Intent().apply {
                    putExtra(EXTRA_SELECTED_FEED_ACCOUNT, selectedFeedAccount.type.value)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
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
        val createPostViewModel = (fragment as BaseCreatePostFragmentNew).getLatestCreatePostData()
        cacheManager.put(
            CreatePostViewModel.TAG,
            createPostViewModel,
            TimeUnit.DAYS.toMillis(7)
        )
        SubmitPostServiceNew.startService(applicationContext, cacheManager.id!!)
        goToFeed(createPostViewModel)
        finish()
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
        }
    }

    private fun setupToolbar() {
        val selectedFeedAccountTypeValue = intent.getIntExtra(EXTRA_SELECTED_FEED_ACCOUNT, FeedAccountUiModel.Type.BUYER.value)
        val selectedFeedAccountType = FeedAccountUiModel.getTypeByValue(selectedFeedAccountTypeValue)
        selectedFeedAccount = when(selectedFeedAccountType) {
            FeedAccountUiModel.Type.SELLER -> FeedAccountUiModel(
                name = userSession.shopName,
                iconUrl = userSession.shopAvatar,
                type = selectedFeedAccountType
            )
            FeedAccountUiModel.Type.BUYER -> FeedAccountUiModel(
                name = userSession.name,
                iconUrl = userSession.profilePicture,
                type = selectedFeedAccountType
            )
            else -> FeedAccountUiModel(
                name = "",
                iconUrl = "",
                type = selectedFeedAccountType
            )
        }


        toolbar_common.apply {
            icon = selectedFeedAccount.iconUrl
            title = getString(R.string.feed_content_post_sebagai)
            subtitle = selectedFeedAccount.name

            setOnBackClickListener {
                onBackPressed()
            }

            setOnAccountClickListener {
                feedAccountBottomSheet.show(supportFragmentManager)
            }

            visibility = View.VISIBLE
        }
        setSupportActionBar(toolbar_common)
    }
}