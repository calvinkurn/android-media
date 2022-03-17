package com.tokopedia.video_widget.carousel

data class VideoCarouselConfigUiModel(
    val autoPlay: Boolean = DEFAULT_IS_AUTO_PLAY,
    val autoPlayAmount: Int = DEFAULT_AUTO_PLAY_AMOUNT, // maximum card with auto play
    val maxAutoPlayWifiDuration: Int = DEFAULT_AUTO_PLAY_DURATION, // maximum video duration,
) {
    companion object {
        private const val DEFAULT_IS_AUTO_PLAY = true
        private const val DEFAULT_AUTO_PLAY_AMOUNT = 5
        private const val DEFAULT_AUTO_PLAY_DURATION = 5
    }
}