package com.tokopedia.tkpd.tkpdreputation.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeListDomain;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.GetLikeDislikePojo;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.LikeDislikeList;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/29/17.
 */

public class GetLikeDislikeMapper implements Func1<Response<TokopediaWsV4Response>,
        GetLikeDislikeReviewDomain> {
    @Override
    public GetLikeDislikeReviewDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() ==
                    null) {
                GetLikeDislikePojo data = response.body().convertDataObj(
                        GetLikeDislikePojo.class);
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
            String messageError = "";
            if (response.body() != null) {
                messageError = response.body().getErrorMessageJoined();
            }
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private GetLikeDislikeReviewDomain mappingToDomain(GetLikeDislikePojo data) {
        return new GetLikeDislikeReviewDomain(mappingToList(data.getList()));
    }

    private List<LikeDislikeListDomain> mappingToList(List<LikeDislikeList> list) {
        List<LikeDislikeListDomain> domainList = new ArrayList<>();
        for (LikeDislikeList pojo : list) {
            domainList.add(new LikeDislikeListDomain(
                    pojo.getReviewId(),
                    pojo.getTotalLike(),
                    pojo.getTotalDislike(),
                    pojo.getLikeStatus()
            ));
        }
        return domainList;
    }
}
