package com.tokopedia.media.loader.test

import android.content.Context.MODE_PRIVATE
import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.data.LOW_QUALITY_SETTINGS
import com.tokopedia.media.loader.data.MEDIA_QUALITY_PREF
import com.tokopedia.media.loader.util.AssetReader
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class InternalCdnAdaptiveLoaderTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()

    override fun fileName(): String {
        return "media_loader_load_time_adaptive.csv"
    }

    override fun setUp() {
        // force the setting to enable the adaptive image loader
        applicationContext
            .getSharedPreferences(MEDIA_QUALITY_PREF, MODE_PRIVATE)
            .edit()
            .putInt("index_image_quality_setting", LOW_QUALITY_SETTINGS)
            .apply()

        super.setUp()
    }

    @Test
    fun load_time_measurement_without_cache_multi_url_low_quality() {
        val urls = AssetReader.get(
            applicationContext,
            "image-url.txt"
        )

        // v1
        urls.forEachIndexed { index, url ->
            loadImageV1Test(
                imageUrl = url,
                skipCache = true
            ) { prop, bitmap ->
                results.add(
                    CsvUtil.CsvLoader(
                        iterationIndex = index,
                        properties = prop,
                        bitmap = bitmap
                    )
                )
            }
        }

        // idle
        idle()

        // v2
        urls.forEachIndexed { index, url ->
            loadImageV2Test(
                imageUrl = url,
                skipCache = true
            ) { prop, _ ->
                try {
                    results[index].improvedLoadTime = prop.loadTime
                } catch (ignored: Throwable) {}
            }
        }

        // save as csv
        saveResult(results)
    }
}
