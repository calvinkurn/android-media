package com.tokopedia.user_identification_common.domain.usecase

//
// Created by Yoris Prayogo on 2019-10-29.
//

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.R
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import rx.Subscriber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class GetKtpStatusUseCase @Inject
constructor(private val resources: Resources, private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: RequestParams, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw
                .query_is_ktp)

        val graphqlRequest = GraphqlRequest(query,
                CheckKtpStatusPojo::class.java, requestParams.parameters, false)

        graphqlUseCase.apply {
            clearRequest()
            addRequest(graphqlRequest)
            execute(subscriber)
        }
    }

    internal fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }


    fun resizeImage(imagePath: String, maxWidth: Int, maxHeight: Int): String {
        var image = BitmapFactory.decodeFile(imagePath)
        val width = image.width
        val height = image.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getRequestParam(imgPath: String): RequestParams {
        return RequestParams.create().apply {
            putString(IMAGE, resizeImage(imgPath, MAX_WIDTH, MAX_HEIGHT))
            putString(IDENTIFIER, "")
            putString(SOURCE, "kyc")
        }
    }

    companion object {
        private val IMAGE = "image"
        private val IDENTIFIER = "id"
        private val SOURCE = "src"

        const val MAX_WIDTH = 500
        const val MAX_HEIGHT = 500
    }

}
