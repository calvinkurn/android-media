package com.tokopedia.broadcaster.chucker.data.converter

import androidx.room.TypeConverter
import com.tokopedia.broadcaster.data.AudioType

class AudioTypeConverter {

    @TypeConverter
    fun toAudio(status: Int): AudioType {
        return when(status) {
            AudioType.PCM.status -> AudioType.PCM
            AudioType.MIC.status -> AudioType.MIC
            else -> AudioType.MIC
        }
    }

    @TypeConverter
    fun toInt(audioType: AudioType): Int {
        return when(audioType) {
            AudioType.PCM -> AudioType.PCM.status
            AudioType.MIC -> AudioType.MIC.status
        }
    }

}