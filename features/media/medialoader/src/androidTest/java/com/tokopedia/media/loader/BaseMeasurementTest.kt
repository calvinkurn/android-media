package com.tokopedia.media.loader

import android.graphics.Bitmap
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.test.WithCacheMeasurementLoaderTest
import com.tokopedia.media.loader.test.WithoutCacheMeasurementLoaderTest
import com.tokopedia.media.loader.util.CsvUtil
import com.tokopedia.media.loader.util.legacyLoadImage
import com.tokopedia.media.loader.util.v2LoadImage
import com.tokopedia.test.application.id_generator.FileWriter

abstract class BaseMeasurementTest : BaseTest() {

    abstract fun fileName(): String
    abstract fun maxIteration(): Int

    private val fileWriter = FileWriter()

    fun saveResult(results: List<CsvUtil.CsvLoader>) {
        fileWriter.write(
            folderName = "media_loader",
            fileName = fileName(),
            text = CsvUtil.createCsvWithHeader(results)
        )
    }

    fun loadImageV1Test(
        skipCache: Boolean = false,
        result: (Int, Properties, Bitmap?) -> Unit
    ) {
        for (index in 1 .. maxIteration()) {
            onImageView {
                it.legacyLoadImage(publicImageUrl) {
                    if (skipCache) {
                        setSignatureKey(ObjectKey(System.currentTimeMillis() + index))
                    }

                    listener(onSuccess = { bitmap, _ ->
                        result(index, this, bitmap)
                    })
                }
            }

            Thread.sleep(LOAD_DELAY)
        }
    }

    fun loadImageV2Test(
        skipCache: Boolean = false,
        results: List<CsvUtil.CsvLoader>
    ) {
        results.forEachIndexed { index, _ ->
            onImageView {
                it.v2LoadImage(publicImageUrl) {
                    if (skipCache) {
                        setSignatureKey(ObjectKey(System.currentTimeMillis() + index))
                    }

                    listener(onSuccess = { _, _ ->
                        results[index].improvedLoadTime = loadTime
                    })
                }
            }

            Thread.sleep(LOAD_DELAY)
        }
    }

    fun clearImage() {
        onImageView {
            it.clearImage()
        }
    }

    companion object {
        private const val LOAD_DELAY = 500L
    }
}
