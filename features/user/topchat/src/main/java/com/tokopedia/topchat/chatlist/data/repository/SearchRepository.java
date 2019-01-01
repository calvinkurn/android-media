package com.tokopedia.topchat.chatlist.data.repository;


import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface SearchRepository {

    Observable<InboxChatViewModel> searchChat(HashMap<String, Object> parameters);
}
