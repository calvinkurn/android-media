package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.BrandDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;

public class BrandDetailsActivity extends DealsBaseActivity {


    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS_BRAND})
    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((DealsModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, DealsHomeActivity.class));

        Uri.Builder uri = Uri.parse(deepLink).buildUpon();

        String brandSeoUrl=extras.getString("slug");
        Brand brand=new Brand();
        brand.setUrl(DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_BRAND+brandSeoUrl);
        extras.putParcelable(BrandDetailsPresenter.BRAND_DATA, brand);
        destination = new Intent(context, BrandDetailsActivity.class)
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
        return BrandDetailsFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);

    }
}