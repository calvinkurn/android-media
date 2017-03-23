package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShopItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * @author Kulomady on 1/19/17.
 */
public class TopAdsShopMapper implements rx.functions.Func1<Response<String>, TopAdsShop> {
    private final String defaultErrorMessage;
    private final String emptyErrorMessage;
    private final String successMessage;
    private Gson gson;

    public TopAdsShopMapper(Context context, Gson gson) {
        this.gson = gson;
        defaultErrorMessage = context.getString(R.string.msg_network_error);
        emptyErrorMessage = context.getString(R.string.msg_empty_wishlist);
        successMessage = "success get get topads shop";
    }

    @Override
    public TopAdsShop call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(defaultErrorMessage);
    }

    private TopAdsShop ValidateResponse(String responseBody) {
        TopAdsHome topAdsResponse = gson.fromJson(responseBody, TopAdsHome.class);
        if (topAdsResponse != null) {
            if (topAdsResponse.getData() != null) {
                return mappingValidResponse(topAdsResponse.getData());
            } else {
                return invalidResponse(defaultErrorMessage);
            }

        } else {
            return invalidResponse(defaultErrorMessage);
        }
    }

    private TopAdsShop mappingValidResponse(TopAdsHome.Data[] topAdsDataResponse) {
        if (topAdsDataResponse != null && topAdsDataResponse.length > 0) {
            TopAdsShop favoriteShop = new TopAdsShop();
            favoriteShop.setDataValid(true);
            favoriteShop.setMessage(successMessage);
            favoriteShop.setTopAdsShopItemList(mappingDataShopItem(topAdsDataResponse));
            return favoriteShop;
        } else {
            return invalidResponse(emptyErrorMessage);
        }
    }

    private List<TopAdsShopItem> mappingDataShopItem(TopAdsHome.Data[] listShopItemResponse) {
        ArrayList<TopAdsShopItem> topAdsShopItems = new ArrayList<>();
        for (TopAdsHome.Data dataResponse : listShopItemResponse) {
            TopAdsShopItem topAdsShopItem = new TopAdsShopItem();
            topAdsShopItem.setAdRefKey(dataResponse.adRefKey);
            topAdsShopItem.setRedirect(dataResponse.redirect);
            topAdsShopItem.setId(dataResponse.id);
            topAdsShopItem.setShopClickUrl(dataResponse.shopClickUrl);

            TopAdsHome.Shop shopResponse = dataResponse.shop;
            mappingShopResponse(topAdsShopItem, dataResponse.shop);

            TopAdsHome.ImageShop imageShopResponse = shopResponse.imageShop;
            topAdsShopItem.setShopImageCover(imageShopResponse.cover);
            topAdsShopItem.setShopImageUrl(imageShopResponse.sUrl);

            topAdsShopItems.add(topAdsShopItem);
        }

        return topAdsShopItems;
    }

    private void mappingShopResponse(TopAdsShopItem topAdsShopItem, TopAdsHome.Shop shopResponse) {
        topAdsShopItem.setLuckyShop(shopResponse.luckyShop);
        topAdsShopItem.setShopId(shopResponse.id);
//        topAdsShopItem.setShopDomain(shopResponse.domain);
        topAdsShopItem.setGoldShop(shopResponse.goldShop);
        topAdsShopItem.setShopLocation(shopResponse.location);
        topAdsShopItem.setShopName(shopResponse.name);
        topAdsShopItem.setShopUri(shopResponse.uri);
    }

    private TopAdsShop invalidResponse(String defaultErrorMessage) {
        TopAdsShop topAdsShop = new TopAdsShop();
        topAdsShop.setDataValid(false);
        topAdsShop.setMessage(defaultErrorMessage);
        return topAdsShop;
    }
}
