package com.tokopedia.topads.dashboard.di;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.topads.dashboard.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.topads.dashboard.data.repository.TopAdsEtalaseListRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsShopService;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsEtalaseCacheDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsEtalaseListRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEtalaseListUseCase;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsEtalaseListPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsEtalaseListPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsGetEtalaseListDI {
    public static TopAdsEtalaseListPresenter createPresenter() {

        TopAdsShopService topAdsShopService = new TopAdsShopService();
        TopAdsShopApi topAdsShopApi = topAdsShopService.getApi();
        TopAdsEtalaseListMapper topAdsEtalaseListMapper = new TopAdsEtalaseListMapper();
        TopAdsEtalaseCacheDataSource topAdsShopCacheDataSource = new TopAdsEtalaseCacheDataSource();

        TopAdsEtalaseFactory topAdsEtalaseFactory = new TopAdsEtalaseFactory(topAdsShopApi,
                topAdsEtalaseListMapper, topAdsShopCacheDataSource);

        TopAdsEtalaseListRepository topAdsEtalaseListRepository = new TopAdsEtalaseListRepositoryImpl(topAdsEtalaseFactory);

        TopAdsEtalaseListUseCase topAdsEtalaseListUseCase = new TopAdsEtalaseListUseCase(topAdsEtalaseListRepository);

        return new TopAdsEtalaseListPresenterImpl(topAdsEtalaseListUseCase);
    }
}
