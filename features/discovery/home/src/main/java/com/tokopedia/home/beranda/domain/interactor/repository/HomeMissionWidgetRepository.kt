package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeWidget.HomeMissionWidget>
)
    : UseCase<HomeWidget.HomeMissionWidget>(), HomeRepository<HomeWidget.HomeMissionWidget> {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeWidget.HomeMissionWidget::class.java)
    }

    private var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): HomeWidget.HomeMissionWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeWidget.HomeMissionWidget {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeWidget.HomeMissionWidget {
        return HomeWidget.HomeMissionWidget()
    }
}