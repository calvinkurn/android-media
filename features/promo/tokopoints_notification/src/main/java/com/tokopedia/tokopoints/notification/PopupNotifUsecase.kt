package com.tokopedia.tokopoints.notification

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.notification.Constant.Companion.API_VERSION
import com.tokopedia.tokopoints.notification.Constant.Companion.API_VERSION_VALUE
import com.tokopedia.tokopoints.notification.Constant.Companion.KEY_TYPE
import com.tokopedia.tokopoints.notification.model.TokoPointDetailEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PopupNotifUsecase  @Inject constructor() {

    @Inject
    lateinit var tnPopupUsecase: MultiRequestGraphqlUseCase

    @GqlQuery("TnPopUp", POPUP_QUERY)
    suspend fun getPopupNotif(type: String) = withContext(Dispatchers.IO) {
        tnPopupUsecase.clearRequest()
        val variables: MutableMap<String, Any> = HashMap()
        variables[API_VERSION] = API_VERSION_VALUE
        variables[KEY_TYPE] = type

        val request = GraphqlRequest(
            TnPopUp.GQL_QUERY,
            TokoPointDetailEntity::class.java, variables, false
        )
        tnPopupUsecase.addRequest(request)
        tnPopupUsecase.executeOnBackground()
    }
}