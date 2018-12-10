package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
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

    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS_DETAILS})

    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((DealsModuleRouter) context.getApplicationContext()).getHomeIntent(context);
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

    public static Intent getCallingIntent(Activity activity,
                                          DealDetailPassData passData) {
        Intent intent = new Intent(activity, DealDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DealDetailsPresenter.HOME_DATA, passData.getSlug());
        bundle.putParcelable(DealDetailsPresenter.PARAM_DEAL_PASSDATA, passData);
        intent.putExtras(bundle);
        return intent;
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

    }

    @Override
    public void replaceFragment(List<Outlet> outlets, int flag) {
        this.outlets = outlets;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, DealDetailsAllRedeemLocationsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(DealsDetailsResponse dealDetail, int flag) {
        this.dealDetail = dealDetail;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, SelectDealQuantityFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(String text, String toolBarText, int flag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(TncBottomSheetFragment.TOOLBAR_TITLE, toolBarText);
        bundle.putString(TncBottomSheetFragment.TEXT, text);
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, TncBottomSheetFragment.createInstance(bundle));
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