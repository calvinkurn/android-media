package com.tokopedia.product.addedit.imagepicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.common.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking
import com.tokopedia.user.session.UserSession


object ImagePickerAddEditNavigation {
    fun getIntent(context: Context, imageUrlOrPathList: List<String>, maxImageCount: Int, isAdding: Boolean): Intent {
        val builder = createImagePickerBuilder(context, ArrayList(imageUrlOrPathList), maxImageCount)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)

        setUpTrackingForImagePicker(context, isAdding)
        return intent
    }

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(context: Context, selectedImagePathList: ArrayList<String>?, maxImageCount: Int): ImagePickerBuilder {
        val listPlaceholderImage = List(maxImageCount) {
            R.drawable.product_add_edit_ic_image_placeholder
        }
        return ImagePickerBuilder.getSquareImageBuilder(context)
                .apply {
                    this.title = context.getString(R.string.action_pick_photo)
                    this.maxFileSizeInKB = AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB
                    this.imagePickerEditorBuilder = ImagePickerEditorBuilder.getSimpleEditBuilder().apply {
                        belowMinResolutionErrorMessage = context.getString(R.string.error_image_under_x_resolution, DEFAULT_MIN_RESOLUTION, DEFAULT_MIN_RESOLUTION)
                        imageTooLargeErrorMessage = context.getString(R.string.error_image_too_large, (AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB / 1024))
                        convertToWebp = true
                    }
                    this.imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                            usePrimaryImageString = true,
                            maximumNoPick = maxImageCount,
                            initialSelectedImagePathList = selectedImagePathList ?: arrayListOf(),
                            placeholderImagePathResList = ArrayList(listPlaceholderImage)
                    )
                }
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
                ProductAddEditImageTracking.trackEditBack(userSession.shopId)
            } else {
                ProductEditEditImageTracking.trackBack(userSession.shopId)
            }
        }
        ImagePickerGlobalSettings.onImageEditorContinue = ImagePickerCallback(ctx, bundle) { ct, bnd ->
            val userSession = UserSession(ct)
            if (bnd?.getBoolean(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, true) == true) {
                ProductAddEditImageTracking.trackEditContinue(userSession.shopId)
            } else {
                ProductEditEditImageTracking.trackContinue(userSession.shopId)
            }
        }
    }
}