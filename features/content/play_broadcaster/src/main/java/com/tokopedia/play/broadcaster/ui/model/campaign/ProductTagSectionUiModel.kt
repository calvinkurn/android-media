package com.tokopedia.play.broadcaster.ui.model.campaign

import android.os.Parcelable
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
@Parcelize
data class ProductTagSectionUiModel(
    val name: String,
    val campaignStatus: CampaignStatus,
    val products: List<ProductUiModel>,
) : Parcelable