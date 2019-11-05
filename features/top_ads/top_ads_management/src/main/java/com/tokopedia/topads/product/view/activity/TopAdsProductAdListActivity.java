package com.tokopedia.topads.product.view.activity;

import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.topads.product.view.fragment.TopAdsProductListFragment;

import java.util.ArrayList;

public class TopAdsProductAdListActivity extends BaseSimpleActivity
        implements TopAdsBaseListFragment.OnAdListFragmentListener {

    private static final String TAG = "TopAdsProductAdListActi";

    private ShowCaseDialog showCaseDialog;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsProductAdListActivity.class.getName();
        final TopAdsProductListFragment topAdsProductAdListFragment =
                (TopAdsProductListFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (topAdsProductAdListFragment == null || topAdsProductAdListFragment.getView() == null) {
            return;
        }

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        if (topAdsProductAdListFragment.getSearchView() != null
                && topAdsProductAdListFragment.getSearchView().getVisibility() == View.VISIBLE) {
            showCaseList.add(
                    new ShowCaseObject(
                            topAdsProductAdListFragment.getSearchView(),
                            getString(R.string.topads_showcase_product_list_title_1),
                            getString(R.string.topads_showcase_product_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE));
        }

        if (topAdsProductAdListFragment.getFilterView() != null
                && topAdsProductAdListFragment.getFilterView().getVisibility() == View.VISIBLE) {
            showCaseList.add(
                    new ShowCaseObject(
                            topAdsProductAdListFragment.getFilterView(),
                            getString(R.string.topads_showcase_product_list_title_2),
                            getString(R.string.topads_showcase_product_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED));
        }

        RecyclerView recyclerView = topAdsProductAdListFragment.getRecyclerView();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (topAdsProductAdListFragment.getView() == null) {
                    return;
                }
                View dateView = topAdsProductAdListFragment.getDateView();
                if (dateView != null) {
                    dateView.setVisibility(View.VISIBLE);
                    showCaseList.add(
                            new ShowCaseObject(
                                    dateView,
                                    getString(R.string.topads_showcase_product_list_title_3),
                                    getString(R.string.topads_showcase_product_list_desc_3)));
                }

                View itemView = topAdsProductAdListFragment.getItemRecyclerView();
                if (itemView != null) {
                    showCaseList.add(
                            new ShowCaseObject(
                                    itemView,
                                    getString(R.string.topads_showcase_product_list_title_4),
                                    getString(R.string.topads_showcase_product_list_desc_4),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE));
                }

                showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                showCaseDialog.show(TopAdsProductAdListActivity.this, showCaseTag, showCaseList);
            }
        }, 300);

    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if (fragment != null) {
            return fragment;
        } else {
            GroupAd groupAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_GROUP);
            fragment = TopAdsProductListFragment.createInstance(groupAd);
            return fragment;
        }
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }
}
