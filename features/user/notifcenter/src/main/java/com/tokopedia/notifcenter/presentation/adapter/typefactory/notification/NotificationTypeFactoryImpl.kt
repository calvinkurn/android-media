package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.uimodel.*
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.*

class NotificationTypeFactoryImpl constructor(
        viewListener: Any
) : BaseAdapterTypeFactory(), NotificationTypeFactory {

    private val notificationListener = viewListener as? NotificationItemListener
    private val loadMoreListener = viewListener as? LoadMoreViewHolder.Listener

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SectionTitleViewHolder.LAYOUT
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

    @LayoutRes
    override fun getItemViewType(
            visitables: List<Visitable<*>>,
            position: Int,
            default: Int
    ): Int {
        val item = visitables.getOrNull(position)
        if (item is NotificationUiModel) {
            return when {
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
     * All ViewHolder that need adapter interface need to created from this
     */
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
            adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            CarouselProductNotificationViewHolder.LAYOUT -> CarouselProductNotificationViewHolder(
                    view, notificationListener,
                    adapterListener as? CarouselProductNotificationViewHolder.Listener,
                    adapterListener as? NotificationAdapterListener
            )
            else -> createViewHolder(view, viewType)
        }
    }

    override fun createViewHolder(view: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(view)
            BigDividerViewHolder.LAYOUT -> BigDividerViewHolder(view)
            NotificationTopAdsBannerViewHolder.LAYOUT -> NotificationTopAdsBannerViewHolder(view)
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