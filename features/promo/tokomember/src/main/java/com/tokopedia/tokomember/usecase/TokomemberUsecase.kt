package com.tokopedia.tokomember.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomember.util.TM_BOTTOMSHEET_DATA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokomemberUsecase @Inject constructor() {

    @Inject
    lateinit var mTmBottomSheetUsecase: MultiRequestGraphqlUseCase

    @GqlQuery("TmBottomSheetData", TM_BOTTOMSHEET_DATA)
    suspend fun getTmBottomSheetData() = withContext(Dispatchers.IO) {
        mTmBottomSheetUsecase.clearRequest()
        val request = GraphqlRequest(TmBottomSheetData.GQL_QUERY,
            Any::class.java,false)
        mTmBottomSheetUsecase.addRequest(request)
        mTmBottomSheetUsecase.executeOnBackground()
    }
}