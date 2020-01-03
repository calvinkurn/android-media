package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/26/17.
 */

public class FaveShopMapper implements Func1<Response<TokopediaWsV4Response>, FavoriteShopDomain> {
    @Override
    public FavoriteShopDomain call(Response<TokopediaWsV4Response> response) {
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
