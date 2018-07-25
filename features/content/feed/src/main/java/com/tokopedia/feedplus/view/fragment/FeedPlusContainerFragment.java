package com.tokopedia.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.explore.view.fragment.ContentExploreFragment;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlusContainerListener;

/**
 * @author by milhamj on 25/07/18.
 */

public class FeedPlusContainerFragment extends BaseDaggerFragment
        implements FeedPlusContainerListener {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    private FeedPlusFragment feedPlusFragment;
    private ContentExploreFragment contentExploreFragment;

    public static FeedPlusContainerFragment newInstance() {
        FeedPlusContainerFragment fragment = new FeedPlusContainerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_plus_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void showFeedPlus() {
        if (feedPlusFragment == null) {
            feedPlusFragment = FeedPlusFragment.newInstance();
        }
        inflateFragment(feedPlusFragment);
    }

    @Override
    public void showContentExplore() {
        if (contentExploreFragment == null) {
            contentExploreFragment = ContentExploreFragment.newInstance();
        }
        inflateFragment(contentExploreFragment);
    }

    private void initView() {
        feedPlusFragment = FeedPlusFragment.newInstance();
        inflateFragment(feedPlusFragment);
    }

    protected void inflateFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .commit();
    }
}
