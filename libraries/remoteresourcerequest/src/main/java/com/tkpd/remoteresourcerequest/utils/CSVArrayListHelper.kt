package com.tkpd.remoteresourcerequest.utils

import com.tkpd.remoteresourcerequest.type.*


class CSVArrayListHelper {

    companion object {
        private const val MULTI_DPI_ARRAY = "multiDpi"
        const val SINGLE_DPI_ARRAY = "singleDpi"
        const val NO_DPI_ARRAY = "noDpi"
        private const val AUDIO_ARRAY = "audio"

        fun getResourceTypeObject(
            name: String,
            type: String,
            version: String,
            isUsed: String
        ): RequestedResourceType {

            val fileName = extractProperString(name)

            val fileTypeObject = when (extractProperString(type)) {
                MULTI_DPI_ARRAY -> MultiDPIImageType(null, fileName)
                SINGLE_DPI_ARRAY -> SingleDPIImageType(null, fileName)
                NO_DPI_ARRAY -> NoDPIImageType(null, fileName)
                AUDIO_ARRAY -> AudioType(fileName)
                else -> PendingType(fileName)
            }
            fileTypeObject.resourceVersion = extractProperString(version)
            val isUsedAnywhere = extractProperString(isUsed)
            fileTypeObject.isUsedAnywhere = !isUsedAnywhere.equals("n", ignoreCase = true)
            return fileTypeObject
        }

        private fun extractProperString(csvString: String): String {
            val name = csvString.trim()
            return name.substring(1, name.length - 1)
        }
    }

}
