package com.tokopedia.tkpd.talk.inboxtalk.listener;

import android.app.Activity;

import com.tokopedia.tkpd.talk.inboxtalk.model.InboxTalk;
import com.tokopedia.tkpd.util.NewPagingHandler;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface InboxTalkView {

    void showError(String string);

    void onConnectionResponse(List<InboxTalk> list, int paging, int isUnread);

    void onTimeoutResponse(String error, int page);

    void onTimeoutResponse(int page);

    void onStateResponse(List<RecyclerViewItem> list, int position, int page, boolean hasNext, String filterString);

    void onCacheResponse(List<InboxTalk> list, int isUnread);

    void onCacheNoResult();

    Activity getActivity();

    void setLoadingFooter();

    void removeLoadingFooter();

    void cancelRequest();

    void setMenuListEnabled(boolean isEnabled);
}
