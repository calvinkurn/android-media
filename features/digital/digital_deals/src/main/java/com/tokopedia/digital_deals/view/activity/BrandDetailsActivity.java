package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.BrandDetailsFragment;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;

public class BrandDetailsActivity extends DealsBaseActivity {


    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS_BRAND})
    public static Intent getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        Intent destination = new Intent();
        if (extras != null) {
            String deepLink = extras.getString(DeepLink.URI);
            Uri.Builder uri = Uri.parse(deepLink).buildUpon();


            String brandSeoUrl = extras.getString("slug");
            Brand brand = new Brand();
            brand.setUrl(DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_BRAND + brandSeoUrl);
            extras.putParcelable(BrandDetailsPresenter.BRAND_DATA, brand);
            destination = new Intent(context, BrandDetailsActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

        }
        return destination;
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