package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoActivity extends BaseSimpleActivity {
    public static final String APP_LINK_EXTRA_SHOP_ID = "shop_id";
    public static final String APP_LINK_EXTRA_SHOP_ATTRIBUTION = "tracker_attribution";
    private static final String SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION";

    private String shopId;
    private String shopDomain;

    @DeepLink(ApplinkConst.SHOP_REVIEW)
    public static Intent getCallingIntent(Context context, Bundle extras){
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ReviewShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(ReviewShopFragment.SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtras(extras);
    }

    public static Intent createIntent(Context context, String shopId, String shopDomain){
        Intent intent = new Intent(context, ReviewShopInfoActivity.class);
        intent.putExtra(ReviewShopFragment.SHOP_ID, shopId);
        intent.putExtra(ReviewShopFragment.SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            shopId = getIntent().getStringExtra(ReviewShopFragment.SHOP_ID);
            shopDomain = getIntent().getStringExtra(ReviewShopFragment.SHOP_DOMAIN);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }
}
