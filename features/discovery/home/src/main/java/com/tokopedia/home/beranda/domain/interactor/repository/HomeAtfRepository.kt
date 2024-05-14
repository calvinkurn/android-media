package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.libra.LibraConst
import com.tokopedia.libra.LibraInstance
import com.tokopedia.libra.LibraOwner
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeAtfRepository @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeAtfData>,
    private val libraInstance: LibraInstance,
) : UseCase<HomeAtfData>(), HomeRepository<HomeAtfData> {

    companion object {
        private const val EXPERIMENT = "experiment"
        private const val VARIANT = "variant"
    }

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeAtfData::class.java)
    }

    private val params = RequestParams.create()

    override suspend fun executeOnBackground(): HomeAtfData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeAtfData {
        putExperimentParam()
        return HomeAtfData(dataList = listOf(AtfData(component = "balance")))
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeAtfData {
        return HomeAtfData()
    }

    private fun putExperimentParam() {
        val variant = libraInstance.variant(LibraOwner.Home, LibraConst.HOME_REVAMP_3_TYPE)
        params.putString(EXPERIMENT, LibraConst.HOME_REVAMP_3_TYPE)
        params.putString(VARIANT, variant)
    }
}
