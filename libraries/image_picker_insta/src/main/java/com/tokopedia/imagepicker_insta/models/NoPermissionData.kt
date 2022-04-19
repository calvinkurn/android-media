package com.tokopedia.imagepicker_insta.models

import androidx.annotation.DrawableRes

data class NoPermissionData(@DrawableRes val iconRes:Int, val title:String, val permissionFunc:()->Boolean, val requestPermission:()->Unit)