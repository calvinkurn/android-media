package com.tokopedia.centralizedpromo.view.model

import android.os.Parcelable
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.parcelize.Parcelize

data class PromoCreationListUiModel(
    val filterItems: List<FilterPromoUiModel>,
    override val items: List<PromoCreationUiModel>,
    override val errorMessage: String
) : BaseUiListModel<PromoCreationUiModel>

@Parcelize
data class PromoCreationUiModel(
    val icon: String,
    val title: String,
    val description: String,
    val notAvailableText: String,
    val titleSuffix: String,
    val ctaLink: String,
    val ctaText: String,
    val eligible: Int,
    val banner: String,
    val infoText: String,
    val headerText: String,
    val bottomText: String,
) : Parcelable, BaseUiListItemModel<CentralizedPromoAdapterTypeFactory> {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isEligible() = eligible == 1
}

data class FilterPromoUiModel(
    val id: String = "0",
    val name: String = "Semua Fitur",
)