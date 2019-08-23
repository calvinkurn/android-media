package com.tokopedia.videoplayer.utils

sealed class PlayerException {
    //video source not found
    object SourceNotFound: PlayerException()

    //invalid initialize of player
    object PlayerInitialize: PlayerException()

    //cannot steam a media into ExoPlayer
    object ExoPlayer: PlayerException()
}