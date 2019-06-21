package com.tokopedia.browse.homepage.domain.usecase

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.browse.homepage.data.source.cache.DigitalBrowseMarketplaceCacheSource
import com.tokopedia.browse.homepage.domain.mapper.MarketplaceViewModelMapper
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseMarketplaceUseCase @Inject
constructor(private val marketplaceViewModelMapper: MarketplaceViewModelMapper,
            private val digitalBrowseMarketplaceCacheSource: DigitalBrowseMarketplaceCacheSource) : GraphqlUseCase() {

    fun createObservable(queryCategory: String): Observable<DigitalBrowseMarketplaceViewModel> {

        val variablesCategory = HashMap<String, Any>()
        variablesCategory.put(PARAM_TYPE, VALUE_TYPE)

        clearRequest()

        addRequest(GraphqlRequest(queryCategory,
                DigitalBrowseMarketplaceData::class.java, variablesCategory, CATEGORY_OPERTAION_NAME))

        return getExecuteObservable(RequestParams.EMPTY)
                .flatMap { graphqlResponse ->
                    var digitalBrowseMarketplaceData = DigitalBrowseMarketplaceData()

                    if (graphqlResponse != null) {
                        digitalBrowseMarketplaceData = graphqlResponse.getData(DigitalBrowseMarketplaceData::class.java)
                    }

                    digitalBrowseMarketplaceCacheSource.saveCache(digitalBrowseMarketplaceData)

                    Observable.just(marketplaceViewModelMapper.transform(digitalBrowseMarketplaceData))
                }
    }

    fun getCache(): Observable<DigitalBrowseMarketplaceViewModel> = digitalBrowseMarketplaceCacheSource.getCache()
            .flatMap { digitalBrowseMarketplaceData ->
                if (digitalBrowseMarketplaceData != null) {
                    Observable.just(marketplaceViewModelMapper.transform(
                            digitalBrowseMarketplaceData))
                } else {
                    Observable.just(null)
                }
            }

    companion object {

        private val PARAM_TYPE = "type"

        private val VALUE_TYPE = 1

        private val CATEGORY_OPERTAION_NAME = "dynamicHomeIcon"
    }
}
