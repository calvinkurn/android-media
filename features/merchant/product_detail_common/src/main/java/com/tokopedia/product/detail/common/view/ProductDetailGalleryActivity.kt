package com.tokopedia.product.detail.common.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.image_gallery.ImageGallery
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.unifycomponents.ImageUnify

class ProductDetailGalleryActivity : AppCompatActivity() {

    private var data: ProductDetailGallery? = null

    private var imageGallery: ImageGallery? = null
    private var closeButton: ImageUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_gallery)

        imageGallery = findViewById(R.id.product_detail_gallery)
        closeButton = findViewById(R.id.product_detail_gallery_close_button)

        intent?.extras?.let {
            data = it.getParcelable(PRODUCT_DETAIL_GALLERY_DATA)
        }

        data?.let { processData(it) }
    }

    private fun processData(data: ProductDetailGallery) {
        val isAutoPlay = DeviceConnectionInfo.isConnectWifi(this)

        val selectedPosition = data.getSelectedPosition()

        val imageGallery = imageGallery ?: return

        imageGallery.apply {
            setImages(
                arrayDrawable = data.generateImageGalleryItems(isAutoPlay),
                defaultIndex = selectedPosition.takeIf { it > -1 } ?: 0
            )

            onOverlayHiddenChange = { isHidden ->
                closeButton?.showWithCondition(!isHidden)
            }
        }

        closeButton?.setOnClickListener { finish() }

        if (selectedPosition == -1) {
            showToaster(imageGallery)
        }

    }

    private fun showToaster(view: View) {
        view.showToasterSuccess(
            message = getString(R.string.pdp_common_gallery_default_selected),
            heightOffset = R.dimen.pdp_common_80_dp,
            ctaText = getString(R.string.pdp_common_oke)
        )
    }

    companion object {

        private const val PRODUCT_DETAIL_GALLERY_DATA = "productDetailGalleryData"

        fun createIntent(
            context: Context,
            productDetailGallery: ProductDetailGallery
        ): Intent {
            val intent = Intent(context, ProductDetailGalleryActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_DETAIL_GALLERY_DATA, productDetailGallery)
            intent.putExtras(bundle)
            return intent
        }
    }
}