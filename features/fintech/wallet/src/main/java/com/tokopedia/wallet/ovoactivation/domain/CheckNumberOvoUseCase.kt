package com.tokopedia.wallet.ovoactivation.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wallet.R
import com.tokopedia.wallet.ovoactivation.data.CheckPhoneOvoEntity
import com.tokopedia.wallet.ovoactivation.data.ResponseCheckPhoneEntity
import com.tokopedia.wallet.ovoactivation.view.model.CheckPhoneOvoModel
import com.tokopedia.wallet.ovoactivation.view.model.ErrorModel
import com.tokopedia.wallet.ovoactivation.view.model.PhoneActionModel
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 24/09/18.
 */
class CheckNumberOvoUseCase @Inject constructor(@param:ApplicationContext private val context: Context,
                                                private val graphqlUseCase: GraphqlUseCase?)
    : UseCase<CheckPhoneOvoModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<CheckPhoneOvoModel> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.wallet_check_phone_query)

                    if (!TextUtils.isEmpty(query)) {
                        val request = GraphqlRequest(query, ResponseCheckPhoneEntity::class.java, false)
                        graphqlUseCase!!.clearRequest()
                        graphqlUseCase.addRequest(request)
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    return@Func1 Observable.error(Exception("Query and/or variable are empty."))
                })
                .map(Func1<GraphqlResponse, ResponseCheckPhoneEntity> {
                    graphqlResponse -> graphqlResponse.getData(ResponseCheckPhoneEntity::class.java) })
                .map { responseCheckPhoneEntity -> responseCheckPhoneEntity.checkPhoneOvoEntity }
                .map { mapper(it) }
    }

    private fun mapper(checkPhoneOvoEntity: CheckPhoneOvoEntity?): CheckPhoneOvoModel {
        val checkPhoneOvoModel = CheckPhoneOvoModel()
        checkPhoneOvoEntity?.let { checkPhoneOvoEntity ->
            checkPhoneOvoModel.phoneNumber = checkPhoneOvoEntity.phoneNumber
            checkPhoneOvoModel.isRegistered = checkPhoneOvoEntity.isRegistered
            checkPhoneOvoModel.registeredApplink = checkPhoneOvoEntity.registeredApplink
            checkPhoneOvoModel.notRegisteredApplink = checkPhoneOvoEntity.notRegisteredApplink
            checkPhoneOvoModel.changeMsisdnApplink = checkPhoneOvoEntity.changeMsisdnApplink
            checkPhoneOvoModel.isAllow = checkPhoneOvoEntity.isAllow

            checkPhoneOvoEntity.errors?.let {
                it.message?.let { message ->
                    val errorModel = ErrorModel()
                    errorModel.message = message
                    checkPhoneOvoModel.errorModel = errorModel
                }
            }

            val phoneActionModel = PhoneActionModel()
            phoneActionModel.titlePhoneAction = checkPhoneOvoEntity.phoneActionEntity!!.title
            phoneActionModel.descPhoneAction = checkPhoneOvoEntity.phoneActionEntity.description
            phoneActionModel.labelBtnPhoneAction = checkPhoneOvoEntity.phoneActionEntity.text
            phoneActionModel.applinkPhoneAction = checkPhoneOvoEntity.phoneActionEntity.applink
            checkPhoneOvoModel.phoneActionModel = phoneActionModel
        }
        return checkPhoneOvoModel
    }

    override fun unsubscribe() {
        graphqlUseCase?.unsubscribe()
    }

}
