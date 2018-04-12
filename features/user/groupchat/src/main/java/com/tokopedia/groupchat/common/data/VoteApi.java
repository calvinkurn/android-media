package com.tokopedia.groupchat.common.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.groupchat.vote.domain.pojo.SendVotePojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public interface VoteApi {

    @FormUrlEncoded
    @POST(VoteUrl.SEND_VOTE)
    Observable<Response<DataResponse<SendVotePojo>>> sendVote(@Path(VoteUrl.PATH_POLL_ID) String pollId,
                                                              @FieldMap Map<String, Object> keyword);
}
