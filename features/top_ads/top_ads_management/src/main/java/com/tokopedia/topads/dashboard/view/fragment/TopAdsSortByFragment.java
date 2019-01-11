package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsSortByAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsSortByView;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsSortByPresenter;

import java.util.List;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByFragment extends BaseListFragment<TopAdsSortByPresenter, TopAdsSortByModel>
        implements TopAdsSortByView {

    private TopAdsSortByPresenter presenter;

    String selectedSort = SortTopAdsOption.LATEST;

    public static Fragment createInstance(String selectedSort) {
        TopAdsSortByFragment fragment = new TopAdsSortByFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsSortByActivity.EXTRA_SORT_SELECTED, selectedSort);
        fragment.setupArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsSortByPresenter();
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_manage_sort;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        selectedSort = arguments.getString(TopAdsSortByActivity.EXTRA_SORT_SELECTED, SortTopAdsOption.LATEST);
    }

    @Override
    protected BaseListAdapter<TopAdsSortByModel> getNewAdapter() {
        TopAdsSortByAdapter topAdsSortByAdapter = new TopAdsSortByAdapter();
        topAdsSortByAdapter.setSortTopAdsOption(selectedSort);
        return topAdsSortByAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        presenter.getListSortTopAds(getResources().getStringArray(R.array.top_ads_sort_option),
                getResources().getStringArray(R.array.top_ads_sort_value));
    }

    @Override
    public void onSuccessGetListSort(List<TopAdsSortByModel> topAdsSortByModels) {
        onSearchLoaded(topAdsSortByModels, topAdsSortByModels.size());
    }

    @Override
    public void onItemClicked(TopAdsSortByModel topAdsSortByModel) {
        ((TopAdsSortByAdapter) adapter).setSortTopAdsOption(topAdsSortByModel.getId());
        adapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra(TopAdsSortByActivity.EXTRA_SORT_SELECTED, topAdsSortByModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected boolean hasNextPage() {
        return false;
    }
}
