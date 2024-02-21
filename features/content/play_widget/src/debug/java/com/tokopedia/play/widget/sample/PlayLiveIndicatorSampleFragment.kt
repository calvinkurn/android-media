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
import com.tokopedia.play.widget.ui.PlayWidgetLiveIndicator
import com.tokopedia.play.widget.ui.PlayWidgetLiveIndicatorView
import com.tokopedia.play.widget.ui.PlayWidgetLiveThumbnailView
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
        binding.indicatorView.setAnalyticModel(
            PlayWidgetLiveIndicatorView.AnalyticModel("channel_id", "product_id", "shop_id")
        )
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

        binding.composeView.setContent {
            NestTheme {
                PlayWidgetLiveIndicator(
                    onClicked = {},
                    Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
