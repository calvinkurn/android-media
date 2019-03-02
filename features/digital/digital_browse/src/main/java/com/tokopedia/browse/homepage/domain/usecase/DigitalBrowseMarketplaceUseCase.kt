package com.tokopedia.browse.homepage.domain.usecase

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseOfficialStoreBrandsEntity
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

    fun createObservable(queryCategory: String, queryOfficial: String): Observable<DigitalBrowseMarketplaceViewModel> {

        val variablesCategory = HashMap<String, Any>()
        variablesCategory.put(PARAM_TYPE, VALUE_TYPE)

        val variablesOfficial = HashMap<String, Any>()
        variablesOfficial.put(PARAM_LANG, VALUE_LANG)
        variablesOfficial.put(PARAM_SOURCE, VALUE_SOURCE)
        variablesOfficial.put(PARAM_TOTAL, VALUE_TOTAL)

        clearRequest()

        addRequest(GraphqlRequest(queryCategory,
                DigitalBrowseMarketplaceData::class.java, variablesCategory, CATEGORY_OPERTAION_NAME, false))
        addRequest(GraphqlRequest(queryOfficial,
                DigitalBrowseOfficialStoreBrandsEntity::class.java, variablesOfficial, OFFICIAL_OPERATION_NAME, false))

        return getExecuteObservable(RequestParams.EMPTY)
                .flatMap { graphqlResponse ->
                    var digitalBrowseMarketplaceData = DigitalBrowseMarketplaceData()

                    if (graphqlResponse != null) {
                        digitalBrowseMarketplaceData = graphqlResponse.getData(DigitalBrowseMarketplaceData::class.java)
                        val officialStoreBrandsEntity = graphqlResponse.getData<DigitalBrowseOfficialStoreBrandsEntity>(DigitalBrowseOfficialStoreBrandsEntity::class.java)
                        digitalBrowseMarketplaceData.popularBrandDatas = officialStoreBrandsEntity.officialStoreBrandList[0].data
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

        private val PARAM_LANG = "lang"
        private val PARAM_SOURCE = "source"
        private val PARAM_TOTAL = "total"
        private val PARAM_TYPE = "type"

        private val VALUE_LANG = "id"
        private val VALUE_SOURCE = "microsite"
        private val VALUE_TOTAL = 6
        private val VALUE_TYPE = 1

        private val OFFICIAL_OPERATION_NAME = "officialStoreBrands"
        private val CATEGORY_OPERTAION_NAME = "dynamicHomeIcon"
    }
}
