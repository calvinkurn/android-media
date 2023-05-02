package com.tokopedia.play.broadcaster.util.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet

/**
 * Created by jegul on 22/06/20
 */
class CoverImagePickerHelper(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val pageSource: PlayBroPageSource,
    private val account: ContentAccountUiModel,
    private val listener: OnChosenListener,
    private val intentHandler: (Intent, Int) -> Unit
) : PlayCoverImageChooserBottomSheet.Listener, PlayGalleryImagePickerBottomSheet.Listener {

    private val cameraIntent = PlayCoverCameraActivity.getIntent(context, pageSource, account)

    private lateinit var coverImageChooserBottomSheet: PlayCoverImageChooserBottomSheet
    private lateinit var galleryImagePickerBottomSheet: PlayGalleryImagePickerBottomSheet

    override fun onChooseProductCover(bottomSheet: PlayCoverImageChooserBottomSheet, productId: String, imageUrl: String) {
        listener.onGetFromProduct(productId, imageUrl)
        bottomSheet.dismiss()
    }

    override fun onGetFromCamera(bottomSheet: PlayCoverImageChooserBottomSheet) {
        intentHandler(cameraIntent, REQUEST_CODE_CAMERA_CAPTURE)
    }

    override fun onChooseFromGalleryClicked(bottomSheet: PlayCoverImageChooserBottomSheet) {
        getGalleryImagePickerBottomSheet()
                .show(fragmentManager)
    }

    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let(listener::onGetFromGallery)
    }

    fun onAttachFragment(childFragment: Fragment) {
        when (childFragment) {
            is PlayGalleryImagePickerBottomSheet -> childFragment.mListener = this
            is PlayCoverImageChooserBottomSheet -> childFragment.mListener = this
        }
    }

    fun show(source: CoverSource = CoverSource.None) {
        when (source) {
            CoverSource.None, is CoverSource.Product -> {
                getPlayCoverImageChooserBottomSheet()
                        .show(fragmentManager)
            }
            CoverSource.Gallery -> {
                onChooseFromGalleryClicked(coverImageChooserBottomSheet)
            }
            CoverSource.Camera -> {
                onGetFromCamera(coverImageChooserBottomSheet)
            }
        }
    }

    fun dismiss() {
        getPlayCoverImageChooserBottomSheet().dismiss()
    }

    /**
     * Add this on onActivityResult() method to delegate the camera result to this class
     *
     * @return true means the result has been handled, false otherwise
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return if (requestCode == REQUEST_CODE_CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getParcelableExtra<Uri>(PlayCoverCameraActivity.EXTRA_IMAGE_URI)
            imageUri?.let(listener::onGetFromCamera)
            getPlayCoverImageChooserBottomSheet().dismiss()
            true
        } else false
    }

    private fun getPlayCoverImageChooserBottomSheet(): PlayCoverImageChooserBottomSheet {
        if (!::coverImageChooserBottomSheet.isInitialized) {
            val fragmentFactory = fragmentManager.fragmentFactory
            val coverChooser = fragmentFactory.instantiate(
                    context.classLoader,
                    PlayCoverImageChooserBottomSheet::class.java.name
            ) as PlayCoverImageChooserBottomSheet

            coverChooser.setShowListener { coverChooser.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

            coverImageChooserBottomSheet = coverChooser
        }

        return coverImageChooserBottomSheet
    }

    private fun getGalleryImagePickerBottomSheet(): PlayGalleryImagePickerBottomSheet {
        if (!::galleryImagePickerBottomSheet.isInitialized) {
            val fragmentFactory = fragmentManager.fragmentFactory
            val imagePicker = fragmentFactory.instantiate(
                    context.classLoader,
                    PlayGalleryImagePickerBottomSheet::class.java.name
            ) as PlayGalleryImagePickerBottomSheet

            galleryImagePickerBottomSheet = imagePicker
        }

        return galleryImagePickerBottomSheet
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_CAPTURE = 21232
    }

    interface OnChosenListener {

        fun onGetFromProduct(productId: String, imageUrl: String)
        fun onGetFromCamera(uri: Uri)
        fun onGetFromGallery(uri: Uri)
    }
}
