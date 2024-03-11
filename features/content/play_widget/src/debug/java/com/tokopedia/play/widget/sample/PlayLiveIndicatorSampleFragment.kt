package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.widget.databinding.FragmentPlayLiveIndicatorSampleBinding
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.play.widget.liveindicator.ui.PlayWidgetLiveBadge
import com.tokopedia.play.widget.liveindicator.ui.PlayWidgetLiveThumbnail
import com.tokopedia.play.widget.liveindicator.ui.rememberLiveThumbnailState
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
            PlayWidgetLiveIndicatorAnalytic.Model("12345", "67890", "13579")
        )
        binding.indicatorView.setImpressionTag("view_based")

        binding.thumbnailView.setAnalyticModel(
            PlayWidgetLiveIndicatorAnalytic.Model("12345", "67890", "13579")
        )
        binding.thumbnailView.setImpressionTag("view_based")

        binding.indicatorView.setOnClickListener {
            binding.thumbnailView.playUrl(
                "https://live-stream.tokopedia.net/live/v0.2/play_20240206070033_bfa3a284-c482-11ee-adc4-42010a294937/live/abr.m3u8",
                50.seconds
            )
        }
    }

    private fun setupComposeBased() {
        binding.composeView.setContent {

            val thumbnailState = rememberLiveThumbnailState()

            NestTheme {
                ConstraintLayout {
                    val (liveIndicator, liveThumbnail) = createRefs()

                    PlayWidgetLiveBadge(
                        onClicked = {
                            thumbnailState.playUrl(
                                "https://live-stream.tokopedia.net/live/v0.2/play_20240206070033_bfa3a284-c482-11ee-adc4-42010a294937/live/abr.m3u8",
                                50.seconds
                            )
                        },
                        analyticModel = PlayWidgetLiveIndicatorAnalytic.Model("12345", "67890", "13579"),
                        impressionTag = "compose_based",
                        modifier = Modifier
                            .constrainAs(liveIndicator) {
                                centerTo(parent)
                            }
                    )

                    PlayWidgetLiveThumbnail(
                        state = thumbnailState,
                        onClicked = {},
                        analyticModel = PlayWidgetLiveIndicatorAnalytic.Model("12345", "67890", "13579"),
                        impressionTag = "compose_based",
                        modifier = Modifier.constrainAs(liveThumbnail) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(liveIndicator.top, margin = 4.dp)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
