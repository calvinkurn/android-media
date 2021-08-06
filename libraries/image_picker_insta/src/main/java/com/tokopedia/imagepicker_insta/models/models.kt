package com.tokopedia.imagepicker_insta.models

import com.tokopedia.imagepicker_insta.States
import com.tokopedia.imagepicker_insta.util.FileUtil

abstract class Asset(val assetPath: String,val folder:String)
class Camera:Asset("","")
data class PhotosData(val filePath:String,val folderName:String, val mediaType:String):Asset(filePath,folderName)

data class PhotosImporterData(val folders:List<String>,val assets: ArrayList<Asset>, val selectedFolder:String?){
    fun addCameraImage(filePath: String):Asset{
        val folderName = FileUtil.getFolderName(filePath)
        val photosData = PhotosData(filePath, folderName, States.mediaType.camera.value())
        assets.add(0,photosData)
        return photosData
    }
}