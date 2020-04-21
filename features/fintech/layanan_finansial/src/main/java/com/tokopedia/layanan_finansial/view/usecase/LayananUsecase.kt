package com.tokopedia.layanan_finansial.view.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.layanan_finansial.di.LayananModule.Companion.LAYANAN_DETAIL_QUERY
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.LayananFinansialOuter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LayananUsecase @Inject constructor(@Named(LAYANAN_DETAIL_QUERY) private val query : String, private val usecase: MultiRequestGraphqlUseCase){

    suspend fun execute() = withContext(Dispatchers.IO){
        usecase.clearRequest()
        val request = GraphqlRequest(query,LayananFinansialOuter::class.java)
        usecase.addRequest(request)
        usecase.executeOnBackground().getSuccessData<LayananFinansialOuter>()
    }
}