package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetHomeBalanceWidgetUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeBalanceWidgetData>
) : UseCase<GetHomeBalanceWidgetData>() {
    private val params = RequestParams.create()

    companion object {
        private const val EXPERIMENT = "experiment"
        private const val VARIANT = "variant"
    }

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(GetHomeBalanceWidgetData::class.java)
    }

    override suspend fun executeOnBackground(): GetHomeBalanceWidgetData {
        graphqlUseCase.clearCache()
        params.putString(EXPERIMENT, RollenceKey.HOME_COMPONENT_ATF)
        params.putString(VARIANT, HomeRollenceController.rollenceAtfValue)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}
