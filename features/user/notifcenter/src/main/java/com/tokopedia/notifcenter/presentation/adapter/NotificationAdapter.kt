package com.tokopedia.notifcenter.presentation.adapter

import android.os.Parcelable
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.viewholder.ViewHolderState
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.CarouselProductNotificationViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload.PayloadBumpReminderState

class NotificationAdapter constructor(
        private val typeFactory: NotificationTypeFactory
) : BaseListAdapter<Visitable<*>, NotificationTypeFactory>(
        typeFactory
), NotificationAdapterListener, CarouselProductNotificationViewHolder.Listener {

    private val productCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private val carouselViewPool = RecyclerView.RecycledViewPool()

    override fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool {
        return carouselViewPool
    }

    override fun isPreviousItemNotification(adapterPosition: Int): Boolean {
        return visitables?.getOrNull(adapterPosition - 1) is NotificationUiModel
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return typeFactory.onCreateViewHolder(parent, viewType, this)
    }

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return typeFactory.getItemViewType(visitables, position, default)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layout.isFullSpan = holder !is RecommendationViewHolder
        super.onBindViewHolder(holder, position)
    }

    override fun saveProductCarouselState(position: Int, state: Parcelable?) {
        state?.let {
            productCarouselState[position] = it
        }
    }

    override fun getSavedCarouselState(position: Int): Parcelable? {
        return productCarouselState[position]
    }

    fun shouldDrawDivider(position: Int): Boolean {
        return isNotificationItem(position) && !isNextItemDivider(position)
    }

    fun loadMore(lastKnownPosition: Int, element: LoadMoreUiModel) {
        val elementData = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = elementData.first
        val item = elementData.second
        if (position == RecyclerView.NO_POSITION) return
        item.loading = true
        notifyItemChanged(position, LoadMoreViewHolder.PAYLOAD_UPDATE_STATE)
    }

    fun failLoadMoreNotification(lastKnownPosition: Int, element: LoadMoreUiModel) {
        val elementData = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = elementData.first
        val item = elementData.second
        if (position == RecyclerView.NO_POSITION) return
        item.loading = false
        notifyItemChanged(position, LoadMoreViewHolder.PAYLOAD_UPDATE_STATE)
    }

    fun insertNotificationData(
            lastKnownPosition: Int,
            element: LoadMoreUiModel,
            response: NotificationDetailResponseModel
    ) {
        val elementData = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = elementData.first
        if (position == RecyclerView.NO_POSITION) return
        visitables.removeAt(position)
        notifyItemRemoved(position)
        if (visitables.addAll(position, response.items)) {
            notifyItemRangeInserted(position, response.items.size)
            adjustDividerPadding(position, response)
        }
    }

    fun loadingStateReminder(viewHolderState: ViewHolderState?) {
        updateLoadingBumpReminderFor(
                viewHolderState = viewHolderState,
                isLoading = true
        )
    }

    fun successUpdateReminderState(
            viewHolderState: ViewHolderState?,
            isBumpReminder: Boolean
    ) {
        updateLoadingBumpReminderFor(
                viewHolderState = viewHolderState,
                isLoading = false,
                hasReminder = isBumpReminder
        )
    }

    private fun updateLoadingBumpReminderFor(
            viewHolderState: ViewHolderState?,
            isLoading: Boolean,
            hasReminder: Boolean? = null
    ) {
        viewHolderState ?: return
        val notificationItem = viewHolderState.visitable
                as? NotificationUiModel ?: return
        val itemMetaData = getUpToDateUiModelPosition(
                viewHolderState.previouslyKnownPosition, notificationItem
        )
        val itemPosition = itemMetaData.first
        val product = viewHolderState.payload
        if (product is ProductData && itemPosition != RecyclerView.NO_POSITION) {
            val payload = PayloadBumpReminderState(product, notificationItem)
            product.update(isLoading, hasReminder)
            notifyItemChanged(itemPosition, payload)
        }
    }

    private fun adjustDividerPadding(position: Int, response: NotificationDetailResponseModel) {
        if (response.hasNext) return
        val nextPosition = position + 1
        val divider = visitables.getOrNull(nextPosition)
        if (divider is BigDividerUiModel) {
            notifyItemChanged(nextPosition, Any())
        }
    }

    fun addTopAdsBanner(banner: NotificationTopAdsBannerUiModel) {
        if (visitables.add(banner)) {
            notifyItemInserted(visitables.size - 1)
        }
    }

    fun addRecomProducts(recommendations: List<Visitable<*>>) {
        val currentItemSize = visitables.size
        if (visitables.addAll(recommendations)) {
            notifyItemRangeInserted(currentItemSize, recommendations.size)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Visitable<NotificationTypeFactory>> getUpToDateUiModelPosition(
            lastKnownPosition: Int, element: T
    ): Pair<Int, T> {
        val item = visitables.getOrNull(lastKnownPosition)
        if (item == element) {
            return Pair(lastKnownPosition, item as T)
        }
        val updatePosition = visitables.indexOf(element)
        return Pair(updatePosition, item as T)
    }

    private fun isNotificationItem(position: Int): Boolean {
        val item = visitables.getOrNull(position) ?: return false
        return item is NotificationUiModel
    }

    private fun isNextItemDivider(position: Int): Boolean {
        val item = visitables.getOrNull(position + 1) ?: return false
        return item is BigDividerUiModel
    }

}