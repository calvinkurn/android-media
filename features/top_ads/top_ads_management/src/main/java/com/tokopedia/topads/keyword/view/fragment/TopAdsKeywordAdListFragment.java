package com.tokopedia.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.seller.base.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.common.view.utils.TopAdsBottomSheetsSelectGroup;
import com.tokopedia.topads.common.TopAdsMenuBottomSheets;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment;
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordListView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenter;
import com.tokopedia.topads.keyword.view.widget.TopAdsKeywordUpdatePriceBottomSheets;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 11/05/18.
 */

public abstract class TopAdsKeywordAdListFragment extends TopAdsBaseListFragment<KeywordAd,
        TopAdsListAdapterTypeFactory, TopAdsKeywordListPresenter> implements TopAdsKeywordListView {

    protected int filterStatus;
    protected GroupAd groupAd;
    protected int selectedPosition;

    @Inject TopAdsKeywordListPresenter presenter;

    private GroupTopAdsListener groupTopAdsListener;
    private TopAdsBottomSheetsSelectGroup bottomSheetsSelectGroup;

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof GroupTopAdsListener) {
            groupTopAdsListener = (GroupTopAdsListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        filterStatus = KeywordStatusTypeDef.KEYWORD_STATUS_ALL;

        if (presenter.isDateUpdated(startDate, endDate)){
            startDate = getPresenter().getStartDate();
            endDate = getPresenter().getEndDate();
        }
        super.onViewCreated(view, savedInstanceState);
        updateLabelDateView(startDate, endDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            groupAd = intent.getParcelableExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
            filterStatus = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
            selectedPosition = intent.getIntExtra(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, 0);
            loadInitialData();
        } else if (requestCode == REQUEST_CODE_AD_SORT_BY && intent != null){
            if (resultCode == Activity.RESULT_OK){
                selectedSort = intent.getParcelableExtra(TopAdsSortByActivity.EXTRA_SORT_SELECTED);
                loadInitialData();
            }
        }

        if (requestCode == TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // top ads new groups/edit existing group/promo not in group has been success
                boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
                if (adStatusChanged) {
                    if (groupTopAdsListener != null) {
                        groupTopAdsListener.setGroupTopAdsSize(1);// force to notify that group already added
                    }
                }
            }
        }
    }

    @Override
    public void loadData(int page) {
        int groupId = groupAd == null? 0 : Integer.parseInt(groupAd.getId());
        presenter.searchKeyword(startDate, endDate, keyword, filterStatus, groupId, page,
                selectedSort != null ? selectedSort.getId() : SortTopAdsOption.LATEST, isPositive());
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsKeywordFilterActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
        intent.putExtra(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, selectedPosition);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SHOW_STATUS, isStatusShown());
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public TopAdsKeywordListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreateAd() {
        presenter.saveSourceTagging(getSourceTagging());
        TopAdsKeywordNewChooseGroupActivity.Companion.start(this, getActivity(), REQUEST_CODE_AD_ADD, isPositive(), null);
    }

    @Override
    public void deleteAd(List<String> ids) {
        presenter.deleteKeywords(ids);
    }

    @Override
    protected TopAdsListAdapterTypeFactory getAdapterTypeFactory() {
        TopAdsListAdapterTypeFactory<KeywordAd> factory = new TopAdsListAdapterTypeFactory<KeywordAd>();
        factory.setOptionMoreCallback(this);
        return factory;
    }

    @Override
    protected void showOption(boolean show) {
        super.showOption(show);
        if (!isPositive()){
            getDateView().setVisibility(View.GONE);
            getView().findViewById(R.id.app_bar_separator).setVisibility(View.GONE);

        } else {
            showDateLabel(show);
        }
    }

    @Override
    public void onSearchLoaded(List<KeywordAd> data, boolean hasNextData) {
        super.onSuccessLoadedData(data, hasNextData);
    }

    @Override
    public void showListError(Throwable throwable) {
        if (getAdapter().getData().size() < 1){
            showDateLabel(false);
        }
        super.showGetListError(throwable);
    }

    @Override
    public void onItemClicked(KeywordAd keywordAd) {

    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_promo_keyword);
        emptyModel.setButtonTitle(getString(R.string.top_ads_keyword_add_keyword));
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
    public void showBulkActionBottomSheet(List<String> adIds) {
        showBottomsheetOptionMore(getString(R.string.topads_multi_select_title, adIds.size()),
                getBottomSheetMenuRes(),
                getOptionMoreBottomSheetItemClickListener(adIds));
    }

    private @MenuRes int getBottomSheetMenuRes(){
        if (isPositive()){
            return R.menu.menu_top_ads_keyword_bottomsheet;
        } else {
            return R.menu.menu_top_ads_keyword_negative_bottomsheet;
        }
    }

    @Override
    public void deleteBulkAction(List<String> adIds) {
        showDeleteConfirmation(getString(R.string.top_ads_keyword_title_delete),
                getString(R.string.top_ads_keyword_confirmation_delete), adIds);
    }

    @Override
    public void trackingDateTopAds(int lastSelection, int selectionType) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        outState.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
    }

    @Override
    public void onEmptyButtonClicked() {
        if (groupTopAdsListener != null && groupTopAdsListener.getGroupTopAdsSize() <= 0) {
            showAddGroupDialog();
        } else {
            onCreateAd();
        }
    }

    @Override
    public void onClickMore(KeywordAd ad) {
        showBottomsheetOptionMore(ad.getName(), getBottomSheetMenuRes(),
                getOptionMoreBottomSheetItemClickListener(Collections.nCopies(1, ad.getId())));
    }


    @Override
    public TopAdsMenuBottomSheets.OnMenuItemSelected getOptionMoreBottomSheetItemClickListener(final List<String> ids) {
        return new TopAdsMenuBottomSheets.OnMenuItemSelected() {
            @Override
            public void onItemSelected(int itemId) {
                if (itemId == R.id.status_active){
                    presenter.setKeywordActive(ids);
                } else if (itemId == R.id.status_inactive) {
                    presenter.setKeywordInactive(ids);
                } else if (itemId == R.id.delete) {
                    deleteBulkAction(ids);
                } else if (itemId == R.id.change_price){
                    showBottomSheetUpdatePrice(ids);
                } else if (itemId == R.id.copy){
                    showBottomSheetCopyKeyword(ids);
                }
            }
        };
    }

    private void showBottomSheetCopyKeyword(final List<String> ids) {
        if (bottomSheetsSelectGroup == null){
            bottomSheetsSelectGroup = new TopAdsBottomSheetsSelectGroup();
            bottomSheetsSelectGroup.setTitle(getString(R.string.menu_copy));
            bottomSheetsSelectGroup.setHintDescription(getString(R.string.top_ads_keyword_copy_hint_desc));
            bottomSheetsSelectGroup.setOnSelectGroupListener(new TopAdsBottomSheetsSelectGroup.OnSelectGroupListener() {
                @Override
                public void searchGroup(String query) {
                    presenter.searchGroupName(query);
                }

                @Override
                public void submitGroup(String selectedId) {
                    presenter.copyKeyword(ids, selectedId);
                    bottomSheetsSelectGroup = null;
                }
            });
        }
        bottomSheetsSelectGroup.show(getActivity().getSupportFragmentManager(), getClass().getSimpleName());
    }

    private void showBottomSheetUpdatePrice(final List<String> ids) {
        TopAdsKeywordUpdatePriceBottomSheets bottomSheets = new TopAdsKeywordUpdatePriceBottomSheets();
        bottomSheets.setOnSubmitClicked(new TopAdsKeywordUpdatePriceBottomSheets.OnSubmitClicked() {
            @Override
            public void submitPrice(String price) {
                presenter.updatePrice(ids, price);
            }
        });
        bottomSheets.show(getActivity().getSupportFragmentManager(), getClass().getSimpleName());
    }

    @Override
    public void onBulkActionError(Throwable e) {

    }

    @Override
    public void onBulkActionSuccess(PageDataResponse<DataBulkKeyword> adBulkActions) {
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

    private void showAddGroupDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity()
                , R.style.AppCompatAlertDialogStyle);
        myAlertDialog.setMessage(getString(R.string.top_ads_keyword_add_group_promo_desc));

        myAlertDialog.setPositiveButton(getString(R.string.top_ads_keyword_add_group_promo_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
                presenter.saveSourceTagging(getSourceTagging());
                TopAdsKeywordAdListFragment.this.startActivityForResult(intent, TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS);
            }
        });

        myAlertDialog.setNegativeButton(getString(R.string.top_ads_keyword_add_group_promo_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.setTitle(R.string.top_ads_keyword_add_group_promo);
        dialog.show();
    }

    public abstract boolean isPositive();

    public abstract boolean isStatusShown();

    public abstract String getSourceTagging();

    public interface GroupTopAdsListener {
        int getGroupTopAdsSize();

        void setGroupTopAdsSize(int size);
    }
}
