package com.tokopedia.home.beranda.presentation.view.helper

interface ExoPlayerStatus {
    fun updateVideoMuted()
    fun isPlayerVideoMuted(): Boolean
    fun currentWindowIndex(): Int
    fun currentPosition(): Long
    fun duration(): Long
    fun isPlayerCreated(): Boolean
    fun isPlayerPrepared(): Boolean
}