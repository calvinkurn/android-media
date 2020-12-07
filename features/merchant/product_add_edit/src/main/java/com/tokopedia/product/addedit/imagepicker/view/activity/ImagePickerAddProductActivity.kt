package com.tokopedia.product.addedit.imagepicker.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking.trackScreen
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking
import com.tokopedia.user.session.UserSession
import java.util.*

class ImagePickerAddProductActivity : ImagePickerActivity() {

    var userSession: UserSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        isEditProduct = intent.getBooleanExtra(IS_EDIT, false)
        isAddProduct = intent.getBooleanExtra(IS_ADD, false)
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

    override fun trackContinue() {}

    override fun trackBack() {
        if (isAddProduct || !isEditProduct) {
            ProductAddChooseImageTracking.trackBack(userSession!!.shopId)
        } else {
            ProductEditChooseImageTracking.trackBack(userSession!!.shopId)
        }
    }

    override fun getEditorIntent(selectedImagePaths: ArrayList<String>): Intent {
        if (isAddProduct || !isEditProduct) {
            ProductAddChooseImageTracking.trackContinue(userSession!!.shopId)
        } else {
            ProductEditChooseImageTracking.trackContinue(userSession!!.shopId)
        }

        val targetIntent = Intent(this, ImagePickerEditPhotoActivity::class.java)
        val origin = super.getEditorIntent(selectedImagePaths)

        targetIntent.putExtras(origin.extras!!)
        targetIntent.putExtra(IS_EDIT, isEditProduct)
        targetIntent.putExtra(IS_ADD, isAddProduct)

        return targetIntent
    }

    companion object {
        private var isEditProduct = false
        private var isAddProduct = false
        const val IS_EDIT = "is_edit"
        const val IS_ADD = "is_add"

        fun getIntent(context: Context?, imagePickerBuilder: ImagePickerBuilder?,
                      isEditProduct: Boolean, isAddProduct: Boolean): Intent {
            // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder)

            val intent = Intent(context, ImagePickerAddProductActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            intent.putExtra(IS_EDIT, isEditProduct)
            intent.putExtra(IS_ADD, isAddProduct)

            return intent
        }
    }
}