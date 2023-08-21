package com.tokopedia.feedplus.presentation.util

import android.content.Context
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by kenny.hadisaputra on 03/04/23
 */
class VideoPlayerManager(
    private val context: Context,
) {

    private val videoMap = ConcurrentHashMap<FeedExoPlayer, String>()

    fun occupy(id: String): FeedExoPlayer {
        val player = getOrCreatePlayer()
        videoMap[player] = id

        return player
    }
    
    fun detach(player: FeedExoPlayer) {
        player.stop()
        videoMap[player] = ""
    }

    private fun release(player: FeedExoPlayer) {
        player.release()
        videoMap.remove(player)
    }

    fun releaseAll() {
        videoMap.keys.forEach { release(it) }
    }

    fun pause(id: String, shouldReset: Boolean = false) {
        val videoPlayer = videoMap.entries.firstOrNull {
            it.value == id
        }?.key
        videoPlayer?.pause()
        if (shouldReset) videoPlayer?.reset()
    }

    fun resume(id: String) {
        videoMap.entries.firstOrNull {
            it.value == id
        }?.key?.resume(shouldReset = false)
    }

    private fun getOrCreatePlayer(): FeedExoPlayer {
        return getUnoccupiedPlayer() ?: run {
            val player = createPlayer()
            videoMap[player] = ""
            player
        }
    }

    private fun getUnoccupiedPlayer(): FeedExoPlayer? {
        return videoMap.entries.firstOrNull { it.value.isEmpty() }?.key
    }

    private fun createPlayer(): FeedExoPlayer {
        return FeedExoPlayer(context)
    }

    fun getPlayerById(id: String) : FeedExoPlayer? {
        return videoMap.entries.firstOrNull {
            it.value == id
        }?.key
    }

}
