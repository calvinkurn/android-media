package com.tokopedia.addongifting.addongallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.GalleryActivityBinding
import com.tokopedia.image_gallery.ImageGalleryItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class AddOnGalleryActivity : BaseSimpleActivity() {

    private var viewBinding: GalleryActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = GalleryActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        viewBinding?.let {
            val imageUrls = intent.extras?.getStringArrayList(AddOnConstant.EXTRA_ADD_ON_IMAGES)?.toList()
                    ?: emptyList()
            val addOnName = intent.extras?.getString(AddOnConstant.EXTRA_ADD_ON_NAME) ?: ""
            val addOnPrice = intent.extras?.getLong(AddOnConstant.EXTRA_ADD_ON_PRICE) ?: 0
            renderView(it, imageUrls, addOnName, addOnPrice)
        }
    }

    private fun renderView(viewBinding: GalleryActivityBinding, imageUrls: List<String>, addOnName: String, addOnPrice: Long) {
        renderImageGallery(viewBinding, imageUrls)
        renderAddOnInfo(viewBinding, addOnName, addOnPrice)
    }

    private fun renderImageGallery(viewBinding: GalleryActivityBinding, imageUrls: List<String>) {
        with(viewBinding) {
            val imageGalleryItems = arrayListOf<ImageGalleryItem>()
            imageUrls.forEach {
                imageGalleryItems.add(ImageGalleryItem(null, it))
            }
            if (imageGalleryItems.isNotEmpty()) {
                imageGalleryAddOn.apply {
                    isHiddenOverlay = true
                    overlayContainer.gone()
                    setImages(imageGalleryItems)
                }
                imageGalleryAddOn.show()
            } else {
                imageGalleryAddOn.gone()
            }

            buttonClose.setOnClickListener {
                finish()
            }
        }
    }

    private fun renderAddOnInfo(viewBinding: GalleryActivityBinding, addOnName: String, addOnPrice: Long) {
        with(viewBinding) {
            labelAddOnName.text = addOnName
            labelAddOnPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(addOnPrice, false).removeDecimalSuffix()
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.gallery_activity
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }

}