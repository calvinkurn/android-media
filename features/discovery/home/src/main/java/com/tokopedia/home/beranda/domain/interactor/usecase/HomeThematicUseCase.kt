package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeThematicResponse
import com.tokopedia.home.beranda.di.module.query.HomeThematicQuery
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeThematicModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by frenzel
 */
class HomeThematicUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeThematicResponse>,
): UseCase<HomeThematicModel>() {
    init {
        graphqlUseCase.setGraphqlQuery(HomeThematicQuery())
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeThematicResponse::class.java)
    }
    override suspend fun executeOnBackground(): HomeThematicModel {
        graphqlUseCase.clearCache()
        val response = graphqlUseCase.executeOnBackground()
        return mapData(response.getHomeThematic.thematic)
    }

    private fun mapData(data: HomeThematicResponse.Thematic): HomeThematicModel {
        return HomeThematicModel(
            isShown = data.isShown,
            colorMode = data.colorMode,
            heightPercentage = data.heightPercentage,
            backgroundImageURL = data.backgroundImageURL,
            foregroundImageURL = data.foregroundImageURL,
        )
    }
}
