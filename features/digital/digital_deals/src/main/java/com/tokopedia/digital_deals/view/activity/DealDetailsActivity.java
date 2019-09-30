package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.digital_deals.view.activity.model.DealDetailPassData;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectDealQuantityFragment;
import com.tokopedia.digital_deals.view.fragment.TncBottomSheetFragment;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;

import java.util.List;

public class DealDetailsActivity extends DealsBaseActivity implements DealFragmentCallbacks {

    private List<Outlet> outlets;
    private DealsDetailsResponse dealDetail;


    public Intent getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        Intent destination = new Intent();
        extras.putString(DealDetailsPresenter.HOME_DATA, extras.getString("slug"));
        destination = new Intent(context, DealDetailsActivity.class)
                .putExtras(extras);
        return destination;
    }

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        return DealDetailsFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> params = UriUtil.destructureUri(ApplinkConst.DEALS_DETAIL, uri, true);
            Bundle extra = getIntent().getExtras();
            if (extra != null) {
                extra.putString("slug", params.get(0));
            }
            getInstanceIntentAppLinkBackToHome(this, extra);
        }
    }

    @Override
    public void replaceFragment(List<Outlet> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.slide_in_up, com.tokopedia.digital_deals.R.anim.slide_in_down,
                com.tokopedia.digital_deals.R.anim.slide_out_down, com.tokopedia.digital_deals.R.anim.slide_out_up);
        transaction.add(com.tokopedia.digital_deals.R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsResponse dealDetail, int flag) {
        this.dealDetail = dealDetail;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(com.tokopedia.digital_deals.R.id.parent_view, SelectDealQuantityFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(String text, String toolBarText, int flag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(TncBottomSheetFragment.TOOLBAR_TITLE, toolBarText);
        bundle.putString(TncBottomSheetFragment.TEXT, text);
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.slide_in_up, com.tokopedia.digital_deals.R.anim.slide_in_down,
                com.tokopedia.digital_deals.R.anim.slide_out_down, com.tokopedia.digital_deals.R.anim.slide_out_up);
        transaction.add(com.tokopedia.digital_deals.R.id.parent_view, TncBottomSheetFragment.createInstance(bundle));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public List<Outlet> getOutlets() {
        return this.outlets;
    }

    @Override
    public DealsDetailsResponse getDealDetails() {
        return dealDetail;
    }

}