package com.tokopedia.core.talk.inboxtalk.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.talk.inboxtalk.model.InboxTalk;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;
import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface InboxTalkPresenter {
    void getInboxTalk(Context context, Map<String, String> param);

    void unSubscribe();

    void saveState(Bundle state, List<RecyclerViewItem> items, int lastCompletelyVisibleItemPosition, String filterString);

    void restoreState(Bundle savedState);

    void getInboxTalkFromCache(Map<String, String> param);

    void deleteTalk(InboxTalk talk, int position);

    void reportTalk(InboxTalk talk, int position);

    int getPositionAction();

    void refreshInboxTalk(Context activity, Map<String, String> paramFirst);

    void followTalk(InboxTalk talk, int position);
}
