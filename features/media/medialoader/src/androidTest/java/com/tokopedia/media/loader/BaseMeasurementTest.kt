package com.tokopedia.media.loader

import android.graphics.Bitmap
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.util.CsvUtil
import com.tokopedia.media.loader.util.legacyLoadImage
import com.tokopedia.media.loader.util.v2LoadImage
import com.tokopedia.test.application.id_generator.FileWriter

abstract class BaseMeasurementTest : BaseTest() {

    abstract fun fileName(): String

    private val fileWriter = FileWriter()

    fun saveResult(results: List<CsvUtil.CsvLoader>) {
        fileWriter.write(
            folderName = "media_loader",
            fileName = fileName(),
            text = CsvUtil.createCsvWithHeader(results)
        )
    }

    fun loadImageV1Test(
        imageUrl: String = publicImageUrl,
        skipCache: Boolean = false,
        result: (Properties, Bitmap?) -> Unit
    ) {
        onImageView {
            it.legacyLoadImage(imageUrl) {
                if (skipCache) {
                    setSignatureKey(ObjectKey(System.currentTimeMillis()))
                }

                listener(onSuccess = { bitmap, _ ->
                    result(this, bitmap)
                })
            }
        }

        Thread.sleep(LOAD_DELAY)
    }

    fun loadImageV2Test(
        imageUrl: String = publicImageUrl,
        skipCache: Boolean = false,
        result: (Properties, Bitmap?) -> Unit
    ) {
        onImageView {
            it.v2LoadImage(imageUrl) {
                if (skipCache) {
                    setSignatureKey(ObjectKey(System.currentTimeMillis()))
                }

                listener(onSuccess = { bitmap, _ ->
                    result(this, bitmap)
                })
            }
        }

        Thread.sleep(LOAD_DELAY)
    }

    fun idle() {
        clearImage()
        Thread.sleep(IDLE_DELAY)
    }

    private fun clearImage() {
        onImageView {
            it.clearImage()
        }
    }

    companion object {
        private const val LOAD_DELAY = 500L
        private const val IDLE_DELAY = 3000L
    }
}
