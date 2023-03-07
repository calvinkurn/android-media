package com.tokopedia.home_account.view.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.adapter.delegate.*
import com.tokopedia.home_account.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.home_account.view.delegate.HomeAccountTdnBannerDelegate
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserAdapter(
    listener: HomeAccountUserListener,
    tokopediaPlusListener: TokopediaPlusListener,
    balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter?,
    memberAdapter: HomeAccountMemberAdapter?,
    userSession: UserSessionInterface,
    shopAdsNewPositionCallback: (Int, CpmModel) -> Unit,
): BaseCommonAdapter() {

    private var memberTitle: Typography? = null

    init {
        delegatesManager.addDelegate(HomeAccountUserAdapterDelegate(listener, tokopediaPlusListener, balanceAndPointAdapter, memberAdapter))
        delegatesManager.addDelegate(HomeAccountUserSettingDelegate(listener))
        delegatesManager.addDelegate(HomeAccountRecommendationTitleDelegate())
        delegatesManager.addDelegate(TopAdsHeadlineViewDelegate(userSession, shopAdsNewPositionCallback))
        delegatesManager.addDelegate(HomeAccountRecommendationItemDelegate(listener))
        delegatesManager.addDelegate(HomeAccountSeparatorDelegate())
        delegatesManager.addDelegate(HomeAccountRecommendationLoaderDelegate())
        delegatesManager.addDelegate(HomeAccountTdnBannerDelegate())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder is ProfileViewHolder) {
            val memberSection = holder.itemView.findViewById<ConstraintLayout>(R.id.home_account_profile_member_section)
            memberTitle = memberSection.findViewById(R.id.home_account_member_layout_title)
        }
    }

    fun setDefaultMemberTitle(title: String) {
        memberTitle?.text = title
    }

    fun setTokopediaPlusContent(tokopediaPlusDataModel: TokopediaPlusDataModel) {
        val profileDataView = itemList.find { it is ProfileDataView } as ProfileDataView
        profileDataView.isSuccessGetTokopediaPlusData = true
        profileDataView.tokopediaPlusWidget = tokopediaPlusDataModel
        notifyItemChanged(0)
    }

    fun onFailedLoadTokopediaWidget(throwable: Throwable) {
        val profileDataView = itemList.find { it is ProfileDataView } as ProfileDataView
        profileDataView.isSuccessGetTokopediaPlusData = false
        notifyItemChanged(0)
    }
}