package com.tokopedia.core.talk.inboxtalk.listener;

import android.os.Bundle;

/**
 * Created by stevenfredian on 5/20/16.
 */
public interface InboxTalkActivityView {
    void followTalk(Bundle bundle);

    void deleteTalk(Bundle bundle);

    void reportTalk(Bundle bundle);
}
