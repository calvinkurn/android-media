package com.tokopedia.core.inboxreputation.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.inboxreputation.adapter.SectionsPagerAdapter;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFragment;
import com.tokopedia.core.inboxreputation.listener.InboxReputationView;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Nisie on 20/01/16.
 */
public class InboxReputationActivity extends DrawerPresenterActivity
        implements InboxReputationView {

    private static final int OFFSCREEN_PAGE_LIMIT = 2;
    public static final String REVIEW_ALL = "inbox-reputation";
    public static final String REVIEW_PRODUCT = "inbox-reputation-my-product";
    public static final String REVIEW_USER = "inbox-reputation-my-review";

    @Bind(R2.id.pager)
    ViewPager viewPager;
    @Bind(R2.id.indicator)
    TabLayout indicator;

    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_reputation;
    }

    @Override
    protected void initView() {
        super.initView();
        drawer.setDrawerPosition(TkpdState.DrawerPosition.INBOX_REVIEW);
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));

        indicator.addTab(indicator.newTab().setText(getString(R.string.title_menu_all)));
        indicator.addTab(indicator.newTab().setText(getString(R.string.title_my_product)));
        indicator.addTab(indicator.newTab().setText(getString(R.string.title_my_review)));

    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_REVIEW;
    }

    @Override
    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(InboxReputationFragment.createInstance(REVIEW_ALL));
        fragmentList.add(InboxReputationFragment.createInstance(REVIEW_PRODUCT));
        fragmentList.add(InboxReputationFragment.createInstance(REVIEW_USER));
        return fragmentList;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public PagerAdapter getViewPagerAdapter() {
        return new SectionsPagerAdapter(getFragmentManager(), getFragmentList());
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TkpdState.RequestCode.CODE_OPEN_DETAIL_REPUTATION:
                getFragmentManager().findFragmentById(R.id.pager).onActivityResult(requestCode,
                        resultCode, data);
                break;
            default:
                break;
        }
    }
}
