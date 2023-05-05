package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.databinding.LayoutPlayWidgetCarouselBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by kenny.hadisaputra on 05/05/23
 */
class PlayWidgetCarouselView : ConstraintLayout {

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

    private val adapter = PlayWidgetCarouselAdapter()
    private val snapHelper = PagerSnapHelper()

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty
    private var mListener: Listener? = null

    init {
        val layoutManager = PlayWidgetCarouselLayoutManager(context)
        val itemDecoration = PlayWidgetCarouselItemDecoration(context)
        binding.rvChannels.layoutManager = layoutManager
        binding.rvChannels.adapter = adapter
        binding.rvChannels.addItemDecoration(itemDecoration)
        snapHelper.attachToRecyclerView(binding.rvChannels)

        binding.rvChannels.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (adapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val snappedView = snapHelper.findSnapView(layoutManager) ?: return
                val snappedPosition = recyclerView.getChildAdapterPosition(snappedView)

                if (snappedPosition < FAKE_COUNT_PER_SIDE) {
                    val distanceStep = adapter.itemCount + (2 * -1) - FAKE_COUNT_PER_SIDE
                    recyclerView.scrollBy(distanceStep * (snappedView.width + itemDecoration.getOffset()), 0)
                } else if (snappedPosition == adapter.itemCount - FAKE_COUNT_PER_SIDE) {
                    val distanceStep = adapter.itemCount - 2 * FAKE_COUNT_PER_SIDE
                    recyclerView.scrollBy(distanceStep * (-snappedView.width - itemDecoration.getOffset()), 0)
                }
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        val prevModel = mModel
        mModel = data

        setupHeader(data)
        setupChannels(data)
    }

    private fun setupHeader(data: PlayWidgetUiModel) {
        binding.tvTitle.text = data.title
        binding.iconChevron.setOnClickListener {
            mListener?.onChevronClicked(this)
        }
    }

    private fun setupChannels(data: PlayWidgetUiModel) {
        val channels = data.items.filterIsInstance<PlayWidgetChannelUiModel>()
        val dataWithFake = if (channels.isEmpty()) emptyList() else buildList {
            addAll(
                if (channels.size < FAKE_COUNT_PER_SIDE) List(
                    FAKE_COUNT_PER_SIDE
                ) { channels.last() }
                else channels.takeLast(FAKE_COUNT_PER_SIDE)
            )
            addAll(channels)
            addAll(
                if (channels.size < FAKE_COUNT_PER_SIDE) List(
                    FAKE_COUNT_PER_SIDE
                ) { channels.first() }
                else channels.take(FAKE_COUNT_PER_SIDE)
            )
        }
        adapter.submitList(dataWithFake) {
            if (channels.isEmpty()) return@submitList
            binding.rvChannels.smoothScrollToPosition(FAKE_COUNT_PER_SIDE)
        }
    }

    companion object {
        private const val FAKE_COUNT_PER_SIDE = 2
    }

    interface Listener {
        fun onChevronClicked(view: PlayWidgetCarouselView)
    }
}
