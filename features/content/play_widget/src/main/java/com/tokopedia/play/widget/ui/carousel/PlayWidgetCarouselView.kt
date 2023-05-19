package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.orZero
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
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (adapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val focusedWidgets = getFocusedWidgets(recyclerView)
                mWidgetInternalListener?.onFocusedWidgetsChanged(focusedWidgets)

//                val snappedView = snapHelper.findSnapView(layoutManager) ?: return
//                val snappedPosition = recyclerView.getChildAdapterPosition(snappedView)

//                if (snappedPosition < FAKE_COUNT_PER_SIDE) {
//                    val distanceStep = adapter.itemCount + (2 * -1) - FAKE_COUNT_PER_SIDE
//                    recyclerView.scrollBy(distanceStep * (snappedView.width + itemDecoration.getOffset()), 0)
//                } else if (snappedPosition == adapter.itemCount - FAKE_COUNT_PER_SIDE) {
//                    val distanceStep = adapter.itemCount - 2 * FAKE_COUNT_PER_SIDE
//                    recyclerView.scrollBy(distanceStep * (-snappedView.width - itemDecoration.getOffset()), 0)
//                }
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        val prevModel = mModel
        mModel = data

        setupChannels(data, scrollToFirstPosition = true)

        binding.rvChannels.addOneTimeGlobalLayoutListener {
            mWidgetInternalListener?.onWidgetCardsScrollChanged(binding.rvChannels)
        }
    }

    fun setWidgetListener(listener: Listener?) {
        mWidgetListener = listener
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        this.mWidgetInternalListener = listener
    }

    private fun setupChannels(data: PlayWidgetUiModel, scrollToFirstPosition: Boolean = false) {
        val channels = data.items.filterIsInstance<PlayWidgetChannelUiModel>()
//        val dataWithFake = if (channels.isEmpty()) emptyList() else buildList {
//            addAll(
//                if (channels.size < FAKE_COUNT_PER_SIDE) List(
//                    FAKE_COUNT_PER_SIDE
//                ) { channels.last() }
//                else channels.takeLast(FAKE_COUNT_PER_SIDE)
//            )
//            addAll(channels)
//            addAll(
//                if (channels.size < FAKE_COUNT_PER_SIDE) List(
//                    FAKE_COUNT_PER_SIDE
//                ) { channels.first() }
//                else channels.take(FAKE_COUNT_PER_SIDE)
//            )
//        }
        adapter.submitList(channels) {
            if (channels.isEmpty()) return@submitList
            val middlePosition = adapter.itemCount / 2
            val middleIndex = middlePosition % channels.size
            val nextInitialPosition = middlePosition + (channels.size - middleIndex)

            if (scrollToFirstPosition) {
                binding.rvChannels.scrollToPosition(nextInitialPosition - 1)
                binding.rvChannels.smoothScrollToPosition(nextInitialPosition)
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
        private const val FAKE_COUNT_PER_SIDE = 2
    }

    interface Listener : PlayWidgetRouterListener {
        fun onReminderClicked(
            view: PlayWidgetCarouselView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int,
        )
    }
}
