package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.data.repository.ReplyRepository;
import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;


import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 11/06/18.
 */
public class GetExistingChatUseCase extends UseCase<ExistingChatPojo> {

    private final ReplyRepository replyRepository;

    @Inject
    public GetExistingChatUseCase( ReplyRepository replyRepository) {
        super();
        this.replyRepository = replyRepository;
    }

    @Override
    public Observable<ExistingChatPojo> createObservable(RequestParams requestParams) {
        return replyRepository.getExistingChat(requestParams.getParameters());
    }

    public static RequestParams generateParam(boolean isUserAskShop, String
            destinationId, String source)
    {
        String destinationShopIdKey = "to_shop_id";
        String destinationUserIdKey = "to_user_id";
        String sourceKey = "source";
        RequestParams requestParams = RequestParams.create();
        if(isUserAskShop) requestParams.putString(destinationShopIdKey,destinationId);
        else requestParams.putString(destinationUserIdKey,destinationId);

        requestParams.putString(sourceKey,source);
        return requestParams;
    }
}
