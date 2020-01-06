package com.tokopedia.tkpd.home.favorite.data.mapper;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.network.entity.home.FavShopItemData;
import com.tokopedia.core.network.entity.home.FavoritShopResponseData;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.FavShopsItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by naveengoyal on 5/7/18.
 */

public class FavoritShopGraphQlMapper implements rx.functions.Func1<Response<GraphqlResponse<FavoritShopResponseData>>, FavoriteShop> {
    public static final String IS_SHOP_FAVORITE = "1";
    private final String defaultErrorMessage;
    private final String emptyErrorMessage;
    private final String successMessage;
    private Gson gson;

    public FavoritShopGraphQlMapper(Context context, Gson gson) {
        this.gson = gson;
        defaultErrorMessage = context.getString(R.string.msg_network_error);
        emptyErrorMessage = context.getString(R.string.msg_empty_wishlist);
        successMessage = "success get get favorite shop";
    }

    private FavoriteShop ValidateResponse(FavoritShopResponseData responseBody) {
        if (responseBody != null && responseBody.getData() != null) {
            return mappingValidResponse(responseBody.getData());
            } else {
                return invalidResponse(defaultErrorMessage);
            }
    }

    private FavoriteShop mappingValidResponse(FavShopItemData shopItemDataResponse) {
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

    private List<FavoriteShopItem> mappingDataShopItem(List<FavShopsItem> listShopItemResponse) {
        ArrayList<FavoriteShopItem> favoriteShopItems = new ArrayList<>();
        for (FavShopsItem shopItem : listShopItemResponse) {
            FavoriteShopItem favoriteShopItem = new FavoriteShopItem();
            favoriteShopItem.setIconUri(shopItem.getImage());
            favoriteShopItem.setId(shopItem.getId());
            favoriteShopItem.setLocation(shopItem.getLocation());
            favoriteShopItem.setName(shopItem.getName());
            favoriteShopItem.setBadgeUrl(getShopBadgeUrl(shopItem.getBadge()));
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

    @Override
    public FavoriteShop call(Response<GraphqlResponse<FavoritShopResponseData>> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body().getData());
        }
        return invalidResponse(defaultErrorMessage);
    }
}
