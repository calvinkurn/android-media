package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.beranda.data.model.TargetedTicker
import com.tokopedia.home.beranda.di.module.query.TargetedTickerQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.interactor.repository.param.TargetedTickerParam
import javax.inject.Inject

class TargetedTicketRepository @Inject constructor(
    private val gqlRepository: GraphqlRepository,
) : HomeRepository<TargetedTicker> {

    override suspend fun getRemoteData(bundle: Bundle): TargetedTicker {
        val page = bundle.getString(PARAM_PAGE, DEFAULT_PARAM_PAGE)
        return gqlRepository.request(
            TargetedTickerQuery(),
            mapOf(PARAM_PAGE to page)
        )
    }

    override suspend fun getCachedData(bundle: Bundle): TargetedTicker {
        return TargetedTicker()
    }

    companion object {
        private const val PARAM_PAGE = "page"
        private const val DEFAULT_PARAM_PAGE = "marketplace.home"

        fun createParam(page: String = DEFAULT_PARAM_PAGE) =
            Bundle().apply {
                putString(PARAM_PAGE, page)
            }
    }
}
