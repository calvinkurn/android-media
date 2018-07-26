package com.tokopedia.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.explore.view.fragment.ContentExploreFragment;
import com.tokopedia.explore.view.listener.ExploreContainerListener;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlusContainerListener;

/**
 * @author by milhamj on 25/07/18.
 */

public class FeedPlusContainerFragment extends BaseDaggerFragment
        implements FeedPlusContainerListener, ExploreContainerListener {

    private FeedPlusFragment feedPlusFragment;
    private ContentExploreFragment contentExploreFragment;
    private FrameLayout feedPlusContainer;
    private FrameLayout contentExploreContainer;

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
        View view = inflater.inflate(R.layout.fragment_feed_plus_container, container, false);
        feedPlusContainer = view.findViewById(R.id.feed_plus_container);
        contentExploreContainer = view.findViewById(R.id.content_explore_container);
        return view;
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
        feedPlusContainer.setVisibility(View.VISIBLE);
        contentExploreContainer.setVisibility(View.GONE);
        inflateFragment(R.id.feed_plus_container, feedPlusFragment);
    }

    @Override
    public void showContentExplore() {
        if (contentExploreFragment == null) {
            contentExploreFragment = ContentExploreFragment.newInstance();
        }
        feedPlusContainer.setVisibility(View.GONE);
        contentExploreContainer.setVisibility(View.VISIBLE);
        inflateFragment(R.id.content_explore_container, contentExploreFragment);
    }

    private void initView() {
        showFeedPlus();
    }

    protected void inflateFragment(@IdRes int containerId, Fragment fragment) {
        if (fragment == null) {
            return;
        }

        String TAG = fragment.getClass().getSimpleName();
        if (getChildFragmentManager().findFragmentByTag(TAG) != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(containerId,
                            getChildFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .add(containerId, fragment, TAG)
                    .commit();
        }
    }
}
