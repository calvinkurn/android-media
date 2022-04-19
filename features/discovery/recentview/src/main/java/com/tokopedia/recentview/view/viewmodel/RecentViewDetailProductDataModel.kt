package com.tokopedia.recentview.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory

/**
 * Created by Lukas on 13/11/20.
 */
data class RecentViewDetailProductDataModel (
    val name: String = "",
    val price: String = "",
    val imageSource: String = "",
    val isFreeReturn: Boolean = false,
    var isWishlist: Boolean  = false,
    val rating: Int = 0,
    val productId: String = "",
    val productLink: String = "",
    val isGold: Boolean  = false,
    val isOfficial: Boolean  = false,
    val shopName: String = "",
    val shopLocation: String = "",
    val labels: List<LabelsDataModel> = listOf(),
    val positionForRecentViewTracking: Int = 0
): Visitable<RecentViewTypeFactory>{

    override fun type(typeFactory: RecentViewTypeFactory): Int = typeFactory.type(this)

}