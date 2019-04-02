package com.tokopedia.digital_deals.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.oms.scrooge.ScroogePGUtil;

import java.util.List;

public class CheckoutActivity extends DealsBaseActivity implements DealFragmentCallbacks {

    private List<Outlet> outlets;
    private final String LOCATION_FRAGMENT = "LOCATION_FRAGMENT";
    private final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private Drawable drawable;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals_appbar;
    }

    @Override
    protected Fragment getNewFragment() {
        drawable=toolbar.getNavigationIcon();
        updateTitle(getResources().getString(R.string.activity_checkout_title));

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        return CheckoutHomeFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE) {
                String orderId = Utils.fetchOrderId(data.getStringExtra(ScroogePGUtil.SUCCESS_MSG_URL));
                String url = data.getStringExtra(ScroogePGUtil.SUCCESS_MSG_URL) + "?from_payment=true" ;
                RouteManager.route(this, url);
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void replaceFragment(List<Outlet> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(LOCATION_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsResponse detailsViewModel, int flag) {

    }

    @Override
    public void replaceFragment(String text, String toolBarText, int flag) {

    }

    @Override
    public List<Outlet> getOutlets() {
        return outlets;
    }

    @Override
    public DealsDetailsResponse getDealDetails() {
        return null;
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    if (manager.getBackStackEntryCount() >= 1) {
                        String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        if (topOnStack.equals(LOCATION_FRAGMENT)) {
                            updateTitle(getResources().getString(R.string.redeem_locations));
                            toolbar.setNavigationIcon(R.drawable.ic_close_deals);

                        } else {
                            updateTitle(getResources().getString(R.string.activity_checkout_title));
                            toolbar.setNavigationIcon(drawable);
                        }
                    } else {
                        updateTitle(getResources().getString(R.string.activity_checkout_title));
                        toolbar.setNavigationIcon(drawable);

                    }
                }
            }
        };

        return result;
    }
}

