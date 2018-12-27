package com.tokopedia.topchat.chatlist.data.factory;

import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.data.source.CloudSearchChatDataSource;
import com.tokopedia.topchat.common.chat.ChatService;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchFactory {

    private SearchChatMapper searchChatMapper;
    private ChatService chatService;

    public SearchFactory(ChatService chatService, SearchChatMapper searchChatMapper){
        this.chatService = chatService;
        this.searchChatMapper = searchChatMapper;
    }

    public CloudSearchChatDataSource createCloudSearchDataSource() {
        return new CloudSearchChatDataSource(chatService, searchChatMapper);
    }
}
