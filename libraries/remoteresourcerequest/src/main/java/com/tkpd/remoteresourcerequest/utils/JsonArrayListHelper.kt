package com.tkpd.remoteresourcerequest.utils

import com.tkpd.remoteresourcerequest.type.*


class JsonArrayListHelper {

    companion object {
        private const val MULTI_DPI_ARRAY = "multiDpi"
        const val SINGLE_DPI_ARRAY = "singleDpi"
        const val NO_DPI_ARRAY = "noDpi"
        private const val AUDIO_ARRAY = "audio"

        fun getResourceNameList() = arrayListOf(
            MULTI_DPI_ARRAY,
            SINGLE_DPI_ARRAY,
            NO_DPI_ARRAY,
            AUDIO_ARRAY
        )

        fun getResourceTypeObject(arrayName: String, fileName: String): RequestedResourceType {
            return when (arrayName) {
                MULTI_DPI_ARRAY -> MultiDPIImageType(null, fileName)
                SINGLE_DPI_ARRAY -> SingleDPIImageType(null, fileName)
                NO_DPI_ARRAY -> NoDPIImageType(null, fileName)
                AUDIO_ARRAY -> AudioType(fileName)
                else -> PendingType(fileName)
            }
        }
    }

}
