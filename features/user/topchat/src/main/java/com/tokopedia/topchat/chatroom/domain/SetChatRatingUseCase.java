package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.data.mapper.SetChatRatingMapper;
import com.tokopedia.topchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SetChatRatingPojo;
import com.tokopedia.topchat.chatroom.data.mapper.SetChatRatingMapper;
import com.tokopedia.topchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 26/03/18.
 */

public class SetChatRatingUseCase extends UseCase<SetChatRatingPojo>{

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_MESSAGE_ID = "msg_id";
    public static final String PARAM_TIMESTAMP = "reply_time";
    public static final String PARAM_RATING = "rate_status";

    private final ChatBotApi chatRatingApi;
    private final SetChatRatingMapper setChatRatingMapper;

    @Inject
    public SetChatRatingUseCase(ChatBotApi chatRatingApi,
                                SetChatRatingMapper setChatRatingMapper){
        this.chatRatingApi = chatRatingApi;
        this.setChatRatingMapper = setChatRatingMapper;
    }

    @Override
    public Observable<SetChatRatingPojo> createObservable(RequestParams requestParams) {
        return chatRatingApi.setChatRating(requestParams.getParameters()).map(setChatRatingMapper);
    }

    public static RequestParams getParams(int messageId,
                                          int userId,
                                          long timeStamp,
                                          int rating){
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_MESSAGE_ID, messageId);
        param.putInt(PARAM_USER_ID, userId);
        param.putLong(PARAM_TIMESTAMP, timeStamp);
        param.putInt(PARAM_RATING, rating);
        return param;
    }
}
