package com.tokopedia.tkpd.home.favorite.data.mapper;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by erry on 31/01/17.
 */

public class AddFavoriteShopMapper implements Func1<Response<String>, FavShop> {

    private final String defaultErrorMessage;
    private Gson gson;


    public AddFavoriteShopMapper(Context context, Gson gson) {
        this.gson = gson;
        defaultErrorMessage = context.getString(R.string.msg_network_error);
    }

    @Override
    public FavShop call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(defaultErrorMessage);
    }

    private FavShop invalidResponse(String mDefaultErrorMessage) {
        FavShop favShop = new FavShop();
        favShop.setValid(false);
        favShop.setMessage(mDefaultErrorMessage);
        return favShop;
    }

    private FavShop ValidateResponse(String body) {
        FavoriteSendData data = gson.fromJson(body, FavoriteSendData.class);
        if(data!=null && data.getResult() !=null) {
            return mappingFavoriteShop(data.getResult());
        } else {
            return invalidResponse(defaultErrorMessage);
        }
    }

    private FavShop mappingFavoriteShop(FavoriteSendData.Result result){
        FavoriteShopItem item = new FavoriteShopItem();
        item.setIsFav(result.getIsSuccess() == 1);
        FavShop favShop = new FavShop();
        favShop.setValid(true);
        favShop.setFavoriteShopItem(item);
        return favShop;
    }
}
