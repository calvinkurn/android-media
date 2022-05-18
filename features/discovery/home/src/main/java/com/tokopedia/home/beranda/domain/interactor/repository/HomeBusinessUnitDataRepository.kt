package com.tokopedia.home.beranda.domain.interactor.repository

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeBusinessUnitDataRepository @Inject constructor(
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
            BusinessUnitItemDataModel(it.value, it.index, params.getInt(PARAM_TAB_POSITION, 0), params.getString(
                PARAM_TAB_NAME, ""))
        }
    }

    fun setParams(id: Int, position: Int, name: String){
        params.parameters.clear()
        params.putInt(PARAM_ID, id)
        params.putInt(PARAM_TAB_POSITION, position)
        params.putString(PARAM_TAB_NAME, name)
    }

    companion object{
        private const val PARAM_ID = "tabId"
        private const val PARAM_TAB_POSITION = "tabPosition"
        private const val PARAM_TAB_NAME = "tabName"
    }
}