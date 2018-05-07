package com.tokopedia.topads.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.topads.product.di.component.DaggerTopAdsProductAdListComponent;
import com.tokopedia.topads.product.view.adapter.TopAdsProductAdListAdapterTypeFactory;
import com.tokopedia.topads.product.view.listener.TopAdsProductAdListView;
import com.tokopedia.topads.product.view.presenter.TopAdsProductAdListPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductListFragment extends TopAdsBaseListFragment<ProductAd,
        TopAdsProductAdListAdapterTypeFactory, TopAdsProductAdListPresenter>
        implements TopAdsProductAdListView {

    private long groupId;
    private GroupAd groupAd;

    @Inject TopAdsProductAdListPresenter presenter;

    @Override
    protected void initInjector() {
        DaggerTopAdsProductAdListComponent.builder()
                .topAdsComponent(TopAdsComponentInstance.getComponent(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    public static Fragment createInstance(GroupAd groupAd) {
        TopAdsProductListFragment fragment = new TopAdsProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_GROUP, groupAd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData(int page) {
        super.loadData(page);
        presenter.searchAd(startDate, endDate, keyword, status, groupId, getCurrentPage(),
                selectedSort != null ? selectedSort.getId() : SortTopAdsOption.LATEST);
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
        if (groupAd != null) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, groupAd.getId());
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME, groupAd.getName());
        }
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public TopAdsProductAdListPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected TopAdsProductAdListAdapterTypeFactory getAdapterTypeFactory() {
        return new TopAdsProductAdListAdapterTypeFactory();
    }

    @Override
    public void onSearchLoaded(List<ProductAd> productAds, boolean hasNextPage) {
        super.renderList(productAds, hasNextPage);
    }
}
