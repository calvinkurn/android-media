package com.tokopedia.topchat.chatlist.data.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topchat.chatlist.domain.pojo.search.RepliesContent;
import com.tokopedia.topchat.chatlist.domain.pojo.search.SearchedMessage;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.common.InboxMessageConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchChatMapper implements Func1<Response<DataResponse<SearchedMessage>>, InboxChatViewModel> {

    @Inject
    public SearchChatMapper() {
    }

    @Override
    public InboxChatViewModel call(Response<DataResponse<SearchedMessage>> response) {
        if (response.isSuccessful() &&
                response.body().getHeader() == null ||
                (response.body().getHeader() != null && response.body().getHeader().getMessages().isEmpty()
                ) || (response.body().getHeader() != null && response.body().getHeader().getMessages().get(0).equals(""))) {
            SearchedMessage data = response.body().getData();
            return convertToDomain(data);
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }

    private InboxChatViewModel convertToDomain(SearchedMessage data) {
        InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
        inboxChatViewModel.setMode(InboxChatViewModel.SEARCH_CHAT_MODE);

        inboxChatViewModel = prepareContact(inboxChatViewModel, data);
        inboxChatViewModel = prepareReplies(inboxChatViewModel, data);

        return inboxChatViewModel;
    }

    private InboxChatViewModel prepareContact(InboxChatViewModel inboxChatViewModel, SearchedMessage data) {
        if (data.getContacts() != null && data.getContacts().getData() != null) {
            int index = 0;

            inboxChatViewModel.setHasNextContacts(data.getContacts().isHasNext());
            inboxChatViewModel.setContactSize(data.getContacts().getData().size());

            ArrayList<Visitable> listContact = new ArrayList<>();

            for (RepliesContent item : data.getContacts().getData()) {
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getContact().getAttributes().getName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getContact().getAttributes().getName()));
                viewModel.setImage(item.getContact().getAttributes().getThumbnail());
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(InboxMessageConstant.STATE_CHAT_READ);
                viewModel.setLabel(getRole(item.getOppositeType()));
                viewModel.setSpanMode(ChatListViewModel.SPANNED_CONTACT);
                viewModel.setSenderId(String.valueOf(item.getOppositeId()));
                listContact.add(viewModel);
                if (index == 0) {
                    viewModel.setSectionSize(data.getContacts().getData().size());
                }
                index++;
            }

            inboxChatViewModel.setListContact(listContact);
        }
        return inboxChatViewModel;
    }

    private InboxChatViewModel prepareReplies(InboxChatViewModel inboxChatViewModel, SearchedMessage data) {
        if (data.getReplies() != null && data.getReplies().getData() != null) {
            int index = 0;

            inboxChatViewModel.setHasNextReplies(data.getReplies().isHasNext());
            inboxChatViewModel.setChatSize(data.getReplies().getData().size());


            ArrayList<Visitable> listReplies = new ArrayList<>();

            for (RepliesContent item : data.getReplies().getData()) {
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getContact().getAttributes().getName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getLastMessage()));
                viewModel.setImage(item.getContact().getAttributes().getThumbnail());
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(InboxMessageConstant.STATE_CHAT_READ);
                viewModel.setLabel(item.getContact().getRole());
                viewModel.setSpanMode(ChatListViewModel.SPANNED_MESSAGE);
                viewModel.setRole(item.getContact().getRole());
                viewModel.setSenderId(String.valueOf(item.getOppositeId()));
                listReplies.add(viewModel);
                if (index == 0) {
                    viewModel.setSectionSize(data.getReplies().getData().size());
                }
                index++;
            }
            inboxChatViewModel.setListReplies(listReplies);
        }
        return inboxChatViewModel;
    }

    private String getRole(int oppositeType) {
        switch (oppositeType) {
            case 1:
                return "Pengguna";
            case 2:
                return "Penjual";
        }
        return null;
    }
}
