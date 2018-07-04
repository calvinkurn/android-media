package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectDealQuantityFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

import static com.tokopedia.digital_deals.view.utils.Utils.Constants.DIGITAL_DEALS_DETAILS;

public class DealDetailsActivity extends BaseSimpleActivity implements DealFragmentCallbacks {

    private List<OutletViewModel> outlets;
    private DealsDetailsViewModel dealDetail;
    private BackCallback backCallback;
    @DeepLink({DIGITAL_DEALS_DETAILS})

    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, DealsHomeActivity.class));

        Uri.Builder uri = Uri.parse(deepLink).buildUpon();

        extras.putString(DealDetailsPresenter.HOME_DATA, extras.getString("slug"));
        destination = new Intent(context, DealDetailsActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {

        return DealDetailsFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        backCallback= (BackCallback) this;

    }

    @Override
    public void replaceFragment(List<OutletViewModel> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsViewModel dealDetail, int flag) {
        this.dealDetail = dealDetail;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, SelectDealQuantityFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public List<OutletViewModel> getOutlets() {
        return this.outlets;
    }

    @Override
    public DealsDetailsViewModel getDealDetails() {
        return dealDetail;
    }

//    @Override
//    public void onBackPressed() {
//        setResult(DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY, );
//        super.onBackPressed();
//    }

    public interface BackCallback {
        boolean getIsLiked();
    }
}