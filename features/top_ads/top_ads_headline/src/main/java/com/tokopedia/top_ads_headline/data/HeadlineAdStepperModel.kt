package com.tokopedia.top_ads_headline.data

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class HeadlineAdStepperModel(
        var groupName: String = "",
        var selectedTopAdsProductMap: HashMap<Category, ArrayList<TopAdsProductModel>> = HashMap(),
        var selectedTopAdsProducts: ArrayList<TopAdsProductModel> = ArrayList(),
        var selectedProductIds: MutableList<Int> = mutableListOf(),
        var selectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        var manualSelectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        var stateRestoreKeyword: Boolean = false,
        var slogan: String = "",
        var cpmModel: CpmModel = CpmModel(),
        var minBid: Int = 0,
        var maxBid: Int = 0,
        var dailyBudget: Float = 0F,
        var adOperations: MutableList<TopAdsManageHeadlineInput.Operation.Group.AdOperation> = ArrayList(),
        var keywordOperations: MutableList<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> = ArrayList(),
        var startDate:String = "",
        var endDate:String = ""
) : StepperModel
