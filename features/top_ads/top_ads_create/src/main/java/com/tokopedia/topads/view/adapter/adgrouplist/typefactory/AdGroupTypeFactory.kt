package com.tokopedia.topads.view.adapter.adgrouplist.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupShimmerUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.LoadingMoreUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel

interface AdGroupTypeFactory : AdapterTypeFactory {
    fun type(model:AdGroupShimmerUiModel) : Int
    fun type(model:AdGroupUiModel) : Int
    fun type(model:CreateAdGroupUiModel) : Int
    fun type(model:ErrorUiModel) : Int
    fun type(model:ReloadInfiniteUiModel) : Int
    fun type(model:LoadingMoreUiModel) : Int
}
