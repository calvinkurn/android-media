package com.tokopedia.play.widget.liveindicator.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.Player
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.widget.databinding.ViewLiveThumbnailPlayerBinding
import com.tokopedia.play.widget.liveindicator.di.rememberDaggerComponent
import com.tokopedia.play.widget.player.VideoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import com.tokopedia.play.widget.R as playwidgetR

class PlayWidgetLiveThumbnailView : AbstractComposeView {

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mAnalyticModel: AnalyticModel? by mutableStateOf(null)
    private var mImpressionTag by mutableStateOf("")

    private var mOnClicked by mutableStateOf({})

    private val thumbnailState = LiveThumbnailState(context)

    @Composable
    override fun Content() {
        NestTheme {
            PlayWidgetLiveThumbnail(
                state = thumbnailState,
                onClicked = mOnClicked,
                analyticModel = mAnalyticModel,
                impressionTag = mImpressionTag,
            )
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClicked = { l?.onClick(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        thumbnailState.stopPlayer()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) thumbnailState.stopPlayer()
    }

    fun setAnalyticModel(model: AnalyticModel) {
        mAnalyticModel = model
    }

    fun setImpressionTag(tag: String) {
        mImpressionTag = tag
    }

    fun playUrl(url: String, playFor: Duration = 3.seconds) {
        thumbnailState.playUrl(url, playFor)
    }

    data class AnalyticModel(
        val channelId: String,
        val productId: String,
        val shopId: String,
    )
}

@Composable
fun PlayWidgetLiveThumbnail(
    state: LiveThumbnailState,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    analyticModel: PlayWidgetLiveThumbnailView.AnalyticModel? = null,
    impressionTag: String = "",
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        state.observeVideoState().collectLatest { state ->
            when (state) {
                LiveThumbnailState.VideoState.Started -> isVisible = true
                LiveThumbnailState.VideoState.Ended -> isVisible = false
                else -> {}
            }
        }
    }

    PlayWidgetLiveThumbnail(
        player = state.player.player,
        isVisible = isVisible,
        onClicked = onClicked,
        modifier = modifier,
        analyticModel = analyticModel,
        impressionTag = impressionTag
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PlayWidgetLiveThumbnail(
    player: Player,
    isVisible: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    analyticModel: PlayWidgetLiveThumbnailView.AnalyticModel? = null,
    impressionTag: String = "",
) {
    val component = rememberDaggerComponent()

    val bgColor = colorResource(id = playwidgetR.color.play_widget_live_thumbnail_dms_bg)

    val density = LocalDensity.current
    val anchorSquareSize = with(density) { 12.dp.toPx() }
    val anchorSquareDiagonal = sqrt(2 * anchorSquareSize.pow(2))
    val totalHeight = 86.dp.plus(with(density) { (anchorSquareDiagonal / 4f).toDp() })

    LaunchedEffect(analyticModel, impressionTag) {
        analyticModel?.let {
            component.getAnalytic().impressLiveThumbnail(it.channelId, it.productId, it.shopId, impressionTag)
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            initialScale = 0.7f,
            transformOrigin = TransformOrigin(0.5f, 1.0f),
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 0.7f,
            transformOrigin = TransformOrigin(0.5f, 1.0f),
        ) + fadeOut(),
        modifier = modifier,
    ) {
        Box(
            Modifier
                .requiredWidth(55.dp)
                .requiredHeight(totalHeight)
                .drawWithCache {
                    onDrawBehind {
                        drawRoundRect(
                            color = bgColor,
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                            size = Size(width = size.width, height = 86.dp.toPx()),
                        )

                        translate(
                            top = 76.dp.toPx(),
                            left = size.width / 2f - 6.dp.toPx()
                        ) {
                            rotate(45f, pivot = Offset(6.dp.toPx(), 6.dp.toPx())) {
                                drawRoundRect(
                                    color = bgColor,
                                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                                    size = Size(width = 12.dp.toPx(), height = 12.dp.toPx()),
                                )
                            }
                        }
                    }
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    analyticModel?.let {
                        component
                            .getAnalytic()
                            .clickLiveThumbnail(it.channelId, it.productId, it.shopId)
                    }
                    onClicked()
                }
        ) {
            PlayWidgetLivePlayer(
                player,
                Modifier
                    .requiredHeight(86.dp)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}

@Composable
private fun PlayWidgetLivePlayer(
    player: Player,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = {
            ViewLiveThumbnailPlayerBinding.inflate(
                LayoutInflater.from(it)
            ).root.apply {
                this@apply.player = player
            }
        }, modifier = modifier
    ) {

    }
}

class LiveThumbnailState(val context: Context) {

    internal val player = VideoPlayer(context).apply {
        mute(true)
    }

    private var videoState by mutableStateOf(VideoState.Unknown)

    init {
        player.addPlayerListener(object : Player.EventListener {
            private var mIsIdleOrEnded = true

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isIdleOrEnded = playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED

                if (playWhenReady && playbackState == Player.STATE_READY) {
                    videoState = VideoState.Started
                } else if (isIdleOrEnded) {
                    run {
                        if (mIsIdleOrEnded) return@run
                        videoState = VideoState.Ended
                    }
                }

                mIsIdleOrEnded = isIdleOrEnded
            }
        })
    }

    internal fun observeVideoState(): Flow<VideoState> {
        return snapshotFlow { videoState }
    }

    fun playUrl(url: String, playFor: Duration = 3.seconds) {
        player.loadUri(
            Uri.parse(url),
            config = VideoPlayer.Config(playFor, isLive = true)
        )
        player.start()
    }

    fun stopPlayer() {
        player.stop()
    }

    @JvmInline
    value class VideoState private constructor(val state: Int) {
        companion object {
            val Unknown = VideoState(-1)
            val Started = VideoState(0)
            val Ended = VideoState(2)
        }
    }
}

@Composable
fun rememberLiveThumbnailState(): LiveThumbnailState {
    val context = LocalContext.current
    return remember(context) {
        LiveThumbnailState(context)
    }
}

@Preview
@Composable
private fun PlayWidgetLiveThumbnailPreview() {
    PlayWidgetLiveThumbnail(
        VideoPlayer(LocalContext.current).player,
        true,
        {},
    )
}
