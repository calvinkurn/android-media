package com.tokopedia.topchat.chatlist.domain.usecase;

import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;

import java.util.List;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DeleteMessageListUseCase extends UseCase<DeleteChatListViewModel>{

    MessageRepository messageRepository;

    public DeleteMessageListUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<DeleteChatListViewModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return messageRepository.deleteMessage(object);
    }

    public static RequestParams generateParam(int page)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("per_page", "10");
        requestParams.putString("platform","android");
        return requestParams;
    }

    public static RequestParams generateParam(List<Pair> listMove) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for(Pair item : listMove){
            ChatListViewModel first = (ChatListViewModel) item.first;
            array.add(Integer.valueOf(first.getId()));
        }
        object.add("list_msg_id", array);
        requestParams.putObject("json", object);
        return requestParams;
    }

    public static RequestParams generateParam(String messageId){
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(Integer.valueOf(messageId));
        object.add("list_msg_id",array);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
