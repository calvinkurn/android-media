package com.tokopedia.broadcast.message.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.data.model.BlastMessageMutation
import com.tokopedia.broadcast.message.data.model.BlastMessageResponse
import com.tokopedia.broadcast.message.data.model.ImageAttachment
import com.tokopedia.broadcast.message.data.model.ProductPayloadMutation
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import java.lang.RuntimeException
import javax.inject.Inject

class SaveChatBlastSellerUseCase @Inject constructor(
        @ImageUploaderQualifier val userSession: UserSessionInterface,
        @ApplicationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase,
        private val uploadImageUseCase: UploadImageUseCase<ImageAttachment.Data>
): UseCase<BlastMessageResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<BlastMessageResponse> {
        val imagePath = requestParams?.getString(PARAM_PATH_IMAGE, "") ?: ""
        return uploadImageUseCase.createObservable(createParamUploadImage(imagePath))
                .flatMap {
                    val imageAttachment = it.dataResultImageUpload
                    if (imageAttachment == null)
                        throw RuntimeException()

                    createGqlSubmitBlastSeller(imageAttachment.picSrc, requestParams)
                }
    }

    private fun createGqlSubmitBlastSeller(imageUrl: String?, requestParams: RequestParams?): Observable<BlastMessageResponse>{
        val mutation = GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_add_broadcast)
        val graphqlRequest = GraphqlRequest(mutation, BlastMessageResponse.Result::class.java, createSubmitVariable(imageUrl, requestParams), false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { graphqlResponse ->
            val errors: List<GraphqlError> = graphqlResponse.getError(BlastMessageResponse.Result::class.java) ?: listOf()
            val response = graphqlResponse.getData<BlastMessageResponse.Result>(BlastMessageResponse.Result::class.java)

            if (errors.isEmpty()){
                Observable.just(response)
            } else {
                val error = errors[0].message
                if (TextUtils.isEmpty(error)){
                    Observable.just(response)
                } else {
                    Observable.error(MessageErrorException(error))
                }
            }
        }.map { it.result }
    }

    private fun createSubmitVariable(imageUrl: String?, requestParams: RequestParams?): MutableMap<String, Any> {
        return mutableMapOf(
                PARAM_MESSAGE to (requestParams?.getString(PARAM_MESSAGE, "") ?: ""),
                PARAM_IMAGE_URL to (imageUrl ?: ""),
                PARAM_HAS_PRODUCT to (requestParams?.getBoolean(PARAM_HAS_PRODUCT, false) == true),
                PARAM_PRODUCT to (requestParams?.getObject(PARAM_PRODUCT) ?: arrayOf<ProductPayloadMutation>())
        )
    }

    companion object {
        private const val PARAM_PATH_IMAGE = "path_image"
        private const val PARAM_ID = "id"
        private const val PARAM_TOKEN = "token"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"

        private const val PARAM_MESSAGE = "message"
        private const val PARAM_IMAGE_URL = "mtImageUrl"
        private const val PARAM_HAS_PRODUCT = "hasProducts"
        private const val PARAM_PRODUCT = "productsPayload"

        fun createRequestParams(mutationModel: BlastMessageMutation) = RequestParams.create()
                .apply {
                    putString(PARAM_PATH_IMAGE, mutationModel.imagePath)
                    putString(PARAM_MESSAGE, mutationModel.message)
                    putBoolean(PARAM_HAS_PRODUCT, mutationModel.hasProducts)
                    putObject(PARAM_PRODUCT, mutationModel.productsPayload)
                }
    }

    private fun createParamUploadImage(pathFileImage: String): RequestParams{
        val map = mutableMapOf<String, RequestBody>()
        val id = RequestBody.create(MediaType.parse("text/plain"), userSession.userId )
        val token = RequestBody.create(MediaType.parse("text/plain"), userSession.accessToken )
        map.put(PARAM_ID, id)
        map.put(PARAM_TOKEN, token)
        return uploadImageUseCase.createRequestParam(pathFileImage, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, map)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        uploadImageUseCase.unsubscribe()
    }
}