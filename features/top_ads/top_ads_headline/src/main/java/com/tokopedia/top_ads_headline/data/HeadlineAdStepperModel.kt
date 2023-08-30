package com.tokopedia.top_ads_headline.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class HeadlineAdStepperModel(
        @SerializedName("group_name")
    var groupName: String = "",
        @SerializedName("selected_topads_product_map")
    var selectedTopAdsProductMap: HashMap<Category, ArrayList<TopAdsProductModel>> = HashMap(),
        @SerializedName("selected_topads_product")
    var selectedTopAdsProducts: ArrayList<TopAdsProductModel> = ArrayList(),
        @SerializedName("selected_product_ids")
    var selectedProductIds: MutableList<String> = mutableListOf(),
        @SerializedName("selected_keywords")
    var selectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        @SerializedName("manual_selected_keywords")
    var manualSelectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        @SerializedName("state_restore_keywords")
    var stateRestoreKeyword: Boolean = false,
        @SerializedName("slogan")
    var slogan: String = "",
        @SerializedName("cpm_model")
    var cpmModel: CpmModel = CpmModel(),
        @SerializedName("min_bid")
    var minBid: String = "0",
        @SerializedName("max_bid")
    var maxBid: String = "0",
        @SerializedName("daily_budget")
    var dailyBudget: Float = 0F,
        @SerializedName("ad_operations")
    var adOperations: MutableList<TopAdsManageHeadlineInput.Operation.Group.AdOperation> = ArrayList(),
        @SerializedName("keyword_operations")
    var keywordOperations: MutableList<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> = ArrayList(),
        @SerializedName("start_date")
    var startDate: String = "",
        @SerializedName("end_date")
    var endDate: String = "",
        @SerializedName("ad_bid_price")
    var adBidPrice: Double = 0.0,
        @SerializedName("current_bid")
    var currentBid:Double = 0.0
) : StepperModel
