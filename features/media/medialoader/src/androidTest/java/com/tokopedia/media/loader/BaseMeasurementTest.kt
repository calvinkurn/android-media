package com.tokopedia.media.loader

import android.graphics.Bitmap
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.util.CsvUtil
import com.tokopedia.media.loader.util.newLoadImage
import com.tokopedia.test.application.id_generator.FileWriter

abstract class BaseMeasurementTest : BaseTest() {

    abstract fun fileName(): String

    protected val fileWriter = FileWriter()

    fun saveComparisonResult(results: List<CsvUtil.CsvLoader>) {
        fileWriter.write(
            folderName = "media_loader",
            fileName = fileName(),
            text = CsvUtil.createComparedLoadTimeCsvWithHeader(results)
        )
    }

    fun loadImageTest(
        imageUrl: String = publicImageUrl,
        skipCache: Boolean = false,
        result: (Properties, Bitmap?) -> Unit
    ) {
        onImageView {
            it.newLoadImage(imageUrl) {
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
