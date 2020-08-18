package com.tokopedia.product.addedit.imagepicker.view.activity

import android.os.Bundle
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.product.addedit.imagepicker.view.activity.VariantPhotoPickerActivity.Companion.EXTRA_IS_EDIT
import com.tokopedia.product.addedit.tracking.ProductAddVariantTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantTracking
import com.tokopedia.user.session.UserSession

class VariantPhotoEditorActivity : ImageEditorActivity() {

    private var userSession: UserSession? = null
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        if (intent.hasExtra(EXTRA_IS_EDIT)) {
            isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
        }
    }

    override fun trackContinue() {
        val shopId = userSession?.shopId ?: ""
        if (isEdit) ProductEditVariantTracking.pickProductVariantPhotos(shopId)
        else ProductAddVariantTracking.pickProductVariantPhotos(shopId)
    }
}