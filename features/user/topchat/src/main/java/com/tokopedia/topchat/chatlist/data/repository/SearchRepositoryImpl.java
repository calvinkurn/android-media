package com.tokopedia.topchat.chatlist.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatlist.data.factory.SearchFactory;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchRepositoryImpl implements SearchRepository{

    private SearchFactory searchFactory;

    public SearchRepositoryImpl(SearchFactory searchFactory){
        this.searchFactory = searchFactory;
    }

    @Override
    public Observable<InboxChatViewModel> searchChat(TKPDMapParam<String, Object> parameters) {
        return searchFactory.createCloudSearchDataSource().searchChat(parameters);
    }
}
