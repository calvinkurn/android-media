package com.tokopedia.tkpd.home.favorite.data.mapper;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.ShopItemData;
import com.tokopedia.core.network.entity.home.TopAdsData;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * @author Kulomady on 1/19/17.
 */
public class FavoriteShopMapper implements rx.functions.Func1<Response<String>, FavoriteShop> {
    public static final String IS_SHOP_FAVORITE = "1";
    private final String defaultErrorMessage;
    private final String emptyErrorMessage;
    private final String successMessage;
    private Gson gson;

    public FavoriteShopMapper(Context context, Gson gson) {
        this.gson = gson;
        defaultErrorMessage = context.getString(R.string.msg_network_error);
        emptyErrorMessage = context.getString(R.string.msg_empty_wishlist);
        successMessage = "success get get favorite shop";
    }

    @Override
    public FavoriteShop call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(defaultErrorMessage);
    }

    private FavoriteShop ValidateResponse(String responseBody) {
        TopAdsData favoriteShopsResponse = gson.fromJson(responseBody, TopAdsData.class);
        if (favoriteShopsResponse != null) {
            if (favoriteShopsResponse.getMessageError() != null
                    && favoriteShopsResponse.getMessageError().size() > 0) {
                String errorMessage = favoriteShopsResponse.getMessageError().get(0);
                return invalidResponse(errorMessage);
            } else if (favoriteShopsResponse.getData() != null) {
                return mappingValidResponse(favoriteShopsResponse.getData());
            } else {
                return invalidResponse(defaultErrorMessage);
            }

        } else {
            return invalidResponse(defaultErrorMessage);
        }
    }

    private FavoriteShop mappingValidResponse(ShopItemData shopItemDataResponse) {
        if (shopItemDataResponse.getList() != null
                && shopItemDataResponse.getList().size() > 0) {
            FavoriteShop favoriteShop = new FavoriteShop();
            favoriteShop.setDataIsValid(true);
            favoriteShop.setMessage(successMessage);
            favoriteShop.setData(mappingDataShopItem(shopItemDataResponse.getList()));
            favoriteShop.setPagingModel(shopItemDataResponse.getPagingHandlerModel());
            return favoriteShop;
        } else {
            return invalidResponse(emptyErrorMessage);
        }
    }

    private List<FavoriteShopItem> mappingDataShopItem(List<ShopItem> listShopItemResponse) {
        ArrayList<FavoriteShopItem> favoriteShopItems = new ArrayList<>();
        for (ShopItem shopItem : listShopItemResponse) {
            FavoriteShopItem favoriteShopItem = new FavoriteShopItem();
            favoriteShopItem.setAdKey(shopItem.adKey);
            favoriteShopItem.setAdR(shopItem.adR);
            favoriteShopItem.setCoverUri(shopItem.coverUri);
            favoriteShopItem.setIconUri(shopItem.iconUri);
            favoriteShopItem.setId(shopItem.id);
            favoriteShopItem.setIsFav(IS_SHOP_FAVORITE.equals(shopItem.isFav));
            favoriteShopItem.setLocation(shopItem.location);
            favoriteShopItem.setShopClickUrl(shopItem.shopClickUrl);
            favoriteShopItem.setName(shopItem.name);
            favoriteShopItem.setBadgeUrl(getShopBadgeUrl(shopItem.shopBadge));
            favoriteShopItems.add(favoriteShopItem);
        }
        return favoriteShopItems;
    }

    private String getShopBadgeUrl(List<Badge> shopBadge) {
        if(shopBadge != null
                && shopBadge.size() != 0
                && shopBadge.get(0) != null
                && shopBadge.get(0).getImageUrl() != null
                && !shopBadge.get(0).getImageUrl().isEmpty()) {
            return shopBadge.get(0).getImageUrl();
        }
        return "";
    }


    private FavoriteShop invalidResponse(String defaultErrorMessage) {
        FavoriteShop favoriteShop = new FavoriteShop();
        favoriteShop.setDataIsValid(false);
        favoriteShop.setMessage(defaultErrorMessage);
        return favoriteShop;
    }
}
