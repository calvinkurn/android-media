package com.tokopedia.video_widget.carousel

interface CarouselVideoPlayerReceiver {

    fun setPlayer(player: CarouselVideoPlayer?)

    fun getPlayer(): CarouselVideoPlayer?

    fun isPlayable(): Boolean
}