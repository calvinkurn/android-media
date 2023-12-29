package com.tokopedia.notifcenter.view.adapter.typefactory

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.data.model.NotifTopAdsHeadline
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel

interface NotificationTypeFactory : AdapterTypeFactory {
    fun type(sectionTitleUiModel: SectionTitleUiModel): Int
    fun type(recommendationTitleUiModel: RecommendationTitleUiModel): Int
    fun type(bigDividerUiModel: BigDividerUiModel): Int
    fun type(notificationUiModel: NotificationUiModel): Int
    fun type(loadMoreUiModel: LoadMoreUiModel): Int
    fun type(notificationTopAdsBannerUiModel: NotificationTopAdsBannerUiModel): Int
    fun type(recommendationUiModel: RecommendationUiModel): Int
    fun type(emptyNotificationUiModel: EmptyNotificationUiModel): Int
    fun type(notifOrderListUiModel: NotifOrderListUiModel): Int
    fun type(notifTopAdsHeadline: NotifTopAdsHeadline): Int
    fun type(affiliateEducationUiModel: NotificationAffiliateEducationUiModel): Int

    /**
     * to support 1 uiModel has several type of view
     * @return must be layout [LayoutRes]
     */
    @LayoutRes
    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    /**
     * use to pass interface implmented on adapter to viewholder
     * @param any can be used to pass several interfaces without the need
     * to call `this` multiple times
     */
    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>>
}
