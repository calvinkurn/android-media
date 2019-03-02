package com.tokopedia.browse.homepage.domain.usecase

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.browse.homepage.data.source.cache.DigitalBrowseServiceCacheSource
import com.tokopedia.browse.homepage.domain.mapper.ServiceViewModelMapper
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseServiceUseCase @Inject
constructor(private val digitalBrowseServiceCacheSource: DigitalBrowseServiceCacheSource,
            private val serviceViewModelMapper: ServiceViewModelMapper) : GraphqlUseCase() {

    fun createObservable(queryCategory: String): Observable<DigitalBrowseServiceViewModel> {

        val variablesCategory = HashMap<String, Any>()
        variablesCategory.put(PARAM_TYPE, VALUE_TYPE)

        clearRequest()

        addRequest(GraphqlRequest(queryCategory,
                DigitalBrowseMarketplaceData::class.java, variablesCategory, CATEGORY_OPERTAION_NAME, false))

        return getExecuteObservable(RequestParams.EMPTY)
                .flatMap { graphqlResponse ->
                    var digitalBrowseMarketplaceData = DigitalBrowseMarketplaceData()

                    if (graphqlResponse != null) {
                        digitalBrowseMarketplaceData = graphqlResponse.getData(DigitalBrowseMarketplaceData::class.java)
                    }

                    digitalBrowseServiceCacheSource.saveCache(digitalBrowseMarketplaceData)

                    Observable.just(serviceViewModelMapper.transform(digitalBrowseMarketplaceData))
                }
    }

    fun getCategoryDataFromCache(): Observable<DigitalBrowseServiceViewModel> = digitalBrowseServiceCacheSource.getCache()
            .flatMap { digitalBrowseMarketplaceData ->
                if (digitalBrowseMarketplaceData != null) {
                    Observable.just(serviceViewModelMapper.transform(
                            digitalBrowseMarketplaceData))
                } else {
                    Observable.just(null)
                }
            }

    companion object {

        private val PARAM_TYPE = "type"

        private val VALUE_TYPE = 2

        private val CATEGORY_OPERTAION_NAME = "dynamicHomeIcon"
    }
}