package com.tokopedia.imagepicker_insta.models

import android.net.Uri
import com.tokopedia.imagepicker_insta.States
import com.tokopedia.imagepicker_insta.util.FileUtil
import java.io.File

abstract class Asset(val assetPath: String,val folder:String,val contentUri:Uri, val createdDate:Long)
class Camera:Asset("","", Uri.EMPTY, 0L)
data class PhotosData(val filePath:String, val folderName:String, val mediaType:String, val uri:Uri, val _createdDate:Long ):Asset(filePath,folderName,uri, _createdDate)

data class PhotosImporterData(val folders:ArrayList<FolderData>,
                              val imageAdapterDataList: ArrayList<ImageAdapterData>,
                              val selectedFolder:String?, ){

    fun addCameraImage(filePath: String):ImageAdapterData{
        val file = File(filePath)
        val folderName = FileUtil.getFolderName(filePath)
        val imageAdapterData = ImageAdapterData(PhotosData(filePath, folderName, States.mediaType.camera.value(), Uri.fromFile(file),file.lastModified()))
        imageAdapterDataList.add(0,imageAdapterData)
        return imageAdapterData
    }
}

data class FolderData(val folderTitle:String,val folderSubtitle:String, val thumbnailUri:Uri)

data class ImageAdapterData(val asset:Asset, val isSelected:Boolean = false, val isInMultiSelectMode:Boolean = false)

object BundleData {
    val TITLE = "title"
    val SUB_TITLE = "subtitle"
    val ICON_RES = "icon_res"
    val MENU_TITLE = "menu_title"
}