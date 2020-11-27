package com.tokopedia.product.detail.view.widget

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.product.detail.view.viewholder.ProductVideoReceiver
import kotlinx.coroutines.*

/**
 * Created by Yehezkiel on 23/11/20
 */
class ProductVideoCoordinator(
        lifecycleOwner: LifecycleOwner? = null,
        val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate) : LifecycleObserver {

    private val scope = CoroutineScope(mainCoroutineDispatcher)
    private var productVideoJob: Job? = null

    private var videoPlayer: ProductExoPlayer? = null
    private var lastReceiverProduct: ProductVideoReceiver? = null
    private var productVideoDataModel: MutableList<ProductVideoDataModel> = mutableListOf()

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    fun onScrollChangedListener(viewPager: ViewPager2, position: Int) {
        productVideoJob?.cancel()
        productVideoJob = scope.launch(mainCoroutineDispatcher) {
            val currentReceiver = (viewPager.getChildAt(0) as? RecyclerView)?.findViewHolderForAdapterPosition(position) as? ProductVideoReceiver

            if (currentReceiver != null) {
                clearPlayerEntry(lastReceiverProduct)

                if (currentReceiver.getPlayer() == null) {
                    currentReceiver.setPlayer(videoPlayer)
                    val videoData = getVideoDataById(currentReceiver.getVideoId())
                    videoPlayer?.start(videoData?.videoUrl ?: "", videoData?.seekPosition ?: 0L, videoData?.isMute ?: true)
                }

                lastReceiverProduct = currentReceiver
            } else {
                pauseSaveLastPosition(lastReceiverProduct)
            }
        }
    }


    fun configureVideoCoordinator(context: Context, videoId: String, videoUrl: String) {
        if (videoPlayer == null) {
            videoPlayer = ProductExoPlayer(context)
        }

        val listVideoId = productVideoDataModel.map { it.videoId }
        if (videoId !in listVideoId) {
            productVideoDataModel.add(ProductVideoDataModel(videoId, videoUrl))
        }
    }

    fun configureVolume(isMute: Boolean, videoId: String) {
        videoPlayer?.toggleVideoVolume(isMute)
        getVideoDataById(videoId)?.isMute = isMute
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        productVideoJob?.cancel()
        videoPlayer?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        scope.coroutineContext.cancelChildren()
        videoPlayer?.destroy()
    }

    fun onStop() {
        videoPlayer?.stop()
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner, {
                it.lifecycle.addObserver(this)
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }

    private fun clearPlayerEntry(receiverProduct: ProductVideoReceiver?) {
        receiverProduct?.let {
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.stop()
            saveLastVideoPosition(it.getVideoId() ?: "")
            it.setPlayer(null)
        }
    }

    private fun pauseSaveLastPosition(receiverProduct: ProductVideoReceiver?) {
        receiverProduct?.let {
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.pause()
            saveLastVideoPosition(it.getVideoId())
            it.setPlayer(null)
        }
    }

    private fun saveLastVideoPosition(videoId: String) {
        productVideoDataModel.firstOrNull { it.videoId == videoId }?.seekPosition = videoPlayer?.getExoPlayer()?.currentPosition
                ?: 0L
    }

    private fun getVideoDataById(videoId: String): ProductVideoDataModel? {
        return productVideoDataModel.firstOrNull { it.videoId == videoId }
    }

    companion object {
        const val VIDEO_SWIPE_DELAY = 100L
    }
}

data class ProductVideoDataModel(
        val videoId: String = "",
        val videoUrl: String = "",
        var seekPosition: Long = 0,
        var isMute: Boolean = true //Mute
)