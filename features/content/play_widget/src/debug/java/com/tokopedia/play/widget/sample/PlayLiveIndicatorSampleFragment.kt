package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.widget.databinding.FragmentPlayLiveIndicatorSampleBinding
import com.tokopedia.play.widget.liveindicator.ui.PlayWidgetLiveIndicator
import com.tokopedia.play.widget.liveindicator.ui.PlayWidgetLiveBadgeView
import com.tokopedia.play.widget.liveindicator.ui.PlayWidgetLiveThumbnailView
import kotlin.time.Duration.Companion.seconds

class PlayLiveIndicatorSampleFragment : Fragment() {

    private var _binding: FragmentPlayLiveIndicatorSampleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayLiveIndicatorSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewBased()
        setupComposeBased()
    }

    private fun setupViewBased() {
        binding.indicatorView.setAnalyticModel(
            PlayWidgetLiveBadgeView.AnalyticModel("12345", "67890", "13579")
        )
        binding.indicatorView.setImpressionTag("view_based")

        binding.thumbnailView.setAnalyticModel(
            PlayWidgetLiveThumbnailView.AnalyticModel("channel_id", "product_id", "shop_id")
        )

        binding.thumbnailView.setListener(PlayWidgetLiveThumbnailView.DefaultListener())

        binding.indicatorView.setOnClickListener {
            binding.thumbnailView.playUrl(
                "https://live-stream.tokopedia.net/live/v0.2/play_20240206070033_bfa3a284-c482-11ee-adc4-42010a294937/live/abr.m3u8",
                50.seconds
            )
        }
    }

    private fun setupComposeBased() {
        binding.composeView.setContent {
            NestTheme {
                PlayWidgetLiveIndicator(
                    onClicked = {},
                    Modifier.fillMaxSize(),
                    analyticModel = PlayWidgetLiveBadgeView.AnalyticModel("channel_id", "product_id", "shop_id"),
                    impressionTag = "compose_based"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
