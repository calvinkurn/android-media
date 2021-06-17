package com.tokopedia.tokomart.searchcategory.presentation.model

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.HASIL_PENCARIAN_DI_TOKONOW
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.LOCAL_SEARCH
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.NONE_OTHER
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ORGANIC
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.TOKO_NOW
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

    val discountPercentageString
        get() = if (discountPercentage > 0) "$discountPercentage%" else ""

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Shop(
            val id: String = "",
            val name: String = "",
    )

    private fun getAsObjectDataLayerMap(
            filterSortValue: String,
            pageId: String,
    ): MutableMap<String, Any> {
        return DataLayer.mapOf(
                "brand", NONE_OTHER,
                "category", NONE_OTHER,
                "dimension100", sourceEngine,
                "dimension61", filterSortValue,
                "dimension81", TOKO_NOW,
                "dimension90", getDimension90(pageId),
                "dimension96", boosterList,
                "id", id,
                "dimension40", TOKONOW_SEARCH_PRODUCT_ORGANIC,
                "name", name,
                "price", priceInt,
                "variant", NONE_OTHER,
        )
    }

    fun getAsImpressionClickObjectDataLayer(
            filterSortValue: String,
            pageId: String,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue, pageId).also {
            it.putAll(DataLayer.mapOf(
                    "position", position,
            ))
        }
    }

    fun getAsATCObjectDataLayer(
            filterSortValue: String,
            pageId: String,
            quantity: Int,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue, pageId).also {
            it.putAll(DataLayer.mapOf(
                    "quantity", quantity,
                    "shop_id", shop.id,
                    "shop_name", shop.name,
            ))
        }
    }

    private fun getDimension90(pageId: String) =
            "$HASIL_PENCARIAN_DI_TOKONOW.$TOKONOW.$LOCAL_SEARCH.$pageId"
}