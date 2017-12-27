package com.tokopedia.core.product.interactor;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.discussion.DataTalk;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.discussion.Talk;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 8/22/17.
 */

class DiscussionMapper implements Func1<Response<TkpdResponse>, LatestTalkViewModel> {

    public DiscussionMapper() {
    }

    @Override
    public LatestTalkViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                DataTalk data =
                        new GsonBuilder().create().fromJson(response.body().getStringData(), DataTalk.class);
                if (!data.getTalkList().isEmpty()) {
                    Talk talk = data.getTalkList().get(0);
                    LatestTalkViewModel domain = new LatestTalkViewModel();
                    domain.setTalkId(talk.getTalkId());
                    domain.setTalkDate(talk.getTalkCreateTimeList().getDateTimeAndroid());
                    domain.setTalkUserID(talk.getTalkUserId());
                    domain.setTalkUsername(talk.getTalkUserName());
                    domain.setTalkUserAvatar(talk.getTalkUserImage());
                    domain.setTalkMessage(talk.getTalkMessage());
                    domain.setTalkCounterComment(talk.getTalkTotalComment());
                    return domain;
                }
            }
        }
        return null;
    }

}