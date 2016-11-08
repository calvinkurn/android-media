package com.tokopedia.core.inboxreputation.listener;

import android.app.Fragment;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by Nisie on 20/01/16.
 */
public interface InboxReputationView {

    PagerAdapter getViewPagerAdapter();

    List<Fragment> getFragmentList();
}
