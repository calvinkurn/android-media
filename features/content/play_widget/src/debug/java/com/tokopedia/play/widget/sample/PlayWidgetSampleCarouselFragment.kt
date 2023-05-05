package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.FragmentPlayWidgetSampleCarouselBinding
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselAdapter
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselItemDecoration
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselLayoutManager
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetSampleCarouselFragment : Fragment() {

    private var _binding: FragmentPlayWidgetSampleCarouselBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { PlayWidgetCarouselAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayWidgetSampleCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = PlayWidgetCarouselLayoutManager(requireContext())
        binding.rvCarousel.layoutManager = layoutManager
        binding.rvCarousel.adapter = adapter
        binding.rvCarousel.addItemDecoration(PlayWidgetCarouselItemDecoration(requireContext()))
        binding.rvCarousel.itemAnimator = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvCarousel)

        val data = getSampleData().flatMap {
            it.model.items.filterIsInstance<PlayWidgetChannelUiModel>()
        }
        val dataWithFake = if (data.isEmpty()) emptyList() else buildList {
            addAll(
                if (data.size < FAKE_COUNT_PER_SIDE) List(FAKE_COUNT_PER_SIDE) { data.last() }
                else data.takeLast(FAKE_COUNT_PER_SIDE)
            )
            addAll(data)
            addAll(
                if (data.size < FAKE_COUNT_PER_SIDE) List(FAKE_COUNT_PER_SIDE) { data.first() }
                else data.take(FAKE_COUNT_PER_SIDE)
            )
        }
        adapter.submitList(dataWithFake) {
            if (data.isEmpty()) return@submitList
            binding.rvCarousel.smoothScrollToPosition(FAKE_COUNT_PER_SIDE)
        }

        val offset12 = resources.getDimensionPixelOffset(R.dimen.play_widget_dp_12)

        binding.rvCarousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (adapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val snappedView = snapHelper.findSnapView(layoutManager) ?: return
                val snappedPosition = recyclerView.getChildAdapterPosition(snappedView)

                if (snappedPosition < FAKE_COUNT_PER_SIDE) {
                    val distanceStep = adapter.itemCount + (2 * -1) - FAKE_COUNT_PER_SIDE
                    recyclerView.scrollBy(distanceStep * (snappedView.width + offset12), 0)
                } else if (snappedPosition == adapter.itemCount - FAKE_COUNT_PER_SIDE) {
                    val distanceStep = adapter.itemCount - 2 * FAKE_COUNT_PER_SIDE
                    recyclerView.scrollBy(distanceStep * (-snappedView.width - offset12), 0)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSampleData(): List<PlayWidgetState> {
        return listOf(
            PlayWidgetState(
                model = PlayWidgetUiMock.getSamplePlayWidget(
                    items = List(5) {
                        PlayWidgetUiMock.getSampleChannelModel(PlayWidgetChannelType.Vod).copy(
                            channelId = it.toString(),
                            title = "Channel $it"
                        )
                    }
                )
            )
        )
    }

    companion object {
        private const val FAKE_COUNT_PER_SIDE = 2
    }
}
