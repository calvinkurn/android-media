package com.tokopedia.imagepicker_insta.models

import android.net.Uri

abstract class Asset(open val assetPath: String, open val folder: String, open val contentUri: Uri, open val createdDate: Long)
class Camera : Asset("", "", Uri.EMPTY, 0L)
data class PhotosData(override val assetPath: String, override val folder: String, override val contentUri: Uri, override val createdDate: Long) :
    Asset(assetPath, folder, contentUri, createdDate)

data class VideoData(
    override val assetPath: String,
    override val folder: String,
    override val contentUri: Uri,
    override val createdDate: Long,
    val durationText: String,
    val canBeSelected: Boolean
) :
    Asset(assetPath, folder, contentUri, createdDate)

data class MediaImporterData(
    val imageAdapterDataList: ArrayList<ImageAdapterData>,
    val folderSet: Set<String>
)

data class MediaUseCaseData(
    val mediaImporterData: MediaImporterData,
    val folderDataList: ArrayList<FolderData>,
    val selectedFolder: String? = null,
    val uriSet: HashSet<Uri>
) {
    fun createMediaUseCaseData(imageAdapterList: ArrayList<ImageAdapterData>, folderSet: Set<String>): MediaUseCaseData {
        return MediaUseCaseData(
            MediaImporterData(imageAdapterList, folderSet),
            folderDataList,
            selectedFolder,
            uriSet
        )
    }
}

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

data class VideoMetaData(val isSupported: Boolean, val duration: Long)