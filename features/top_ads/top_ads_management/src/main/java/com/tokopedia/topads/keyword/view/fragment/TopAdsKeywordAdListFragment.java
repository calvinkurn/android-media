package com.tokopedia.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.seller.base.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment;
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.topads.keyword.domain.model.Datum;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordListView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenter;

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

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
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
        Log.e(getClass().getSimpleName(), "load data");
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
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_AD_ADD, isPositive());
    }

    @Override
    protected TopAdsListAdapterTypeFactory getAdapterTypeFactory() {
        return new TopAdsListAdapterTypeFactory<KeywordAd>();
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
    public void trackingDateTopAds(int lastSelection, int selectionType) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        outState.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {

    }

    @Override
    public void onAdClicked(KeywordAd ad) {
        presenter.saveSourceTagging(getSourceTagging());
        startActivityForResult(TopAdsKeywordDetailActivity.createInstance(getActivity(), ad, ad.getId()), REQUEST_CODE_AD_CHANGE);
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
    public void showListError(Throwable throwable) {
        if (getAdapter().getData().size() < 1){
            showDateLabel(false);
        }
        super.showGetListError(throwable);
    }

    @Override
    public void onSearchLoaded(List<KeywordAd> data, boolean hasNextData) {
        super.onSuccessLoadedData(data, hasNextData);
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
