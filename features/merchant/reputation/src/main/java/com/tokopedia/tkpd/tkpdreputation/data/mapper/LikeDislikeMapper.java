package com.tokopedia.tkpd.tkpdreputation.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.LikeDislikePojo;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeMapper implements Func1<Response<TokopediaWsV4Response>, LikeDislikeDomain> {
    @Override
    public LikeDislikeDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() ==
                    null) {
                LikeDislikePojo data = response.body().convertDataObj(
                        LikeDislikePojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private LikeDislikeDomain mappingToDomain(LikeDislikePojo data) {
        return new LikeDislikeDomain(
                data.getTotalLike(),
                data.getTotalDislike(),
                data.getLikeStatus()
        );
    }
}
