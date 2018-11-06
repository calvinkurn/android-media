package com.tokopedia.browse.homepage.domain.usecase;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.data.source.cache.DigitalBrowseServiceCacheSource;
import com.tokopedia.browse.homepage.domain.mapper.ServiceViewModelMapper;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
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

public class DigitalBrowseServiceUseCase extends GraphqlUseCase {

    private static final String PARAM_TYPE = "type";

    private static final int VALUE_TYPE = 2;

    private static final String CATEGORY_OPERTAION_NAME = "dynamicHomeIcon";

    private DigitalBrowseServiceCacheSource digitalBrowseServiceCacheSource;
    private ServiceViewModelMapper serviceViewModelMapper;

    @Inject
    public DigitalBrowseServiceUseCase(DigitalBrowseServiceCacheSource digitalBrowseServiceCacheSource,
                                       ServiceViewModelMapper serviceViewModelMapper) {
        this.digitalBrowseServiceCacheSource = digitalBrowseServiceCacheSource;
        this.serviceViewModelMapper = serviceViewModelMapper;
    }

    public Observable<DigitalBrowseServiceViewModel> createObservable(String queryCategory) {

        Map<String, Object> variablesCategory = new HashMap<>();
        variablesCategory.put(PARAM_TYPE, VALUE_TYPE);

        clearRequest();

        addRequest(new GraphqlRequest(queryCategory,
                DigitalBrowseMarketplaceData.class, variablesCategory, CATEGORY_OPERTAION_NAME));

        return getExecuteObservable(RequestParams.EMPTY)
                .flatMap(new Func1<GraphqlResponse, Observable<DigitalBrowseServiceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseServiceViewModel> call(GraphqlResponse graphqlResponse) {
                        DigitalBrowseMarketplaceData digitalBrowseMarketplaceData = new DigitalBrowseMarketplaceData();

                        if (graphqlResponse != null) {
                            digitalBrowseMarketplaceData = graphqlResponse.getData(DigitalBrowseMarketplaceData.class);
                        }

                        digitalBrowseServiceCacheSource.saveCache(digitalBrowseMarketplaceData);

                        return Observable.just(serviceViewModelMapper.transform(digitalBrowseMarketplaceData));
                    }
                });
    }

    public Observable<DigitalBrowseServiceViewModel> getCategoryDataFromCache() {
        return digitalBrowseServiceCacheSource.getCache()
                .flatMap(new Func1<DigitalBrowseMarketplaceData, Observable<DigitalBrowseServiceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseServiceViewModel> call(DigitalBrowseMarketplaceData digitalBrowseMarketplaceData) {
                        if (digitalBrowseMarketplaceData != null) {
                            return Observable.just(serviceViewModelMapper.transform(
                                    digitalBrowseMarketplaceData));
                        } else {
                            return Observable.just(null);
                        }
                    }
                });
    }
}