package com.tokopedia.topchat.chatlist.data.factory;

import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.data.source.CloudSearchChatDataSource;
import com.tokopedia.topchat.common.chat.api.ChatApi;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchFactory {

    private SearchChatMapper searchChatMapper;
    private ChatApi chatApi;

    public SearchFactory(ChatApi chatApi, SearchChatMapper searchChatMapper){
        this.chatApi = chatApi;
        this.searchChatMapper = searchChatMapper;
    }

    public CloudSearchChatDataSource createCloudSearchDataSource() {
        return new CloudSearchChatDataSource(chatApi, searchChatMapper);
    }
}
