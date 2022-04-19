package com.tokopedia.digital_deals.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.RevampCheckoutDealsFragment;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.oms.scrooge.ScroogePGUtil;

import java.util.List;

public class CheckoutActivity extends DealsBaseActivity implements DealFragmentCallbacks {

    private List<Outlet> outlets;
    private final String LOCATION_FRAGMENT = "LOCATION_FRAGMENT";
    private final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private Drawable drawable;

    public static String EXTRA_DEALDETAIL = "EXTRA_DEALDETAIL";
    public static String EXTRA_CART = "EXTRA_CART";
    public static String EXTRA_VERIFY = "EXTRA_VERIFY";


    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_base_simple_deals_appbar;
    }

    @Override
    protected int getToolbarResourceID() {
        return com.tokopedia.digital_deals.R.id.toolbar_checkout;
    }

    @Override
    protected int getParentViewResourceID() {
        return com.tokopedia.digital_deals.R.id.deals_checkout_parent_view;
    }

    @Override
    protected Fragment getNewFragment() {
        drawable=toolbar.getNavigationIcon();
        updateTitle(getResources().getString(com.tokopedia.digital_deals.R.string.activity_checkout_title));

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        return RevampCheckoutDealsFragment.Companion.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE) {
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
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up);
        transaction.add(com.tokopedia.digital_deals.R.id.deals_checkout_parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
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
                            updateTitle(getResources().getString(com.tokopedia.digital_deals.R.string.redeem_locations));
                            toolbar.setNavigationIcon(com.tokopedia.digital_deals.R.drawable.ic_close_deals);

                        } else {
                            updateTitle(getResources().getString(com.tokopedia.digital_deals.R.string.activity_checkout_title));
                            toolbar.setNavigationIcon(drawable);
                        }
                    } else {
                        updateTitle(getResources().getString(com.tokopedia.digital_deals.R.string.activity_checkout_title));
                        toolbar.setNavigationIcon(drawable);

                    }
                }
            }
        };

        return result;
    }
}

