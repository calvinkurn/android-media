package com.tokopedia.topads.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.product.di.component.DaggerTopAdsProductAdListComponent;
import com.tokopedia.topads.product.view.adapter.TopAdsProductAdListAdapterTypeFactory;
import com.tokopedia.topads.product.view.listener.TopAdsProductAdListView;
import com.tokopedia.topads.product.view.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            status = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
            groupId = intent.getLongExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
            loadInitialData();
        } else if (requestCode == REQUEST_CODE_AD_SORT_BY && intent != null) {
            selectedSort = intent.getParcelableExtra(TopAdsSortByActivity.EXTRA_SORT_SELECTED);
            loadInitialData();
        }
    }

    @Override
    public void loadData(int page) {
        presenter.searchAd(startDate, endDate, keyword, status, groupId, page,
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
    public void onCreateAd() {
        UnifyTracking.eventTopAdsProductNewPromoProduct();
        presenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT);
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    protected TopAdsProductAdListAdapterTypeFactory getAdapterTypeFactory() {
        return new TopAdsProductAdListAdapterTypeFactory();
    }

    @Override
    public void onSearchLoaded(List<ProductAd> productAds, boolean hasNextPage) {
        super.onSuccessLoadedData(productAds, hasNextPage);
    }

    @Override
    public void showListError(Throwable throwable) {
        if (getAdapter().getData().size() < 1){
            showDateLabel(false);
        }
        super.showGetListError(throwable);
    }

    @Override
    public void onItemClicked(ProductAd ad) {

    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_promo_product_empty);
        emptyModel.setTitle(getString(R.string.top_ads_empty_product_title_promo_text));
        emptyModel.setContent(getString(R.string.top_ads_empty_product_promo_content_text));
        emptyModel.setButtonTitle(getString(R.string.menu_top_ads_add_promo_product));
        emptyModel.setCallback(this);
        return emptyModel;
    }

    @Override
    public void onFirstTimeLaunched() {
        groupAd = getArguments().getParcelable(TopAdsExtraConstant.EXTRA_GROUP);
        if (groupAd != null && !TextUtils.isEmpty(groupAd.getId())) {
            groupId = Integer.valueOf(groupAd.getId());
        }
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        groupAd = savedInstanceState.getParcelable(TopAdsExtraConstant.EXTRA_GROUP);
        if (groupAd != null && !TextUtils.isEmpty(groupAd.getId())) {
            groupId = Integer.valueOf(groupAd.getId());
        }
    }

    @Override
    public void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductPageProductDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAdClicked(ProductAd ad) {
        presenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT);
        Intent intent = new Intent(getActivity(), TopAdsDetailProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, ad.getId());
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, ad);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, true);
        intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, true);
        startActivityForResult(intent, REQUEST_CODE_AD_CHANGE);
    }
}
