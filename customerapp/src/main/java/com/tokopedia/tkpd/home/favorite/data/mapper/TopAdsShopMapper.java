package com.tokopedia.tkpd.home.favorite.data.mapper;

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
    private Gson gson;

    public TopAdsShopMapper(Context context, Gson gson) {
        this.gson = gson;
        defaultErrorMessage = context.getString(R.string.msg_network_error);
        emptyErrorMessage = context.getString(R.string.msg_empty_wishlist);
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

    private TopAdsShop mappingValidResponse(List<TopAdsHome.Data> topAdsDataResponse) {
        if (topAdsDataResponse != null && topAdsDataResponse.size() > 0) {
            TopAdsShop favoriteShop = new TopAdsShop();
            favoriteShop.setDataValid(true);
            favoriteShop.setTopAdsShopItemList(mappingDataShopItem(topAdsDataResponse));
            return favoriteShop;
        } else {
            return invalidResponse(emptyErrorMessage);
        }
    }

    private List<TopAdsShopItem> mappingDataShopItem(List<TopAdsHome.Data> listShopItemResponse) {
        ArrayList<TopAdsShopItem> topAdsShopItems = new ArrayList<>();
        for (TopAdsHome.Data dataResponse : listShopItemResponse) {
            TopAdsShopItem topAdsShopItem = new TopAdsShopItem();
            topAdsShopItem.setAdRefKey(dataResponse.getAdRefKey());
            topAdsShopItem.setRedirect(dataResponse.getRedirect());
            topAdsShopItem.setId(dataResponse.getId());
            topAdsShopItem.setShopClickUrl(dataResponse.getAdClickUrl());

            mappingShopResponse(topAdsShopItem, dataResponse.getHeadline().getShop());

            topAdsShopItem.setShopImageCover(dataResponse.getHeadline().getShop().getImageShop().getCover());
            topAdsShopItem.setShopImageCoverEcs(dataResponse.getHeadline().getShop().getImageShop().getCoverEcs());
            topAdsShopItem.setShopImageUrl(dataResponse.getHeadline().getImage().getFullUrl());
            topAdsShopItem.setShopImageEcs(dataResponse.getHeadline().getShop().getImageShop().getSEcs());
            topAdsShopItems.add(topAdsShopItem);
        }

        return topAdsShopItems;
    }

    private void mappingShopResponse(TopAdsShopItem topAdsShopItem, TopAdsHome.Shop shopResponse) {
        topAdsShopItem.setShopId(shopResponse.getId());
        topAdsShopItem.setShopLocation(shopResponse.getLocation());
        topAdsShopItem.setShopName(shopResponse.getName());
    }

    private TopAdsShop invalidResponse(String defaultErrorMessage) {
        TopAdsShop topAdsShop = new TopAdsShop();
        topAdsShop.setDataValid(false);
        topAdsShop.setMessage(defaultErrorMessage);
        return topAdsShop;
    }
}
