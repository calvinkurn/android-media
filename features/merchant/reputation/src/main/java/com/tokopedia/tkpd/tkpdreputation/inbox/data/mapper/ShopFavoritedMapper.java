package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo.FavoriteCheckResult;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/26/17.
 */

public class ShopFavoritedMapper implements Func1<Response<FavoriteCheckResult>, CheckShopFavoriteDomain> {

    @Override
    public CheckShopFavoriteDomain call(Response<FavoriteCheckResult> response) {
        if (response.isSuccessful()) {
            if (response.body().getShopIds() != null) {
                return mappingToDomain(!response.body().getShopIds().isEmpty());
            } else {
                throw new ErrorMessageException("");
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private CheckShopFavoriteDomain mappingToDomain(boolean isFavorited) {
        return new CheckShopFavoriteDomain(isFavorited);
    }
}
