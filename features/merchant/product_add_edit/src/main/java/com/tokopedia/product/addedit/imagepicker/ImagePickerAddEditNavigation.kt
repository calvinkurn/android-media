package com.tokopedia.product.addedit.imagepicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.core.ImagePickerCallback
import com.tokopedia.imagepicker.core.ImagePickerGlobalSettings
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking
import com.tokopedia.user.session.UserSession

object ImagePickerAddEditNavigation {
    fun getIntent(context: Context, imageUrlOrPathList: List<String>, isAdding: Boolean): Intent {
        val intent = ImagePickerActivity.getIntent(context, createImagePickerBuilder(context, ArrayList(imageUrlOrPathList)))
        setUpTrackingForImagePicker(context, isAdding)
        return intent
    }

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(context: Context, selectedImagePathList: ArrayList<String>?): ImagePickerBuilder {

        val title = context.getString(R.string.action_pick_photo)

        val placeholderDrawableRes = arrayListOf(
                com.tokopedia.product.addedit.R.drawable.ic_utama,
                com.tokopedia.product.addedit.R.drawable.ic_depan,
                com.tokopedia.product.addedit.R.drawable.ic_samping,
                com.tokopedia.product.addedit.R.drawable.ic_atas,
                com.tokopedia.product.addedit.R.drawable.ic_detail
        )

        val imagePickerPickerTabTypeDef = intArrayOf(
                ImagePickerTabTypeDef.TYPE_GALLERY,
                ImagePickerTabTypeDef.TYPE_CAMERA
        )

        val imagePickerEditorBuilder = ImagePickerEditorBuilder.getDefaultBuilder().apply {
            this.belowMinResolutionErrorMessage = context.getString(R.string.error_image_under_x_resolution, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION)
            this.imageTooLargeErrorMessage = context.getString(R.string.error_image_too_large, (AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB / 1024))
            this.isRecheckSizeAfterResize = false
        }

        val imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                selectedImagePathList,
                placeholderDrawableRes,
                R.string.label_primary,
                AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS, false)

        return ImagePickerBuilder(
                title,
                imagePickerPickerTabTypeDef,
                GalleryType.IMAGE_ONLY,
                AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                imagePickerEditorBuilder,
                imagePickerMultipleSelectionBuilder)
    }

    private fun setUpTrackingForImagePicker(ctx: Context, isAdding: Boolean) {
        val bundle = Bundle().apply { putBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, isAdding) }
        ImagePickerGlobalSettings.onImagePickerOpen = ImagePickerCallback(ctx, bundle) { _, bnd ->
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddChooseImageTracking.trackScreen()
            }
        }

        ImagePickerGlobalSettings.onImagePickerBack = ImagePickerCallback(ctx) { ct, bnd ->
            val userSession = UserSession(ct)
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddChooseImageTracking.trackBack(userSession.shopId)
            } else {
                ProductEditChooseImageTracking.trackBack(userSession.shopId)
            }
        }
        ImagePickerGlobalSettings.onImageEditorOpen = ImagePickerCallback(ctx) { ct, bnd ->
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddEditImageTracking.trackScreen();
            }
        }
        ImagePickerGlobalSettings.onImageEditorBack = ImagePickerCallback(ctx) { ct, bnd ->
            val userSession = UserSession(ct)
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddEditImageTracking.trackEditBack(userSession.shopId);
            } else {
                ProductEditEditImageTracking.trackBack(userSession.shopId);
            }
        }
        ImagePickerGlobalSettings.onImageEditorContinue = ImagePickerCallback(ctx) { ct, bnd ->
            val userSession = UserSession(ct)
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddEditImageTracking.trackEditContinue(userSession.shopId);
            } else {
                ProductEditEditImageTracking.trackContinue(userSession.shopId);
            }
        }
    }
}