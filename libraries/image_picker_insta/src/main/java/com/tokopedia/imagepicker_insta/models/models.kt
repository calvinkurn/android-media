package com.tokopedia.imagepicker_insta.models

import android.net.Uri
import com.tokopedia.imagepicker_insta.States
import com.tokopedia.imagepicker_insta.util.FileUtil
import java.io.File

abstract class Asset(val assetPath: String,val folder:String,val contentUri:Uri)
class Camera:Asset("","", Uri.EMPTY)
data class PhotosData(val filePath:String,val folderName:String, val mediaType:String, val uri:Uri):Asset(filePath,folderName,uri)

data class PhotosImporterData(val folders:List<String>,val assets: ArrayList<Asset>, val selectedFolder:String?){
    fun addCameraImage(filePath: String):Asset{
        val folderName = FileUtil.getFolderName(filePath)
        val photosData = PhotosData(filePath, folderName, States.mediaType.camera.value(), Uri.fromFile(File(filePath)))
        assets.add(0,photosData)
        return photosData
    }
}