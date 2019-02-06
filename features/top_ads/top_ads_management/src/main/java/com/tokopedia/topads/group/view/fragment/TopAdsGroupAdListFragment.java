package com.tokopedia.topads.group.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.common.TopAdsMenuBottomSheets;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.group.di.component.DaggerTopAdsGroupAdListComponent;
import com.tokopedia.topads.group.view.listener.TopAdsGroupAdListView;
import com.tokopedia.topads.group.view.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdListFragment extends TopAdsBaseListFragment<GroupAd, BaseAdapterTypeFactory,
        TopAdsGroupAdListPresenter> implements TopAdsGroupAdListView {

    @Inject TopAdsGroupAdListPresenter presenter;

    @Override
    protected void initInjector() {
        DaggerTopAdsGroupAdListComponent.builder()
                .topAdsComponent(TopAdsComponentInstance.getComponent(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    public static Fragment createInstance() {
        return new TopAdsGroupAdListFragment();
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
            loadInitialData();
        } else if (requestCode == REQUEST_CODE_AD_SORT_BY && intent != null) {
            selectedSort = intent.getParcelableExtra(TopAdsSortByActivity.EXTRA_SORT_SELECTED);
            loadInitialData();
        }
    }

    @Override
    public void loadData(int page) {
        presenter.searchAd(startDate, endDate, keyword, status, page,
                selectedSort != null ? selectedSort.getId() : SortTopAdsOption.LATEST);
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public TopAdsGroupAdListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreateAd() {
        presenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_GROUP);
        UnifyTracking.eventTopAdsProductNewPromoGroup(getActivity());
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    public void deleteAd(List<String> ids) {
        presenter.deleteGroupAd(ids);
    }

    @Override
    protected TopAdsListAdapterTypeFactory getAdapterTypeFactory() {
        TopAdsListAdapterTypeFactory<GroupAd> factory = new TopAdsListAdapterTypeFactory<>();
        factory.setOptionMoreCallback(this);
        return factory;
    }

    @Override
    public void onSearchLoaded(List<GroupAd> groupAds, boolean hasNextPage) {
        super.onSuccessLoadedData(groupAds, hasNextPage);
    }

    @Override
    public void onBulkActionError(Throwable throwable) {

    }

    @Override
    public void onBulkActionSuccess(GroupAdBulkAction groupAdBulkAction) {
        finishActionMode();
        loadInitialData();
        setResultAdListChanged();
    }

    @Override
    public void showListError(Throwable throwable) {
        if (getAdapter().getData().size() < 1){
            showDateLabel(false);
        }
        super.showGetListError(throwable);
    }

    @Override
    public void onItemClicked(GroupAd groupAd) {

    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_promo_group);
        emptyModel.setTitle(getString(R.string.top_ads_empty_group_title_promo_text));
        emptyModel.setContent(getString(R.string.top_ads_empty_group_promo_content_text));
        emptyModel.setButtonTitle(getString(R.string.menu_top_ads_add_promo_group));
        emptyModel.setCallback(this);
        return emptyModel;
    }

    @Override
    public void onFirstTimeLaunched() {

    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {

    }

    @Override
    public void showBulkActionBottomSheet(final List<String> adIds) {
        showBottomsheetOptionMore(getString(R.string.topads_multi_select_title,adIds.size()),
                R.menu.menu_top_ads_group_bottomsheet,
                getOptionMoreBottomSheetItemClickListener(adIds));
    }

    @Override
    public void deleteBulkAction(List<String> adIds) {
        showDeleteConfirmation(getString(R.string.title_delete_group),
                getString(R.string.top_ads_delete_group_alert), adIds);
    }

    @Override
    public void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductPageGroupDateCustom(getActivity());
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAdClicked(GroupAd ad) {
        presenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_GROUP);
        Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, ad.getId());
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, ad);
        intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, true);
        startActivityForResult(intent, REQUEST_CODE_AD_CHANGE);
    }

    @Override
    public void onClickMore(final GroupAd ad) {
        showBottomsheetOptionMore(ad.getName(), R.menu.menu_top_ads_group_bottomsheet,
                getOptionMoreBottomSheetItemClickListener(Collections.nCopies(1, ad.getId())));
    }

    @Override
    public TopAdsMenuBottomSheets.OnMenuItemSelected getOptionMoreBottomSheetItemClickListener(final List<String> ids){
        return new TopAdsMenuBottomSheets.OnMenuItemSelected() {
            @Override
            public void onItemSelected(int itemId) {
                if (itemId == R.id.status_active){
                    presenter.setGroupActive(ids);
                } else if (itemId == R.id.status_inactive) {
                    presenter.setGroupInactive(ids);
                } else if (itemId == R.id.delete) {
                    deleteBulkAction(ids);
                }
            }
        };
    }
}
