package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.SalamWidgetQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by firman on 09-06-2020
 */

class HomeSalamWidgetRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SalamWidget>)
    : UseCase<SalamWidget>(), HomeRepository<SalamWidget> {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SalamWidget::class.java)
    }

    companion object {
        const val NULL_RESPONSE = "null response"
    }

    override suspend fun executeOnBackground(): SalamWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(SalamWidgetQuery())
        val response = graphqlUseCase.executeOnBackground()
        return if (response != null) response
        else throw (MessageErrorException(NULL_RESPONSE))
    }

    override suspend fun getRemoteData(bundle: Bundle): SalamWidget {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): SalamWidget {
        return SalamWidget()
    }
}