package ai.advance.liveness.lib

import android.graphics.PointF
import android.graphics.RectF

import org.json.JSONArray
import org.json.JSONObject

class FaceInfo {
    private val position: RectF = RectF()
    private var yaw = 0.0f
    var faceQuality = 0.0f
    private var mouthOpenProb = -1.0f
    private var leftEyeOpenProb = -1.0f
    private var rightEyeOpenProb = -1.0f
    private var landmark: ArrayList<PointF>? = null //
    var faceTooSmall = false

    override fun toString(): String {
        return "FaceInfo{ position=" + this.position.toShortString() +
                ", yaw=" + this.yaw +
                ", faceQuality=" + this.faceQuality +
                ", mouthOpenProb=" + this.mouthOpenProb + "}"
    }

    class FaceInfoFactory {
        companion object {

            fun createFaceInfo(faceInfoString: String): FaceInfo? {
                try {
                    val faceInfoJson = JSONObject(faceInfoString)
                    return createFaceInfo(faceInfoJson)
                } catch (exception: Exception) {
                    // TODO: check exception handle
                    return null
                }

            }

            fun createFaceInfo(faceInfoJson: JSONObject): FaceInfo? {
                try {
                    val faceInfo = FaceInfo()
                    faceInfo.leftEyeOpenProb = faceInfoJson.optDouble("leftEyeOpenProb").toFloat()
                    faceInfo.rightEyeOpenProb = faceInfoJson.optDouble("rightEyeOpenProb").toFloat()
                    faceInfo.mouthOpenProb = faceInfoJson.optDouble("mouthOpenProb").toFloat()
                    faceInfo.faceQuality = faceInfoJson.optDouble("faceQuality").toFloat()
                    faceInfo.yaw = faceInfoJson.optDouble("yaw").toFloat()
                    var array: JSONArray = faceInfoJson.optJSONArray("faceBoundingbox")
                    faceInfo.position.left = array.optDouble(0).toFloat()
                    faceInfo.position.top = array.optDouble(1).toFloat()
                    faceInfo.position.right = array.optDouble(2).toFloat()
                    faceInfo.position.bottom = array.optDouble(3).toFloat()
                    array = faceInfoJson.optJSONArray("landmark")
                    if (array.length() % 2 == 0) {
                        var j = 0
                        for (i in 0 until array.length() / 2) {
                            faceInfo.landmark?.add(PointF())
                            faceInfo.landmark?.get(i)?.x = array.optDouble(j).toFloat()
                            j++
                            faceInfo.landmark?.get(i)?.y = array.optDouble(j).toFloat()
                            j++
                        }
                    }
                    return faceInfo
                } catch (e: Exception) {
                    return null
                }

            }
        }
    }
}
