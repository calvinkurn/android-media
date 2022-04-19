package com.tokopedia.broadcaster.log.data.converter

import androidx.room.TypeConverter
import com.tokopedia.broadcaster.data.BitrateMode

class BitrateModeConverter {

    @TypeConverter
    fun toBitrateMode(status: String): BitrateMode {
        return when(status) {
            BitrateMode.LadderAscend.status -> BitrateMode.LadderAscend
            BitrateMode.LogarithmicDescend.status -> BitrateMode.LogarithmicDescend
            else -> BitrateMode.LadderAscend
        }
    }

    @TypeConverter
    fun toInt(mode: BitrateMode): String {
        return when(mode) {
            BitrateMode.LadderAscend -> BitrateMode.LadderAscend.status
            BitrateMode.LogarithmicDescend -> BitrateMode.LogarithmicDescend.status
        }
    }

}