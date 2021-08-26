package com.tokopedia.universal_sharing.view.activity

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by Yoris on 26/08/21.
 */

class GlobalSharingActivity: BaseActivity() {

    private var isNeedPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!hasStoragePermissions()) {
            askPermission()
        }

        checkSharingOptions()
    }

    private fun checkSharingOptions() {
        intent.extras?.run {
            when(getString(KEY_SHARE_TYPE)) {
                SHARE_TYPE_NATIVE -> {
                    showAndroidChooserSharing(this)
                }
                SHARE_TYPE_UNIVERSAL_DIALOG -> {
                    showUniversalDialog(this)
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

    private fun showUniversalDialog(bundle: Bundle) {
        val image = bundle.getString(KEY_IMAGE_URL)
        val text = bundle.getString(KEY_TEXT)

        val universalDialog = UniversalShareBottomSheet.createInstance().apply {
            setOgImageUrl(image ?: "")
        }
        universalDialog.showNow(supportFragmentManager, "")
    }

    private fun showAndroidChooserSharing(bundle: Bundle) {
        val text = bundle.getString(KEY_TEXT)
        val image = bundle.getString(KEY_IMAGE_URL)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, text)

        if(image?.isNotEmpty() == true) {
            downloadImage(image) {
                share.putExtra(Intent.EXTRA_STREAM, it)
            }
        }
        startActivityForResult(Intent.createChooser(share, "Share With"), REQUEST_CODE_CHOOSER)
    }

    private fun shareToInstagram(bundle: Bundle) {
        val image = bundle.getString(KEY_IMAGE_URL) ?: ""
        if(image.isNotEmpty()) {
            downloadImage(image) {
                openInstagramStory(it)
            }
        }
    }

    private fun openInstagramStory(assetPath: Uri) {
        println("Image path $assetPath")
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 100) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                isNeedPermission = false
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun downloadImage(imageURL: String, onSuccess: (Uri) -> Unit) {
        Glide.with(this)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    val img = saveImage(this@GlobalSharingActivity, resource.toBitmap())
                    if(img != null) {
                        onSuccess(img)
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) { super.onLoadFailed(errorDrawable) }
            })
    }

    private fun saveImage(
        context: Context,
        bitmap: Bitmap): Uri? {
        var fos: OutputStream? = null
        var imageFile: File? = null
        var imageUri: Uri? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "tkpd-shared-image")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
                imageUri =
                    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri == null) throw IOException("Failed to create new MediaStore record.")
                fos = resolver.openOutputStream(imageUri)
            } else {
                val imagesDir = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    ).toString()
                )
                if (!imagesDir.exists()) imagesDir.mkdir()
                imageFile = File(imagesDir, "tkpd-test-image.png")
                fos = FileOutputStream(imageFile)
            }
            if (!bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    fos
                )
            ) throw IOException("Failed to save bitmap.")
            fos?.flush()
        } finally {
            fos?.close()
        }
        if (imageFile != null) {
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.toString()), null, null)
            imageUri = FileProvider.getUriForFile(
                this,
                "$packageName.provider",
                imageFile
            )
        }
        return imageUri
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