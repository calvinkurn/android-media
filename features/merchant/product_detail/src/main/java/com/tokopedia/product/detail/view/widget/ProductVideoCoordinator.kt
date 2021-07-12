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
        private val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate) : LifecycleObserver {

    private val scope = CoroutineScope(mainCoroutineDispatcher)
    private var productVideoJob: Job? = null

    private var videoPlayer: ProductExoPlayer? = null
    private var lastReceiverProduct: ProductVideoReceiver? = null
    private var productVideoDataModel: MutableList<ProductVideoDataModel> = mutableListOf()
    private var currentVideoId: String = ""

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    fun onScrollChangedListener(viewPager: ViewPager2, position: Int) {
        productVideoJob?.cancel()
        productVideoJob = scope.launch(mainCoroutineDispatcher) {
            val currentReceiver = (viewPager.getChildAt(0) as? RecyclerView)?.findViewHolderForAdapterPosition(position) as? ProductVideoReceiver
            val isCurrentReceiverVideoType = currentReceiver != null

            if (isCurrentReceiverVideoType) {
                if (lastReceiverProduct != currentReceiver) {
                    //Means it swipe to another video and we need to change video URL
                    clearPreviousVideoEntry(lastReceiverProduct)
                }

                //Set back the exoplayer to playerview
                currentReceiver!!.setPlayer(videoPlayer)
                val videoData = getVideoDataById(currentReceiver.getVideoId())
                videoPlayer?.start(videoData?.videoUrl ?: "", videoData?.seekPosition
                        ?: 0L, videoData?.isMute
                        ?: true, lastReceiverProduct != currentReceiver)
                currentVideoId = currentReceiver.getVideoId()

                lastReceiverProduct = currentReceiver
            } else {
                lastReceiverProduct?.let {
                    //Pause and save previous video player
                    pauseVideoAndSaveLastPosition()
                }
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

    fun configureVideoCoordinator(context: Context, data: ProductVideoDataModel) {
        if (videoPlayer == null) {
            videoPlayer = ProductExoPlayer(context)
        }

        val listVideoId = productVideoDataModel.map { it.videoId }
        if (data.videoId !in listVideoId) {
            productVideoDataModel.add(data)
        }
    }

    fun configureVolume(isMute: Boolean, videoId: String) {
        if (videoId.isEmpty()) return
        videoPlayer?.toggleVideoVolume(isMute)
        getVideoDataById(videoId)?.isMute = isMute
    }

    fun updateAndResume(newData: List<ProductVideoDataModel>) {
        val updatedCurrentVideoPosition = newData.firstOrNull { it.videoId == currentVideoId }?.seekPosition
                ?: 0
        val currentVideoData = productVideoDataModel.firstOrNull { it.videoId == currentVideoId }?.seekPosition
                ?: 0

        newData.forEachIndexed { index, i ->
            productVideoDataModel[index].isMute = i.isMute
            productVideoDataModel[index].seekPosition = i.seekPosition
        }

        //If they not resume video in video detail, just ignore it
        if (currentVideoData != updatedCurrentVideoPosition) {
            val currentData = productVideoDataModel.firstOrNull { it.videoId == currentVideoId }
            configureVolume(currentData?.isMute ?: false, currentData?.videoId ?: "")
            resumeAndSeekTo(updatedCurrentVideoPosition)
        }
    }

    fun getVideoDataModel(): List<ProductVideoDataModel> {
        return productVideoDataModel
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        productVideoJob?.cancel()
        videoPlayer?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        resetVideoCoordinator()
        scope.coroutineContext.cancelChildren()
    }

    fun onStopAndSaveLastPosition() {
        videoPlayer?.stop()
        saveLastVideoPosition(currentVideoId)
    }

    fun pauseVideoAndSaveLastPosition() {
        videoPlayer?.pause()
        saveLastVideoPosition(currentVideoId)
    }

    private fun resetVideoCoordinator() {
        onPause()
        videoPlayer?.destroy()
        currentVideoId = ""
        productVideoDataModel = mutableListOf()
        videoPlayer = null
        lastReceiverProduct = null
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

    private fun clearPreviousVideoEntry(receiverProduct: ProductVideoReceiver?) {
        receiverProduct?.let {
            videoPlayer?.setVideoStateListener(null)
            onStopAndSaveLastPosition()
            it.setPlayer(null)
        }
    }

    private fun resumeAndSeekTo(lastSeenVideoPosition: Long) {
        videoPlayer?.resumeAndSeekTo(lastSeenVideoPosition)
    }

    private fun getVideoDataById(videoId: String): ProductVideoDataModel? {
        return productVideoDataModel.firstOrNull { it.videoId == videoId }
    }

    private fun saveLastVideoPosition(videoId: String) {
        productVideoDataModel.firstOrNull { it.videoId == videoId }?.seekPosition = videoPlayer?.getExoPlayer()?.currentPosition
                ?: 0L
    }
}

data class ProductVideoDataModel(
        val videoId: String = "",
        val videoUrl: String = "",
        var seekPosition: Long = 0,
        var isMute: Boolean = true
)