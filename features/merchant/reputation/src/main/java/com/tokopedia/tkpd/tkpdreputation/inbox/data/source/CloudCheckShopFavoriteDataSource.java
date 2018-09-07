package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ShopFavoritedMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class CloudCheckShopFavoriteDataSource {
    private final TomeService tomeService;
    private final ShopFavoritedMapper shopFavoritedMapper;

    public CloudCheckShopFavoriteDataSource(TomeService tomeService, ShopFavoritedMapper shopFavoritedMapper) {
        this.tomeService = tomeService;
        this.shopFavoritedMapper = shopFavoritedMapper;
    }


    public Observable<CheckShopFavoriteDomain> checkShopIsFavorited(RequestParams requestParams) {
        return tomeService.getApi()
                .checkIsShopFavorited(
                        requestParams.getString(CheckShopFavoritedUseCase
                                .PARAM_USER_ID, ""),
                        requestParams.getString(CheckShopFavoritedUseCase.PARAM_SHOP_ID, ""))
                .map(shopFavoritedMapper);
    }
}
