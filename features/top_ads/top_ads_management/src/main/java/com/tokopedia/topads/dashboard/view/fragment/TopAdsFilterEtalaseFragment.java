package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;
import com.tokopedia.topads.dashboard.di.TopAdsGetEtalaseListDI;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.topads.dashboard.view.listener.TopAdsEtalaseListView;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsEtalaseListPresenter;

import java.util.ArrayList;
import java.util.List;

public class TopAdsFilterEtalaseFragment extends TopAdsFilterRadioButtonFragment<TopAdsEtalaseListPresenter>
        implements TopAdsEtalaseListView {

    private int selectedEtalaseId;
    private String shopId;

    public static TopAdsFilterEtalaseFragment createInstance(int etalaseID) {
        TopAdsFilterEtalaseFragment fragment = new TopAdsFilterEtalaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, etalaseID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_etalase;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedEtalaseId = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = new SessionHandler(getActivity()).getShopID();
        RetryDataBinder topAdsRetryDataBinder = new TopAdsRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.populateEtalaseList(shopId);
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsGetEtalaseListDI.createPresenter();
        presenter.attachView(this);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        // will populate radio item from API
        return null;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedEtalaseId == 0) { // default to all etalase
            selectedAdapterPosition = 0;
            return;
        }
        if (selectedAdapterPosition > 0) { // has been updated for first time only
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedEtalaseId) {
                selectedAdapterPosition = i;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_etalase);
    }

    @Override
    public Intent addResult(Intent intent) {
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE,
                Integer.parseInt(getSelectedRadioValue()));
        return intent;
    }

    @Override
    public void onLoadSuccess(@NonNull List<Etalase> etalaseList) {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        radioButtonItemList.add(getDefaultRadioButton());

        for (int i = 0; i < etalaseList.size(); i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(etalaseList.get(i).getEtalaseName());
            radioButtonItem.setValue(String.valueOf(etalaseList.get(i).getEtalaseId()));
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }

        updateSelectedPosition(radioButtonItemList);

        adapter.showRetryFull(false);
        setAdapterData(radioButtonItemList);
    }

    @Override
    public void onLoadSuccessEtalaseEmpty() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        radioButtonItemList.add(getDefaultRadioButton());

        selectedAdapterPosition = 0;

        adapter.showRetryFull(false);
        setAdapterData(radioButtonItemList);
    }


    private RadioButtonItem getDefaultRadioButton() {
        RadioButtonItem defaultRadioButton = new RadioButtonItem();
        defaultRadioButton.setName(getString(R.string.title_all_etalase));
        defaultRadioButton.setValue("0");
        defaultRadioButton.setPosition(0);
        return defaultRadioButton;
    }

    @Override
    public void onLoadConnectionError() {
        // NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.error_connection_problem));
        adapter.showRetryFull(true);
    }

    @Override
    public void onLoadError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
        adapter.showRetryFull(true);
    }

    @Override
    public void showLoad(boolean isShow) {
        adapter.showLoadingFull(isShow);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.populateEtalaseList(shopId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}