package com.tokopedia.content.product.preview.view.components.player

import android.content.Context
import java.util.concurrent.ConcurrentHashMap

class ProductPreviewVideoPlayerManager(
    private val context: Context
) {

    private val videoMap = ConcurrentHashMap<ProductPreviewExoPlayer, String>()

    fun occupy(id: String): ProductPreviewExoPlayer {
        val player = getOrCreatePlayer(id)
        videoMap[player] = id
        return player
    }

    fun detach(player: ProductPreviewExoPlayer) {
        player.stop()
        videoMap[player] = ""
    }

    private fun release(player: ProductPreviewExoPlayer) {
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
        }?.key?.resume()
    }

    private fun getOrCreatePlayer(id: String): ProductPreviewExoPlayer {
        return getUnoccupiedPlayer(id) ?: run {
            val player = createPlayer()
            videoMap[player] = ""
            player
        }
    }

    private fun getUnoccupiedPlayer(id: String): ProductPreviewExoPlayer? {
        return videoMap.entries.firstOrNull { it.value.isEmpty() || it.value == id }?.key
    }

    private fun createPlayer(): ProductPreviewExoPlayer {
        return ProductPreviewExoPlayer(context)
    }

    fun getPlayerById(id: String): ProductPreviewExoPlayer? {
        return videoMap.entries.firstOrNull {
            it.value == id
        }?.key
    }
}
