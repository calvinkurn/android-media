package com.tokopedia.topchat.chatlist.data.repository;

import com.tokopedia.topchat.chatlist.data.factory.SearchFactory;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchRepositoryImpl implements SearchRepository {

    private SearchFactory searchFactory;

    public SearchRepositoryImpl(SearchFactory searchFactory) {
        this.searchFactory = searchFactory;
    }

    @Override
    public Observable<InboxChatViewModel> searchChat(HashMap<String, Object> parameters) {
        return searchFactory.createCloudSearchDataSource().searchChat(parameters);
    }
}
