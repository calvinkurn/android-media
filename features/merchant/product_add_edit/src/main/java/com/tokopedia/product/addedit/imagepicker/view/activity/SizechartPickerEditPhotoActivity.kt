package com.tokopedia.product.addedit.imagepicker.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.product.addedit.tracking.ProductAddVariantTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantTracking
import com.tokopedia.user.session.UserSession

class SizechartPickerEditPhotoActivity : ImageEditorActivity() {
    var userSession: UserSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
    }

    override fun trackOpen() {
        // no-op
    }

    override fun trackContinue() {
        val shopId = userSession?.shopId.orEmpty()
        if (isEdit)
            ProductEditVariantTracking.pickSizeChartImage(shopId)
        else
            ProductAddVariantTracking.pickSizeChartImage(shopId)
    }

    override fun trackBack() {
        // no-op
    }

    companion object {
        private var isEdit = false
        const val EXTRA_IS_EDIT = "EXTRA_IS_EDIT"

        fun createIntent(context: Context?, uriOrPath: String?, isEdit: Boolean?): Intent {
            val imageEditorIntent = getIntent(context, uriOrPath,
                    null,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                    intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                    ImageEditActionTypeDef.ACTION_CONTRAST,
                    ImageEditActionTypeDef.ACTION_CROP,
                    ImageEditActionTypeDef.ACTION_ROTATE),
                    ImageRatioTypeDef.RATIO_1_1, false,
                    ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB.toLong(), null)
            imageEditorIntent.putExtra(EXTRA_IS_EDIT, isEdit)
            return imageEditorIntent
        }
    }
}