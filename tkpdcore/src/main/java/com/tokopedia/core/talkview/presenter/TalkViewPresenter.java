package com.tokopedia.core.talkview.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.talkview.inbox.model.TalkDetail;
import com.tokopedia.core.talkview.model.TalkBaseModel;
import com.tokopedia.core.talkview.product.model.CommentTalk;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by stevenfredian on 4/28/16.
 */
public interface TalkViewPresenter {

    void getComment(Context talkView, Map<String, String> paramComment);

    void reply(Context talkView, Map<String, String> param);

    void unSubscribe();

    void deleteTalk();

    void followTalk();

    void reportTalk();

    void deleteCommentTalk(TalkDetail talk, int position);

    void reportCommentTalk(TalkDetail talk, int position);

    void deleteCommentTalk(CommentTalk talk, int position);

    void reportCommentTalk(CommentTalk talk, int position);

    void saveState(Bundle state, ArrayList<TalkBaseModel> items, int lastCompletelyVisibleItemPosition, int page, boolean hasNext);

    void restoreState(Bundle savedState);
}
