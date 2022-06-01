package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel

import com.tokopedia.topads.dashboard.data.model.TopAdsDeletedAdsResponse
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.DeletedGroupItemsAdapterTypeFactory

class DeletedGroupItemsItemModel(val topAdsDeletedAdsItem: TopAdsDeletedAdsResponse.TopadsDeletedAds.TopAdsDeletedAdsItem) :
    DeletedGroupItemsModel() {
    override fun type(typesFactory: DeletedGroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}
