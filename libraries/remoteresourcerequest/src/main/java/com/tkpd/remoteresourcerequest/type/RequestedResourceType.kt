package com.tkpd.remoteresourcerequest.type

import android.util.DisplayMetrics
import com.tkpd.remoteresourcerequest.utils.Constants
import com.tkpd.remoteresourcerequest.utils.Constants.AUDIO_ARRAY
import com.tkpd.remoteresourcerequest.utils.Constants.MULTI_DPI_ARRAY
import com.tkpd.remoteresourcerequest.utils.Constants.NO_DPI_ARRAY
import com.tkpd.remoteresourcerequest.utils.Constants.SINGLE_DPI_ARRAY
import com.tkpd.remoteresourcerequest.utils.Constants.URL_SEPARATOR
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView

/** A class to defining any resource type to be downloaded from remote **/
sealed class RequestedResourceType {
    internal val commonPath: String = "%s/%s"
    internal var isRequestedFromWorker: Boolean = false
    internal abstract val relativeFilePath: String
    internal abstract var isUsedAnywhere: Boolean
    internal abstract var remoteFileCompleteUrl: String

    abstract var remoteFileName: String
    abstract var imageView: DeferredImageView?
    abstract var resourceVersion: String
}

sealed class ImageType : RequestedResourceType() {
    abstract var densityType: Int
    override var resourceVersion = ""
    override var isUsedAnywhere = true
}

/**
 * @link MultiDPIImageType
 */
data class MultiDPIImageType(
        override var imageView: DeferredImageView?,
        override var remoteFileName: String,
        override var remoteFileCompleteUrl: String = ""
) : ImageType() {

    init {
        /**
         * User may create this object even before preparing ResourceDownloadManager. We don't
         * need that. We always prioritize ResourceDownloadManager.initialize() call before
         * MultiDPIImage object gets created.
         */
        check(DensityFinder.densityUrlPath.isNotEmpty()) {
            Constants.CONTEXT_NOT_INITIALIZED_MESSAGE
        }
    }

    override val relativeFilePath =
            commonPath.format(DensityFinder.densityUrlPath, remoteFileName)
    override var densityType = DensityFinder.decidedImageDensity
}

data class SingleDPIImageType(
        override var imageView: DeferredImageView?,
        override var remoteFileName: String,
        override var remoteFileCompleteUrl: String = ""
) : ImageType() {

    override val relativeFilePath =
            commonPath.format(SINGLE_DPI_ARRAY, remoteFileName)
    override var densityType = DisplayMetrics.DENSITY_MEDIUM

}

data class NoDPIImageType(
        override var imageView: DeferredImageView?,
        override var remoteFileName: String,
        override var remoteFileCompleteUrl: String = ""
) : ImageType() {
    override val relativeFilePath =
            commonPath.format(NO_DPI_ARRAY, remoteFileName)
    override var densityType = 0
}

data class AudioType(
        override var remoteFileName: String,
        override var remoteFileCompleteUrl: String = ""
) : RequestedResourceType() {

    override var isUsedAnywhere = true
    override val relativeFilePath: String = commonPath.format("raw", remoteFileName)
    override var imageView: DeferredImageView? = null
    override var resourceVersion = ""
}

data class PendingType(
        override var remoteFileName: String,
        override var remoteFileCompleteUrl: String = ""
) : RequestedResourceType() {
    override var isUsedAnywhere = true
    override val relativeFilePath: String = remoteFileName
    override var imageView: DeferredImageView? = null
    override var resourceVersion = ""
}

object ImageTypeMapper {
    fun getImageType(deferredImageView: DeferredImageView): RequestedResourceType {
        val fileName =
                if (deferredImageView.mCompleteUrl.isNotEmpty()) {
                    val index = deferredImageView.mCompleteUrl.lastIndexOf(URL_SEPARATOR)
                    if (index != -1)
                        deferredImageView.mCompleteUrl.substring(
                                deferredImageView.mCompleteUrl.lastIndexOf(URL_SEPARATOR) + 1)
                    else
                        deferredImageView.mRemoteFileName
                } else
                    deferredImageView.mRemoteFileName
        return when (deferredImageView.dpiSupportType) {
            1 -> SingleDPIImageType(deferredImageView, fileName, deferredImageView.mCompleteUrl)
            2 -> NoDPIImageType(deferredImageView, fileName, deferredImageView.mCompleteUrl)
            else -> MultiDPIImageType(deferredImageView, fileName, deferredImageView.mCompleteUrl)
        }
    }

}

object ResourceTypeMapper {

    fun getResourceType(fileType: String, fileName: String): RequestedResourceType {
        return when (fileType) {
            MULTI_DPI_ARRAY -> MultiDPIImageType(null, fileName)
            SINGLE_DPI_ARRAY -> SingleDPIImageType(null, fileName)
            NO_DPI_ARRAY -> NoDPIImageType(null, fileName)
            AUDIO_ARRAY -> AudioType(fileName)
            else -> PendingType(fileName)
        }
    }

}
