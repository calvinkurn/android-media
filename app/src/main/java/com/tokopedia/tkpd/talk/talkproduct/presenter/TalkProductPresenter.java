package com.tokopedia.tkpd.talk.talkproduct.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.talk.talkproduct.model.Talk;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.List;
import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface TalkProductPresenter {
    void getTalkProduct(Context context, Map<String, String> param);

    void unSubscribe();

    void saveState(Bundle state, List<RecyclerViewItem> items, int lastCompletelyVisibleItemPosition);

    void restoreState(Bundle savedState);

    void refreshTalkProduct(Context activity, Map<String, String> param);

    void followTalk(Talk talk, int position);

    void deleteTalk(Talk talk, int position);

    void reportTalk(Talk talk, int position);

    int getPositionAction();
}
