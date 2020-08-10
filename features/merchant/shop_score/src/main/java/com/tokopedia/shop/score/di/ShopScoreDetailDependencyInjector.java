package com.tokopedia.shop.score.di;

import android.content.Context;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.shop.score.data.factory.ShopScoreFactory;
import com.tokopedia.shop.score.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.shop.score.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.shop.score.data.source.cloud.ShopScoreCloud;
import com.tokopedia.shop.score.data.source.disk.ShopScoreCache;
import com.tokopedia.shop.score.domain.GoldMerchantApi;
import com.tokopedia.shop.score.domain.GoldMerchantService;
import com.tokopedia.shop.score.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.shop.score.view.presenter.ShopScoreDetailPresenterImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDependencyInjector {
    public static ShopScoreDetailPresenterImpl getPresenter(Context context) {
        GoldMerchantService service = new GoldMerchantService();
        GoldMerchantApi api = service.getApi();
        UserSessionInterface sessionHandler = new UserSession(context);
        ShopScoreCloud shopScoreCloud = new ShopScoreCloud(api, sessionHandler);
        PersistentCacheManager persistentCacheManager = new PersistentCacheManager(context);
        ShopScoreCache shopScoreCache = new ShopScoreCache(persistentCacheManager);
        ShopScoreDetailMapper shopScoreDetailMapper = new ShopScoreDetailMapper(context, sessionHandler);
        ShopScoreFactory shopScoreFactory = new ShopScoreFactory(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
        ShopScoreRepositoryImpl shopScoreRepository = new ShopScoreRepositoryImpl(shopScoreFactory);
        GetShopScoreDetailUseCase getShopScoreDetailUseCase = new GetShopScoreDetailUseCase(shopScoreRepository);
        return new ShopScoreDetailPresenterImpl(getShopScoreDetailUseCase);
    }
}
