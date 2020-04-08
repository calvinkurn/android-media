package com.tokopedia.product.addedit.preview.presentation.model

import android.os.Parcelable
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-04-01.
 */
@Parcelize
data class ProductInputModel (
        var detailInputModel: DetailInputModel = DetailInputModel(),
        var descriptionInputModel: DescriptionInputModel = DescriptionInputModel(),
        var shipmentInputModel: ShipmentInputModel = ShipmentInputModel(),
        var variantInputModel: ProductVariantInputModel = ProductVariantInputModel(),
        var productId: Long = 0L,
        var completionPercent: Int = 0,
        var draftId: Long = 0L
) : Parcelable
