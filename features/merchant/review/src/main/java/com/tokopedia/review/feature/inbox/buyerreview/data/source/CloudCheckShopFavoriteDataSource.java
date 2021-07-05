package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService;
import com.tokopedia.usecase.RequestParams;

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
