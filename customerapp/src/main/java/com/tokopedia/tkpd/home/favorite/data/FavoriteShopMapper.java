package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.ShopItemData;
import com.tokopedia.core.network.entity.home.TopAdsData;
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
    private final String mDefaultErrorMessage;
    private final String mEmptyErrorMessage;
    private final String mSuccessMessage;
    private Gson mGson;

    public FavoriteShopMapper(Context context, Gson gson) {
        mGson = gson;
        mDefaultErrorMessage = context.getString(R.string.msg_network_error);
        mEmptyErrorMessage = context.getString(R.string.msg_empty_wishlist);
        mSuccessMessage = "success get get favorite shop";
    }

    @Override
    public FavoriteShop call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(mDefaultErrorMessage);
    }

    private FavoriteShop ValidateResponse(String responseBody) {
        TopAdsData favoriteShopsResponse = mGson.fromJson(responseBody, TopAdsData.class);
        if (favoriteShopsResponse != null) {
            if (favoriteShopsResponse.getMessageError() != null
                    && favoriteShopsResponse.getMessageError().size() > 0) {
                String errorMessage = favoriteShopsResponse.getMessageError().get(0);
                return invalidResponse(errorMessage);
            } else if (favoriteShopsResponse.getData() != null) {
                return mappingValidResponse(favoriteShopsResponse.getData());
            } else {
                return invalidResponse(mDefaultErrorMessage);
            }

        } else {
            return invalidResponse(mDefaultErrorMessage);
        }
    }

    private FavoriteShop mappingValidResponse(ShopItemData shopItemDataResponse) {
        if (shopItemDataResponse.getList() != null
                && shopItemDataResponse.getList().size() > 0) {
            FavoriteShop favoriteShop = new FavoriteShop();
            favoriteShop.setDataIsValid(true);
            favoriteShop.setMessage(mSuccessMessage);
            favoriteShop.setData(mappingDataShopItem(shopItemDataResponse.getList()));
            return favoriteShop;
        } else {
            return invalidResponse(mEmptyErrorMessage);
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
            favoriteShopItem.setIsFav(shopItem.isFav);
            favoriteShopItem.setLocation(shopItem.location);
            favoriteShopItem.setShopClickUrl(shopItem.shopClickUrl);
            favoriteShopItem.setName(shopItem.name);
            favoriteShopItems.add(favoriteShopItem);
        }
        return favoriteShopItems;
    }


    private FavoriteShop invalidResponse(String defaultErrorMessage) {
        FavoriteShop favoriteShop = new FavoriteShop();
        favoriteShop.setDataIsValid(false);
        favoriteShop.setMessage(defaultErrorMessage);
        return favoriteShop;
    }
}
