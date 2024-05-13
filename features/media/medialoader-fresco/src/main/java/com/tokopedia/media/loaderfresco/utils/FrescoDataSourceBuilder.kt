package com.tokopedia.media.loaderfresco.utils

import android.content.Context
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder

internal object FrescoDataSourceRequest {
    fun frescoDataSourceBuilder(url: String?, context: Context): DataSource<CloseableReference<CloseableImage>> {
        val generatedUri = url.generateFrescoUri()
        val request = ImageRequestBuilder.newBuilderWithSource(generatedUri).build()

        val imagePipeline = Fresco.getImagePipeline()
        return imagePipeline.fetchDecodedImage(request, context)
    }
}
