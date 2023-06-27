package com.tokopedia.notifcenter.ui.adapter

import android.os.Parcelable
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.data.model.NotifTopAdsHeadline
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.notifcenter.ui.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.ui.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.notifcenter.ui.adapter.viewholder.ViewHolderState
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.CarouselProductNotificationViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.SectionTitleViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.payload.PayloadBumpReminderState
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.payload.PayloadOrderList
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.payload.PayloadWishlistState
import com.tokopedia.topads.sdk.domain.model.CpmModel

class NotificationAdapter constructor(
    private val typeFactory: NotificationTypeFactory,
    private val listener: Listener
) : BaseListAdapter<Visitable<*>, NotificationTypeFactory>(
    typeFactory
),
    NotificationAdapterListener,
    CarouselProductNotificationViewHolder.Listener,
    NotificationOrderListViewHolder.Listener {

    private val productCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private val orderWidgetCarouselState: ArrayMap<String, Parcelable> = ArrayMap()
    private val carouselViewPool = RecyclerView.RecycledViewPool()
    private val widgetTimeline = RecyclerView.RecycledViewPool()
    private val orderWidgetPool = RecyclerView.RecycledViewPool()
    private var recommendationTitlePosition: Int? = null
    var shopAdsWidgetAdded = false
    var affiliateBannerPair: Pair<Int, NotificationAffiliateEducationUiModel>? = null

    interface Listener {
        fun hasFilter(): Boolean
    }

    override fun saveOrderWidgetState(key: String, currentState: Parcelable?) {
        orderWidgetCarouselState[key] = currentState
    }

    override fun getSavedOrderCarouselState(key: String): Parcelable? {
        return orderWidgetCarouselState[key]
    }

    override fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool {
        return carouselViewPool
    }

    override fun getWidgetTimelineViewPool(): RecyclerView.RecycledViewPool {
        return widgetTimeline
    }

    override fun getNotificationOrderViewPool(): RecyclerView.RecycledViewPool? {
        return orderWidgetPool
    }

    override fun isPreviousItemNotification(adapterPosition: Int): Boolean {
        return visitables?.getOrNull(adapterPosition - 1) is NotificationUiModel
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
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

    fun lastItemIsErrorNetwork(): Boolean {
        return visitables.getOrNull(visitables.lastIndex) is ErrorNetworkModel
    }

    fun shouldDrawDivider(position: Int): Boolean {
        return isNotificationItem(position) && !isNextItemDivider(position)
    }

    fun loadMore(lastKnownPosition: Int, element: LoadMoreUiModel) {
        val elementData = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = elementData.first
        val item = elementData.second ?: return
        if (position == RecyclerView.NO_POSITION) return
        item.loading = true
        notifyItemChanged(position, LoadMoreViewHolder.PAYLOAD_UPDATE_STATE)
    }

    fun failLoadMoreNotification(lastKnownPosition: Int, element: LoadMoreUiModel) {
        val elementData = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = elementData.first
        val item = elementData.second ?: return
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
            viewHolderState.previouslyKnownPosition,
            notificationItem
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

    fun addAffiliateEducationArticles(banner: NotificationAffiliateEducationUiModel) {
        if (visitables.add(banner)) {
            affiliateBannerPair = Pair(visitables.size - 1, banner)
            notifyItemInserted(visitables.size - 1)
        }
    }

    fun removeLoadingComponents() {
        for (i in visitables.size - 1 downTo 0) {
            if (visitables[i] is LoadingMoreModel) {
                visitables.removeAt(i)
                notifyItemRemoved(i)
                // updating banner position as now the list size is decreased
                affiliateBannerPair = affiliateBannerPair?.copy(visitables.size - 1)
                return
            }
        }
    }

    fun reAddAffiliateBanner() {
        affiliateBannerPair?.second?.let {
            visitables.add(it)
            notifyItemInserted(visitables.size - 1)
            // refresh the cache
            affiliateBannerPair = Pair(visitables.size - 1, it)
        }
    }

    fun removeAffiliateBanner() {
        val position = affiliateBannerPair?.first ?: -1
        if (position > -1) {
            visitables.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addRecomProducts(recommendations: List<Visitable<*>>) {
        val currentItemSize = visitables.size
        recommendations.forEach { item ->
            visitables.add(item)
            if (item is RecommendationTitleUiModel) recommendationTitlePosition = visitables.size
        }
        notifyItemRangeInserted(currentItemSize, recommendations.size)
    }

    fun addShopAds(cpmModel: CpmModel) {
        recommendationTitlePosition?.let {
            val shopAdsPosition = it + (cpmModel.data?.firstOrNull()?.cpm?.position ?: SHOPADS_POSITION_8)
            if (shopAdsPosition <= visitables.size) {
                visitables.add(shopAdsPosition, NotifTopAdsHeadline(cpmModel))
                shopAdsWidgetAdded = true
            }
        }
    }

    override fun showErrorNetwork() {
        if (!listener.hasFilter() && hasNotifOrderList()) {
            clearElementExceptOrderList()
            visitables.add(errorNetworkModel)
            notifyDataSetChanged()
        } else {
            super.showErrorNetwork()
        }
    }

    override fun isShowLoadingMore(): Boolean {
        return visitables.size > 0 && !hasNotifOrderList()
    }

    override fun clearAllElements() {
        if (!listener.hasFilter() && hasNotifOrderList()) {
            clearElementExceptOrderList()
            notifyDataSetChanged()
        } else {
            super.clearAllElements()
        }
    }

    fun removeAllItems() {
        visitables.clear()
        notifyDataSetChanged()
    }

    fun hasNotifOrderList(): Boolean {
        return visitables.getOrNull(0) is NotifOrderListUiModel
    }

    private fun clearElementExceptOrderList() {
        val orderListUiModel = visitables.getOrNull(0)
        visitables.clear()
        visitables.add(orderListUiModel)
    }

    fun updateOrRenderOrderListState(
        response: NotifOrderListResponse?,
        inserted: () -> Unit = {}
    ) {
        if (response == null) return
        if (hasNotifOrderList()) {
            val payload = PayloadOrderList(response.notifcenterNotifOrderList)
            getOrderListUiModel()?.update(payload.orderList)
            notifyItemChanged(0, payload)
        } else {
            visitables.add(0, response.notifcenterNotifOrderList)
            notifyItemInserted(0)
            inserted()
            updateSectionTitlePadding()
        }
    }

    private fun updateSectionTitlePadding() {
        val nextItem = visitables.getOrNull(1) ?: return
        if (nextItem is SectionTitleUiModel) {
            notifyItemChanged(1, SectionTitleViewHolder.PAYLOAD_UPDATE_PADDING)
        }
    }

    private fun getOrderListUiModel(): NotifOrderListUiModel? {
        return visitables.getOrNull(0) as? NotifOrderListUiModel
    }

    private inline fun <reified T : Visitable<NotificationTypeFactory>> getUpToDateUiModelPosition(
        lastKnownPosition: Int,
        element: T
    ): Pair<Int, T?> {
        val item = visitables.getOrNull(lastKnownPosition)
        if (item == element) {
            return Pair(lastKnownPosition, item as? T)
        }
        val updatePosition = visitables.indexOf(element)
        return Pair(updatePosition, visitables.getOrNull(updatePosition) as? T)
    }

    private fun isNotificationItem(position: Int): Boolean {
        val item = visitables.getOrNull(position) ?: return false
        return item is NotificationUiModel
    }

    private fun isNextItemDivider(position: Int): Boolean {
        val item = visitables.getOrNull(position + 1) ?: return false
        return item is BigDividerUiModel
    }

    fun updateFailedAddToWishlist(
        notification: NotificationUiModel,
        productData: ProductData,
        position: Int
    ) {
        val itemMetaData = getUpToDateUiModelPosition(position, notification)
        val itemPosition = itemMetaData.first
        if (itemPosition != RecyclerView.NO_POSITION) {
            productData.isWishlist = false
            val payload = PayloadWishlistState(productData, notification)
            notifyItemChanged(itemPosition, payload)
        }
    }

    companion object {
        private const val SHOPADS_POSITION_8 = 8
    }
}
