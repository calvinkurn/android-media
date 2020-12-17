package com.tokopedia.top_ads_headline.data

import android.os.Parcelable
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.sdk.domain.model.CpmModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditHeadlineAdsModel(
        var groupDataItem: WithoutGroupDataItem = WithoutGroupDataItem(),
        var headlineAdDetail: SingleAd = SingleAd(),
        var selectedTopAdsProductMap: HashMap<Category, ArrayList<TopAdsProductModel>> = HashMap(),
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
        var keywordOperations: List<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> = ArrayList()
) : Parcelable