package com.tokopedia.topchat.chatlist.data.source;

import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class CloudSearchChatDataSource {

    private SearchChatMapper searchChatMapper;
    private ChatApi chatApi;

    public CloudSearchChatDataSource(ChatApi chatApi, SearchChatMapper searchChatMapper) {
        this.chatApi = chatApi;
        this.searchChatMapper = searchChatMapper;
    }

    public Observable<InboxChatViewModel> searchChat(HashMap<String, Object> requestParams) {
        return chatApi.searchChat(requestParams).map(searchChatMapper);
    }
}
