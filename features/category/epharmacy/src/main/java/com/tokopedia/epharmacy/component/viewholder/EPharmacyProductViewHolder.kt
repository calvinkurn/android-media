package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class EPharmacyProductViewHolder(private val view: View,
                                 private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.product_name)
    private val storeNameText = view.findViewById<Typography>(R.id.store_name)
    private val storeLocationText = view.findViewById<Typography>(R.id.store_location)
    private val productQuantity = view.findViewById<Typography>(R.id.product_quantity)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)

    companion object {
        val LAYOUT = R.layout.epharmacy_product_view_item
    }

    override fun bind(element: EPharmacyProductDataModel) {
        renderProductData(element)
    }

    private fun renderProductData(dataModel: EPharmacyProductDataModel) {
        dataModel.product?.apply {
            productText.text = name ?: ""
            productQuantity.text = quantity.toString()
            productImageUnify.loadImage(productImage)
//            storeNameText.text = storeName ?: ""
//            storeLocationText.text = storeLocation ?: ""
        }
    }
}