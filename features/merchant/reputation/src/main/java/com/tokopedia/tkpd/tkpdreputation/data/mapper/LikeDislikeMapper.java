package com.tokopedia.tkpd.tkpdreputation.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.LikeDislikePojo;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeMapper implements Func1<Response<TkpdResponse>, LikeDislikeDomain> {
    @Override
    public LikeDislikeDomain call(Response<TkpdResponse> response) {
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
