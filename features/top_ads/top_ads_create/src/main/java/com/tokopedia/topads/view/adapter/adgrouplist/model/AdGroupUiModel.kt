package com.tokopedia.topads.view.adapter.adgrouplist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.data.AdGroupSettingData
import com.tokopedia.topads.data.AdGroupStatsData
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory

data class AdGroupUiModel(
    val groupId:String = "",
    val groupName:String = "",
    val adGroupSetting: AdGroupSettingData = AdGroupSettingData(),
    val adGroupStats:AdGroupStatsData = AdGroupStatsData(),
    val selected:Boolean = false
) : Visitable<AdGroupTypeFactory> {
    override fun type(typeFactory: AdGroupTypeFactory) = typeFactory.type(this)
}
