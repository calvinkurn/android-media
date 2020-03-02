package ai.advance.liveness.lib

import ai.advance.common.utils.BitmapUtil
import ai.advance.common.utils.LogUtil
import android.graphics.*
import android.util.Base64
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class DetectionFrame(private val yuvData: ByteArray, private val mCameraAngle: Int, private val mWidth: Int, private val mHeight: Int, val detectionType: Detector.DetectionType) {

    val bitmapPixels: ByteArray
        get() {
            val bitmap = bitmap
            height = bitmap.height
            width = bitmap.width
            val argbData = BitmapUtil.getPixelsARGB(bitmap)
            bitmap.recycle()
            return argbData
        }

    val bitmap: Bitmap
        get() = getBitmap(EXPECT_SIZE)

    @Deprecated("", ReplaceWith(""))
    val isBrightLight: Boolean
        get() = mBrightness > 1

    @Deprecated("", ReplaceWith(""))
    val isWeakLight: Boolean
        get() = mBrightness < 0

    var width: Int = 0
    var height: Int = 0

    var faceInfo: FaceInfo? = null
    var mBrightness: Double = 0.toDouble()
    var faceWarnCode: Detector.WarnCode? = null
    var mActionStatus: Detector.ActionStatus? = null

    val formatBitmap: String
        get() = getFormatBitmap(EXPECT_SIZE)

    init {
        faceInfo = null
    }

    fun hasFace(): Boolean {
        return faceInfo != null
    }

    private fun getBitmap(scaleSize: Int): Bitmap {
        return parseYuvToBitmap(scaleSize)
    }

    fun update(faceInfo: String) {
        this.faceInfo = FaceInfo.FaceInfoFactory.createFaceInfo(faceInfo)
    }

    fun update(livenessResult: JSONObject) {


        try {
            faceWarnCode = Detector.WarnCode.valueOf(livenessResult.getInt("code"))
            mActionStatus = Detector.ActionStatus.valueOf(livenessResult.getInt("status"))
            if (livenessResult.getString("faceInfo") != "") {
                val faceInfo = JSONObject(livenessResult.getString("faceInfo"))
                this.faceInfo = FaceInfo.FaceInfoFactory.createFaceInfo(faceInfo)
            }
        } catch (e: JSONException) {
            LogUtil.debug("json_error", e.toString())
        }

    }

    private fun parseYuvToBitmap(scaleSize: Int): Bitmap {

        val localYuvImage = YuvImage(yuvData, 17, mWidth, mHeight, null)
        val localByteArrayOutputStream = ByteArrayOutputStream()

        localYuvImage.compressToJpeg(Rect(mWidth - mHeight, 0, mWidth, mHeight), 80, localByteArrayOutputStream)
        val options = BitmapFactory.Options()
        options.inSampleSize = mHeight / scaleSize
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        val decodeBitmap = BitmapUtil.decodeRGBStreamToBitmap(localByteArrayOutputStream, options)
        val rotateBitmap: Bitmap
        if (GuardianLivenessDetectionSDK.isEmulator) {
            rotateBitmap = decodeBitmap
        } else {
            rotateBitmap = BitmapUtil.rotateBitmap(decodeBitmap, (mCameraAngle - 180).toFloat())
        }
        val matrix = Matrix()
        val scale = scaleSize / rotateBitmap.width.toFloat()
        matrix.setScale(scale, scale)
        val scaleBitmap = Bitmap.createBitmap(rotateBitmap, 0, 0, rotateBitmap.width, rotateBitmap.height, matrix, true)
        // scaleBitmap must be 600px*600px
        rotateBitmap.recycle()
        return scaleBitmap
    }

    private fun getFormatBitmap(scaleSize: Int): String {
        val bitmap = getBitmap(scaleSize)
        val bytes = bitmap2Bytes(bitmap)
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    companion object {
        private const val EXPECT_SIZE = 600
    }

}
