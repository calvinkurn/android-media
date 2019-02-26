package com.tokopedia.affiliate.feature.createpost.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.listener.SubmitPostNotificationManager
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/1/18.
 */
open class SubmitPostUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val uploadMultipleImageUseCase: UploadMultipleImageUseCase,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<SubmitPostData>() {

    val notificationManager: SubmitPostNotificationManager? = null

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<SubmitPostData> {
        val imageList = requestParams.getObject(PARAM_IMAGE_LIST) as List<String>
        val mainImageIndex = requestParams.getInt(PARAM_MAIN_IMAGE_INDEX, 0)
        return uploadMultipleImageUseCase
                .createObservable(
                        UploadMultipleImageUseCase.createRequestParams(
                                getMediumList(imageList, mainImageIndex)
                        )
                )
                .flatMap(submitPostToGraphql(requestParams))
    }

    private fun getMediumList(imageList: List<String>, mainImageIndex: Int): List<SubmitPostMedium> {
        val mediumList = ArrayList<SubmitPostMedium>()
        mediumList.add(SubmitPostMedium(imageList[mainImageIndex], 0))
        for (i in imageList.indices) {
            if (i != mainImageIndex) {
                mediumList.add(SubmitPostMedium(imageList[i], mediumList.size))
            }
        }
        return mediumList
    }


    private fun submitPostToGraphql(requestParams: RequestParams): Func1<List<SubmitPostMedium>, Observable<SubmitPostData>> {
        return Func1 { mediumList ->
            notificationManager?.onSubmitPost()

            val query = GraphqlHelper.loadRawString(
                    context.resources,
                    R.raw.mutation_af_submit_post
            )

            val variables = HashMap<String, Any>()
            variables[PARAM_INPUT] = getContentSubmitInput(requestParams, mediumList)

            val graphqlRequest = GraphqlRequest(
                    query,
                    SubmitPostData::class.java,
                    variables)

            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase
                    .createObservable(RequestParams.create())
                    .map(mapGraphqlResponse())
        }
    }

    private fun mapGraphqlResponse(): Func1<GraphqlResponse, SubmitPostData> {
        return Func1 { graphqlResponse -> graphqlResponse.getData(SubmitPostData::class.java) }
    }

    protected open fun getContentSubmitInput(requestParams: RequestParams,
                                             mediumList: List<SubmitPostMedium>): ContentSubmitInput {
        val input = ContentSubmitInput()
        input.type = requestParams.getString(PARAM_TYPE, "")
        input.token = requestParams.getString(PARAM_TOKEN, "")
        input.adID = requestParams.getString(PARAM_AD_ID, "")
        input.productID = requestParams.getString(PARAM_PRODUCT_ID, "")
        input.media = mediumList
        return input
    }

    companion object {
        internal const val PARAM_TYPE = "type"
        internal const val PARAM_TOKEN = "token"
        internal const val PARAM_IMAGE_LIST = "image_list"
        internal const val PARAM_MAIN_IMAGE_INDEX = "main_image_index"
        internal const val TYPE_AFFILIATE = "affiliate"

        private const val PARAM_AD_ID = "adID"
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_INPUT = "input"

        const val SUCCESS = 1

        fun createRequestParams(productId: String, adId: String, token: String,
                                imageList: List<String>, mainImageIndex: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_TYPE, TYPE_AFFILIATE)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_AD_ID, adId)
            requestParams.putString(PARAM_TOKEN, token)
            requestParams.putObject(PARAM_IMAGE_LIST, imageList)
            requestParams.putInt(PARAM_MAIN_IMAGE_INDEX, mainImageIndex)
            return requestParams
        }
    }
}
