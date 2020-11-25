package com.tokopedia.home_wishlist.model.datamodel

import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class BannerTopAdsDataModel (
        val topAdsDataModel: TopAdsImageViewModel = TopAdsImageViewModel(),
        val isOnBulkRemoveProgress: Boolean = false
): WishlistDataModel {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel::class.java == this::class.java
                && (dataModel as BannerTopAdsDataModel).topAdsDataModel.imageUrl == dataModel.topAdsDataModel.imageUrl
                && dataModel.topAdsDataModel.applink == dataModel.topAdsDataModel.applink
                && dataModel.topAdsDataModel.adClickUrl == dataModel.topAdsDataModel.adClickUrl
                && dataModel.isOnBulkRemoveProgress == dataModel.isOnBulkRemoveProgress

    }

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getUniqueIdentity(): Any {
        return this::class.java.simpleName
    }
}