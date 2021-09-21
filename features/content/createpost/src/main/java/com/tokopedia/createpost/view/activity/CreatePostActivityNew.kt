package com.tokopedia.createpost.view.activity

import android.app.Application
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
import com.tokopedia.createpost.analyics.CreatePostAnalytics
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.domain.usecase.UploadMultipleImageUsecaseNew
import com.tokopedia.createpost.view.fragment.ContentCreateCaptionFragment
import com.tokopedia.createpost.view.fragment.CreatePostPreviewFragmentNew
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.service.SubmitPostServiceNew
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.createpost.view.viewmodel.MediaModel
import com.tokopedia.createpost.view.viewmodel.MediaType
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.model.MimeType
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_create_post_new.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CreatePostActivityNew : BaseSimpleActivity(), CreateContentPostCOmmonLIstener {

    @Inject
    lateinit var createPostAnalytics: CreatePostAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
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

    override fun updateHeader(header: HeaderViewModel) {
        content_post_avatar.loadImageCircle(header.avatar)
        content_post_avatar.showWithCondition(header.avatar.isNotBlank())
        content_post_name.text = header.title
    }

    override fun openProductTagginPageOnPreviewMediaClick(position: Int) {
        (fragment as BaseCreatePostFragmentNew).getLatestCreatePostData().currentCorouselIndex =
            position
        intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)
        content_action_post_button?.text = getString(R.string.feed_content_text_lanjut)
        inflateFragment()
    }

    override fun clickProductTagBubbleAnalytics(mediaType: String, productId: String) {
        TODO("Not yet implemented")
    }

    private fun isVideoFile(uri: Uri): Boolean {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString()
        )
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.toLowerCase());
        val slashIndex = mimeType?.indexOf("/") ?: 0
        return mimeType?.substring(0, slashIndex) == MediaType.VIDEO
    }

    private fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostModule(CreatePostModule(applicationContext))
            .build()
            .inject(this)
    }

    companion object {
        const val TYPE_CONTENT_TAGGING_PAGE = "content-tagging-page"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"
        fun createIntent(
            context: Context,
            createPostViewModel: CreatePostViewModel,
            isCreatePostPage: Boolean,
            isFirstLaunch: Boolean,
        ): Intent {
            val intent = Intent(context, CreatePostActivityNew::class.java)

                intent.putExtra(PARAM_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
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
            val uris = bundle.get("ip_uris")
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

            content_action_post_button?.text = getString(R.string.feed_content_text_lanjut)
            if (!create_post_toolbar.isVisible)
                create_post_toolbar?.visibility = View.VISIBLE
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
        content_back_button.setOnClickListener {
            onBackPressed()
        }
        create_post_toolbar.visibility = View.VISIBLE

        content_action_post_button.setOnClickListener {
            if (content_action_post_button?.text == getString(R.string.feed_content_text_lanjut)) {
                createPostAnalytics.eventNextOnProductTaggingPage((fragment as BaseCreatePostFragmentNew).getLatestCreatePostData().completeImageList.size)
                intent.putExtra(PARAM_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
                content_action_post_button?.text = getString(R.string.feed_content_text_post)
                inflateFragment()
            } else if (content_action_post_button?.text == getString(R.string.feed_content_text_post)) {
                postFeed()
            }
        }

    }


    override fun onBackPressed() {

        if (intent.extras?.get(PARAM_TYPE) == TYPE_CONTENT_TAGGING_PAGE) {

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
                finish()
            }
            dialog.show()
        } else {
            createPostAnalytics.eventClickBackOnPreviewPage()
            supportFragmentManager.popBackStack()
            content_action_post_button?.text = getString(R.string.feed_content_text_lanjut)
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)

        }

    }

    private fun postFeed() {
        createPostAnalytics.eventClickPostOnPreviewPage()
        KeyboardHandler.hideSoftKeyboard(this)
        val cacheManager = SaveInstanceCacheManager(this, true)
        cacheManager.put(
            CreatePostViewModel.TAG,
            (fragment as BaseCreatePostFragmentNew).getLatestCreatePostData(),
            TimeUnit.DAYS.toMillis(7)
        )
        SubmitPostServiceNew.startService(applicationContext, cacheManager.id!!)
        goToFeed()
        finish()
    }

    private fun goToFeed() {
        this.let {
            val applink = ApplinkConst.HOME_FEED
            val intent = RouteManager.getIntent(it, applink)
            intent.putExtra("show_posting_progress_bar", true)
            startActivity(intent)
        }
    }
}