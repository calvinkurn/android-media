package com.tokopedia.topchat.chatlist.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

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

    public Observable<InboxChatViewModel> searchChat(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().searchChat(requestParams).map(searchChatMapper);
    }
}
