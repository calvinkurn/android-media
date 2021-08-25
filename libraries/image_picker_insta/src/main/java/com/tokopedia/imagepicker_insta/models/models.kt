package com.tokopedia.imagepicker_insta.models

import android.net.Uri
import com.tokopedia.imagepicker_insta.States
import com.tokopedia.imagepicker_insta.util.FileUtil
import java.io.File

abstract class Asset(val assetPath: String,val folder:String,val contentUri:Uri, val createdDate:Long)
class Camera:Asset("","", Uri.EMPTY, 0L)
data class PhotosData(val filePath:String, val folderName:String, val mediaType:String, val uri:Uri, val _createdDate:Long ):Asset(filePath,folderName,uri, _createdDate)

data class PhotosImporterData(val folders:ArrayList<FolderData>,
                              val assets: ArrayList<Asset>,
                              val selectedFolder:String?, ){

    fun addCameraImage(filePath: String):Asset{
        val file = File(filePath)
        val folderName = FileUtil.getFolderName(filePath)
        val photosData = PhotosData(filePath, folderName, States.mediaType.camera.value(), Uri.fromFile(file),file.lastModified())
        assets.add(0,photosData)
        return photosData
    }
}

data class FolderData(val folderTitle:String,val folderSubtitle:String, val thumbnailUri:Uri)