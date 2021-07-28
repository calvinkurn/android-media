package com.tokopedia.topchat.chatlist.domain.usecase;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DeleteMessageListUseCase extends UseCase<DeleteChatListUiModel> {

    MessageRepository messageRepository;

    @Inject
    public DeleteMessageListUseCase(MessageRepository messageRepository) {
        super();
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<DeleteChatListUiModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return messageRepository.deleteMessage(object);
    }

    public static RequestParams generateParam(int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("per_page", "10");
        requestParams.putString("platform", "android");
        return requestParams;
    }

    public static RequestParams generateParam(String messageId) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(Long.valueOf(messageId));
        object.add("list_msg_id", array);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
