package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.home.favorite.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListBadge;

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

    public WishlistViewModel prepareDataWishlist(DomainWishlist wishListData) {
        WishlistViewModel wishlistViewModel = new WishlistViewModel();
        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
        if (wishListData != null && wishListData.getData() != null) {
            for (DataWishlist dataWishlist : wishListData.getData()) {
                WishlistItem wishlistItem = new WishlistItem();
                wishlistItem.setName(dataWishlist.getName());
                wishlistItem.setPrice(dataWishlist.getPrice());
                wishlistItem.setShopName(dataWishlist.getShop_name());
                wishlistItem.setProductId(dataWishlist.getId());
                wishlistItem.setProductImage(dataWishlist.getProductImageUrl());
                ArrayList<String> badges = new ArrayList<>();
                if (dataWishlist.getBadges() !=null) {
                    for (WishListBadge badge : dataWishlist.getBadges()) {
                        badges.add(badge.getImgUrl());
                    }
                }
                wishlistItem.setBadgeImageUrl(badges);
                wishlistItems.add(wishlistItem);
            }
        }
        wishlistViewModel.setWishlistItems(wishlistItems);
        return wishlistViewModel;
    }

    public TopAdsShopViewModel prepareDataTopAdsShop(TopAdsShop adsShop) {
        TopAdsShopViewModel shopViewModel = new TopAdsShopViewModel();
        ArrayList<TopAdsShopItem> shopItems = new ArrayList<>();
        if (adsShop.getTopAdsShopItemList() != null) {
            for (com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShopItem item
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
                shopItem.setShopLocation(item.getShopLocation());
                shopItem.setFav(item.isSelected());
                shopItems.add(shopItem);
            }
        }
        shopViewModel.setAdsShopItems(shopItems);
        return shopViewModel;
    }

    public List<Visitable> prepareListFavoriteShop(FavoriteShop favoriteShop) {
        List<Visitable> elementList = new ArrayList<>();
        for (FavoriteShopItem favoriteShopItem : favoriteShop.getData()) {
            favoriteShopItem.setIsFav(true);
            elementList.add(prepareDataFavoriteShop(favoriteShopItem));
        }
        return elementList;
    }
}
