package com.tokopedia.topchat.chatlist.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface SearchRepository {

    Observable<InboxChatViewModel> searchChat(TKPDMapParam<String, Object> parameters);
}
