package com.tokopedia.topchat.chatlist.domain.usecase;

import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DeleteMessageListUseCase extends UseCase<DeleteChatListViewModel> {

    MessageRepository messageRepository;

    @Inject
    public DeleteMessageListUseCase(MessageRepository messageRepository) {
        super();
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<DeleteChatListViewModel> createObservable(RequestParams requestParams) {
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

    public static RequestParams generateParam(List<Pair> listMove) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (Pair item : listMove) {
            ChatListViewModel first = (ChatListViewModel) item.first;
            array.add(Integer.valueOf(first.getId()));
        }
        object.add("list_msg_id", array);
        requestParams.putObject("json", object);
        return requestParams;
    }

    public static RequestParams generateParam(String messageId) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(Integer.valueOf(messageId));
        object.add("list_msg_id", array);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
