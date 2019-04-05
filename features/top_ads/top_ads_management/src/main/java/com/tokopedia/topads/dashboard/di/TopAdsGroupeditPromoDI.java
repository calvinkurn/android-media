package com.tokopedia.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.common.util.TopAdsSourceTaggingUseCaseUtil;
import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsShopAdFactory;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailShopMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditProductGroupToNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMoveProductGroupToExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGroupEditPromoPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGroupEditPromoPresenterImpl;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public class TopAdsGroupeditPromoDI {

    public static TopAdsGroupEditPromoPresenter createPresenter(Context context) {

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context));
        TopAdsOldManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();
        TopAdsDetailShopMapper topAdsDetailShopMapper = new TopAdsDetailShopMapper();
        TopAdsBulkActionMapper topAdsBulkActionMapper = new TopAdsBulkActionMapper();
        TopAdsDetailProductMapper topAdsDetailProductMapper = new TopAdsDetailProductMapper();
        TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper = new TopAdsDetailGroupDomainMapper();

        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper, topAdsDetailGroupDomainMapper);
        TopAdsShopAdFactory topAdsShopAdFactory = new TopAdsShopAdFactory(context, topAdsManagementApi, topAdsDetailShopMapper);
        TopAdsProductAdFactory topAdsProductAdFactory = new TopAdsProductAdFactory(context, topAdsManagementApi, topAdsDetailProductMapper, topAdsBulkActionMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
        TopAdsShopAdsRepository topAdsShopAdsRepository = new TopAdsShopAdsRepositoryImpl(topAdsShopAdFactory);
        TopAdsProductAdsRepository topAdsProductAdsRepository = new TopAdsProductAdsRepositoryImpl(topAdsProductAdFactory);

        TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsSearchGroupAdsNameUseCase(topAdsGroupAdsRepository);
        TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase = new TopAdsCheckExistGroupUseCase(topAdsGroupAdsRepository);
        TopAdsEditProductGroupToNewGroupUseCase topAdsEditProductGroupToNewGroupUseCase =
                new TopAdsEditProductGroupToNewGroupUseCase(topAdsShopAdsRepository,topAdsGroupAdsRepository);
        TopAdsMoveProductGroupToExistGroupUseCase topAdsMoveProductGroupToExistGroupUseCase =
                new TopAdsMoveProductGroupToExistGroupUseCase(topAdsProductAdsRepository);
        TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase = TopAdsSourceTaggingUseCaseUtil.getTopAdsGetSourceTaggingUseCase(context);

        return new TopAdsGroupEditPromoPresenterImpl(topAdsSearchGroupAdsNameUseCase,
                topAdsCheckExistGroupUseCase, topAdsEditProductGroupToNewGroupUseCase,
                topAdsMoveProductGroupToExistGroupUseCase,  topAdsGetSourceTaggingUseCase);
    }
}
