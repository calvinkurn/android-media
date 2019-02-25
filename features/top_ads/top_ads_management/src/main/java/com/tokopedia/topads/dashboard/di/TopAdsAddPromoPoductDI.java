package com.tokopedia.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsManageGroupPromoPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsManageGroupPromoPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsAddPromoPoductDI {
    public static TopAdsManageGroupPromoPresenter createPresenter(Context context) {

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context));
        TopAdsOldManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();
        TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper = new TopAdsDetailGroupDomainMapper();


        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper, topAdsDetailGroupDomainMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);

        TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsSearchGroupAdsNameUseCase(topAdsGroupAdsRepository);
        TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase = new TopAdsCheckExistGroupUseCase(topAdsGroupAdsRepository);
        return new TopAdsManageGroupPromoPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsCheckExistGroupUseCase);
    }
}
