package com.tokopedia.tokomart.searchcategory.presentation.model

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.HASIL_PENCARIAN_DI_TOKONOW
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.LOCAL_SEARCH
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW

data class ProductItemDataView(
        val id: String = "",
        val imageUrl300: String = "",
        val name: String = "",
        val price: String = "",
        val priceInt: Double = 0.0,
        val discountPercentage: Int = 0,
        val originalPrice: String = "",
        val parentId: String = "",
        val shop: Shop = Shop(),
        val ratingAverage: String = "",
        val variantATC: VariantATCDataView? = null,
        val nonVariantATC: NonVariantATCDataView? = null,
        val labelGroupDataViewList: List<LabelGroupDataView> = listOf(),
        val labelGroupVariantDataViewList: List<LabelGroupVariantDataView> = listOf(),
        val sourceEngine: String = "",
        val boosterList: String = "",
        val position: Int = 0,
): Visitable<BaseSearchCategoryTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Shop(
            val id: String = "",
    )

    fun getAsObjectDataLayer(
            filterSortValue: String,
            pageId: String,
    ): Any {
        return DataLayer.mapOf(
                "brand", "none / other",
                "category", "none / other",
                "dimension100", sourceEngine,
                "dimension61", filterSortValue,
                "dimension81", "toko now",
                "dimension90", getDimension90(pageId),
                "dimension96", boosterList,
                "id", id,
                "list", "/tokonow - searchproduct - organic",
                "name", name,
                "position", "{position}",
                "price", priceInt,
                "variant", "none / other"
        )
    }

    private fun getDimension90(pageId: String) =
            "$HASIL_PENCARIAN_DI_TOKONOW.$TOKONOW.$LOCAL_SEARCH.$pageId"
}