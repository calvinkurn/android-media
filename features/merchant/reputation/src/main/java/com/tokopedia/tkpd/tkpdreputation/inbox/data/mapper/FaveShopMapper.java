package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/26/17.
 */

public class FaveShopMapper implements Func1<Response<TkpdResponse>, FavoriteShopDomain> {
    @Override
    public FavoriteShopDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                try {
                    int status = response.body().getJsonData().getInt("is_success");
                    return convertToDomain(status);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ErrorMessageException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            } else {
                throw new ErrorMessageException((response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty())
                        ? response.body().getErrorMessages().get(0)
                        : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        } else {
            throw new ErrorMessageException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }


    private FavoriteShopDomain convertToDomain(int status) {
        return new FavoriteShopDomain(status);
    }
}
