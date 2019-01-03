package com.tokopedia.topchat.chatlist.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.topchat.chatlist.domain.pojo.message.ListMessage;
import com.tokopedia.topchat.chatlist.domain.pojo.message.MessageData;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageMapper implements Func1<Response<TokopediaWsV4Response>, InboxChatViewModel> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Inject
    public GetMessageMapper() {
    }

    @Override
    public InboxChatViewModel call(Response<TokopediaWsV4Response> response) {
        //TODO MAKE ERROR INTERCEPTOR

        if (response.isSuccessful()
                && (!response.body().isNullData()
                && response.body().getErrorMessageJoined().equals(""))
                || !response.body().isNullData() && response.body().getErrorMessages() == null) {
            MessageData data = response.body().convertDataObj(MessageData.class);
            return convertToDomain(data);
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }

    private InboxChatViewModel convertToDomain(MessageData data) {
        InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
        inboxChatViewModel.setMode(InboxChatViewModel.GET_CHAT_MODE);
        inboxChatViewModel.setHasNext(data.isHasNext());

        ArrayList<Visitable> list = new ArrayList<>();
        for (ListMessage item : data.getList()) {
            ChatListViewModel viewModel = new ChatListViewModel();
            viewModel.setId(String.valueOf(item.getMsgId()));
            viewModel.setSenderId(String.valueOf(item.getAttributes().getContact().getId()));
            viewModel.setName(item.getAttributes().getContact().getAttributes().getName());
            viewModel.setImage(item.getAttributes().getContact().getAttributes().getThumbnail());
            viewModel.setLabel(item.getAttributes().getContact().getAttributes().getTag());
            viewModel.setMessage(item.getAttributes().getLastReplyMsg());
            viewModel.setTime(item.getAttributes().getLastReplyTime());
            viewModel.setReadStatus(item.getAttributes().getReadStatus());
            viewModel.setUnreadCounter(item.getAttributes().getUnreads());
            viewModel.setRole(item.getAttributes().getContact().getRole());
            list.add(viewModel);
        }
        inboxChatViewModel.setListReplies(list);
        inboxChatViewModel.setHasTimeMachine(data.getTimeMachineStatus() == 1);
        return inboxChatViewModel;
    }
}