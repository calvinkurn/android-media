package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetBusinessUnitDataUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeWidget.Data>
) : UseCase<List<BusinessUnitItemDataModel>>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setTypeClass(HomeWidget.Data::class.java)
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): List<BusinessUnitItemDataModel> {
        graphqlUseCase.clearCache()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().homeWidget.contentItemTabList.withIndex().map {
            BusinessUnitItemDataModel(it.value, it.index)
        }
    }

    fun setParams(id: Int){
        params.parameters.clear()
        params.putInt(PARAM_ID, id)
    }

    companion object{
        private const val PARAM_ID = "tabId"
    }
}