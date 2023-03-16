package com.tokopedia.telemetry.model

import android.view.MotionEvent
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.telemetry.SECTION_TELEMETRY_DURATION
import com.tokopedia.telemetry.TelemetryActLifecycleCallback.Companion.SAMPLING_RATE_MS
import java.math.RoundingMode
import java.text.DecimalFormat

class Touch(val diff: Int, val x: Int, val y: Int, val pressure: Float) {
    override fun toString(): String {
        return "[$diff,$x,$y,$pressure]"
    }
}

class Typing(var diff: Int, var diffCount: Int) {
    override fun toString(): String {
        return "[$diff,$diffCount]"
    }
}

class Coord(var diff: Int, val x: Float, val y: Float, val z: Float, var visit: Int) {
    override fun toString(): String {
        return "[$diff,$x,$y,$z,$visit]"
    }

    fun equal(
        x: Float,
        y: Float,
        z: Float
    ): Boolean {
        return this.x == x && this.y == y && this.z == z
    }
}

@Suppress("MagicNumber")
class TelemetrySection(
    var eventName: String,
    var startTime: Long
) {
    var endTime: Long = 0L
    var eventNameEnd: String = ""
    val typingList: MutableList<Typing> = mutableListOf()
    val touchList: MutableList<Touch> = mutableListOf()
    val accelList: MutableList<Coord> = mutableListOf()
    val gyroList: MutableList<Coord> = mutableListOf()

    fun toJson(): String {
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.HALF_UP

        val mapAccList: Array<FloatArray> = Array(accelList.size) {
            FloatArray(4)
        }
        accelList.onEachIndexed { index, coord ->
            mapAccList[index][0] = coord.diff.toFloat()
            mapAccList[index][1] = df.format(coord.x).toFloat()
            mapAccList[index][2] = df.format(coord.y).toFloat()
            mapAccList[index][3] = df.format(coord.z).toFloat()
        }

        val mapGyroList: Array<FloatArray> = Array(gyroList.size) {
            FloatArray(4)
        }
        gyroList.onEachIndexed { index, coord ->
            mapGyroList[index][0] = coord.diff.toFloat()
            mapGyroList[index][1] = df.format(coord.x).toFloat()
            mapGyroList[index][2] = df.format(coord.y).toFloat()
            mapGyroList[index][3] = df.format(coord.z).toFloat()
        }

        val mapTypeList: Array<IntArray> = Array(typingList.size) {
            IntArray(2)
        }
        typingList.onEachIndexed { index, typing ->
            mapTypeList[index][0] = typing.diff
            mapTypeList[index][1] = typing.diffCount
        }

        val mapTouchList: Array<FloatArray> = Array(touchList.size) {
            FloatArray(4)
        }
        touchList.onEachIndexed { index, touch ->
            mapTouchList[index][0] = touch.diff.toFloat()
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
    @SerializedName("p1")
    var accList: Array<FloatArray>,
    @SerializedName("p2")
    var gyroList: Array<FloatArray>,
    @SerializedName("p3")
    var typeList: Array<IntArray>,
    @SerializedName("p4")
    var touchList: Array<FloatArray>
)

object Telemetry {

    @JvmField
    var telemetrySectionList: MutableList<TelemetrySection> = mutableListOf()

    private var currStartTime = 0L
    private const val MAX_SECTION = 5
    private val lock = Any()

    @JvmStatic
    fun addSection(eventName: String) {
        synchronized(lock) {
            if (telemetrySectionList.size >= MAX_SECTION) {
                telemetrySectionList.removeLast()
            }
            currStartTime = System.currentTimeMillis()
            telemetrySectionList.add(
                0,
                TelemetrySection(
                    eventName,
                    currStartTime
                )
            )
        }
    }

    fun getCurrentSectionName(): String {
        return if (telemetrySectionList.isEmpty()) {
            ""
        } else {
            telemetrySectionList[0].eventName
        }
    }

    @JvmStatic
    fun addStopTime(eventNameStop: String = "", timeStop: Long = System.currentTimeMillis()) {
        synchronized(lock) {
            if (telemetrySectionList.isEmpty()) return
            val sizeList = telemetrySectionList.size
            for (i in 0 until sizeList) {
                val telemetrySection = telemetrySectionList[i]
                if (telemetrySection.endTime == 0L) {
                    if (timeStop - telemetrySection.startTime > SECTION_TELEMETRY_DURATION) {
                        telemetrySection.endTime =
                            telemetrySection.startTime + SECTION_TELEMETRY_DURATION
                    } else {
                        telemetrySection.endTime = timeStop
                    }
                }
                if (telemetrySection.eventNameEnd.isEmpty()) {
                    telemetrySection.eventNameEnd = eventNameStop
                }
            }
        }
    }

    @JvmStatic
    fun addTouch(type: Int, x: Int, y: Int, pressure: Float): Boolean {
        if (telemetrySectionList.size > 0 &&
            type == MotionEvent.ACTION_DOWN
        ) {
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
    fun addAccel(x: Float, y: Float, z: Float): Boolean {
        val accelList = telemetrySectionList[0].accelList
        return addToListCheckLastCoord(accelList, x, y, z)
    }

    @JvmStatic
    fun addGyro(x: Float, y: Float, z: Float): Boolean {
        val gyroList = telemetrySectionList[0].gyroList
        return addToListCheckLastCoord(gyroList, x, y, z)
    }

    private fun addToListCheckLastCoord(
        list: MutableList<Coord>,
        x: Float,
        y: Float,
        z: Float
    ): Boolean {
        val lastCoord = list.lastOrNull()
        val elapsedDiff = getElapsedDiff()
        val diffPrev = if (lastCoord != null) {
            elapsedDiff - lastCoord.diff
        } else {
            SAMPLING_RATE_MS
        }
        if (diffPrev >= SAMPLING_RATE_MS) {
            if (elapsedDiff < SECTION_TELEMETRY_DURATION) {
                list.add(Coord(elapsedDiff, x, y, z, 0))
            } else {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun addTyping(diffChar: Int) {
        try {
            val typingList = telemetrySectionList[0].typingList
            val elapsedDiff = getElapsedDiff()
            if (elapsedDiff < SECTION_TELEMETRY_DURATION) {
                typingList.add(Typing(elapsedDiff, diffChar))
            }
        } catch (ignored: Exception) {
        }
    }
}
