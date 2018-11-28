package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.topads.dashboard.R;
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant;
import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditTypeFactory;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.DataCreditViewHolder;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddCreditPresenter;

import java.util.List;

import javax.inject.Inject;

public class TopAdsAddCreditFragment extends BaseListFragment<DataCredit, TopAdsCreditTypeFactory>
        implements TopAdsAddCreditView, DataCreditViewHolder.OnSelectedListener {

    private static final String EXTRA_SELECTION_POSITION = "EXTRA_SELECTION_POSITION";

    private int selectedCreditPos = -1;
    private Button submitButton;

    @Inject
    TopAdsAddCreditPresenter presenter;

    public static Fragment createInstance() {
        return new TopAdsAddCreditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ads_add_credit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCredit();
            }
        });
        initErrorNetworkViewModel();
    }

    private void initErrorNetworkViewModel() {
        ErrorNetworkModel errorNetworkModel = new ErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_error_network);
        getAdapter().setErrorNetworkModel(errorNetworkModel);
    }

    @Override
    public void loadData(int page) {
        populateData();
    }

    @Override
    protected TopAdsCreditTypeFactory getAdapterTypeFactory() {
        return new TopAdsCreditTypeFactory(this);
    }


    private void populateData() {
        submitButton.setVisibility(View.GONE);
        presenter.populateCreditList();
    }

    @Override
    public void onCreditListLoaded(@NonNull List<DataCredit> creditList) {
        super.renderList(creditList);
        if (!creditList.isEmpty() && selectedCreditPos < 0){
            select(getDefaultSelection(creditList));
        }

        if (creditList.isEmpty()){
            submitButton.setVisibility(View.GONE);
            submitButton.setEnabled(false);
        } else {
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true);
        }
    }

    @Override
    public void onLoadCreditListError() {
        super.showGetListError(null);
    }

    private int getDefaultSelection(List<DataCredit> creditList) {
        for (int i = 0; i < creditList.size(); i++) {
            DataCredit dataCredit = creditList.get(i);
            if (dataCredit.getSelected() > 0) {
                return i;
            }
        }
        return 0;
    }

    private void chooseCredit() {
        if(selectedCreditPos > -1) {
            DataCredit selected = getAdapter().getData().get(selectedCreditPos);
            getActivity().setResult(Activity.RESULT_OK);
            Intent intent = new Intent(getActivity(), TopAdsPaymentCreditActivity.class);
            intent.putExtra(TopAdsDashboardConstant.EXTRA_CREDIT, selected);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECTION_POSITION, selectedCreditPos);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        select(savedInstanceState.getInt(EXTRA_SELECTION_POSITION));
    }

    @Override
    protected void initInjector() {
        getComponent(TopAdsDashboardComponent.class).inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(DataCredit dataCredit) { }

    @Override
    public boolean isPositionChecked(int pos) {
        return pos == selectedCreditPos;
    }

    @Override
    public void select(int pos) {
        selectedCreditPos = pos;
        getAdapter().notifyDataSetChanged();
    }
}