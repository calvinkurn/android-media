package com.tokopedia.imagepicker_insta.models

import android.net.Uri
import com.tokopedia.imagepicker_insta.util.FileUtil
import java.io.File

abstract class Asset(val assetPath: String, val folder: String, val contentUri: Uri, val createdDate: Long)
class Camera : Asset("", "", Uri.EMPTY, 0L)
data class PhotosData(val filePath: String, val folderName: String, val uri: Uri, val _createdDate: Long) : Asset(filePath, folderName, uri, _createdDate)
data class VideoData(val filePath: String, val folderName: String, val uri: Uri, val _createdDate: Long, val durationText: String, val canBeSelected: Boolean) :
    Asset(filePath, folderName, uri, _createdDate)

data class MediaImporterData(
    val imageAdapterDataList: ArrayList<ImageAdapterData>,
    val folderSet: Set<String>
) {

    fun addCameraImage(filePath: String): ImageAdapterData {
        val file = File(filePath)
        val folderName = FileUtil.getFolderName(filePath)
        val imageAdapterData = ImageAdapterData(PhotosData(filePath, folderName, Uri.fromFile(file), file.lastModified()))
        imageAdapterDataList.add(0, imageAdapterData)
        return imageAdapterData
    }
}

data class MediaUseCaseData(val mediaImporterData: MediaImporterData, val folderDataList: List<FolderData>, val selectedFolder: String? = null)

data class FolderData(val folderTitle: String, val folderSubtitle: String, val thumbnailUri: Uri)

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
) {
    fun hasData(): Boolean {
        return scale != null && panX != null && panY != null
    }

    fun hasChanged(): Boolean {
        if (scale != null && scale == 1f) {
            return false
        }
        return (panX != null && panX != 0f) || (panY != null && panY != 0f)
    }
}

object BundleData {
    val TITLE = "title"
    val SUB_TITLE = "subtitle"
    val TOOLBAR_ICON_RES = "icon_res"
    val TOOLBAR_ICON_URL = "icon_url"
    val MENU_TITLE = "menu_title"
    val MAX_MULTI_SELECT_ALLOWED = "max_multi_select"
    val APPLINK_AFTER_CAMERA_CAPTURE = "link_cam"
    val APPLINK_FOR_GALLERY_PROCEED = "link_gall"
    val APPLINK_FOR_BACK_NAVIGATION = "link_back"
    val URIS = "ip_uris"
}