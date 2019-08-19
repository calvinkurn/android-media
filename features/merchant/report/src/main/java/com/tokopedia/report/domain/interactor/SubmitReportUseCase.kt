package com.tokopedia.report.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.report.data.model.ImageAttachment
import com.tokopedia.report.data.model.SubmitReportResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
class SubmitReportUseCase @Inject constructor(private val userSession: UserSessionInterface,
                                              @Named("product_report_submit") private val gqlMutationQuery: String,
                                              private val graphqlUseCase: GraphqlUseCase,
                                              private val uploadImageUseCase: UploadImageUseCase<ImageAttachment.Data>): UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        if (requestParams == null) return Observable.error(Throwable("Invalid Params"))

        val additionalFields = requestParams.parameters[PARAM_FIELDS] as MutableMap<String, Any>
        val photos = additionalFields[KEY_PHOTO] as? List<String> ?: listOf()

        return with(photos.map { createParamUploadImage(it) }){
            Observable.from(this)
                    .flatMap { uploadImageUseCase.createObservable(it) }.map {
                        it.dataResultImageUpload.picSrc
                    }.filter { it != null }
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        if (it.isNotEmpty()) {
                            additionalFields[KEY_PHOTO] = it
                            requestParams.putObject(PARAM_FIELDS, additionalFields)
                        }
                        createSubmitReport(requestParams)
                    }
        }
    }

    private fun createSubmitReport(requestParams: RequestParams): Observable<Boolean> {
        val graphqlRequest = GraphqlRequest(gqlMutationQuery, SubmitReportResponse.Data::class.java,
                mapOf("input" to requestParams.parameters), false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { gqlResponse ->
            val gqlError = gqlResponse.getError(SubmitReportResponse.Data::class.java)
                    ?.filter { it.message.isNotBlank() }?.map { it.message } ?: listOf()
            if (gqlError.isNotEmpty()){
                Observable.error(MessageErrorException(gqlError.joinToString(", ")))
            } else {
                Observable.just(gqlResponse.getData<SubmitReportResponse.Data>(SubmitReportResponse.Data::class.java).response.isSuccess)
            }
        }
    }

    private fun createParamUploadImage(photoUri: String): RequestParams {
        val id = RequestBody.create(MediaType.parse("text/plain"), userSession.userId )
        val token = RequestBody.create(MediaType.parse("text/plain"), userSession.accessToken )
        val map = mapOf(PARAM_ID to id, PARAM_TOKEN to token)
        return uploadImageUseCase.createRequestParam(photoUri, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, map)
    }

    companion object{
        private const val PARAM_ID = "id"
        private const val PARAM_TOKEN = "token"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"

        private const val KEY_PHOTO = "photo"

        private const val PARAM_FIELDS = "additional_fields"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_CATEGORY_ID = "category_id"

        fun createRequestParamn(categoryId: Int, productId: Int, fields: Map<String, Any>) = RequestParams.create()
                .apply {
                    putInt(PARAM_PRODUCT_ID, productId)
                    putInt(PARAM_CATEGORY_ID, categoryId)
                    putObject(PARAM_FIELDS, fields)
                }
    }
}