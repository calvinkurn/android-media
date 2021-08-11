package com.tokopedia.imagepicker_insta.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {
    val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12
    val CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE = 13

    fun hasAllPermission(context: Context):Boolean{
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
            return hasArrayOfPermissions(context,arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }else{
            return hasArrayOfPermissions(context,arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }

    }

    fun hasArrayOfPermissions(context: Context, arrayOfPermissions:ArrayList<String>):Boolean{
        var hasAllPermissions = true
        arrayOfPermissions.forEach {
            hasAllPermissions = hasAllPermissions && ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        return hasAllPermissions
    }

    fun requestReadPermission(activity: AppCompatActivity){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
    }

    fun requestCameraAndWritePermission(activity: AppCompatActivity) {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA), CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE)
        }else{
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA), CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE)
        }

    }
}