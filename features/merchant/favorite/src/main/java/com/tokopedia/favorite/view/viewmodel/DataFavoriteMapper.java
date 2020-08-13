package com.tokopedia.favorite.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.TopAdsShop;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author madi on 4/6/17.
 */

public class DataFavoriteMapper {

    @Inject
    public DataFavoriteMapper() {
    }

    public FavoriteShopViewModel prepareDataFavoriteShop(FavoriteShopItem favoriteShop) {
        FavoriteShopViewModel favoriteShopViewModel = new FavoriteShopViewModel();
        favoriteShopViewModel.setShopId(favoriteShop.getId());
        favoriteShopViewModel.setShopName(favoriteShop.getName());
        favoriteShopViewModel.setShopAvatarImageUrl(favoriteShop.getIconUri());
        favoriteShopViewModel.setShopLocation(favoriteShop.getLocation());
        favoriteShopViewModel.setFavoriteShop(favoriteShop.isFav());
        favoriteShopViewModel.setBadgeUrl(favoriteShop.getBadgeUrl());
        return favoriteShopViewModel;
    }

    public TopAdsShopViewModel prepareDataTopAdsShop(TopAdsShop adsShop) {
        TopAdsShopViewModel shopViewModel = new TopAdsShopViewModel();
        ArrayList<TopAdsShopItem> shopItems = new ArrayList<>();
        if (adsShop.getTopAdsShopItemList() != null) {
            for (com.tokopedia.favorite.domain.model.TopAdsShopItem item
                    : adsShop.getTopAdsShopItemList()) {
                TopAdsShopItem shopItem = new TopAdsShopItem();
                shopItem.setShopId(item.getShopId());
                shopItem.setShopDomain(item.getShopDomain());
                shopItem.setShopName(item.getShopName());
                shopItem.setAdKey(item.getAdRefKey());
                shopItem.setShopClickUrl(item.getShopClickUrl());
                shopItem.setShopCoverUrl(item.getShopImageCover());
                shopItem.setShopCoverEcs(item.getShopImageCoverEcs());
                shopItem.setShopImageUrl(item.getShopImageUrl());
                shopItem.setShopImageEcs(item.getShopImageEcs());
                shopItem.setShopLocation(item.getShopLocation());
                shopItem.setFav(item.isSelected());
                shopItems.add(shopItem);
            }
        }
        shopViewModel.setAdsShopItems(shopItems);
        return shopViewModel;
    }

    public List<Visitable<?>> prepareListFavoriteShop(FavoriteShop favoriteShop) {
        List<Visitable<?>> elementList = new ArrayList<>();
        for (FavoriteShopItem favoriteShopItem : favoriteShop.getData()) {
            favoriteShopItem.setIsFav(true);
            elementList.add(prepareDataFavoriteShop(favoriteShopItem));
        }
        return elementList;
    }
}
