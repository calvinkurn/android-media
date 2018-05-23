package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectDealQuantityFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

public class DealDetailsActivity extends BaseSimpleActivity implements DealFragmentCallbacks {

    private List<OutletViewModel> outlets;
    private CategoryItemsViewModel dealDetail;

    @Override
    protected Fragment getNewFragment() {
        CategoryItemsViewModel itemsViewModel = getIntent().getParcelableExtra(DealDetailsPresenter.HOME_DATA);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DealDetailsPresenter.HOME_DATA, itemsViewModel);
        return DealDetailsFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);

    }

    @Override
    public void replaceFragment(List<OutletViewModel> outlets, int flag) {
        this.outlets=outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(CategoryItemsViewModel categoryItemsViewModel, int flag) {
        this.dealDetail=categoryItemsViewModel;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_view, SelectDealQuantityFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public List<OutletViewModel> getOutlets() {
        return this.outlets;
    }

    @Override
    public CategoryItemsViewModel getDealDetails() {
        return dealDetail;
    }
}