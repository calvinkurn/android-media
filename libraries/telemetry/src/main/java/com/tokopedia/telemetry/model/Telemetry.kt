package com.tokopedia.telemetry.model

import android.view.MotionEvent
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.telemetry.SECTION_TELEMETRY_DURATION
import java.math.RoundingMode
import java.text.DecimalFormat

class Touch(val time: Int, val x: Int, val y: Int, val pressure: Float) {
    override fun toString(): String {
        return "[$time,$x,$y,$pressure]"
    }
}

class Typing(var ts: Int, var diffCount: Int) {
    override fun toString(): String {
        return "[$ts,$diffCount]"
    }
}

class Coord(var ts: Int, val x: Float, val y: Float, val z: Float, var visit: Int) {
    override fun toString(): String {
        return "[$ts,$x,$y,$z,$visit]"
    }
}

class TelemetrySection(
    var eventName: String,
    var startTime: Long,
    var endTime: Long = 0L,
    var eventNameEnd: String = "",
    var typingList: MutableList<Typing> = mutableListOf(),
    var touchList: MutableList<Touch> = mutableListOf(),
    var accelList: MutableList<Coord> = mutableListOf(),
    var gyroList: MutableList<Coord> = mutableListOf()
) {
    fun toJson(): String {
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.HALF_UP

        val mapAccList: Array<FloatArray> = Array(accelList.size) {
            FloatArray(4)
        }
        accelList.onEachIndexed { index, coord ->
            mapAccList[index][0] = coord.ts.toFloat()
            mapAccList[index][1] = df.format(coord.x).toFloat()
            mapAccList[index][2] = df.format(coord.y).toFloat()
            mapAccList[index][3] = df.format(coord.z).toFloat()
        }

        val mapGyroList: Array<FloatArray> = Array(gyroList.size) {
            FloatArray(4)
        }
        gyroList.onEachIndexed { index, coord ->
            mapGyroList[index][0] = coord.ts.toFloat()
            mapGyroList[index][1] = df.format(coord.x).toFloat()
            mapGyroList[index][2] = df.format(coord.y).toFloat()
            mapGyroList[index][3] = df.format(coord.z).toFloat()
        }

        val mapTypeList: Array<IntArray> = Array(typingList.size) {
            IntArray(2)
        }
        typingList.onEachIndexed { index, typing ->
            mapTypeList[index][0] = typing.ts
            mapTypeList[index][1] = typing.diffCount
        }

        val mapTouchList: Array<FloatArray> = Array(touchList.size) {
            FloatArray(4)
        }
        touchList.onEachIndexed { index, touch ->
            mapTouchList[index][0] = touch.time.toFloat()
            mapTouchList[index][1] = df.format(touch.x).toFloat()
            mapTouchList[index][2] = df.format(touch.y).toFloat()
            mapTouchList[index][3] = touch.pressure
        }
        val telemetryRaw = TelemetryRaw(
            startTime = this.startTime,
            endTime = this.endTime,
            accList = mapAccList,
            gyroList = mapGyroList,
            typeList = mapTypeList,
            touchList = mapTouchList
        )
        return Gson().toJson(telemetryRaw)
    }
}

class TelemetryRaw(
    @SerializedName("start_time")
    var startTime: Long,
    @SerializedName("end_time")
    var endTime: Long,
    @SerializedName("acc")
    var accList: Array<FloatArray>,
    @SerializedName("gyro")
    var gyroList: Array<FloatArray>,
    @SerializedName("type")
    var typeList: Array<IntArray>,
    @SerializedName("touch")
    var touchList: Array<FloatArray>
)

class Telemetry {
    companion object {

        @JvmField
        var telemetrySectionList: MutableList<TelemetrySection> = mutableListOf()

        private var currStartTime = 0L

        @JvmStatic
        fun addSection(eventName: String) {
            currStartTime = System.currentTimeMillis()
            telemetrySectionList.add(
                0,
                TelemetrySection(
                    eventName,
                    currStartTime
                )
            )
        }

        fun getCurrentSectionName():String {
            return if (telemetrySectionList.isEmpty()) {
                ""
            } else {
                telemetrySectionList[0].eventName
            }
        }

        @JvmStatic
        fun addStopTime(eventNameStop: String = "", timeStop: Long = System.currentTimeMillis()) {
            if (telemetrySectionList.isEmpty()) return
            for (telemetrySection in telemetrySectionList) {
                if (telemetrySection.endTime == 0L) {
                    telemetrySection.endTime = timeStop
                }
                if (telemetrySection.eventNameEnd.isEmpty()) {
                    telemetrySection.eventNameEnd = eventNameStop
                }
            }
        }

        /**
         * return false if touch is out of duration
         */
        @JvmStatic
        fun addTouch(type: Int, x: Int, y: Int, pressure: Float): Boolean {
            if (telemetrySectionList.size <= 0) return true
            if (type == MotionEvent.ACTION_DOWN) {
                val timeDiff = getElapsedDiff()
                if (timeDiff < SECTION_TELEMETRY_DURATION) {
                    telemetrySectionList[0].touchList.add(
                        Touch(timeDiff, x, y, pressure)
                    )
                } else {
                    return false
                }
            }
            return true
        }

        fun hasOpenTime() = telemetrySectionList.size > 0 && telemetrySectionList[0].endTime == 0L

        fun getElapsedDiff() = (System.currentTimeMillis() - currStartTime).toInt()

        @JvmStatic
        fun addAccel(x: Float, y: Float, z: Float) {
            val accelList = telemetrySectionList[0].accelList
            val lastCoord = accelList.lastOrNull()
            if (lastCoord != null && lastCoord.x == x && lastCoord.y == y && lastCoord.z == z) {
                accelList.last().visit++
            } else {
                accelList.add(Coord(getElapsedDiff(), x, y, z, 0))
            }
        }

        @JvmStatic
        fun addGyro(x: Float, y: Float, z: Float) {
            val gyroList = telemetrySectionList[0].gyroList
            val lastCoord = gyroList.lastOrNull()
            if (lastCoord != null && lastCoord.x == x && lastCoord.y == y && lastCoord.z == z) {
                gyroList.last().visit++
            } else {
                gyroList.add(Coord(getElapsedDiff(), x, y, z, 0))
            }
        }

        @JvmStatic
        fun addTyping(diffChar: Int) {
            try {
                val typingList = telemetrySectionList[0].typingList
                typingList.add(Typing(getElapsedDiff(), diffChar))
            } catch (e: Exception) {

            }
        }
    }

}