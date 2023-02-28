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
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.ProductAttachmentUiModel.Companion.statusActive
import com.tokopedia.chat_common.data.ProductAttachmentUiModel.Companion.statusWarehouse
import com.tokopedia.chat_common.view.adapter.BaseChatAdapter
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
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwBubbleViewHolder
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview

/**
 * @author : Steven 02/01/19
 */
class TopChatRoomAdapter constructor(
    private val context: Context?,
    private val adapterTypeFactory: TopChatTypeFactoryImpl
) : BaseChatAdapter(adapterTypeFactory),
    ProductCarouselListAttachmentViewHolder.Listener,
    ProductBundlingCarouselViewHolder.Listener,
    AdapterListener {

    private val productCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private val productBundlingCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private var bottomMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDateIndex: Int? = null
    private val carouselViewPool = RecyclerView.RecycledViewPool()
    private val handler = Handler(Looper.getMainLooper())
    private var offset = 0
    private var offsetUiModelMap = ArrayMap<Visitable<*>, Int>()
    private var _srwUiModel: MutableLiveData<SrwBubbleUiModel?> = MutableLiveData()
    val srwUiModel: LiveData<SrwBubbleUiModel?> get() = _srwUiModel

    /**
     * String - the replyId or localId
     * BaseChatViewModel - the bubble/reply
     */
    private var replyMap: ArrayMap<String, BaseChatUiModel> = ArrayMap()

    override fun enableShowDate(): Boolean = false
    override fun enableShowTime(): Boolean = false

    fun hasPreviewOnList(localId: String?): Boolean {
        return localId != null && replyMap.contains(localId)
    }

    fun removePreviewMsg(localId: String) {
        if (!hasPreviewOnList(localId)) return
        val chatBubblePosition = getLocalIdMsgPosition(localId)
        if (chatBubblePosition == RecyclerView.NO_POSITION) return
        visitables.removeAt(chatBubblePosition)
        notifyItemRemoved(chatBubblePosition)
        replyMap.remove(localId)
    }

    fun updatePreviewUiModel(
        visitable: Visitable<*>,
        localId: String
    ) {
        val chatBubblePosition = getLocalIdMsgPosition(localId)
        if (chatBubblePosition == RecyclerView.NO_POSITION) return
        visitables[chatBubblePosition] = visitable
        notifyItemChanged(chatBubblePosition, Payload.REBIND)
    }

    fun updatePreviewState(
        localId: String
    ) {
        val chatBubblePosition = getLocalIdMsgPosition(localId)
        if (chatBubblePosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(chatBubblePosition, Payload.REBIND)
    }

    fun deleteMsg(replyTimeNano: String) {
        val chatBubblePosition = visitables.indexOfFirst {
            it is BaseChatUiModel && it.replyTime == replyTimeNano
        }
        if (chatBubblePosition == RecyclerView.NO_POSITION) return
        val msg = visitables[chatBubblePosition] as? BaseChatUiModel ?: return
        val deletedBubbleUiModel = MessageUiModel.Builder()
            .withBaseChatUiModel(msg)
            .withSafelySendableUiModel(msg)
            .withMarkAsDeleted()
            .build()
        visitables.removeAt(chatBubblePosition)
        visitables.add(chatBubblePosition, deletedBubbleUiModel)
        notifyItemChanged(chatBubblePosition)
    }

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return adapterTypeFactory.getItemViewType(visitables, position, default)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return adapterTypeFactory.createViewHolder(
            parent,
            viewType,
            this,
            this,
            this
        )
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

    fun addNewMessage(item: SendableUiModel) {
        if (item is Visitable<*> && item.localId.isNotEmpty()) {
            val indexToAdd = getOffsetSafely()
            replyMap[item.localId] = item
            visitables.add(indexToAdd, item)
            notifyItemInserted(indexToAdd)
        }
    }

    fun getBubblePosition(localId: String, replyTime: String): Int {
        return if (replyMap.contains(localId)) {
            getLocalIdMsgPosition(localId)
        } else {
            visitables.indexOfFirst {
                it is BaseChatUiModel && it.replyTime == replyTime
            }
        }
    }

    private fun getLocalIdMsgPosition(localId: String) = visitables.indexOfFirst {
        it is BaseChatUiModel && it.localId == localId
    }

    override fun isOpposite(adapterPosition: Int, isSender: Boolean): Boolean {
        val nextItem = visitables.getOrNull(adapterPosition + 1)
        val nextItemIsSender: Boolean = when (nextItem) {
            is SendableUiModel -> nextItem.isSender
            is ProductCarouselUiModel -> nextItem.isSender
            is ReviewUiModel -> nextItem.isSender
            else -> true
        }
        return isSender != nextItemIsSender
    }

    override fun getCarouselViewPool(): RecyclerView.RecycledViewPool {
        return carouselViewPool
    }

    override fun changeToFallbackUiModel(element: ReviewUiModel, lastKnownPosition: Int) {
        handler.post {
            postChangeToFallbackUiModel(lastKnownPosition, element)
        }
    }

    override fun removeErrorNetwork() {
        handler.post {
            super.removeErrorNetwork()
        }
    }

    override fun showLoading() {
        handler.post {
            super.showLoading()
        }
    }

    override fun addElement(position: Int, element: Visitable<*>?) {
        visitables.add(position, element)
        notifyItemInserted(position)
    }

    private fun postChangeToFallbackUiModel(lastKnownPosition: Int, element: ReviewUiModel) {
        val itemPair = getUpToDateUiModelPosition(lastKnownPosition, element)
        val position = itemPair.first
        if (position == RecyclerView.NO_POSITION) return
        itemPair.second ?: return
        val message = FallbackAttachmentUiModel.Builder()
            .withResponseFromGQL(element.reply)
            .withMsg(element.reply.attachment.fallback.html)
            .build()
        visitables[position] = message
        notifyItemChanged(position)
    }

    fun showRetryFor(model: ImageUploadUiModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is ImageUploadUiModel) {
            (visitables[position] as ImageUploadUiModel).isRetry = true
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

    fun addHeaderDateIfDifferent(preview: SendableUiModel) {
        if (preview is Visitable<*>) {
            addHeaderDateIfDifferent(preview as Visitable<*>)
        }
    }

    fun addHeaderDateIfDifferent(visitable: Visitable<*>) {
        if (visitable is BaseChatUiModel) {
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
        mapListChat(listChat)
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(0, listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun addTopData(listChat: List<Visitable<Any>>?) {
        if (listChat == null || listChat.isEmpty()) return
        mapListChat(listChat)
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun mapListChat(listChat: List<Visitable<*>>) {
        listChat.filterIsInstance(BaseChatUiModel::class.java)
            .forEach {
                val id = it.localId
                if (id.isEmpty()) return@forEach
                replyMap[id] = it
            }
    }

    fun reset() {
        visitables.clear()
        offsetUiModelMap.clear()
        bottomMostHeaderDate = null
        topMostHeaderDate = null
        topMostHeaderDateIndex = null
        notifyDataSetChanged()
    }

    fun isLastMessageBroadcast(): Boolean {
        if (visitables.isEmpty()) return false
        val latestMessage = visitables.first()
        return (latestMessage is MessageUiModel && latestMessage.isFromBroadCast()) ||
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
        return latestMessage is MessageUiModel &&
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

    fun removeViewHolder(element: Visitable<*>, position: Int) {
        val itemPair = getUpToDateUiModelPosition(
            position,
            element
        )
        val latestPosition = itemPair.first
        if (latestPosition != RecyclerView.NO_POSITION) {
            visitables.removeAt(latestPosition)
            notifyItemRemoved(latestPosition)
        }
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
        updateProductResult: UpdateProductStockResult,
        stockCount: Int,
        status: String
    ) {
        val itemPair = getUpToDateUiModelPosition(
            updateProductResult.lastKnownPosition,
            updateProductResult.product
        )
        val parentPair: Pair<Int, Visitable<*>?>? = updateProductResult.parentMetaData?.let {
            getUpToDateUiModelPosition(
                it.lastKnownPosition,
                it.uiModel
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
        lastKnownPosition: Int,
        review: ReviewUiModel,
        state: Int,
        reviewClickAt: Int
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
        updateSrwTopMargin()
    }

    fun hasSrwBubble(): Boolean {
        return _srwUiModel.value != null
    }

    fun removeSrwBubble() {
        val srwModel = srwUiModel.value ?: return
        val srwModelPosition = getUpToDateSrwUiModelPosition(srwModel) ?: return
        visitables.removeAt(srwModelPosition)
        notifyItemRemoved(srwModelPosition)
        offset--
        offsetUiModelMap.remove(srwModel)
        _srwUiModel.value = null
    }

    /**
     * Remove SRW bubble if [productId] is not relevant with
     * current visible SRW bubble
     */
    fun removeSrwBubble(productId: String) {
        val srwModel = srwUiModel.value ?: return
        if (!srwModel.isRelatedTo(productId)) {
            removeSrwBubble()
        }
    }

    fun isLastMsgSrwBubble(): Boolean {
        return visitables.getOrNull(0) is SrwBubbleUiModel
    }

    fun setSrwBubbleState(expanded: Boolean) {
        if (expanded) {
            expandSrwBubble()
        } else {
            collapseSrwBubble()
        }
    }

    fun collapseSrwBubble() {
        val srwModel = srwUiModel.value ?: return
        val srwModelPosition = getUpToDateSrwUiModelPosition(srwModel) ?: return
        srwModel.isExpanded = false
        notifyItemChanged(srwModelPosition, SrwBubbleViewHolder.Signal.COLLAPSED)
    }

    fun expandSrwBubble() {
        val srwModel = srwUiModel.value ?: return
        val srwModelPosition = getUpToDateSrwUiModelPosition(srwModel) ?: return
        srwModel.isExpanded = true
        notifyItemChanged(srwModelPosition, SrwBubbleViewHolder.Signal.EXPANDED)
    }

    fun findTickerPosition(replyId: String): Int {
        for (idx in visitables.indices) {
            if (isReplyIdMatch(idx, replyId)) {
                return idx
            }
        }
        return RecyclerView.NO_POSITION
    }

    private fun isReplyIdMatch(idx: Int, replyId: String): Boolean {
        val currentItem = visitables.getOrNull(idx)
        return if (currentItem == null || currentItem !is MessageUiModel) {
            false
        } else {
            /**
             * Check if replyId or localId same as ticker reminder replyId
             */
            currentItem.replyId == replyId || currentItem.localId == replyId
        }
    }

    private fun getUpToDateSrwUiModelPosition(
        uiModel: SrwBubbleUiModel
    ): Int? {
        var lastKnownPosition = offsetUiModelMap[uiModel] ?: return null
        val upToDateUiModelData = getUpToDateUiModelPosition(lastKnownPosition, uiModel)
        if (lastKnownPosition != upToDateUiModelData.first) {
            lastKnownPosition = upToDateUiModelData.first
        }
        return lastKnownPosition
    }

    private fun getOffsetSafely(): Int {
        return if (visitables.size < offset) {
            visitables.size
        } else {
            offset
        }
    }

    private fun updateSrwTopMargin() {
        val srwModel = srwUiModel.value ?: return
        val srwModelPosition = getUpToDateSrwUiModelPosition(srwModel) ?: return
        notifyItemChanged(srwModelPosition, SrwBubbleViewHolder.Signal.UPDATE_TOP_MARGIN)
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

    override fun saveProductBundlingCarouselState(position: Int, state: Parcelable?) {
        state?.let {
            productBundlingCarouselState[position] = it
        }
    }

    override fun getProductBundlingCarouselState(position: Int): Parcelable? {
        return productBundlingCarouselState[position]
    }
}
