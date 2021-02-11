package com.tokopedia.play.widget.player

/**
 * Created by jegul on 21/10/20
 */
interface PlayVideoPlayerReceiver {

    fun setPlayer(player: PlayVideoPlayer?)

    fun getPlayer(): PlayVideoPlayer?

    fun isPlayable(): Boolean
}