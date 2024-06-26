package com.tokopedia.utils.file

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*

/***
 * Util to put file/image/video into public folder without using legacy Environment.getExternalStoragePublicDirectory
 * WRITE_EXTERNAL_STORAGE Permission is still need to be asked for android 28 and below
 */
object PublicFolderUtil {

    /**
     * @param bitmap: input Bitmap to be put
     * @param fileName: output fileName, without prefix directory
     * @param compressFormat: Compress Format for image, default is jpeg
     * @param mimeType: mimeType for Image, default "image/jpeg"
     * @param directory: (API 29 and above only) public directory to put, default is Directory Pictures
     *
     * Note: WRITE_EXTERNAL_STORAGE Permission is still need to be asked for android 28 and below
     */
    fun putImageToPublicFolder(
            context: Context, bitmap: Bitmap, fileName: String,
            compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
            mimeType: String = "image/jpeg",
            directory: String? = Environment.DIRECTORY_PICTURES
    ): Pair<File?, Uri?> {
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val contentValues = createContentValues(fileName, mimeType, directory, null)
            val contentUri = getContentUriFromMime(mimeType)
            val uri = contentResolver.insert(contentUri, contentValues)
            uri?.let {
                contentResolver.openOutputStream(uri)?.let { outputStream ->
                    BufferedOutputStream(outputStream)
                    bitmap.compress(compressFormat, 100, outputStream)
                    outputStream.close()
                }
                resetPending(contentResolver, contentValues, uri)
            }
            return fileAndUriPair(contentResolver, uri)
        } catch (ex: Exception) {
            return null to null
        }
    }

    /**
     * @param localFile: input file to put
     * @param outputFileName: output fileName, without prefix directory
     * @param mimeType: mimeType for Image, default "image/jpeg"
     * @param directory: (API 29 and above only) public directory to put. If not given, will determine automatically by mimeType
     *
     * Note: WRITE_EXTERNAL_STORAGE Permission is still need to be asked for android 28 and below
     */
    fun putFileToPublicFolder(
            context: Context,
            localFile: File,
            outputFileName: String,
            mimeType: String,
            directory: String? = null,
    ): Pair<File?, Uri?> {

        try {
            val contentResolver: ContentResolver = context.contentResolver
            val contentValues = createContentValues(outputFileName, mimeType, directory, localFile.absolutePath)
            val contentUri = getContentUriFromMime(mimeType)
            val uri = contentResolver.insert(contentUri, contentValues)
            uri?.let {
                contentResolver.openOutputStream(uri)?.let { outputStream ->
                    var inputStream: FileInputStream? = null
                    try {
                        inputStream = FileInputStream(localFile)
                        copy(inputStream, outputStream)
                    } finally {
                        inputStream?.close()
                        outputStream.close()
                    }
                }
                resetPending(contentResolver, contentValues, uri)
            }
            return fileAndUriPair(contentResolver, uri, outputFileName)
        } catch (ex: Exception) {
            return null to null
        }
    }

    private fun fileAndUriPair (contentResolver: ContentResolver, uri:Uri?, outputFileName: String = ""): Pair<File?, Uri?> {
        val filePath = FileUtil.getPath(contentResolver, uri)
        if (filePath == null) return null to null

        var resultFile = File(filePath)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                val resultPath = resultFile.absolutePath.replace(resultFile.name, outputFileName)
                resultFile.renameTo(File(resultPath))
                resultFile = File(resultPath)
            } catch (ex: Exception) {}
        }
        return resultFile to uri
    }


    private fun getContentUriFromMime(mimeType: String): Uri {
        return when {
            mimeType.startsWith("image") -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            mimeType.startsWith("video") -> {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            mimeType.startsWith("audio") -> {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            else -> {
                MediaStore.Files.getContentUri("external")
            }
        }
    }

    private fun getDirectoryFromMime(mimeType: String): String {
        return when {
            mimeType.startsWith("image") -> {
                Environment.DIRECTORY_PICTURES
            }
            mimeType.startsWith("video") -> {
                Environment.DIRECTORY_MOVIES
            }
            mimeType.startsWith("audio") -> {
                Environment.DIRECTORY_MUSIC
            }
            else -> {
                Environment.DIRECTORY_DOWNLOADS
            }
        }
    }

    private fun createContentValues(
            fileName: String,
            mimeType: String,
            directory: String? = null,
            filePath: String?
    ): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    directory ?: getDirectoryFromMime(mimeType)
            )
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
        } else if (filePath!= null) {
            contentValues.put(MediaStore.MediaColumns.DATA, filePath);
        }
        return contentValues
    }

    private fun resetPending(contentResolver: ContentResolver, contentValues: ContentValues, uri: Uri){
        contentValues.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}
