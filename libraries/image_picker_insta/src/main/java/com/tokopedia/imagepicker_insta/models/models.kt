package com.tokopedia.imagepicker_insta.models

import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri

abstract class Asset(open val contentUri: Uri, open val createdDate: Long)
class Camera : Asset(Uri.EMPTY, 0L)
data class PhotosData(override val contentUri: Uri, override val createdDate: Long) :
    Asset(contentUri, createdDate)

data class VideoData(
    override val contentUri: Uri,
    override val createdDate: Long,
    val durationText: String,
    val canBeSelected: Boolean
) :
    Asset(contentUri, createdDate)

data class MediaImporterData(
    val imageAdapterDataList: ArrayList<ImageAdapterData>,
)

data class ImportedMediaMetaData(val mediaImporterData: MediaImporterData,val lastProcessedId: Long?)


data class MediaVmMData(val mediaUseCaseData: MediaUseCaseData, val folderName:String?=null, val isNewItem:Boolean = false)

data class MediaUseCaseData(val mediaImporterData: MediaImporterData)

data class FolderData(val folderTitle: String, val folderSubtitle: String, val thumbnailUri: Uri, val itemCount: Int)

data class ImageAdapterData(
    val asset: Asset,
    var checkedPosition: Int = 0
)

data class ZoomInfo(
    var scale: Float? = null,
    var panY: Float? = null,
    var panX: Float? = null,
    var bmpHeight: Int? = null,
    var bmpWidth: Int? = null,
    var matrix: Matrix?=null,
    var rectF: RectF?=null
) {
    fun hasData(): Boolean {
        return matrix!=null
    }

    fun hasChanged(): Boolean {
        return true
    }
}

data class VideoMetaData(val isSupported: Boolean, val duration: Long)

data class QueryConfiguration(val videoMaxDuration:Long)

data class VideoSize(val width: Int, val height: Int)