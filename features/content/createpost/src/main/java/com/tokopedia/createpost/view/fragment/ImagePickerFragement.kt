package com.tokopedia.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.imagepicker_insta.activity.MainActivity
import com.tokopedia.imagepicker_insta.models.BundleData

class ImagePickerFragement: BaseCreatePostFragmentNew() {

    override fun fetchContentForm() {
        presenter.fetchContentForm(createPostModel.productIdList, createPostModel.authorType, createPostModel.postId)
    }
    companion object {
        private const val REQUEST_IMAGE_PICKER = 1234
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ImagePickerFragement()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(
            R.layout.fragment_image_picker, container, false)
    }

    private fun openImagePicker(title:String, subtitle:String, iconUrl:String){
        val intent = MainActivity.getIntent(requireContext(),
            title = title,
            subtitle = subtitle,
            toolbarIconUrl = iconUrl,
            applinkForBackNavigation = "",
            applinkForGalleryProceed = "",
            applinkToNavigateAfterMediaCapture = ""
        )
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initVar()
    }
    private fun initVar() {
        createPostModel = arguments?.getParcelable(CreatePostViewModel.TAG) ?: CreatePostViewModel()

    }


    override fun onSuccessGetContentForm(
        feedContentForm: FeedContentForm,
        isFromTemplateToken: Boolean,
    ) {
        val authors = feedContentForm.authors
        openImagePicker(
            getString(R.string.feed_content_post_sebagai),authors.first().name,
            authors.first().thumbnail)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_PICKER -> if (resultCode == Activity.RESULT_OK) {
                val mediaList = data?.extras?.getParcelableArrayList<Uri>(BundleData.URIS)

                activityListener?.launchProductTagFragment(mediaList)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                activity?.finish()

            }
        }
    }
}

