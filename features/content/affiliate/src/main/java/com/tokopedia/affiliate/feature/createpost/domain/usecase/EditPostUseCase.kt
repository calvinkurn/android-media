package com.tokopedia.affiliate.feature.createpost.domain.usecase

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

/**
 * @author by milhamj on 10/19/18.
 */
class EditPostUseCase @Inject constructor(
        @ApplicationContext context: Context,
        uploadMultipleImageUseCase: UploadMultipleImageUseCase,
        graphqlUseCase: GraphqlUseCase) : SubmitPostUseCase(context, uploadMultipleImageUseCase, graphqlUseCase) {

    override fun getContentSubmitInput(requestParams: RequestParams,
                                       mediumList: List<SubmitPostMedium>): ContentSubmitInput {
        val input = ContentSubmitInput()
        input.type = requestParams.getString(SubmitPostUseCase.PARAM_TYPE, "")
        input.token = requestParams.getString(SubmitPostUseCase.PARAM_TOKEN, "")
        input.activityId = requestParams.getString(PARAM_POST_ID, "")
        input.action = requestParams.getString(PARAM_ACTION, "")
        input.media = mediumList
        return input
    }

    companion object {
        private const val PARAM_POST_ID = "ID"
        private const val PARAM_ACTION = "action"
        private const val ACTION_UPDATE = "update"

        fun createRequestParams(postId: String, token: String,
                                imageList: List<String>, mainImageIndex: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SubmitPostUseCase.PARAM_TYPE, SubmitPostUseCase.TYPE_AFFILIATE)
            requestParams.putString(PARAM_POST_ID, postId)
            requestParams.putString(SubmitPostUseCase.PARAM_TOKEN, token)
            requestParams.putString(PARAM_ACTION, ACTION_UPDATE)
            requestParams.putObject(SubmitPostUseCase.PARAM_IMAGE_LIST, imageList)
            requestParams.putInt(SubmitPostUseCase.PARAM_MAIN_IMAGE_INDEX, mainImageIndex)
            return requestParams
        }
    }
}
