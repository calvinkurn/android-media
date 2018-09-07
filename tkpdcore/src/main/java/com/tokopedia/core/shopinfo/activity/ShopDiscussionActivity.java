package com.tokopedia.core.shopinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.shopinfo.fragment.ShopTalkFragment;

/**
 * Created by nathan on 3/5/18.
 */

public class ShopDiscussionActivity extends BaseSimpleActivity {
    public static final String APP_LINK_EXTRA_SHOP_ID = "shop_id";
    public static final String APP_LINK_EXTRA_SHOP_ATTRIBUTION = "tracker_attribution";
    private static final String SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION";

    public static final String SHOP_ID = "SHOP_ID";

    private String shopId;

    @DeepLink(ApplinkConst.SHOP_TALK)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopDiscussionActivity.class)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtras(extras);
    }

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopDiscussionActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(SHOP_ID);
        updateShopDiscussionIntent();
        super.onCreate(savedInstanceState);
    }

    /**
     * Old Discussion fragment need this intent, need updated code
     * com.tokopedia.core.shopinfo.presenter.ShopTalkPresenterImpl
     */
    @Deprecated
    private void updateShopDiscussionIntent() {
        getIntent().putExtra("shop_id", shopId);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopTalkFragment.createInstance();
    }
}
