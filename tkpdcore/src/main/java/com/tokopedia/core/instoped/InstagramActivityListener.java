package com.tokopedia.core.instoped;

import com.tokopedia.core.instoped.fragment.InstagramMediaFragment;

/**
 * Created by sebastianuskh on 1/3/17.
 */
public interface InstagramActivityListener {
    void triggerAppBarAnimation(boolean b);

    InstagramMediaFragment.OnGetInstagramMediaListener onGetInstagramMediaListener();
}
