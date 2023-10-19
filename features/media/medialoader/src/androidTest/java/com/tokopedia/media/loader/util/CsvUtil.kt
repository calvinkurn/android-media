package com.tokopedia.media.loader.util

import android.graphics.Bitmap
import com.tokopedia.media.loader.data.Properties

object CsvUtil {

    fun createComparedLoadTimeCsvWithHeader(data: List<CsvLoader>): String {
        val header = "Iteration, Url, Load Time v1 (millis), Load Time v2 (millis), Width, Height"

        return buildString {
            appendLine(header)

            data.forEach {
                appendLine(createComparedLoadTimeCsv(it))
            }
        }
    }

    fun createBasicLoadTimeCsvWithHeader(data: List<CsvLoader>): String {
        val header = "#, Url, Load Time"

        return buildString {
            appendLine(header)

            data.forEach {
                appendLine(createBasicLoadTimeCsv(it))
            }
        }
    }

    private fun createComparedLoadTimeCsv(data: CsvLoader): String {
        with(data) {
            val bitmapWidth = (bitmap?.width?: 0).toString()
            val bitmapHeight = (bitmap?.height?: 0).toString()

            return buildString {
                // iteration index
                append(iterationIndex.toString())
                append(", ")

                // url
                append(properties.data.toString())
                append(", ")

                // load time (legacy)
                append(properties.loadTime)
                append(", ")

                // load time (v2)
                append(improvedLoadTime)
                append(", ")

                append(bitmapWidth)
                append(", ")

                append(bitmapHeight)
            }
        }
    }

    private fun createBasicLoadTimeCsv(data: CsvLoader): String {
        with(data) {
            return buildString {
                // iteration index
                append(iterationIndex.toString())
                append(", ")

                // url
                append(properties.data.toString())
                append(", ")

                // load time (legacy)
                append(loadTime)
            }
        }
    }

    data class CsvLoader(
        val iterationIndex: Int,
        val properties: Properties,
        var improvedLoadTime: String = "0",
        var loadTime: String = "0",
        val bitmap: Bitmap? = null
    )
}
