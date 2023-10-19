package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class WebpLoadTimeTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()
    private val maxIteration = 50

    override fun fileName(): String {
        return "media_loader_load_time_webp_support.csv"
    }

    @Test
    fun load_time_webp_support() {
        (0..maxIteration).forEach {
            val startTime = System.currentTimeMillis()

            loadImageTest(
                imageUrl = IMAGE_URL,
                skipCache = true
            ) { prop, _ ->
                val endTime = System.currentTimeMillis()

                results.add(
                    CsvUtil.CsvLoader(
                        iterationIndex = it,
                        properties = prop,
                        loadTime = (endTime - startTime).toString()
                    )
                )
            }
        }

        saveResult()
    }

    // basic load time CSV
    private fun saveResult() {
        fileWriter.write(
            folderName = "media_loader",
            fileName = fileName(),
            text = CsvUtil.createBasicLoadTimeCsvWithHeader(results)
        )
    }

    companion object {
        // supported X-Tkp-Fmt header
        private const val IMAGE_URL = "https://images.tokopedia.net/img/cache/200/haryot/2023/10/11/6986d90d-fe5f-47eb-b766-46a1efa1f3b5.jpg"
    }
}
