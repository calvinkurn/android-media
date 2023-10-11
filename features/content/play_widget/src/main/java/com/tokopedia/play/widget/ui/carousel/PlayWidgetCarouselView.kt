package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.carousel.PlayWidgetCarouselAnalyticListener
import com.tokopedia.play.widget.databinding.LayoutPlayWidgetCarouselBinding
import com.tokopedia.play.widget.ui.IPlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetRouterListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.WidgetInList
import com.tokopedia.play.widget.ui.model.ext.setMute
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCardCarouselChannelView
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCardCarouselUpcomingView
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCarouselAdapter
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCarouselViewHolder
import kotlin.math.min

/**
 * Created by kenny.hadisaputra on 05/05/23
 */
class PlayWidgetCarouselView : ConstraintLayout, IPlayWidgetView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = LayoutPlayWidgetCarouselBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private val videoContentListener = object : PlayWidgetCarouselViewHolder.VideoContent.Listener {
        override fun onChannelImpressed(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onImpressChannelCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }
        }

        override fun onChannelClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onClickChannelCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }

            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                item.appLink
            )
        }

        override fun onMuteButtonClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            shouldMute: Boolean,
            position: Int
        ) {
            if (!mModel.config.autoPlay) {
                onChannelClicked(view, item, position)
            } else {
                isPositionValid(position) {
                    mAnalyticListener?.onClickToggleMuteButton(
                        this@PlayWidgetCarouselView,
                        item,
                        mModel.config,
                        it
                    )
                }

                mIsMuted = shouldMute
                updateChannels()
            }
        }

        override fun onProductImpressed(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            product: PlayWidgetProduct,
            productPosition: Int,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onImpressProductCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    product,
                    productPosition,
                    it
                )
            }
        }

        override fun onProductClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            product: PlayWidgetProduct,
            productPosition: Int,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onClickProductCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    product,
                    productPosition,
                    it
                )
            }

            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                product.appLink
            )
        }

        override fun onPartnerClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onClickPartnerName(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }

            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                item.partner.appLink
            )
        }

        override fun onOverlayClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            val itemView = binding.rvChannels.findViewHolderForAdapterPosition(position)?.itemView
            if (itemView == null) {
                binding.rvChannels.smoothScrollToPosition(position)
            } else {
                val offset = itemDecoration.getHorizontalOffset()
                val itemWidth = view.width

                val currPosition = mSelectedWidgetPos
                val distance = (position - currPosition) * (offset + itemWidth)

                binding.rvChannels.smoothScrollBy(
                    distance,
                    0,
                    null,
                    500
                )
            }
        }
    }

    private val upcomingListener = object : PlayWidgetCarouselViewHolder.UpcomingContent.Listener {
        override fun onChannelImpressed(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onImpressChannelCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }
        }

        override fun onChannelClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onImpressChannelCard(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }

            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                item.appLink
            )
        }

        override fun onReminderClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onClickToggleReminderChannel(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it,
                    reminderType.reminded
                )

                mWidgetListener?.onReminderClicked(
                    this@PlayWidgetCarouselView,
                    item.channelId,
                    reminderType,
                    it
                )
            }
        }

        override fun onPartnerClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            isPositionValid(position) {
                mAnalyticListener?.onClickPartnerName(
                    this@PlayWidgetCarouselView,
                    item,
                    mModel.config,
                    it
                )
            }

            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                item.partner.appLink
            )
        }

        override fun onOverlayClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            val itemView = binding.rvChannels.findViewHolderForAdapterPosition(position)?.itemView
            if (itemView == null) {
                binding.rvChannels.smoothScrollToPosition(position)
            } else {
                val offset = itemDecoration.getHorizontalOffset()
                val itemWidth = view.width

                val currPosition = mSelectedWidgetPos
                val distance = (position - currPosition) * (offset + itemWidth)

                binding.rvChannels.smoothScrollBy(
                    distance,
                    0,
                    null,
                    500
                )
            }
        }
    }

    private val adapter = PlayWidgetCarouselAdapter(
        videoContentListener = videoContentListener,
        upcomingListener = upcomingListener
    )

    private val snapHelper = PagerSnapHelper()

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

    private var mWidgetListener: Listener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null
    private var mAnalyticListener: PlayWidgetCarouselAnalyticListener? = null

    private val itemDecoration = PlayWidgetCarouselItemDecoration(context)
    private val layoutManager = PlayWidgetCarouselLayoutManager(context)

    private var mSelectedWidgetPos = RecyclerView.NO_POSITION
    private var mIsMuted: Boolean = true

    init {
        binding.rvChannels.layoutManager = layoutManager
        binding.rvChannels.adapter = adapter
        binding.rvChannels.itemAnimator = null
        binding.rvChannels.addItemDecoration(itemDecoration)
        snapHelper.attachToRecyclerView(binding.rvChannels)

        binding.rvChannels.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (adapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val snappedView = snapHelper.findSnapView(layoutManager) ?: return
                val snappedPosition = recyclerView.getChildAdapterPosition(snappedView)

                // Case 1:
                // [3 4 0 1 3 4 0 1 3 4] 0 1 3 4 [0 1 3 4 0 1 3 4 0 1]

                // Case 2: (Fake Count = 7, itemCount = 5)
                // [4 5 0 1 3 4 5] 0 1 3 4 5 [0 1 3 4 5 0 1]

                val realItemSize = adapter.itemCount - (2 * FAKE_COUNT_PER_SIDE)
                if (snappedPosition < FAKE_COUNT_PER_SIDE) {
                    val stepToOriginalStart = FAKE_COUNT_PER_SIDE - snappedPosition
                    val modulus = stepToOriginalStart % realItemSize
                    val substractBy = if (modulus == 0) realItemSize else modulus
                    val stepToCorrectIndex = stepToOriginalStart + realItemSize - substractBy

                    recyclerView.scrollBy(stepToCorrectIndex * (snappedView.width + itemDecoration.getHorizontalOffset()), 0)
                    onWidgetSelected(snappedPosition + stepToCorrectIndex)
                } else if (snappedPosition >= FAKE_COUNT_PER_SIDE + realItemSize) {
                    val stepToOriginalEnd = snappedPosition - FAKE_COUNT_PER_SIDE - realItemSize + 1 // e.g pos 14
                    val modulus = stepToOriginalEnd % realItemSize
                    val addBy = if (modulus == 0) realItemSize else modulus
                    val stepToCorrectIndex = (-1 * stepToOriginalEnd) - realItemSize + addBy

                    recyclerView.scrollBy(stepToCorrectIndex * (snappedView.width + itemDecoration.getHorizontalOffset()), 0)
                    onWidgetSelected(snappedPosition + stepToCorrectIndex)
                } else {
                    onWidgetSelected(snappedPosition)
                }
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        mModel = data

        if (!mModel.config.autoPlay) mIsMuted = true // when new config is off, default to mute

        val channels = data.items.filterIsInstance<PlayWidgetChannelUiModel>()

        val currPosition = this.mSelectedWidgetPos
        val currItem = adapter.currentList.getOrNull(currPosition)

        val nextPosition = if (currItem != null) {
            val currChannelId = currItem.channel.channelId
            val newDataWithFake = getDataWithFake(channels)
            val newIndex = newDataWithFake.indexOfFirst { it.channelId == currChannelId }

            when {
                newIndex != -1 -> newIndex
                currPosition in FAKE_COUNT_PER_SIDE until channels.size -> currPosition
                else -> FAKE_COUNT_PER_SIDE
            }
        } else {
            FAKE_COUNT_PER_SIDE
        }

        updateChannels(
            channels,
            selectedPosition = nextPosition
        )
    }

    fun setWidgetListener(listener: Listener?) {
        mWidgetListener = listener
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        this.mWidgetInternalListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetCarouselAnalyticListener?) {
        mAnalyticListener = listener
    }

    private fun updateChannels(
        channels: List<PlayWidgetChannelUiModel> = mModel.items.filterIsInstance<PlayWidgetChannelUiModel>(),
        selectedPosition: Int = mSelectedWidgetPos
    ) {
        val dataWithFake = getDataWithFake(channels)
        val finalModel = dataWithFake.mapIndexed { index, channel ->
            val isSelected = index == selectedPosition
            PlayWidgetCarouselAdapter.Model(
                channel.setMute(mIsMuted),
                isSelected
            )
        }

        adapter.submitList(finalModel) {
            if (channels.isEmpty()) return@submitList
            roughlyScrollTo(selectedPosition) {
                onWidgetSelected(selectedPosition)

                val focusedWidgets = getFocusedWidgets(binding.rvChannels)
                mWidgetInternalListener?.onFocusedWidgetsChanged(focusedWidgets)
            }
        }
    }

    private fun getDataWithFake(channels: List<PlayWidgetChannelUiModel>): List<PlayWidgetChannelUiModel> {
        return if (channels.isEmpty()) {
            emptyList()
        } else {
            buildList {
                var leftFakeCount = FAKE_COUNT_PER_SIDE
                do {
                    val takeCount = min(leftFakeCount, channels.size)
                    addAll(
                        0,
                        channels.takeLast(takeCount).map {
                            it.copy(channelId = "fakeStart-${it.channelId}")
                        }
                    )
                    leftFakeCount -= takeCount
                } while (leftFakeCount > 0)

                addAll(channels)

                var rightFakeCount = FAKE_COUNT_PER_SIDE
                do {
                    val takeCount = min(rightFakeCount, channels.size)
                    addAll(
                        channels.take(takeCount).map {
                            it.copy(channelId = "fakeEnd-${it.channelId}")
                        }
                    )
                    rightFakeCount -= takeCount
                } while (rightFakeCount > 0)
            }
        }
    }

    private fun roughlyScrollTo(position: Int, onScrolled: () -> Unit) {
        binding.rvChannels.scrollToPosition(position)
        binding.rvChannels.post {
            val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
            val view = binding.rvChannels.findViewHolderForAdapterPosition(firstItem)?.itemView ?: return@post
            val scrollBy = ((2 * view.x + view.width) / 2 - binding.rvChannels.width / 2).toInt()

            binding.rvChannels.scrollBy(scrollBy, 0)

            onScrolled()
        }
    }

    private fun getFocusedWidgets(recyclerView: RecyclerView): List<WidgetInList> {
        val snappedView = snapHelper.findSnapView(recyclerView.layoutManager) ?: return emptyList()
        return listOf(
            WidgetInList(
                widget = snappedView,
                position = recyclerView.getChildAdapterPosition(snappedView)
            )
        )
    }

    private fun onWidgetSelected(position: Int) {
        if (mSelectedWidgetPos == position) return

        mSelectedWidgetPos = position
        updateChannels(selectedPosition = position)
    }

    private fun isPositionValid(positionInAdapter: Int, onValid: (Int) -> Unit) {
        val realPosition = positionInAdapter - FAKE_COUNT_PER_SIDE
        if (realPosition < 0 || realPosition >= mModel.items.size) return

        onValid(realPosition)
    }

    companion object {
        private const val FAKE_COUNT_PER_SIDE = 7
    }

    interface Listener : PlayWidgetRouterListener {
        fun onReminderClicked(
            view: PlayWidgetCarouselView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int
        ) {}
    }
}
