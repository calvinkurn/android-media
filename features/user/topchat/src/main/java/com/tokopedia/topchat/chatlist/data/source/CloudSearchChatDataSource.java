package com.tokopedia.topchat.chatlist.data.source;

import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.common.chat.ChatService;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class CloudSearchChatDataSource {

    private SearchChatMapper searchChatMapper;
    private ChatService chatService;

    public CloudSearchChatDataSource(ChatService chatService, SearchChatMapper searchChatMapper) {
        this.chatService = chatService;
        this.searchChatMapper = searchChatMapper;
    }

    public Observable<InboxChatViewModel> searchChat(HashMap<String, Object> requestParams) {
        return chatService.getApi().searchChat(requestParams).map(searchChatMapper);
    }
}
