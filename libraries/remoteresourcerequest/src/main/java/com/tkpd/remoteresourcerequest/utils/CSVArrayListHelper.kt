package com.tkpd.remoteresourcerequest.utils

import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import com.tkpd.remoteresourcerequest.type.ResourceTypeMapper


class CSVArrayListHelper {

    companion object {

        private const val FILE_NAME_INDEX = 0
        private const val FILE_TYPE_INDEX = 1
        private const val FILE_VERSION_INDEX = 2
        private const val IS_FILE_USED_ANYWHERE_INDEX = 3

        private const val CSV_SEPARATOR = ","
        private const val FILE_NOT_USED_ANYWHERE = "n"

        fun getResourceTypeObject(singleCSVEntry: String): RequestedResourceType {
            val resourceInfo = singleCSVEntry.split(CSV_SEPARATOR)
            return getResourceTypeInternal(resourceInfo[FILE_NAME_INDEX].trim(),
                    resourceInfo[FILE_TYPE_INDEX].trim(),
                    resourceInfo[FILE_VERSION_INDEX].trim(),
                    resourceInfo[IS_FILE_USED_ANYWHERE_INDEX].trim())
        }

        private fun getResourceTypeInternal(fileName: String,
                                            fileType: String,
                                            fileVersion: String,
                                            isUsed: String): RequestedResourceType {

            val fileTypeObject = ResourceTypeMapper
                    .getResourceType(fileType, fileName)

            fileTypeObject.resourceVersion = fileVersion
            fileTypeObject.isUsedAnywhere = !isUsed.equals(FILE_NOT_USED_ANYWHERE, true)
            return fileTypeObject
        }
    }

}
