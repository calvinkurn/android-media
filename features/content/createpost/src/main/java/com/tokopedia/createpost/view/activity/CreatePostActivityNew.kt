package com.tokopedia.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.fragment.ContentCreateCaptionFragment
import com.tokopedia.createpost.view.fragment.CreatePostPreviewFragmentNew
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker_insta.activity.MainActivity
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.activity_create_post_new.*

class CreatePostActivityNew : BaseSimpleActivity(), CreateContentPostCOmmonLIstener  {

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

    override fun deleteItemFromProductTagList(position: Int) {
        TODO("Not yet implemented")
    }

    override fun updateHeader(header: HeaderViewModel) {
        content_post_avatar.loadImageCircle(header.avatar)
        content_post_avatar.showWithCondition(header.avatar.isNotBlank())

        content_post_name.text = header.title

    }

    companion object {
        const val TYPE_CONTENT_TAGGING_PAGE= "content-tagging-page"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"
        fun createIntent(context: Context, createPostViewModel: CreatePostViewModel, isCreatePostPage: Boolean): Intent {
            val intent = Intent(context, CreatePostActivityNew::class.java)
            if (isCreatePostPage)
                intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)
            else
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

        return when (intent.extras?.get(PARAM_TYPE)) {
            TYPE_CONTENT_TAGGING_PAGE -> CreatePostPreviewFragmentNew.createInstance(intent.extras ?: Bundle())
            TYPE_CONTENT_PREVIEW_PAGE -> ContentCreateCaptionFragment.createInstance(intent.extras ?: Bundle())
            else -> {
                finish()
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
        content_action_post_button.setOnClickListener {
            if (content_action_post_button?.text == getString(R.string.feed_content_text_lanjut)) {
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

            val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.feed_content_dialog_title))
            dialog.setDescription(getString(R.string.feed_content_dialog_desc))
            dialog.setPrimaryCTAText(getString(R.string.feed_content_primary_cta_text))
            dialog.setSecondaryCTAText(getString(R.string.feed_content_sec_cta_text))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()

            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
                finish()
            }
            dialog.show()
        } else {
            supportFragmentManager.popBackStack()
            content_action_post_button?.text = getString(R.string.feed_content_text_lanjut)
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_TAGGING_PAGE)

        }

    }
    private fun postFeed(){

    }

    private fun openImagePicker(title:String, subtitle:String, iconUrl:String){
        val intent = MainActivity.getIntent(this,
            title = title,
            subtitle = subtitle,
            toolbarIconUrl = iconUrl,
            applinkForBackNavigation = "",
            applinkForGalleryProceed = "",
            applinkToNavigateAfterMediaCapture = ""
        )
        startActivity(intent)
    }

}