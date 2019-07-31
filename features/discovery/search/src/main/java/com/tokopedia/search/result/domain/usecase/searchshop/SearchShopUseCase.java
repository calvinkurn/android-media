package com.tokopedia.search.result.domain.usecase.searchshop;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

final class SearchShopUseCase extends UseCase<SearchShopModel> {

    private Repository<SearchShopModel> searchShopModelRepository;
    private Repository<FavoriteShopListModel> favoriteShopListModelRepository;

    SearchShopUseCase(Repository<SearchShopModel> searchShopModelRepository,
                             Repository<FavoriteShopListModel> favoriteShopListModelRepository) {
        this.searchShopModelRepository = searchShopModelRepository;
        this.favoriteShopListModelRepository = favoriteShopListModelRepository;
    }

    @Override
    public Observable<SearchShopModel> createObservable(RequestParams requestParams) {
        return searchShopModelRepository
                .query(requestParams.getParameters())
                .flatMap(getFavoriteData(requestParams.getString(SearchApiConst.USER_ID, "")));
    }

    private Func1<SearchShopModel, Observable<SearchShopModel>> getFavoriteData(final String userId) {
        return searchShopModel -> {
            Observable<SearchShopModel> searchShopModelObservable = Observable.just(searchShopModel);

            if(textIsEmpty(userId) || searchShopModel.shopItemList.isEmpty()) {
                return searchShopModelObservable;
            }

            return Observable.zip(
                    searchShopModelObservable,
                    favoriteShopListModelRepository.query(getFavoriteShopListParameters(userId, searchShopModel)),
                    zipFunctionFavoriteShop()
            );
        };
    }

    private boolean textIsEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private Map<String, Object> getFavoriteShopListParameters(String userId, SearchShopModel searchShopModel) {
        Map<String, Object> favoriteShopListParameters = new HashMap<>();

        favoriteShopListParameters.put(SearchApiConst.USER_ID, userId);
        favoriteShopListParameters.put(SearchApiConst.SHOP_ID, getShopIdListAsString(searchShopModel));

        return favoriteShopListParameters;
    }

    private String getShopIdListAsString(SearchShopModel searchShopModel) {
        List<String> shopIdList = new ArrayList<>();

        for(SearchShopModel.ShopItem shopItem : searchShopModel.shopItemList) {
            shopIdList.add(shopItem.shopId);
        }

        return StringUtils.join(shopIdList, ",");
    }

    private Func2<SearchShopModel, FavoriteShopListModel, SearchShopModel> zipFunctionFavoriteShop() {
        return (searchShopModel, favoriteShopListModel) -> {
            enrichWithFavoriteData(searchShopModel, favoriteShopListModel);

            return searchShopModel;
        };
    }

    private void enrichWithFavoriteData(SearchShopModel shopViewModel,
                                        FavoriteShopListModel favoriteCheckResult) {
        if(shopViewModel == null || favoriteCheckResult == null) return;

        List<String> favoritedIdList = favoriteCheckResult.favoriteShopList;

        for (SearchShopModel.ShopItem shopItem : shopViewModel.shopItemList) {
            shopItem.isFavorited = favoritedIdList.contains(shopItem.shopId);
        }
    }
}
