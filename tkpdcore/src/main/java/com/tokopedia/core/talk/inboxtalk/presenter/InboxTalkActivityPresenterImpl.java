package com.tokopedia.core.talk.inboxtalk.presenter;

import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.talk.inboxtalk.listener.InboxTalkActivityView;

/**
 * Created by stevenfredian on 5/20/16.
 */
public class InboxTalkActivityPresenterImpl implements InboxTalkActivityPresenter{

    private final InboxTalkActivityView viewListener;

    public InboxTalkActivityPresenterImpl(InboxTalkActivity inboxTalkActivity) {
        this.viewListener = inboxTalkActivity;
    }
}
