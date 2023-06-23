package com.tokopedia.tokopedianow.oldcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.oldcategory.domain.model.TokonowCategoryDetail.CategoryDetail
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel

data class CategoryModel(
        @SerializedName("get_targeted_ticker")
        @Expose
        val targetedTicker: GetTargetedTickerResponse = GetTargetedTickerResponse(),

        @SerializedName("category_detail")
        @Expose
        val categoryDetail: CategoryDetail = CategoryDetail(),

        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct(),

        @SerializedName("category_filter")
        @Expose
        val categoryFilter: DataValue = DataValue(),

        @SerializedName("quick_filter")
        @Expose
        val quickFilter: DataValue = DataValue(),

        @SerializedName("channel")
        @Expose
        val bannerChannel: Channels = Channels(),

        @SerializedName("TokonowRepurchaseWidget")
        @Expose
        val tokonowRepurchaseWidget: RepurchaseData = RepurchaseData(),

        @SerializedName("feedbackFieldToggle")
        @Expose
        val feedbackFieldToggle: GetFeedbackFieldModel = GetFeedbackFieldModel()
)
