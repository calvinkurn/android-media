package com.tokopedia.imagepicker_insta.models

abstract class Asset(val assetPath: String)
data class PhotosData(val filePath:String,val folderName:String, val mediaType:String):Asset(filePath)