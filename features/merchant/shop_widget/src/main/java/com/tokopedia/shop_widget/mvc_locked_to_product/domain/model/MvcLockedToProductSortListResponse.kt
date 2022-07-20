package com.tokopedia.shop_widget.mvc_locked_to_product.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MvcLockedToProductSortListResponse(
    @SerializedName("filter_sort_product")
    @Expose
    var filterSortProduct: FilterSortProduct = FilterSortProduct()
) {
    data class FilterSortProduct(
        @SerializedName("data")
        @Expose
        var data: Data = Data()
    ) {
        data class Data(
            @SerializedName("sort")
            @Expose
            var sort: List<Sort> = listOf()
        ) {
            data class Sort(
                @SerializedName("name")
                @Expose
                var name: String = "",
                @SerializedName("key")
                @Expose
                var key: String = "",
                @SerializedName("value")
                @Expose
                var value: String = ""
            )
        }
    }
}