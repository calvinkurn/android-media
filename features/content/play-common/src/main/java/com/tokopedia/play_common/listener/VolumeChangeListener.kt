package com.tokopedia.play_common.listener

import com.tokopedia.play_common.media.VolumeInfo

interface VolumeChangeListener {
    fun onVolumeChanged(volumeInfo: VolumeInfo)
}