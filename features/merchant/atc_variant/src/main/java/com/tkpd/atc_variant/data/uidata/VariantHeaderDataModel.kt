package com.tkpd.atc_variant.data.uidata

import android.os.Bundle
import com.tkpd.atc_variant.util.PAYLOAD_UPDATE_IMAGE_ONLY
import com.tkpd.atc_variant.util.PAYLOAD_UPDATE_PRICE_ONLY
import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantHeaderDataModel(
        val position: Long = 0,
        val productImage: String = "",
        val headerData: ProductHeaderData = ProductHeaderData()
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantHeaderDataModel) {
            productImage == newData.productImage &&
                    headerData == newData.headerData
        } else {
            false
        }
    }

    override fun getChangePayload(newData: AtcVariantVisitable): Bundle? {
        val bundle = Bundle()
        return if (newData is VariantHeaderDataModel) {
            if (headerData != newData.headerData && productImage != newData.productImage) {
                return null
            }

            if (productImage != newData.productImage) {
                bundle.putInt("payload", PAYLOAD_UPDATE_IMAGE_ONLY)
            }

            if (headerData != newData.headerData) {
                bundle.putInt("payload", PAYLOAD_UPDATE_PRICE_ONLY)
            }

            bundle
        } else {
            null
        }
    }

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class ProductHeaderData(
        val productMainPrice: String = "",
        val productDiscountedPercentage: Int = 0,
        val productCampaignIdentifier: Int = 0,
        val productSlashPrice: String = "",
        val productStockWording: String = ""
)