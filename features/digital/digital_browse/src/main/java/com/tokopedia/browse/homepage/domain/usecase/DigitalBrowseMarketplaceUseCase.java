package com.tokopedia.browse.homepage.domain.usecase;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseOfficialStoreBrandsEntity;
import com.tokopedia.browse.homepage.data.source.cache.DigitalBrowseMarketplaceCacheSource;
import com.tokopedia.browse.homepage.domain.mapper.MarketplaceViewModelMapper;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseMarketplaceUseCase extends GraphqlUseCase {

    private static final String PARAM_LANG = "lang";
    private static final String PARAM_SOURCE = "source";
    private static final String PARAM_TOTAL = "total";
    private static final String PARAM_TYPE = "type";

    private static final String VALUE_LANG = "id";
    private static final String VALUE_SOURCE = "microsite";
    private static final int VALUE_TOTAL = 6;
    private static final int VALUE_TYPE = 1;

    private static final String OFFICIAL_OPERATION_NAME = "officialStoreBrands";
    private static final String CATEGORY_OPERTAION_NAME = "dynamicHomeIcon";

    private DigitalBrowseMarketplaceCacheSource digitalBrowseMarketplaceCacheSource;
    private MarketplaceViewModelMapper marketplaceViewModelMapper;

    @Inject
    public DigitalBrowseMarketplaceUseCase(MarketplaceViewModelMapper marketplaceViewModelMapper,
                                           DigitalBrowseMarketplaceCacheSource digitalBrowseMarketplaceCacheSource) {
        this.marketplaceViewModelMapper = marketplaceViewModelMapper;
        this.digitalBrowseMarketplaceCacheSource = digitalBrowseMarketplaceCacheSource;
    }

    public Observable<DigitalBrowseMarketplaceViewModel> createObservable(String queryCategory, String queryOfficial) {

        Map<String, Object> variablesCategory = new HashMap<>();
        variablesCategory.put(PARAM_TYPE, VALUE_TYPE);

        Map<String, Object> variablesOfficial = new HashMap<>();
        variablesOfficial.put(PARAM_LANG, VALUE_LANG);
        variablesOfficial.put(PARAM_SOURCE, VALUE_SOURCE);
        variablesOfficial.put(PARAM_TOTAL, VALUE_TOTAL);

        clearRequest();

        addRequest(new GraphqlRequest(queryCategory,
                DigitalBrowseMarketplaceData.class, variablesCategory, CATEGORY_OPERTAION_NAME));
        addRequest(new GraphqlRequest(queryOfficial,
                DigitalBrowseOfficialStoreBrandsEntity.class, variablesOfficial, OFFICIAL_OPERATION_NAME));

        return getExecuteObservable(RequestParams.EMPTY)
                .flatMap(new Func1<GraphqlResponse, Observable<DigitalBrowseMarketplaceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseMarketplaceViewModel> call(GraphqlResponse graphqlResponse) {
                        DigitalBrowseMarketplaceData digitalBrowseMarketplaceData = new DigitalBrowseMarketplaceData();

                        if (graphqlResponse != null) {
                            digitalBrowseMarketplaceData = graphqlResponse.getData(DigitalBrowseMarketplaceData.class);
                            DigitalBrowseOfficialStoreBrandsEntity officialStoreBrandsEntity = graphqlResponse.getData(DigitalBrowseOfficialStoreBrandsEntity.class);
                            digitalBrowseMarketplaceData.setPopularBrandDatas(officialStoreBrandsEntity.getOfficialStoreBrandList().get(0).getData());
                        }

                        digitalBrowseMarketplaceCacheSource.saveCache(digitalBrowseMarketplaceData);

                        return Observable.just(marketplaceViewModelMapper.transform(digitalBrowseMarketplaceData));
                    }
                });
    }

    public Observable<DigitalBrowseMarketplaceViewModel> getMarketplaceDataFromCache() {
        return digitalBrowseMarketplaceCacheSource.getCache()
                .flatMap(new Func1<DigitalBrowseMarketplaceData, Observable<DigitalBrowseMarketplaceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseMarketplaceViewModel> call(DigitalBrowseMarketplaceData digitalBrowseMarketplaceData) {
                        if (digitalBrowseMarketplaceData != null) {
                            return Observable.just(marketplaceViewModelMapper.transform(
                                    digitalBrowseMarketplaceData));
                        } else {
                            return Observable.just(null);
                        }
                    }
                });
    }
}
