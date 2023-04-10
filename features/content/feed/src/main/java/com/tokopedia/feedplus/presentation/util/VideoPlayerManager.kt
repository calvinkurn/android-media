package com.tokopedia.feedplus.presentation.util

import android.content.Context
import android.util.Log
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer

/**
 * Created by kenny.hadisaputra on 03/04/23
 */
class VideoPlayerManager(
    private val context: Context,
) {

    private val videoMap = mutableMapOf<FeedExoPlayer, Boolean>()

    fun occupy(): FeedExoPlayer {
        val player = getOrCreatePlayer()
        videoMap[player] = true

        Log.d("VideoPlayerFactory", "${videoMap.entries.size} Video Player")

        return player
    }
    
    fun detach(player: FeedExoPlayer) {
        videoMap[player] = false
    }

    private fun release(player: FeedExoPlayer) {
        player.release()
        videoMap.remove(player)
    }

    fun releaseAll() {
        videoMap.keys.forEach { release(it) }
    }

    private fun getOrCreatePlayer(): FeedExoPlayer {
        return getUnoccupiedPlayer() ?: run {
            val player = createPlayer()
            videoMap[player] = false
            player
        }
    }

    private fun getUnoccupiedPlayer(): FeedExoPlayer? {
        return videoMap.entries.firstOrNull { !it.value }?.key
    }

    private fun createPlayer(): FeedExoPlayer {
        return FeedExoPlayer(context)
    }

}
