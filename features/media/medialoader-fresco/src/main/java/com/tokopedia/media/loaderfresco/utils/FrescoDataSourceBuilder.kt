package com.tokopedia.media.loaderfresco.utils

import android.content.Context
import android.net.Uri
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.tokopedia.media.loaderfresco.data.Properties

internal object FrescoDataSourceRequest {
    fun frescoDataSourceBuilder(properties: Properties, context: Context): DataSource<CloseableReference<CloseableImage>> {
        val source = properties.data
        val generatedUri = (source as String).generateFrescoUri()

        val request = ImageRequestBuilder.newBuilderWithSource(generatedUri).build()

        val imagePipeline = Fresco.getImagePipeline()
        return imagePipeline.fetchDecodedImage(request, context)
    }
}
