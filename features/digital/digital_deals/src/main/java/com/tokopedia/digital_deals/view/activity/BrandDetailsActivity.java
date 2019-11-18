package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.BrandDetailsFragment;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;

import java.util.List;

public class BrandDetailsActivity extends DealsBaseActivity {
    String brandSeoUrl;


    public Bundle getInstanceIntentAppLinkBackToHome(Context context, String brandSeoUrl) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Brand brand = new Brand();
            brand.setUrl(DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_BRAND + brandSeoUrl);
            extras.putParcelable(BrandDetailsPresenter.BRAND_DATA, brand);

        }
        return extras;
    }

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_base_simple_deals;
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    protected int getParentViewResourceID(){
        return com.tokopedia.digital_deals.R.id.deals_home_parent_view;
    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        Uri uri = getIntent().getData();
        Bundle bundle = getIntent().getExtras();
        if (uri != null) {
            List<String> params = UriUtil.destructureUri(ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL, uri, true);
            brandSeoUrl = params.get(0);
            bundle = getInstanceIntentAppLinkBackToHome(this, brandSeoUrl);
        }
        return BrandDetailsFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}