package com.tokopedia.core.product.interactor;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.discussion.DataCommentTalk;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.discussion.TalkComment;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 8/22/17.
 */

public class DiscussionCommentMapper implements Func1<Response<TkpdResponse>, LatestTalkViewModel> {

    public DiscussionCommentMapper() {
    }

    @Override
    public LatestTalkViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                DataCommentTalk dataCommentTalk =
                        new GsonBuilder().create().fromJson(response.body().getStringData(), DataCommentTalk.class);
                LatestTalkViewModel latestTalkViewModel = new LatestTalkViewModel();
                TalkComment talkComment = dataCommentTalk.getTalkDetail().get(0);

                latestTalkViewModel.setCommentId(talkComment.getCommentId());
                latestTalkViewModel.setCommentId(talkComment.getCommentId());
                latestTalkViewModel.setCommentMessage(talkComment.getCommentMessage());
                latestTalkViewModel.setCommentDate(talkComment.getCommentCreateTimeList().getDateTimeAndroid());
                latestTalkViewModel.setCommentUserId(talkComment.getCommentUserId());
                latestTalkViewModel.setCommentUserName(
                        talkComment.getCommentIsSeller() == 1 ? talkComment.getCommentShopName() : talkComment.getCommentUserName()
                );
                latestTalkViewModel.setCommentUserLabel(talkComment.getCommentUserLabel());
                latestTalkViewModel.setCommentUserAvatar(
                        talkComment.getCommentIsSeller() == 1 ? talkComment.getCommentShopImage() : talkComment.getCommentUserImage()
                );

                return latestTalkViewModel;
            }
        }
        return null;
    }
}
