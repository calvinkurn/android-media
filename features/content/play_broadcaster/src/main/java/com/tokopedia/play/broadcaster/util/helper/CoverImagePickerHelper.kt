package com.tokopedia.play.broadcaster.util.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet

/**
 * Created by jegul on 22/06/20
 */
class CoverImagePickerHelper(
        private val context: Context,
        private val fragmentManager: FragmentManager,
        private val listener: OnChosenListener,
        private val intentHandler: (Intent, Int) -> Unit
) : PlayCoverImageChooserBottomSheet.Listener, PlayGalleryImagePickerBottomSheet.Listener {

    private val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)

    private lateinit var coverImageChooserBottomSheet: PlayCoverImageChooserBottomSheet
    private lateinit var galleryImagePickerBottomSheet: PlayGalleryImagePickerBottomSheet

    override fun onChooseProductCover(bottomSheet: PlayCoverImageChooserBottomSheet, productId: Long, imageUrl: String) {
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

    fun show(source: CoverSourceEnum = CoverSourceEnum.NONE) {
        when (source) {
            CoverSourceEnum.NONE, CoverSourceEnum.PRODUCT -> {
                getPlayCoverImageChooserBottomSheet()
                        .show(fragmentManager)
            }
            CoverSourceEnum.GALLERY -> {
                onChooseFromGalleryClicked(coverImageChooserBottomSheet)
            }
            CoverSourceEnum.CAMERA -> {
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

            coverChooser.mListener = this
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

            imagePicker.mListener = this
            imagePicker.setShowListener { imagePicker.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

            galleryImagePickerBottomSheet = imagePicker
        }

        return galleryImagePickerBottomSheet
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_CAPTURE = 21232
    }

    interface OnChosenListener {

        fun onGetFromProduct(productId: Long, imageUrl: String)
        fun onGetFromCamera(uri: Uri)
        fun onGetFromGallery(uri: Uri)
    }
}