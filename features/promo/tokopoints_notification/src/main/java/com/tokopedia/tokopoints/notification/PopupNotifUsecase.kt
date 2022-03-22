package com.tokopedia.tokopoints.notification

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.notification.model.popupnotif.PopupNotifResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PopupNotifUsecase  @Inject constructor() {

    @Inject
    lateinit var tnPopupUsecase: MultiRequestGraphqlUseCase

    @GqlQuery("TnPopUp", POPUP_QUERY)
    suspend fun getPopupNotif(type: String) = withContext(Dispatchers.IO) {
        tnPopupUsecase.clearRequest()

        val request = GraphqlRequest(
            TnPopUp.GQL_QUERY,
            PopupNotifResponse::class.java, false
        )
        tnPopupUsecase.addRequest(request)
        tnPopupUsecase.executeOnBackground()
    }
}