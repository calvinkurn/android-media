package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.videoplayer.TokomemberDashVideoPlayer
import com.tokopedia.tokomember_seller_dashboard.view.videoplayer.VideoStateListener
import kotlinx.android.synthetic.main.tm_dash_video_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TIME_THREE_SEC = 3000L
private const val TIME_SECOND = 1000L
private const val MINUTE_IN_HOUR = 60

class TokomemberVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var videoPlayer: TokomemberDashVideoPlayer? = null
    private var productVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        View.inflate(context, R.layout.tm_dash_video_view, this)
        setClicks()
    }

    fun playVideo(url: String) {
        if (videoPlayer == null) {
            context?.let {
                productVideoJob = scope.launch {
                    videoPlayer = TokomemberDashVideoPlayer(it)
                    layout_video?.player = videoPlayer?.getExoPlayer()
                    videoPlayer?.start("https://vod.tokopedia.net/956dbf4f21834399a24926874064e2bd/1cfe279a2e2b454391a7a7ae2e780170-c76ce7414a3e828c29aab17379bef6fe-sd.mp4")

                    videoPlayer?.setVideoStateListener(object : VideoStateListener {
                        override fun onInitialStateLoading() {
                            loader?.show()
                        }
                        override fun onVideoReadyToPlay() {
                            loader?.hide()
                            timer_view?.visible()
                            var time = (videoPlayer?.getExoPlayer()?.duration ?: 0L) / TIME_SECOND
                            object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
                                override fun onTick(millisUntilFinished: Long) {
                                    time -= 1
                                    timer_view?.text =
                                        String.format(
                                            "%02d:%02d",
                                            (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                            time % MINUTE_IN_HOUR
                                        )
                                }
                                override fun onFinish() {
                                    timer_view?.gone()
                                }
                            }.start()
                        }
                        override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {

                        }
                    })
                }
            }
        }
    }

    private fun setClicks() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

}