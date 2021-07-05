package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.data.uimodel.*

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
            parent: ViewGroup, viewType: Int, adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>>
}