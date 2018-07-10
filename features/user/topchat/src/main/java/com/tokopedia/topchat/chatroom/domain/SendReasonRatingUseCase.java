package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.data.mapper.SendReasonRatingMapper;
import com.tokopedia.topchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SendReasonRatingPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 6/11/18.
 */
public class SendReasonRatingUseCase extends UseCase<SendReasonRatingPojo> {

    private static final String PARAM_REASON = "reason";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_MESSAGE_ID = "msg_id";
    private static final String PARAM_TIMESTAMP = "reply_time";

    private final ChatBotApi chatRatingApi;
    private final SendReasonRatingMapper mapper;

    @Inject
    public SendReasonRatingUseCase(ChatBotApi chatRatingApi,
                                   SendReasonRatingMapper mapper) {
        this.chatRatingApi = chatRatingApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<SendReasonRatingPojo> createObservable(RequestParams requestParams) {
        return chatRatingApi.sendReasonRating(requestParams.getParameters())
                .map(mapper);
    }

    public static RequestParams getParam(int messageId, int userId, String reason, long replyTimeNano) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REASON, reason);
        params.putInt(PARAM_USER_ID, userId);
        params.putInt(PARAM_MESSAGE_ID, messageId);
        params.putLong(PARAM_TIMESTAMP, replyTimeNano);
        return params;
    }
}
