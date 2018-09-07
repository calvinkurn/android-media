package com.tokopedia.browse.homepage.domain.usecase;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.data.source.cloud.DigitalBrowseMarketplaceDataCloudSource;
import com.tokopedia.browse.homepage.domain.mapper.ServiceViewModelMapper;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseServiceUseCase extends GraphqlUseCase {

    private DigitalBrowseMarketplaceDataCloudSource digitalBrowseMarketplaceDataCloudSource;
    private ServiceViewModelMapper serviceViewModelMapper;

    @Inject
    public DigitalBrowseServiceUseCase(DigitalBrowseMarketplaceDataCloudSource digitalBrowseMarketplaceDataCloudSource,
                                       ServiceViewModelMapper serviceViewModelMapper) {
        this.digitalBrowseMarketplaceDataCloudSource = digitalBrowseMarketplaceDataCloudSource;
        this.serviceViewModelMapper = serviceViewModelMapper;
    }

    public Observable<DigitalBrowseServiceViewModel> createObservable(String query) {
        return digitalBrowseMarketplaceDataCloudSource.getData(new HashMap<>())
                .flatMap(new Func1<DigitalBrowseMarketplaceData, Observable<DigitalBrowseServiceViewModel>>() {
                    @Override
                    public Observable<DigitalBrowseServiceViewModel> call(DigitalBrowseMarketplaceData digitalBrowseMarketplaceData) {
                        return Observable.just(serviceViewModelMapper.transform(digitalBrowseMarketplaceData));
                    }
                });
    }
}
