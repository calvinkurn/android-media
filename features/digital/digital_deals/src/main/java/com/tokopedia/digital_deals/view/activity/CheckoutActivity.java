package com.tokopedia.digital_deals.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;
import com.tokopedia.oms.scrooge.ScroogePGUtil;

import java.util.List;

public class CheckoutActivity extends BaseSimpleActivity implements DealFragmentCallbacks {

    private List<OutletViewModel> outlets;
    private final String LOCATION_FRAGMENT = "LOCATION_FRAGMENT";
    private final String HOME_FRAGMENT = "HOME_FRAGMENT";


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        toolbar.setTitle(getResources().getString(R.string.activity_checkout_title));
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
                Intent intent = ((TkpdCoreRouter) getApplication())
                        .getOrderListDetailActivity(this, orderId, "DEALS");
                this.startActivity(intent);
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void replaceFragment(List<OutletViewModel> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(LOCATION_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsViewModel detailsViewModel, int flag) {

    }

    @Override
    public List<OutletViewModel> getOutlets() {
        return outlets;
    }

    @Override
    public DealsDetailsViewModel getDealDetails() {
        return null;
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    if (manager.getBackStackEntryCount() >= 1) {
                        String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        if (topOnStack.equals(LOCATION_FRAGMENT))
                            toolbar.setTitle(getResources().getString(R.string.redeem_locations));
                        else
                            toolbar.setTitle(getResources().getString(R.string.activity_checkout_title));
                    } else {
                        toolbar.setTitle(getResources().getString(R.string.activity_checkout_title));
                    }
                }
            }
        };

        return result;
    }
}

