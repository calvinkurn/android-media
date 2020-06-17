package com.tokopedia.shop_score.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.shop_score.data.factory.ShopScoreFactory;
import com.tokopedia.shop_score.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.shop_score.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.shop_score.data.source.cloud.ShopScoreCloud;
import com.tokopedia.shop_score.data.source.disk.ShopScoreCache;
import com.tokopedia.shop_score.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.shop_score.view.presenter.ShopScoreDetailPresenterImpl;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDependencyInjector {
    public static ShopScoreDetailPresenterImpl getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        GoldMerchantService service = new GoldMerchantService();
        GoldMerchantApi api = service.getApi();
        SessionHandler sessionHandler = new SessionHandler(context);
        ShopScoreCloud shopScoreCloud = new ShopScoreCloud(api, sessionHandler);
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        ShopScoreCache shopScoreCache = new ShopScoreCache(globalCacheManager);
        ShopScoreDetailMapper shopScoreDetailMapper = new ShopScoreDetailMapper(context);
        ShopScoreFactory shopScoreFactory =
                new ShopScoreFactory(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
        ShopScoreRepositoryImpl shopScoreRepository = new ShopScoreRepositoryImpl(shopScoreFactory);
        GetShopScoreDetailUseCase getShopScoreDetailUseCase =
                new GetShopScoreDetailUseCase(
                        threadExecutor, postExecutionThread, shopScoreRepository
                );
        return new ShopScoreDetailPresenterImpl(getShopScoreDetailUseCase);
    }
}
