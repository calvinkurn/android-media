package com.tokopedia.universal_sharing.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import java.io.File

/**
 * Created by Yoris on 26/08/21.
 */

class GlobalSharingActivity: BaseActivity() {

    private var isNeedPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasStoragePermissions()) {
            askPermission()
        } else {
            checkSharingOptions()
        }
    }

    private fun checkSharingOptions() {
        intent.extras?.run {
            when(getString(KEY_SHARE_TYPE)) {
                SHARE_TYPE_NATIVE -> {
                    showAndroidChooserSharing(this)
                }
                SHARE_TYPE_UNIVERSAL_DIALOG -> {
                    // TO BE ADDED: Universal Sharing Bottom Sheet
                    showAndroidChooserSharing(this)
                }
                SHARE_TYPE_IG_STORY -> {
                    shareToInstagram(this)
                }
                else -> {
                    showAndroidChooserSharing(this)
                }
            }
        }
    }

    private fun showAndroidChooserSharing(bundle: Bundle) {
        val text = bundle.getString(KEY_TEXT)
        val image = bundle.getString(KEY_IMAGE_URL)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, text)
        share.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        if(image?.isNotEmpty() == true) {
            SharingUtil.saveImageFromURLToStorage(this@GlobalSharingActivity, image) {
                val imgFile = getFileProvider(File(it))
                share.putExtra(Intent.EXTRA_STREAM, imgFile)
            }
        }
        startActivityForResult(Intent.createChooser(share, "Bagikan"), REQUEST_CODE_CHOOSER)
    }

    private fun shareToInstagram(bundle: Bundle) {
        val image = bundle.getString(KEY_IMAGE_URL) ?: ""
        SharingUtil.saveImageFromURLToStorage(this@GlobalSharingActivity, image) {
            val imgFile = getFileProvider(File(it))
            openInstagramStory(imgFile)
        }
    }

    private fun openInstagramStory(assetPath: Uri) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.setDataAndType(assetPath, "image/png")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, REQUEST_CODE_INSTAGRAM)
    }

    private fun hasStoragePermissions() : Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun askPermission() {
        isNeedPermission = true
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }

    private fun getFileProvider(imageFile: File): Uri {
        return FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            imageFile
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 100) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                isNeedPermission = false
                checkSharingOptions()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_CHOOSER -> {
                finish()
            }
            REQUEST_CODE_INSTAGRAM -> {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val KEY_IMAGE_URL = "image"
        const val KEY_TEXT = "text"
        const val KEY_SHARE_TYPE = "type"

        const val SHARE_TYPE_NATIVE = "1"
        const val SHARE_TYPE_UNIVERSAL_DIALOG = "2"
        const val SHARE_TYPE_IG_STORY = "3"

        private const val REQUEST_CODE_CHOOSER = 100
        private const val REQUEST_CODE_INSTAGRAM = 102
    }
}