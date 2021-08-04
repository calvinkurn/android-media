package com.tokopedia.imagepicker_insta.models

abstract class Asset(val assetPath: String,val folder:String)
class Camera:Asset("","")
data class PhotosData(val filePath:String,val folderName:String, val mediaType:String):Asset(filePath,folderName)

data class PhotosImporterData(val folders:List<String>,val assets: List<Asset>, val selectedFolder:String?)