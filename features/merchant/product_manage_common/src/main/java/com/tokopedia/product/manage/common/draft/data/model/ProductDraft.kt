package com.tokopedia.product.manage.common.draft.data.model

import android.os.Parcelable
import com.tokopedia.product.manage.common.draft.data.model.description.DescriptionInputModel
import com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantInputModel
import com.tokopedia.product.manage.common.draft.data.model.detail.DetailInputModel
import com.tokopedia.product.manage.common.draft.data.model.shipment.ShipmentInputModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDraft(
        var detailInputModel: DetailInputModel = DetailInputModel(),
        var descriptionInputModel: DescriptionInputModel = DescriptionInputModel(),
        var shipmentInputModel: ShipmentInputModel = ShipmentInputModel(),
        var variantInputModel: ProductVariantInputModel = ProductVariantInputModel(),
        var productId: Long = 0L,
        var completionPercent: Int = 0
): Parcelable