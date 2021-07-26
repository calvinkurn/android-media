package com.tokopedia.home_account.view.listener

import android.view.View
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.adapter.HomeAccountFinancialAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

interface HomeAccountUserListener {
    fun onProfileClicked()
    fun onEditProfileClicked()
    fun onSettingItemClicked(item: CommonDataView)
    fun onMemberItemClicked(applink: String, type: Int)
    fun onSwitchChanged(item: CommonDataView, isActive: Boolean, switch: SwitchUnify)
    fun onItemViewBinded(position: Int, itemView: View, data: Any)
    fun onProfileAdapterReady(financialAdapter: HomeAccountFinancialAdapter, memberAdapter: HomeAccountMemberAdapter)
    fun onCommonAdapterReady(position: Int, commonAdapter: HomeAccountUserCommonAdapter)
    fun onIconWarningClicked(profile: ProfileDataView)

    fun onFinancialErrorClicked(type: Int)
    fun onMemberErrorClicked()

    fun onProductRecommendationImpression(item: RecommendationItem, adapterPosition: Int)
    fun onProductRecommendationClicked(item: RecommendationItem, adapterPosition: Int)
    fun onProductRecommendationThreeDotsClicked(item: RecommendationItem, adapterPosition: Int)
}