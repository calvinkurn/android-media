package com.tokopedia.topads.group.view.activity;

import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.topads.group.view.fragment.TopAdsGroupAdListFragment;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListActivity extends BaseSimpleActivity
        implements TopAdsBaseListFragment.OnAdListFragmentListener {
    private ShowCaseDialog showCaseDialog;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsGroupAdListActivity.class.getName();
        final TopAdsGroupAdListFragment topAdsGroupAdListFragment =
                (TopAdsGroupAdListFragment) getSupportFragmentManager().findFragmentByTag(TopAdsGroupAdListFragment.class.getSimpleName());
        if (topAdsGroupAdListFragment == null || topAdsGroupAdListFragment.getView() == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();

            showCaseList.add(
                    new ShowCaseObject(
                            topAdsGroupAdListFragment.getSearchView(),
                            getString(R.string.topads_showcase_group_list_title_1),
                            getString(R.string.topads_showcase_group_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE));

            showCaseList.add(
                    new ShowCaseObject(
                            topAdsGroupAdListFragment.getFilterView(),
                            getString(R.string.topads_showcase_group_list_title_2),
                            getString(R.string.topads_showcase_group_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED));

            RecyclerView recyclerView = topAdsGroupAdListFragment.getRecyclerView();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(topAdsGroupAdListFragment.getView() == null){
                        return;
                    }
                    View dateView = topAdsGroupAdListFragment.getDateView();
                    if (dateView != null) {
                        dateView.setVisibility(View.VISIBLE);
                        showCaseList.add(
                                new ShowCaseObject(
                                        dateView,
                                        getString(R.string.topads_showcase_group_list_title_3),
                                        getString(R.string.topads_showcase_group_list_desc_3)));
                    }

                    View itemView = topAdsGroupAdListFragment.getItemRecyclerView();
                    if (itemView != null) {
                        showCaseList.add(
                                new ShowCaseObject(
                                        itemView,
                                        getString(R.string.topads_showcase_group_list_title_4),
                                        getString(R.string.topads_showcase_group_list_desc_4),
                                        ShowCaseContentPosition.UNDEFINED,
                                        Color.WHITE));
                    }

                    showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                    showCaseDialog.show(TopAdsGroupAdListActivity.this, showCaseTag, showCaseList);
                }
            }, 300);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }));
        }

    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            fragment = TopAdsGroupAdListFragment.createInstance();
            return fragment;
        }
    }

    @Override
    protected String getTagFragment() {
        return TopAdsGroupAdListFragment.class.getSimpleName();
    }
}
