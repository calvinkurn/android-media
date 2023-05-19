package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.widget.databinding.LayoutPlayWidgetCarouselBinding
import com.tokopedia.play.widget.ui.IPlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetRouterListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.WidgetInList
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCardCarouselChannelView
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCardCarouselUpcomingView
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCarouselAdapter
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCarouselViewHolder
import kotlin.math.abs
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

    private var mIsMuted: Boolean = true

    private val videoContentListener = object : PlayWidgetCarouselViewHolder.VideoContent.Listener {
        override fun onChannelImpressed(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {

        }

        override fun onMuteButtonClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            shouldMute: Boolean,
            position: Int
        ) {
            mIsMuted = shouldMute
            //TODO("Update mute button for the selected channel")
        }

        override fun onProductClicked(
            view: PlayWidgetCardCarouselChannelView,
            product: PlayWidgetProduct,
            position: Int
        ) {
            mWidgetListener?.onWidgetOpenAppLink(
                this@PlayWidgetCarouselView,
                product.appLink,
            )
        }
    }

    private val upcomingListener = object : PlayWidgetCarouselViewHolder.UpcomingContent.Listener {
        override fun onChannelImpressed(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {

        }

        override fun onReminderClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        ) {
            mWidgetListener?.onReminderClicked(
                this@PlayWidgetCarouselView,
                item.channelId,
                reminderType,
                position,
            )
        }
    }

    private val adapter = PlayWidgetCarouselAdapter(
        videoContentListener = videoContentListener,
        upcomingListener = upcomingListener,
    )

    private val snapHelper = PagerSnapHelper()

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

    private var mWidgetInternalListener: PlayWidgetInternalListener? = null
    private var mWidgetListener: Listener? = null

    private val itemDecoration = PlayWidgetCarouselItemDecoration(context)

    init {
        val layoutManager = PlayWidgetCarouselLayoutManager(context)
        binding.rvChannels.layoutManager = layoutManager
        binding.rvChannels.adapter = adapter
        binding.rvChannels.addItemDecoration(itemDecoration)
        snapHelper.attachToRecyclerView(binding.rvChannels)

        binding.rvChannels.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var mSnappedPosition = RecyclerView.NO_POSITION

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (adapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val focusedWidgets = getFocusedWidgets(recyclerView)
                mWidgetInternalListener?.onFocusedWidgetsChanged(focusedWidgets)

                val snappedView = snapHelper.findSnapView(layoutManager) ?: return
                val snappedPosition = recyclerView.getChildAdapterPosition(snappedView)

                //Case 1:
                //[3 4 0 1 3 4 0 1 3 4] 0 1 3 4 [0 1 3 4 0 1 3 4 0 1]

                //Case 2: (Fake Count = 7, itemCount = 5)
                //[4 5 0 1 3 4 5] 0 1 3 4 5 [0 1 3 4 5 0 1]

                val realItemSize = adapter.itemCount - (2 * FAKE_COUNT_PER_SIDE)
                if (snappedPosition < FAKE_COUNT_PER_SIDE) {
                    val stepToOriginalStart = FAKE_COUNT_PER_SIDE - snappedPosition
                    val modulus = stepToOriginalStart % realItemSize
                    val substractBy = if (modulus == 0) realItemSize else modulus
                    val stepToCorrectIndex = stepToOriginalStart + realItemSize - substractBy

                    recyclerView.scrollBy(stepToCorrectIndex * (snappedView.width + itemDecoration.getOffset()), 0)
                } else if (snappedPosition >= FAKE_COUNT_PER_SIDE + realItemSize) {
                    val stepToOriginalEnd = snappedPosition - FAKE_COUNT_PER_SIDE - realItemSize + 1 //e.g pos 14
                    val modulus = stepToOriginalEnd % realItemSize
                    val addBy = if (modulus == 0) realItemSize else modulus
                    val stepToCorrectIndex = (-1 * stepToOriginalEnd) - realItemSize + addBy

                    recyclerView.scrollBy(stepToCorrectIndex * (snappedView.width + itemDecoration.getOffset()), 0)
                }

                if (snappedPosition == mSnappedPosition) return
                adapter.setSelected(snappedPosition)

                mSnappedPosition = snappedPosition
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        val prevModel = mModel
        mModel = data

        setupChannels(data, scrollToFirstPosition = adapter.currentList.size == 0)
    }

    fun setWidgetListener(listener: Listener?) {
        mWidgetListener = listener
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        this.mWidgetInternalListener = listener
    }

    private fun setupChannels(data: PlayWidgetUiModel, scrollToFirstPosition: Boolean = false) {
        val channels = data.items.filterIsInstance<PlayWidgetChannelUiModel>()
        val dataWithFake = if (channels.isEmpty()) emptyList() else buildList {

            var leftFakeCount = FAKE_COUNT_PER_SIDE
            do {
                val takeCount = min(leftFakeCount, channels.size)
                addAll(
                    0,
                    channels.takeLast(takeCount)
                )
                leftFakeCount -= takeCount
            } while (leftFakeCount > 0)

            addAll(channels)

            var rightFakeCount = FAKE_COUNT_PER_SIDE
            do {
                val takeCount = min(rightFakeCount, channels.size)
                addAll(
                    channels.take(takeCount)
                )
                rightFakeCount -= takeCount
            } while (rightFakeCount > 0)
        }

        adapter.submitList(dataWithFake) {
            if (channels.isEmpty()) return@submitList
            if (scrollToFirstPosition) {
                binding.rvChannels.scrollToPosition(FAKE_COUNT_PER_SIDE - 1)
                binding.rvChannels.smoothScrollToPosition(FAKE_COUNT_PER_SIDE)
            }
        }
    }

    private fun getFocusedWidgets(recyclerView: RecyclerView): List<WidgetInList> {
        val snappedView = snapHelper.findSnapView(recyclerView.layoutManager) ?: return emptyList()
        return listOf(
            WidgetInList(
                widget = snappedView,
                position = recyclerView.getChildAdapterPosition(snappedView),
            )
        )
    }

    companion object {
        private const val FAKE_COUNT_PER_SIDE = 10
    }

    interface Listener : PlayWidgetRouterListener {
        fun onReminderClicked(
            view: PlayWidgetCarouselView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int,
        ) {}
    }
}
