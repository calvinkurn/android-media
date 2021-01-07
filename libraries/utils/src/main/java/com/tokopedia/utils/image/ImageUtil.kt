package com.tokopedia.utils.image

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.view.Display
import android.view.WindowManager
import androidx.exifinterface.media.ExifInterface
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.FileUtil.writeBufferToFile
import com.tokopedia.utils.file.FileUtil.writeStreamToFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

object ImageUtil {

    const val PNG_EXT = ".png"
    const val JPG_EXT = ".jpg"

    const val DEF_WIDTH = 2560
    const val DEF_HEIGHT = 2560

    const val DEF_SMALL_WIDTH = 612
    const val DEF_SMALL_HEIGHT = 816

    const val DEFAULT_DIRECTORY = "/"

    /**
     * This method calculates maximum size of both width and height of bitmap.
     * It is twice the device screen diagonal for default implementation (extra quality to zoom image).
     * Size cannot exceed max texture size.
     *
     * @return - max bitmap size in pixels.
     */
    private fun calculateMaxBitmapSize(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display
        val width: Int
        val height: Int
        val size = Point()
        display = wm.defaultDisplay
        display.getSize(size)
        width = size.x
        height = size.y
        // Twice the device screen diagonal as default
        var maxBitmapSize = sqrt(width.toDouble().pow(2.0) + height.toDouble().pow(2.0)).toInt()
        // Check for max texture size via Canvas
        val canvas = Canvas()
        val maxCanvasSize: Int = min(canvas.maximumBitmapWidth, canvas.maximumBitmapHeight)
        if (maxCanvasSize > 0) {
            maxBitmapSize = min(maxBitmapSize, maxCanvasSize)
        }
        return maxBitmapSize
    }

    // This will handle OOM for too large Bitmap
    @JvmStatic
    fun getBitmapFromFile(context: Context, imagePath: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val maxBitmapSize = calculateMaxBitmapSize(context)
        options.inSampleSize = calculateInSampleSize(options, maxBitmapSize, maxBitmapSize)
        options.inJustDecodeBounds = false
        var tempPic: Bitmap? = null
        var decodeAttemptSuccess = false
        while (!decodeAttemptSuccess) {
            try {
                tempPic = BitmapFactory.decodeFile(imagePath, options)
                decodeAttemptSuccess = true
            } catch (error: OutOfMemoryError) {
                options.inSampleSize *= 2
            }
        }
        return tempPic
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int { // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width lower or equal to the requested height and width.
        if (height > reqHeight || width > reqWidth) {
            while (height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun getWidthAndHeight(file: File): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth to options.outHeight
        } catch (e: Exception) {
            0 to 0
        }
    }

    /**
     * This function is to determine if the image from file should be load as fit center or center crop
     * If the ratio between width/height is too big, it should be viewed as fit center. Otherwise, it will OOM
     */
    @JvmStatic
    fun shouldLoadFitCenter(file: File): Boolean {
        val (width, height) = getWidthAndHeight(file)
        val min: Int
        val max: Int
        if (width > height) {
            min = height
            max = width
        } else {
            min = width
            max = height
        }
        return min != 0 && max / min > 2
    }

    @JvmStatic
    fun getWidthAndHeight(filePath: String): Pair<Int, Int> {
        return getWidthAndHeight(File(filePath))
    }

    @JvmStatic
    fun getWidthAndHeight(context: Context, uri: Uri): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val input = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(input, null, options)
            input?.close()
            options.outWidth to options.outHeight
        } catch (ignored: Exception) {
            0 to 0
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getOrientation(path: String?): Int {
        return try {
            val exif = ExifInterface(path!!)
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } catch (e: Throwable) {
            ExifInterface.ORIENTATION_NORMAL
        }
    }

    @JvmOverloads
    @JvmStatic
    fun trimBitmap(imagePath: String?, expectedRatio: Float, currentRatio: Float, needCheckRotate: Boolean,
                   targetRelativeDirectory: String? = DEFAULT_DIRECTORY): String? {
        val bitmapToEdit: Bitmap = getBitmapFromPath(imagePath, DEF_WIDTH, DEF_HEIGHT, needCheckRotate)
                ?: return null
        val width = bitmapToEdit.width
        val height = bitmapToEdit.height
        var left = 0
        var right = width
        var top = 0
        var bottom = height
        var expectedWidth = width
        var expectedHeight = height
        if (expectedRatio < currentRatio) { // trim left and right
            expectedWidth = (expectedRatio * height).toInt()
            left = (width - expectedWidth) / 2
            right = left + expectedWidth
        } else { // trim top and bottom
            expectedHeight = (width / expectedRatio).toInt()
            top = (height - expectedHeight) / 2
            bottom = top + expectedHeight
        }
        val isPng: Boolean = isPng(imagePath)
        var outputBitmap: Bitmap? = null
        return try {
            outputBitmap = Bitmap.createBitmap(expectedWidth, expectedHeight, bitmapToEdit.config)
            val canvas = Canvas(outputBitmap)
            canvas.drawBitmap(bitmapToEdit, Rect(left, top, right, bottom),
                    Rect(0, 0, expectedWidth, expectedHeight), null)
            val file = writeImageToTkpdPath(outputBitmap, isPng, targetRelativeDirectory)
            bitmapToEdit.recycle()
            outputBitmap.recycle()
            System.gc()
            file?.absolutePath ?: imagePath
        } catch (e: Throwable) {
            if (outputBitmap != null && !outputBitmap.isRecycled) {
                outputBitmap.recycle()
            }
            imagePath
        }
    }

    @JvmStatic
    fun getBitmapFromPath(imagePath: String?, maxWidth: Int = DEF_WIDTH, maxHeight: Int = DEF_HEIGHT, needCheckRotate: Boolean = true): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        var tempPic: Bitmap? = null
        var decodeAttemptSuccess = false
        while (!decodeAttemptSuccess) {
            try {
                tempPic = BitmapFactory.decodeFile(imagePath, options)
                decodeAttemptSuccess = true
            } catch (error: OutOfMemoryError) {
                options.inSampleSize *= 2
            }
        }
        if (needCheckRotate) {
            if (tempPic != null) {
                return try {
                    rotate(tempPic, imagePath)
                } catch (e1: Throwable) {
                    tempPic
                }
            }
        }
        return tempPic
    }

    @Throws(IOException::class)
    fun rotate(bitmap: Bitmap, path: String?): Bitmap? {
        val orientation: Int = getOrientation(path)
        return if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            bitmap
        } else rotateBitmap(bitmap, orientation)
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (ignore: OutOfMemoryError) {
            null
        }
    }

    @JvmStatic
    fun getMinResolution(filePath: String): Int {
        return getMinResolution(File(filePath))
    }

    @JvmStatic
    fun getMinResolution(file: File): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        return Math.min(options.outWidth, options.outHeight)
    }

    @JvmStatic
    fun isPng(referencePath: String?) = referencePath?.endsWith(PNG_EXT) ?: false

    fun getTokopediaPhotoPath(isPng: Boolean, relativePathDirectory: String? = DEFAULT_DIRECTORY): File {
        return File(FileUtil.getTokopediaInternalDirectory(relativePathDirectory).absolutePath,
                FileUtil.generateUniqueFileName() + if (isPng) PNG_EXT else JPG_EXT)
    }

    @JvmOverloads
    @JvmStatic
    fun getTokopediaPhotoPath(referencePath: String?, directory: String? = DEFAULT_DIRECTORY): File {
        return getTokopediaPhotoPath(isPng(referencePath), directory)
    }

    /**
     * copy the inputstream to Tkpd Cache Directory
     * The file represents the copy of the original bitmap and can be deleted/modified
     * without changing the original image
     */
    @JvmOverloads
    @JvmStatic
    fun writeImageToTkpdPath(source: InputStream?, isPng: Boolean, directoryRelativePath: String? = DEFAULT_DIRECTORY): File? {
        if (source == null) {
            return null
        }
        val photo: File = getTokopediaPhotoPath(isPng, directoryRelativePath)
        if (photo.exists()) {
            photo.delete()
        }
        return if (writeStreamToFile(source, photo)) {
            photo
        } else null
    }

    /**
     * compress the bitmap, then write to internal Tokopedia File
     */
    @JvmOverloads
    @JvmStatic
    fun writeImageToTkpdPath(bitmap: Bitmap, isPng: Boolean, directoryDef: String? = DEFAULT_DIRECTORY, quality: Int = 100): File? {
        val file: File = getTokopediaPhotoPath(isPng, directoryDef)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(if (isPng) CompressFormat.PNG else CompressFormat.JPEG, quality, out)
            out.flush()
            out.close()
        } catch (e: Throwable) {
            return null
        }
        return file
    }

    /**
     * write byte buffer to internal Tokopedia File
     * This "cache file" is a representation of the bytes.
     */
    @JvmOverloads
    @JvmStatic
    fun writeImageToTkpdPath(buffer: ByteArray?, isPng: Boolean, relativePathDirectory: String? = DEFAULT_DIRECTORY): File? {
        if (buffer != null) {
            val photo: File = getTokopediaPhotoPath(isPng, relativePathDirectory)
            if (photo.exists()) {
                photo.delete()
            }
            if (writeBufferToFile(buffer, photo.path)) {
                return photo
            }
        }
        return null
    }

    /*
        Compress Image like WA
     */
    @JvmOverloads
    @JvmStatic
    @Throws(IOException::class)
    fun compressImageFile(filePath: String, quality: Int = 100, reqWidth: Int = DEF_SMALL_WIDTH, reqHeight: Int = DEF_SMALL_HEIGHT): File {
        val bitmap = getBitmapFromPath(filePath, reqWidth, reqHeight, true)
        val file = if (bitmap != null) {
            writeImageToTkpdPath(bitmap, isPng(filePath), DEFAULT_DIRECTORY, quality)
        } else {
            null
        }
        if (file == null || !file.exists()) {
            throw IOException()
        } else {
            return file
        }
    }

    @JvmStatic
    fun brightBitmap(bitmap: Bitmap, brightness: Float): Bitmap? {
        val colorTransform = floatArrayOf(1f, 0f, 0f, 0f, brightness, 0f, 1f, 0f, 0f, brightness, 0f, 0f, 1f, 0f, brightness, 0f, 0f, 0f, 1f, 0f)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        colorMatrix.set(colorTransform)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        val paint = Paint()
        paint.colorFilter = colorFilter
        return try {
            val resultBitmap = bitmap.copy(bitmap.config, true)
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
            bitmap.recycle()
            resultBitmap
        } catch (error: OutOfMemoryError) {
            bitmap
        }
    }

    @JvmStatic
    fun contrastBitmap(bitmap: Bitmap, contrast: Float): Bitmap? {
        val colorTransform = floatArrayOf(
                contrast, 0f, 0f, 0f, 0f, 0f, contrast, 0f, 0f, 0f, 0f, 0f, contrast, 0f, 0f, 0f, 0f, 0f, 1f, 0f)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        colorMatrix.set(colorTransform)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        val paint = Paint()
        paint.colorFilter = colorFilter
        return try {
            val resultBitmap = bitmap.copy(bitmap.config, true)
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
            bitmap.recycle()
            resultBitmap
        } catch (outOfMemoryError: OutOfMemoryError) {
            bitmap
        }
    }

    @JvmStatic
    fun rotateBitmapByDegree(bitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.setRotate(degree)
        return try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (ignore: OutOfMemoryError) {
            null
        }
    }

    @JvmOverloads
    @JvmStatic
    fun resizeBitmap(imagePath: String, maxWidth: Int, maxHeight: Int, needCheckRotate: Boolean,
                     resultRelativeDirectory: String? = DEFAULT_DIRECTORY): String {
        val bitmapToEdit = getBitmapFromPath(imagePath, maxWidth, maxHeight, needCheckRotate)
        val isPng = isPng(imagePath)
        val outputBitmap: Bitmap
        return try {
            outputBitmap = Bitmap.createBitmap(bitmapToEdit!!.width, bitmapToEdit.height, bitmapToEdit.config)
            val canvas = Canvas(outputBitmap)
            canvas.drawBitmap(bitmapToEdit, 0f, 0f, null)
            val file = writeImageToTkpdPath(outputBitmap, isPng, resultRelativeDirectory)
            bitmapToEdit.recycle()
            outputBitmap.recycle()
            System.gc()
            file?.absolutePath ?: imagePath
        } catch (e: Throwable) {
            imagePath
        }
    }
}