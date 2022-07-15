package com.tokopedia.home_account.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.adapter.delegate.*
import com.tokopedia.home_account.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.home_account.view.delegate.HomeAccountTdnBannerDelegate
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusCons
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.usercomponents.tokopediaplus.ui.TokopediaPlusWidget

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserAdapter(
    listener: HomeAccountUserListener,
    balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter?,
    memberAdapter: HomeAccountMemberAdapter?,
    userSession: UserSessionInterface,
    shopAdsNewPositionCallback: (Int, CpmModel) -> Unit,
): BaseCommonAdapter() {

    private var tokopediaPlusWidget: TokopediaPlusWidget? = null
    private var memberTitle: Typography? = null

    init {
        delegatesManager.addDelegate(HomeAccountUserAdapterDelegate(listener, balanceAndPointAdapter, memberAdapter))
        delegatesManager.addDelegate(HomeAccountUserSettingDelegate(listener))
        delegatesManager.addDelegate(HomeAccountRecommendationTitleDelegate())
        delegatesManager.addDelegate(TopAdsHeadlineViewDelegate(userSession, shopAdsNewPositionCallback))
        delegatesManager.addDelegate(HomeAccountRecommendationItemDelegate(listener))
        delegatesManager.addDelegate(HomeAccountSeparatorDelegate())
        delegatesManager.addDelegate(HomeAccountRecommendationLoaderDelegate())
        delegatesManager.addDelegate(HomeAccountTdnBannerDelegate())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileViewHolder) {
            tokopediaPlusWidget = holder.itemView.findViewById(R.id.tokopediaPlusWidget)
            memberTitle = holder.itemView.findViewById(R.id.home_account_member_layout_title)
        }

        super.onBindViewHolder(holder, position)
    }

    fun setDefaultMemberTitle(title: String) {
        memberTitle?.text = title
    }

    fun setTokopediaPlusListener(tokopediaPlusListener: TokopediaPlusListener) {
        tokopediaPlusWidget?.listener = tokopediaPlusListener
    }

    fun setTokopediaPlusContent(tokopediaPlusDataModel: TokopediaPlusDataModel) {
        tokopediaPlusWidget?.setContent(
            TokopediaPlusParam(TokopediaPlusCons.SOURCE_ACCOUNT_PAGE, tokopediaPlusDataModel)
        )
    }

    fun onFailedLoadTokopediaWidget(throwable: Throwable) {
        tokopediaPlusWidget?.onError(throwable)
    }

    fun showLoadingTokoPediaPlus(isShow: Boolean) {
        if (isShow) tokopediaPlusWidget?.showLoading()
        else tokopediaPlusWidget?.hideLoading()
    }
}