package com.tokopedia.product.addedit.preview.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.REQUEST_CODE_SIZE
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by faisalramd on 2020-04-01.
 */
@Parcelize
data class ProductInputModel (
        var detailInputModel: DetailInputModel = DetailInputModel(),
        var descriptionInputModel: DescriptionInputModel = DescriptionInputModel(),
        var shipmentInputModel: ShipmentInputModel = ShipmentInputModel(),
        var variantInputModel: VariantInputModel = VariantInputModel(),
        var productId: Long = 0L,
        var completionPercent: Int = 0,
        var draftId: Long = 0L,
        // requestCode related to checkEnabledOrNot function on preview page,
        // it's for handling behaviour of enabling shipment and description stepper
        // when click back pressed in add mode
        var requestCode: Array<Int> = Array(REQUEST_CODE_SIZE){NO_DATA},
        var itemSold: Int = 0, // count of successful item transaction
        var isDataChanged: Boolean = false
) : Parcelable {
        @IgnoredOnParcel
        var lastVariantData = ProductVariantInputModel()

        fun getVariantDefaultVariantPrice(isEditMode: Boolean) = if (isEditMode ||
                lastVariantData.price == Int.ZERO.toBigInteger()) {
                detailInputModel.price
        } else {
                lastVariantData.price
        }

        fun resetVariantData() {
                if (variantInputModel.hasVariant()) {
                        lastVariantData = variantInputModel.getPrimaryVariantData()
                        variantInputModel.products = emptyList()
                        variantInputModel.selections = emptyList()
                }
        }
}
