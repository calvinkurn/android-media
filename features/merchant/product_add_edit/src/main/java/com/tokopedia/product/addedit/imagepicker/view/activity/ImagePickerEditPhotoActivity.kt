package com.tokopedia.product.addedit.imagepicker.view.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking.trackEditBack
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking.trackEditContinue
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking.trackScreen
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking.trackBack
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking.trackContinue
import com.tokopedia.user.session.UserSession
import java.io.File
import java.util.*

class ImagePickerEditPhotoActivity : ImageEditorActivity() {

    var userSession: UserSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        isEditProduct = intent.getBooleanExtra(IS_EDIT, false)
        isAddProduct = intent.getBooleanExtra(IS_ADD, false)

        val selectedImagePaths = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS)
        if (!checkImagePathsExist(selectedImagePaths)) {
            Toast.makeText(context, R.string.error_message_invalid_photos, Toast.LENGTH_LONG).show()
            finish()
        }

        super.onCreate(savedInstanceState)
        userSession = UserSession(context)

        // change the word selanjutnya to lanjut
        val tvDone = findViewById<TextView>(com.tokopedia.imagepicker.R.id.tv_done)
        tvDone.text = getString(R.string.action_continue)
    }

    override fun trackOpen() {
        if (isAddProduct || !isEditProduct) {
            trackScreen()
        }
    }

    override fun trackContinue() {
        if (isAddProduct || !isEditProduct) {
            trackEditContinue(userSession!!.shopId)
        } else {
            trackContinue(userSession!!.shopId)
        }
    }

    override fun trackBack() {
        if (isAddProduct || !isEditProduct) {
            trackEditBack(userSession!!.shopId)
        } else {
            trackBack(userSession!!.shopId)
        }
    }

    private fun checkImagePathsExist(selectedImagePaths: ArrayList<String>): Boolean {
        var imagePathsExist = true

        for (selectedImagePath in selectedImagePaths) {
            if (!selectedImagePath.startsWith(HTTP_PREFIX)) {
                val file = File(selectedImagePath)
                if (!file.exists()) {
                    imagePathsExist = false
                }
            }
        }

        return imagePathsExist
    }

    companion object {
        private var isEditProduct = false
        private var isAddProduct = false
        const val IS_EDIT = "is_edit"
        const val IS_ADD = "is_add"
    }
}