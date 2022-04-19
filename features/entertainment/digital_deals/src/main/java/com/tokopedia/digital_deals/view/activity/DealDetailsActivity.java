package com.tokopedia.digital_deals.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.RevampSelecDealsQuantityFragment;
import com.tokopedia.digital_deals.view.fragment.TncBottomSheetFragment;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;

import java.util.List;

public class DealDetailsActivity extends DealsBaseActivity implements DealFragmentCallbacks {

    private List<Outlet> outlets;
    private DealsDetailsResponse dealDetail;
    private String slug;

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_base_simple_deals;
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    protected int getParentViewResourceID() {
        return com.tokopedia.digital_deals.R.id.deals_home_parent_view;
    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        Uri uri = getIntent().getData();
        Bundle extras = getIntent().getExtras();
        if (uri != null) {
            List<String> params = UriUtil.destructureUri(ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG, uri, true);
            slug = params.get(0);
            if (extras == null) {
                extras = new Bundle();
                extras.putString(DealDetailsPresenter.HOME_DATA, slug);
            } else {
                extras.putString(DealDetailsPresenter.HOME_DATA, slug);
            }
        }
        return DealDetailsFragment.createInstance(extras);
}


    @Override
    public void replaceFragment(List<Outlet> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up);
        transaction.add(com.tokopedia.digital_deals.R.id.deals_home_parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsResponse dealDetail, int flag) {
        this.dealDetail = dealDetail;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(com.tokopedia.digital_deals.R.id.deals_home_parent_view, RevampSelecDealsQuantityFragment.Companion.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(String text, String toolBarText, int flag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(TncBottomSheetFragment.TOOLBAR_TITLE, toolBarText);
        bundle.putString(TncBottomSheetFragment.TEXT, text);
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up);
        transaction.add(com.tokopedia.digital_deals.R.id.deals_home_parent_view, TncBottomSheetFragment.createInstance(bundle));
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