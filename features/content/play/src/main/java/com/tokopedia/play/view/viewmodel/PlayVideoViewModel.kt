package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play_common.player.TokopediaPlayManager
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoViewModel @Inject constructor(
        private val playManager: TokopediaPlayManager
) : ViewModel() {

    private val _observableVODPlayer = MutableLiveData<ExoPlayer>()
    val observableVODPlayer: LiveData<ExoPlayer> = _observableVODPlayer

    fun startVideoWithUrlString(urlString: String) {
        playManager.playVideoWithString(urlString)
        _observableVODPlayer.value = playManager.videoPlayer
    }
}