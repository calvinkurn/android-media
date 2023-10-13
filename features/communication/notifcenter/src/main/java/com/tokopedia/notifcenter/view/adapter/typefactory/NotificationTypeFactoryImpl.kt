package com.tokopedia.notifcenter.view.adapter.typefactory

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
import com.tokopedia.notifcenter.view.listener.NotificationAffiliateEduEventListener
import com.tokopedia.notifcenter.view.listener.NotificationItemListener
import com.tokopedia.notifcenter.view.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.view.adapter.viewholder.NotificationShopAdsViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.affiliate.NotificationAffiliateEducationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.BannerNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.BigDividerViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.CarouselProductNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationWithRecomViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NormalNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationErrorViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationLoadMoreViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationLoadingViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.RecommendationTitleViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.SectionTitleViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.SingleProductNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.WidgetNotificationViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class NotificationTypeFactoryImpl constructor(
    viewListener: Any
) : BaseAdapterTypeFactory(), NotificationTypeFactory {

    var recommendationListener: RecommendationListener? = null

    private val notificationListener = viewListener as NotificationItemListener
    private val loadMoreListener = viewListener as LoadMoreViewHolder.Listener
    var affiliateEducationListener: NotificationAffiliateEduEventListener? = null

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SectionTitleViewHolder.LAYOUT
    }

    override fun type(notifTopAdsHeadline: NotifTopAdsHeadline): Int {
        return NotificationShopAdsViewHolder.LAYOUT
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

    override fun type(affiliateEducationUiModel: NotificationAffiliateEducationUiModel): Int {
        return NotificationAffiliateEducationViewHolder.LAYOUT
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
                item.isTrackHistory() -> WidgetNotificationViewHolder.LAYOUT
                item.isTypeDefault() -> NormalNotificationViewHolder.LAYOUT
                item.isTypeSingleProduct() -> SingleProductNotificationViewHolder.LAYOUT
                item.isCarouselProduct() -> CarouselProductNotificationViewHolder.LAYOUT
                item.isBanner() -> BannerNotificationViewHolder.LAYOUT
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
                view,
                notificationListener,
                adapterListener as? CarouselProductNotificationViewHolder.Listener,
                notifAdapterListener
            )
            BigDividerViewHolder.LAYOUT -> BigDividerViewHolder(
                view,
                notifAdapterListener
            )
            WidgetNotificationViewHolder.LAYOUT -> WidgetNotificationViewHolder(
                view,
                notificationListener,
                notifAdapterListener
            )
            NotificationOrderListViewHolder.LAYOUT -> NotificationOrderListViewHolder(
                view,
                notificationListener,
                notifAdapterListener,
                adapterListener as? NotificationOrderListViewHolder.Listener
            )
            else -> createViewHolder(view, viewType)
        }
    }

    override fun createViewHolder(view: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            NotificationShopAdsViewHolder.LAYOUT -> NotificationShopAdsViewHolder(view)
            NotificationErrorViewHolder.LAYOUT -> NotificationErrorViewHolder(
                view,
                notificationListener
            )
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(view)
            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)
            NotificationTopAdsBannerViewHolder.LAYOUT -> NotificationTopAdsBannerViewHolder(view)
            NotificationLoadMoreViewHolder.LAYOUT -> NotificationLoadMoreViewHolder(view)
            NotificationLoadingViewHolder.LAYOUT -> NotificationLoadingViewHolder(view)
            EmptyNotificationViewHolder.LAYOUT -> EmptyNotificationViewHolder(view)
            EmptyNotificationWithRecomViewHolder.LAYOUT -> EmptyNotificationWithRecomViewHolder(view)
            RecommendationViewHolder.LAYOUT -> RecommendationViewHolder(
                view,
                recommendationListener
            )
            LoadMoreViewHolder.LAYOUT -> LoadMoreViewHolder(view, loadMoreListener)
            BannerNotificationViewHolder.LAYOUT -> BannerNotificationViewHolder(
                view,
                notificationListener
            )
            SingleProductNotificationViewHolder.LAYOUT -> SingleProductNotificationViewHolder(
                view,
                notificationListener
            )
            NormalNotificationViewHolder.LAYOUT -> NormalNotificationViewHolder(
                view,
                notificationListener
            )
            NotificationAffiliateEducationViewHolder.LAYOUT -> NotificationAffiliateEducationViewHolder(
                view,
                affiliateEducationListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
