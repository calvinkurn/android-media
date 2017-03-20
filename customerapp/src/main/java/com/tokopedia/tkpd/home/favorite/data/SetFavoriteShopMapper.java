package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.interactor.ShopItemParam;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by erry on 31/01/17.
 */

public class SetFavoriteShopMapper implements Func1<Response<String>, FavShop> {

    private final String mDefaultErrorMessage;
    private Gson mGson;
    private ShopItemParam shopItem;


    public SetFavoriteShopMapper(Context context, Gson mGson, ShopItemParam shopItem) {
        this.mGson = mGson;
        this.shopItem = shopItem;
        mDefaultErrorMessage = context.getString(R.string.msg_network_error);
    }

    @Override
    public FavShop call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(mDefaultErrorMessage);
    }

    private FavShop invalidResponse(String mDefaultErrorMessage) {
        FavShop favShop = new FavShop();
        favShop.setmIsValid(false);
        favShop.setmMessage(mDefaultErrorMessage);
        return favShop;
    }

    private FavShop ValidateResponse(String body) {
        FavoriteSendData data = mGson.fromJson(body, FavoriteSendData.class);
        if (data != null && data.getResult() != null) {
            return mappingFavoriteShop(data.getResult());
        } else {
            return invalidResponse(mDefaultErrorMessage);
        }
    }

    private FavShop mappingFavoriteShop(FavoriteSendData.Result result) {
        FavoriteShopItem item = new FavoriteShopItem();
        item.setIsFav(result.getIsSuccess() == 1);
        item.setName(shopItem.getShopName());
        item.setShopClickUrl(shopItem.getShopClickUrl());
        item.setLocation(shopItem.getShopLocation());
        item.setId(shopItem.getShopId());
        item.setAdKey(shopItem.getAdKey());
        item.setCoverUri(shopItem.getShopCoverUrl());
        item.setIconUri(shopItem.getShopImageUrl());

        FavShop favShop = new FavShop();
        favShop.setmIsValid(true);
        favShop.setFavoriteShopItem(item);
        return favShop;
    }
}
