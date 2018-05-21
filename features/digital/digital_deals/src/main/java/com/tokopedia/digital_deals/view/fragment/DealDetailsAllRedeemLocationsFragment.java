package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.adapter.DealDetailsAllLocationsAdapter;
import com.tokopedia.digital_deals.view.contractor.DealDetailsAllRedeemLocationsContract;
import com.tokopedia.digital_deals.view.presenter.DealDetailsAllLocationsRedeemPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

import javax.inject.Inject;


public class DealDetailsAllRedeemLocationsFragment extends BaseDaggerFragment implements DealDetailsAllRedeemLocationsContract.View{

    @Inject
    public DealDetailsAllLocationsRedeemPresenter mPresenter;
    private DealFragmentCallbacks fragmentCallbacks;
    private Toolbar toolbar;
    private RecyclerView recyclerView;


    public static Fragment createInstance() {
        Fragment fragment = new DealDetailsAllRedeemLocationsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem_locations, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);
        mPresenter.initialize(fragmentCallbacks.getOutlets());

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (DealDetailsActivity)activity;
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_deals));
        recyclerView=view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void renderBrandDetails(List<OutletViewModel> outletViewModelList) {

        DealDetailsAllLocationsAdapter adapter=new DealDetailsAllLocationsAdapter(getActivity(), outletViewModelList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
