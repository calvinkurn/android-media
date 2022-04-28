package com.tokopedia.loginregister.inactive_phone_number.domain

import android.annotation.SuppressLint
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.inactive_phone_number.data.model.RegisterCheckModel
import com.tokopedia.sessioncommon.domain.query.LoginQueries
import javax.inject.Inject

@SuppressLint("DeprecatedMethod")
class InactivePhoneNumberUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): InactivePhoneNumberUseCaseContract, GraphqlUseCase<RegisterCheckModel>(graphqlRepository) {

    override suspend fun getData(phoneNumber: String): RegisterCheckModel {
        setTypeClass(RegisterCheckModel::class.java)
        setGraphqlQuery(LoginQueries.registerCheckQuery)

        setRequestParams(mapOf("id" to phoneNumber))
        return executeOnBackground()
    }

}