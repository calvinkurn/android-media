package com.tokopedia.topads.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.common.view.utils.TopAdsBottomSheetsSelectGroup;
import com.tokopedia.topads.common.TopAdsMenuBottomSheets;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.product.di.component.DaggerTopAdsProductAdListComponent;
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory;
import com.tokopedia.topads.product.view.listener.TopAdsProductAdListView;
import com.tokopedia.topads.product.view.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductListFragment extends TopAdsBaseListFragment<ProductAd,
        TopAdsListAdapterTypeFactory, TopAdsProductAdListPresenter>
        implements TopAdsProductAdListView {

    private long groupId;
    private GroupAd groupAd;
    private TopAdsBottomSheetsSelectGroup bottomSheetsSelectGroup;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        UnifyTracking.eventTopAdsProductNewPromoProduct(getActivity());
        presenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT);
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    public void deleteAd(List<String> ids) {
        presenter.deleteAds(ids);
    }

    @Override
    protected TopAdsListAdapterTypeFactory getAdapterTypeFactory() {
        TopAdsListAdapterTypeFactory<ProductAd> factory = new TopAdsListAdapterTypeFactory<>();
        factory.setOptionMoreCallback(this);
        return factory;
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
    public void onBulkActionError(Throwable throwable) {

    }

    @Override
    public void onBulkActionSuccess(ProductAdBulkAction productAdBulkAction) {
        finishActionMode();
        loadInitialData();
        setResultAdListChanged();
    }

    @Override
    public void onGetGroupAdListError() {
        if (bottomSheetsSelectGroup != null){
            bottomSheetsSelectGroup.setError(getString(R.string.error_connection_problem));
        }
    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        if (bottomSheetsSelectGroup != null){
            bottomSheetsSelectGroup.resetDialog();
            bottomSheetsSelectGroup.setGroupAds(groupAds);
        }
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
    public void showBulkActionBottomSheet(List<String> adIds) {
        showBottomsheetOptionMore(getString(R.string.topads_multi_select_title, adIds.size()),
                R.menu.menu_top_ads_product_bottomsheet,
                getOptionMoreBottomSheetItemClickListener(adIds));
    }

    @Override
    public void deleteBulkAction(List<String> adIds) {
        showDeleteConfirmation(getString(R.string.title_delete_promo),
                getString(R.string.top_ads_delete_product_alert), adIds);
    }

    @Override
    public TopAdsMenuBottomSheets.OnMenuItemSelected getOptionMoreBottomSheetItemClickListener(final List<String> ids) {
        return new TopAdsMenuBottomSheets.OnMenuItemSelected() {
            @Override
            public void onItemSelected(int itemId) {
                if (itemId == R.id.status_active){
                    presenter.setAdActive(ids);
                } else if (itemId == R.id.status_inactive) {
                    presenter.setAdInactive(ids);
                } else if (itemId == R.id.change_group) {
                    showBottomSheetMoveGroup(ids);
                } else if (itemId == R.id.delete) {
                    deleteBulkAction(ids);
                }
            }
        };
    }

    private void showBottomSheetMoveGroup(final List<String> ids) {
        if (bottomSheetsSelectGroup == null){
            bottomSheetsSelectGroup = new TopAdsBottomSheetsSelectGroup();
            bottomSheetsSelectGroup.setTitle(getString(R.string.label_top_ads_change_group));
            bottomSheetsSelectGroup.setHintDescription(getString(R.string.label_top_ads_info_group_option_exist));
            bottomSheetsSelectGroup.setOnSelectGroupListener(new TopAdsBottomSheetsSelectGroup.OnSelectGroupListener() {
                @Override
                public void searchGroup(String query) {
                    presenter.searchGroupName(query);
                }

                @Override
                public void submitGroup(String selectedId) {
                    presenter.moveAdsToExistingGroup(ids, selectedId);
                    bottomSheetsSelectGroup = null;
                }
            });
        }
        bottomSheetsSelectGroup.show(getActivity().getSupportFragmentManager(), getClass().getSimpleName());
    }

    @Override
    public void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductPageProductDateCustom(getActivity());
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(getActivity(), AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
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

    @Override
    public void onClickMore(ProductAd item) {
        showBottomsheetOptionMore(item.getName(), R.menu.menu_top_ads_product_bottomsheet,
                getOptionMoreBottomSheetItemClickListener(Collections.nCopies(1, item.getId())));
    }
}
