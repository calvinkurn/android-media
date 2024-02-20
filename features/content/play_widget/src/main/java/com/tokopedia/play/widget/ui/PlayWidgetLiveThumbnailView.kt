package com.tokopedia.play.widget.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.google.android.exoplayer2.Player
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.widget.databinding.ViewLiveThumbnailPlayerBinding
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.play.widget.liveindicator.di.DaggerPlayWidgetLiveIndicatorComponent
import com.tokopedia.play.widget.liveindicator.di.PlayWidgetLiveIndicatorComponent
import com.tokopedia.play.widget.player.VideoPlayer
import com.tokopedia.play_common.util.addImpressionListener
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import com.tokopedia.play.widget.R as playwidgetR

private typealias OnClickedHandler = () -> Unit
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

    private val componentFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DaggerPlayWidgetLiveIndicatorComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                ).build() as T
        }
    }

    private var mComponent: PlayWidgetLiveIndicatorComponent? = null

    private var mAnalyticModel: AnalyticModel? = null

    private var mListener: Listener? = null

    private val videoPlayer = VideoPlayer(context).apply {
        mute(true)
    }

    private val transitionState = MutableTransitionState(visibility == View.VISIBLE)

    private var mOnClicked by mutableStateOf({})

    private fun onAnimateVisibilityFinished(isVisible: Boolean) {
        showWithCondition(isVisible)
    }

    init {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                mComponent = createComponent()
            }

            override fun onViewDetachedFromWindow(view: View) {
                mComponent = null
            }
        })

        addImpressionListener {
            analytic { impressLiveThumbnail(it.channelId, it.productId, it.shopId) }
        }
        setOnClickListener(null)
    }

    init {
        videoPlayer.addPlayerListener(object : Player.EventListener {
            private var mIsIdleOrEnded = true

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isIdleOrEnded = playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED

                if (playWhenReady && playbackState == Player.STATE_READY) {
                    mListener?.onPreviewStarted(this@PlayWidgetLiveThumbnailView)
                } else if (isIdleOrEnded) {
                    run {
                        if (mIsIdleOrEnded) return@run
                        mListener?.onPreviewEnded(this@PlayWidgetLiveThumbnailView)
                    }
                }

                mIsIdleOrEnded = isIdleOrEnded
            }
        })
    }

    @Composable
    override fun Content() {
        NestTheme {
            PlayWidgetLiveThumbnail(
                videoPlayer.player,
                transitionState,
                ::onAnimateVisibilityFinished,
                mOnClicked,
            )
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClicked = {
            analytic { clickLiveThumbnail(it.channelId, it.productId, it.shopId) }
            l?.onClick(this)
        }
    }

    fun setAnalyticModel(model: AnalyticModel) {
        mAnalyticModel = model
    }

    private fun createComponent(): PlayWidgetLiveIndicatorComponent {
        val owner = findViewTreeViewModelStoreOwner() ?: error("VM Store Owner not found")
        return ViewModelProvider(owner, componentFactory).get()
    }

    private fun analytic(onAnalytic: PlayWidgetLiveIndicatorAnalytic.(AnalyticModel) -> Unit) {
        val model = mAnalyticModel ?: return
        mComponent?.getAnalytic()?.onAnalytic(model)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        visibility = View.GONE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) videoPlayer.stop()
    }

    fun playUrl(url: String, playFor: Duration = 3.seconds) {
        videoPlayer.loadUri(
            Uri.parse(url),
            config = VideoPlayer.Config(playFor, isLive = true)
        )
        videoPlayer.start()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun showAnimated() {
        visibility = View.VISIBLE
        transitionState.targetState = true
    }

    fun hideAnimated() {
        transitionState.targetState = false
    }

    interface Listener {
        fun onPreviewStarted(view: PlayWidgetLiveThumbnailView)
        fun onPreviewEnded(view: PlayWidgetLiveThumbnailView)
    }

    class DefaultListener : Listener {
        override fun onPreviewStarted(view: PlayWidgetLiveThumbnailView) {
            view.showAnimated()
        }

        override fun onPreviewEnded(view: PlayWidgetLiveThumbnailView) {
            view.hideAnimated()
        }
    }

    data class AnalyticModel(
        val channelId: String,
        val productId: String,
        val shopId: String,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PlayWidgetLiveThumbnail(
    player: Player,
    state: MutableTransitionState<Boolean>,
    onAnimateVisibilityFinished: (Boolean) -> Unit,
    onClickedHandler: OnClickedHandler,
) {
    val bgColor = colorResource(id = playwidgetR.color.play_widget_live_thumbnail_dms_bg)

    val density = LocalDensity.current
    val anchorSquareSize = with(density) { 12.dp.toPx() }
    val anchorSquareDiagonal = sqrt(2 * anchorSquareSize.pow(2))
    val totalHeight = 86.dp.plus(with(density) { (anchorSquareDiagonal / 4f).toDp() })

    LaunchedEffect(state) {
        snapshotFlow { state.currentState }
            .collectLatest {
                if (it == state.targetState) onAnimateVisibilityFinished(state.targetState)
            }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = scaleIn(
            initialScale = 0.7f,
            transformOrigin = TransformOrigin(0.5f, 1.0f),
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 0.7f,
            transformOrigin = TransformOrigin(0.5f, 1.0f),
        ) + fadeOut()
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
                .clickable(onClick = onClickedHandler)
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

@Preview
@Composable
private fun PlayWidgetLiveThumbnailPreview() {
    PlayWidgetLiveThumbnail(
        VideoPlayer(LocalContext.current).player,
        MutableTransitionState(false),
        {},
        {}
    )
}
