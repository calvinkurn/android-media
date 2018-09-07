package com.tokopedia.browse.homepage.domain.usecase;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryGroupEntity;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseDynamicHomeIcon;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseOfficialStoreBrandsEntity;
import com.tokopedia.browse.homepage.data.source.cache.DigitalBrowseMarketplaceCacheSource;
import com.tokopedia.browse.homepage.data.source.cloud.DigitalBrowseMarketplaceDataCloudSource;
import com.tokopedia.browse.homepage.domain.mapper.MarketplaceViewModelMapper;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseMarketplaceUseCase extends GraphqlUseCase {

    private static final String PARAM_LANG = "lang";
    private static final String PARAM_SOURCE = "source";
    private static final String PARAM_TOTAL = "total";

    private static final String VALUE_LANG = "id";
    private static final String VALUE_SOURCE = "microsite";
    private static final int VALUE_TOTAL = 6;

    private static final String OPERATION_NAME = "officialStoreBrands";

    private DigitalBrowseMarketplaceDataCloudSource digitalBrowseMarketplaceDataCloudSource;
    private DigitalBrowseMarketplaceCacheSource digitalBrowseMarketplaceCacheSource;
    private MarketplaceViewModelMapper marketplaceViewModelMapper;

    @Inject
    public DigitalBrowseMarketplaceUseCase(DigitalBrowseMarketplaceDataCloudSource digitalBrowseMarketplaceDataCloudSource,
                                           MarketplaceViewModelMapper marketplaceViewModelMapper,
                                           DigitalBrowseMarketplaceCacheSource digitalBrowseMarketplaceCacheSource) {
        this.digitalBrowseMarketplaceDataCloudSource = digitalBrowseMarketplaceDataCloudSource;
        this.marketplaceViewModelMapper = marketplaceViewModelMapper;
        this.digitalBrowseMarketplaceCacheSource = digitalBrowseMarketplaceCacheSource;
    }

    public Observable<DigitalBrowseMarketplaceViewModel> createObservable(String query) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(PARAM_LANG, VALUE_LANG);
        variables.put(PARAM_SOURCE, VALUE_SOURCE);
        variables.put(PARAM_TOTAL, VALUE_TOTAL);

        clearRequest();

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                DigitalBrowseOfficialStoreBrandsEntity.class, variables, OPERATION_NAME);

        addRequest(graphqlRequest);

        return digitalBrowseMarketplaceDataCloudSource.getData(new HashMap<>())
                .zipWith(getExecuteObservable(RequestParams.EMPTY),
                        new Func2<DigitalBrowseMarketplaceData, GraphqlResponse, DigitalBrowseMarketplaceViewModel>() {
                            @Override
                            public DigitalBrowseMarketplaceViewModel call(DigitalBrowseMarketplaceData digitalBrowseMarketplaceData, GraphqlResponse graphqlResponse) {
                                if (graphqlResponse != null) {
                                    DigitalBrowseOfficialStoreBrandsEntity officialStoreBrandsEntity = graphqlResponse.getData(DigitalBrowseOfficialStoreBrandsEntity.class);
                                    digitalBrowseMarketplaceData.setPopularBrandDatas(officialStoreBrandsEntity.getOfficialStoreBrandList().get(0).getData());
                                }

                                digitalBrowseMarketplaceCacheSource.saveCache(digitalBrowseMarketplaceData);

                                return marketplaceViewModelMapper.transform(digitalBrowseMarketplaceData);
                            }
                        });
    }

    public Observable<DigitalBrowseMarketplaceViewModel> getMarketplaceDataFromCache() {
        return digitalBrowseMarketplaceCacheSource.getCache()
                .flatMap(new Func1<DigitalBrowseMarketplaceData, Observable<DigitalBrowseMarketplaceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseMarketplaceViewModel> call(DigitalBrowseMarketplaceData digitalBrowseMarketplaceData) {
                        if (digitalBrowseMarketplaceData != null) {
                            return Observable.just(marketplaceViewModelMapper.transform(digitalBrowseMarketplaceData));
                        } else {
                            DigitalBrowseCategoryGroupEntity categoryGroupEntity = new DigitalBrowseCategoryGroupEntity();
                            categoryGroupEntity.setCategoryRow(new ArrayList<>());

                            List<DigitalBrowseCategoryGroupEntity> list = new ArrayList<>();
                            list.add(categoryGroupEntity);

                            DigitalBrowseDynamicHomeIcon digitalBrowseDynamicHomeIcon = new DigitalBrowseDynamicHomeIcon();
                            digitalBrowseDynamicHomeIcon.setDynamicHomeCategoryGroupEntities(list);

                            DigitalBrowseMarketplaceData data = new DigitalBrowseMarketplaceData();
                            data.setPopularBrandDatas(new ArrayList<>());
                            data.setCategoryGroups(digitalBrowseDynamicHomeIcon);

                            return Observable.just(marketplaceViewModelMapper.transform(data));
                        }
                    }
                });
    }
}
