package com.tokopedia.home_account.view.listener

import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
interface HomeAccountUserListener {
    fun onEditProfileClicked()
    fun onSettingItemClicked(item: CommonDataView)
    fun onMemberItemClicked(applink: String)
    fun onSwitchChanged(item: CommonDataView, isActive: Boolean)
    fun onProductRecommendationImpression(item: RecommendationItem, adapterPosition: Int)
    fun onProductRecommendationClicked(item: RecommendationItem, adapterPosition: Int)
    fun onProductRecommendationThreeDotsClicked(item: RecommendationItem, adapterPosition: Int)
}