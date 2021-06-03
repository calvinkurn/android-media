package com.tokopedia.topchat.chatroom.view.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.ProductAttachmentViewModel.Companion.statusActive
import com.tokopedia.chat_common.data.ProductAttachmentViewModel.Companion.statusWarehouse
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.util.ChatRoomDiffUtil
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.BroadcastSpamHandlerViewHolder.Companion.PAYLOAD_UPDATE_STATE
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ReviewViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

/**
 * @author : Steven 02/01/19
 */
class TopChatRoomAdapter constructor(
    private val context: Context?,
    private val adapterTypeFactory: TopChatTypeFactoryImpl
) : BaseChatAdapter(adapterTypeFactory), ProductCarouselListAttachmentViewHolder.Listener,
    AdapterListener {

    private val productCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private var bottomMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDateIndex: Int? = null
    private val carouselViewPool = RecyclerView.RecycledViewPool()
    private val handler = Handler(Looper.getMainLooper())
    private var offset = 0
    private var offsetUiModelMap = ArrayMap<Visitable<*>, Int>()
    private var _srwUiModel: MutableLiveData<SrwBubbleUiModel?> = MutableLiveData()
    val srwUiModel: LiveData<SrwBubbleUiModel?> get() = _srwUiModel

    override fun enableShowDate(): Boolean = false
    override fun enableShowTime(): Boolean = false

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return adapterTypeFactory.getItemViewType(visitables, position, default)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return adapterTypeFactory.createViewHolder(parent, viewType, this, this)
    }

    override fun saveProductCarouselState(position: Int, state: Parcelable?) {
        state?.let {
            productCarouselState[position] = it
        }
    }

    override fun getSavedCarouselState(position: Int): Parcelable? {
        return productCarouselState[position]
    }

    override fun addElement(visitables: MutableList<out Visitable<Any>>?) {
        addTopData(visitables)
    }

    override fun isOpposite(adapterPosition: Int, isSender: Boolean): Boolean {
        val nextItem = visitables.getOrNull(adapterPosition + 1)
        val nextItemIsSender: Boolean = when (nextItem) {
            is SendableViewModel -> nextItem.isSender
            is ProductCarouselUiModel -> nextItem.isSender
            is ReviewUiModel -> nextItem.isSender
            else -> true
        }
        return isSender != nextItemIsSender
    }

    override fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool {
        return carouselViewPool
    }

    override fun changeToFallbackUiModel(element: ReviewUiModel, lastKnownPosition: Int) {
        handler.post {
            postChangeToFallbackUiModel(lastKnownPosition, element)
        }
    }

    private fun postChangeToFallbackUiModel(lastKnownPosition: Int, element: ReviewUiModel) {
        val itemPair = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = itemPair.first
        if (position == RecyclerView.NO_POSITION) return
        itemPair.second ?: return
        val message = FallbackAttachmentViewModel(element.reply)
        visitables[position] = message
        notifyItemChanged(position)
    }

    fun showRetryFor(model: ImageUploadViewModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is ImageUploadViewModel) {
            (visitables[position] as ImageUploadViewModel).isRetry = true
            notifyItemChanged(position)
        }
    }

    fun addWidgetHeader(widgets: List<Visitable<TopChatTypeFactory>>) {
        if (widgets.isEmpty()) return
        val currentLastPosition = visitables.lastIndex
        visitables.addAll(widgets)
        notifyItemRangeInserted(currentLastPosition, widgets.size)
    }

    fun removeLastHeaderDateIfSame(chatRoom: ChatroomViewModel) {
        if (chatRoom.listChat.isEmpty()) return
        assignLastHeaderDate()
        if (this.topMostHeaderDate?.date == chatRoom.latestHeaderDate) {
            topMostHeaderDateIndex?.let {
                visitables.removeAt(it)
                notifyItemRemoved(it)
            }
        }
    }

    fun removeLatestHeaderDateIfSame(newBottomList: ArrayList<Visitable<*>>) {
        if (newBottomList.isEmpty()) return
        val newListOldestDate = newBottomList[newBottomList.lastIndex]
        if (newListOldestDate is HeaderDateUiModel) {
            if (newListOldestDate == this.bottomMostHeaderDate) {
                newBottomList.removeAt(newBottomList.lastIndex)
            }
        }
    }

    fun setLatestHeaderDate(latestHeaderDate: String) {
        this.bottomMostHeaderDate = HeaderDateUiModel(latestHeaderDate)
    }

    fun addHeaderDateIfDifferent(visitable: Visitable<*>) {
        if (visitable is BaseChatViewModel) {
            val chatTime = visitable.replyTime?.toLong()?.div(SECONDS) ?: return
            val previousChatTime = bottomMostHeaderDate?.dateTimestamp ?: return
            if (!sameDay(chatTime, previousChatTime)) {
                val indexToAdd = getOffsetSafely()
                bottomMostHeaderDate = HeaderDateUiModel(chatTime)
                visitables.add(indexToAdd, bottomMostHeaderDate)
                notifyItemInserted(indexToAdd)
            }
        }
    }

    fun updateAttachmentView(
        firstVisible: Int,
        lastVisible: Int,
        attachments: ArrayMap<String, Attachment>
    ) {
        if (firstVisible > lastVisible) return
        if (firstVisible < 0 || lastVisible >= visitables.size) return
        for (itemPosition in firstVisible..lastVisible) {
            val item = visitables[itemPosition]
            if (item is DeferredAttachment && attachments.containsKey(item.id)) {
                val attachment = attachments[item.id] ?: continue
                if (item.isLoading) {
                    if (attachment is ErrorAttachment) {
                        item.syncError()
                    } else {
                        item.updateData(attachment.parsedAttributes)
                    }
                    notifyItemChanged(itemPosition, DeferredAttachment.PAYLOAD_DEFERRED)
                }
            }
            if (item is ProductCarouselUiModel || item is BroadCastUiModel) {
                notifyItemChanged(itemPosition, DeferredAttachment.PAYLOAD_DEFERRED)
            }
        }
    }

    private fun assignLastHeaderDate() {
        if (visitables.size <= 1) return
        val topMostDateHeaderItemIndex = visitables.size - 2
        val topMostDateHeaderItem = visitables[topMostDateHeaderItemIndex]
        if (topMostDateHeaderItem is HeaderDateUiModel) {
            topMostHeaderDate = topMostDateHeaderItem
            topMostHeaderDateIndex = topMostDateHeaderItemIndex
        }
    }

    private fun sameDay(chatTime: Long?, previousChatTime: Long?): Boolean {
        if (context == null || chatTime == null || previousChatTime == null) return false
        return compareTime(context, chatTime, previousChatTime)
    }

    fun dataExistAt(position: Int): Boolean {
        return visitables.getOrNull(position) != null
    }

    fun showBottomLoading() {
        if (visitables[0] !is LoadingMoreModel) {
            visitables.add(0, loadingMoreModel)
            notifyItemInserted(0)
        }
    }

    fun hideBottomLoading() {
        if (visitables[0] is LoadingMoreModel) {
            visitables.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun addBottomData(listChat: List<Visitable<*>>) {
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(0, listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun addTopData(listChat: MutableList<out Visitable<Any>>?) {
        if (listChat == null || listChat.isEmpty()) return
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.clear()
        bottomMostHeaderDate = null
        topMostHeaderDate = null
        topMostHeaderDateIndex = null
        notifyDataSetChanged()
    }

    fun isLastMessageBroadcast(): Boolean {
        if (visitables.isEmpty()) return false
        val latestMessage = visitables.first()
        return (latestMessage is MessageViewModel && latestMessage.isFromBroadCast()) ||
                latestMessage is BroadcastSpamHandlerUiModel ||
                latestMessage is BroadCastUiModel
    }

    fun addBroadcastSpamHandler(): Int {
        var insertedPosition = RecyclerView.NO_POSITION
        if (visitables.isEmpty()) return insertedPosition
        val latestMessage = visitables.first()
        if (isFromBroadcast(latestMessage) || isFromUnifiedBroadcast(latestMessage)) {
            val spamHandlerModel = BroadcastSpamHandlerUiModel()
            insertedPosition = 0
            visitables.add(insertedPosition, spamHandlerModel)
            notifyItemInserted(insertedPosition)
        }
        return insertedPosition
    }

    private fun isFromUnifiedBroadcast(latestMessage: Visitable<*>?): Boolean {
        return latestMessage is BroadCastUiModel && latestMessage.isOpposite
    }

    private fun isFromBroadcast(latestMessage: Visitable<*>?): Boolean {
        return latestMessage is MessageViewModel &&
                latestMessage.isFromBroadCast() &&
                !latestMessage.isSender
    }

    fun removeBroadcastHandler(element: BroadcastSpamHandlerUiModel) {
        val elementIndex = visitables.indexOf(element)
        if (elementIndex == RecyclerView.NO_POSITION || elementIndex >= visitables.size) return
        visitables.removeAt(elementIndex)
        notifyItemRemoved(elementIndex)
    }

    fun removeBroadcastHandler() {
        val bcHandlerPost = findBroadcastHandlerPosition()
        if (bcHandlerPost != RecyclerView.NO_POSITION) {
            removeBroadcastHandler(bcHandlerPost)
        }
    }

    fun updateBroadcastHandlerState(element: BroadcastSpamHandlerUiModel) {
        val elementIndex = visitables.indexOf(element)
        if (elementIndex == RecyclerView.NO_POSITION || elementIndex >= visitables.size) return
        notifyItemChanged(elementIndex, PAYLOAD_UPDATE_STATE)
    }

    fun findBroadcastHandler(): BroadcastSpamHandlerUiModel? {
        val bcHandlerPost = findBroadcastHandlerPosition()
        if (bcHandlerPost != RecyclerView.NO_POSITION) {
            return visitables[bcHandlerPost] as BroadcastSpamHandlerUiModel
        }
        return null
    }


    fun updateReviewState(
        review: ReviewUiModel,
        lastKnownPosition: Int,
        reviewClickAt: Int,
        state: Int
    ) {
        handler.post {
            postUpdateReviewState(lastKnownPosition, review, state, reviewClickAt)
        }
    }

    fun updateProductStock(
        updateProductResult: UpdateProductStockResult, stockCount: Int, status: String
    ) {
        val itemPair = getUpToDateUiModelPosition(
            updateProductResult.lastKnownPosition, updateProductResult.product
        )
        val parentPair: Pair<Int, Visitable<*>?>? = updateProductResult.parentMetaData?.let {
            getUpToDateUiModelPosition(
                it.lastKnownPosition, it.uiModel
            )
        }
        val position = if (parentPair != null && parentPair.first != RecyclerView.NO_POSITION) {
            parentPair.first
        } else {
            itemPair.first
        }
        if (position == RecyclerView.NO_POSITION) return
        val item = itemPair.second ?: updateProductResult.product
        when (status) {
            ProductStatus.ACTIVE.name -> {
                item.status = statusActive
                item.remainingStock = stockCount
            }
            ProductStatus.INACTIVE.name -> {
                item.remainingStock = 0
                item.status = statusWarehouse
            }
        }
        val payload = SingleProductAttachmentContainer.PayloadUpdateStock(item.productId)
        notifyItemChanged(position, payload)
    }

    private fun postUpdateReviewState(
        lastKnownPosition: Int, review: ReviewUiModel,
        state: Int, reviewClickAt: Int
    ) {
        val itemPair = getUpToDateUiModelPosition(lastKnownPosition, review)
        val position = itemPair.first
        if (position == RecyclerView.NO_POSITION) return
        val item = itemPair.second ?: return
        when (state) {
            ReputationCommonConstants.REVIEWED -> {
                item.reviewCard.apply {
                    isReviewed = true
                    rating = reviewClickAt.toFloat()
                    reviewUrl = UriUtil.buildUri(ApplinkConst.REVIEW_DETAIL, feedBackId)
                }
                notifyItemChanged(position, ReviewViewHolder.PAYLOAD_REVIEWED)
            }
            else -> {
                notifyItemChanged(position, ReviewViewHolder.PAYLOAD_NOT_REVIEWED)
            }
        }
    }

    fun resetReviewState(review: ReviewUiModel, lastKnownPosition: Int) {
        handler.post {
            postResetReviewState(lastKnownPosition, review)
        }
    }

    fun addSrwBubbleUiModel(
        srwState: SrwFrameLayout.SrwState?,
        products: List<SendablePreview>
    ) {
        srwState ?: return
        val srwModel = SrwBubbleUiModel(srwState, products)
        val indexToAdd = getOffsetSafely()
        _srwUiModel.value = srwModel
        visitables.add(indexToAdd, srwModel)
        notifyItemInserted(indexToAdd)
        offset++
        offsetUiModelMap[srwModel] = indexToAdd
    }

    override fun addElement(item: Visitable<*>) {
        val indexToAdd = getOffsetSafely()
        visitables.add(indexToAdd, item)
        notifyItemInserted(indexToAdd)
    }

    fun removeSrwBubble() {
        val srwModel = srwUiModel.value ?: return
        var lastKnownPosition = offsetUiModelMap[srwModel] ?: return
        val upToDateUiModelData = getUpToDateUiModelPosition(lastKnownPosition, srwModel)
        if (lastKnownPosition != upToDateUiModelData.first) {
            lastKnownPosition = upToDateUiModelData.first
        }
        visitables.removeAt(lastKnownPosition)
        notifyItemRemoved(lastKnownPosition)
        offset--
        offsetUiModelMap.remove(srwModel)
        _srwUiModel.value = null
    }

    fun isLastMsgSrwBubble(): Boolean {
        return visitables.getOrNull(0) is SrwBubbleUiModel
    }

    private fun getOffsetSafely(): Int {
        return if (visitables.size < offset) {
            visitables.size
        } else {
            offset
        }
    }

    private fun postResetReviewState(lastKnownPosition: Int, review: ReviewUiModel) {
        val itemPair = getUpToDateUiModelPosition(lastKnownPosition, review)
        val position = itemPair.first
        if (position == RecyclerView.NO_POSITION) return
        itemPair.second ?: return
        notifyItemChanged(position, ReviewViewHolder.PAYLOAD_NOT_REVIEWED)
    }

    private fun removeBroadcastHandler(index: Int) {
        if (index >= visitables.size) return
        val item = visitables[index]
        if (item is BroadcastSpamHandlerUiModel) {
            visitables.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun findBroadcastHandlerPosition(): Int {
        if (!isPossibleBroadcastHandlerExist()) return RecyclerView.NO_POSITION
        for (index in 0..1) {
            if (isBroadcastHandlerPosition(index) != RecyclerView.NO_POSITION) return index
        }
        return RecyclerView.NO_POSITION
    }

    private fun isBroadcastHandlerPosition(index: Int): Int {
        if (index >= visitables.size) return RecyclerView.NO_POSITION
        val item = visitables[index]
        if (item is BroadcastSpamHandlerUiModel) {
            return index
        }
        return RecyclerView.NO_POSITION
    }

    private fun isPossibleBroadcastHandlerExist(): Boolean {
        return visitables.isNotEmpty() && visitables.size >= 2
    }

    /**
     * return Pair<Int, T?>.
     * the first Int is the latest / up-to-date ui model position
     * the second T? is the ui model at the up-to-date position
     */
    private inline fun <reified T : Visitable<*>> getUpToDateUiModelPosition(
        lastKnownPosition: Int, element: T
    ): Pair<Int, T?> {
        val item = visitables.getOrNull(lastKnownPosition)
        if (item == element) {
            return Pair(lastKnownPosition, item as? T)
        }
        val updatePosition = visitables.indexOf(element)
        return Pair(updatePosition, visitables.getOrNull(updatePosition) as? T)
    }
}