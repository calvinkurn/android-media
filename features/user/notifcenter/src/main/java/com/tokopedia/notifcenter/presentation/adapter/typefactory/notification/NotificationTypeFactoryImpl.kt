package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.data.uimodel.*
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class NotificationTypeFactoryImpl constructor(
        viewListener: Any
) : BaseAdapterTypeFactory(), NotificationTypeFactory {

    var recommendationListener: RecommendationListener? = null

    private val notificationListener = viewListener as NotificationItemListener
    private val loadMoreListener = viewListener as LoadMoreViewHolder.Listener

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SectionTitleViewHolder.LAYOUT
    }

    override fun type(recommendationTitleUiModel: RecommendationTitleUiModel): Int {
        return RecommendationTitleViewHolder.LAYOUT
    }

    override fun type(bigDividerUiModel: BigDividerUiModel): Int {
        return BigDividerViewHolder.LAYOUT
    }

    override fun type(notificationUiModel: NotificationUiModel): Int {
        return NormalNotificationViewHolder.LAYOUT
    }

    override fun type(loadMoreUiModel: LoadMoreUiModel): Int {
        return LoadMoreViewHolder.LAYOUT
    }

    override fun type(notificationTopAdsBannerUiModel: NotificationTopAdsBannerUiModel): Int {
        return NotificationTopAdsBannerViewHolder.LAYOUT
    }

    override fun type(recommendationUiModel: RecommendationUiModel): Int {
        return RecommendationViewHolder.LAYOUT
    }

    override fun type(emptyNotificationUiModel: EmptyNotificationUiModel): Int {
        return EmptyNotificationViewHolder.LAYOUT
    }

    override fun type(notifOrderListUiModel: NotifOrderListUiModel): Int {
        return NotificationOrderListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return NotificationLoadingViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return NotificationLoadMoreViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return EmptyNotificationWithRecomViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel?): Int {
        return NotificationErrorViewHolder.LAYOUT
    }

    @LayoutRes
    override fun getItemViewType(
            visitables: List<Visitable<*>>,
            position: Int,
            default: Int
    ): Int {
        val item = visitables.getOrNull(position)
        if (item is NotificationUiModel) {
            return when {
                item.isTrackHistory() && item.hasWidget() -> WidgetNotificationViewHolder.LAYOUT
                item.isTypeDefault() -> NormalNotificationViewHolder.LAYOUT
                item.isTypeSingleProduct() -> SingleProductNotificationViewHolder.LAYOUT
                item.isCarouselProduct() -> CarouselProductNotificationViewHolder.LAYOUT
                item.isBanner() -> BannerNotificationTitleViewHolder.LAYOUT
                else -> NormalNotificationViewHolder.LAYOUT
            }
        }
        return default
    }

    /**
     * All ViewHolder that need [NotificationAdapterListener] interface need to created from this
     */
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
            adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val notifAdapterListener = adapterListener as? NotificationAdapterListener
        return when (viewType) {
            CarouselProductNotificationViewHolder.LAYOUT -> CarouselProductNotificationViewHolder(
                    view, notificationListener,
                    adapterListener as? CarouselProductNotificationViewHolder.Listener,
                    notifAdapterListener
            )
            BigDividerViewHolder.LAYOUT -> BigDividerViewHolder(
                    view, notifAdapterListener
            )
            WidgetNotificationViewHolder.LAYOUT -> WidgetNotificationViewHolder(
                    view, notificationListener, notifAdapterListener
            )
            else -> createViewHolder(view, viewType)
        }
    }

    override fun createViewHolder(view: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            NotificationOrderListViewHolder.LAYOUT -> NotificationOrderListViewHolder(
                    view, notificationListener
            )
            NotificationErrorViewHolder.LAYOUT -> NotificationErrorViewHolder(
                    view, notificationListener
            )
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(view)
            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)
            NotificationTopAdsBannerViewHolder.LAYOUT -> NotificationTopAdsBannerViewHolder(view)
            NotificationLoadMoreViewHolder.LAYOUT -> NotificationLoadMoreViewHolder(view)
            NotificationLoadingViewHolder.LAYOUT -> NotificationLoadingViewHolder(view)
            EmptyNotificationViewHolder.LAYOUT -> EmptyNotificationViewHolder(view)
            EmptyNotificationWithRecomViewHolder.LAYOUT -> EmptyNotificationWithRecomViewHolder(
                    view
            )
            RecommendationViewHolder.LAYOUT -> RecommendationViewHolder(
                    view, recommendationListener
            )
            LoadMoreViewHolder.LAYOUT -> LoadMoreViewHolder(
                    view, loadMoreListener
            )
            BannerNotificationTitleViewHolder.LAYOUT -> BannerNotificationTitleViewHolder(
                    view, notificationListener
            )
            SingleProductNotificationViewHolder.LAYOUT -> SingleProductNotificationViewHolder(
                    view, notificationListener
            )
            NormalNotificationViewHolder.LAYOUT -> NormalNotificationViewHolder(
                    view, notificationListener
            )
            else -> super.createViewHolder(view, type)
        }
    }

}