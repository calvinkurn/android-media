package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsManagementRouter;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsCheckProductPromoFragment;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckProductPromoActivity extends BaseSimpleActivity implements HasComponent<TopAdsComponent>{
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_SOURCE = "source";

    private String shopId;
    private String itemId;
    private String source;

    @DeepLink(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            String userId = extras.getString(PARAM_USER_ID, "");
            if (!TextUtils.isEmpty(userId)) {
                if (SessionHandler.getLoginID(context).equalsIgnoreCase(userId)) {
                    Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                    return getCallingIntent(context)
                            .setData(uri.build())
                            .putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID,
                                    uri.build().getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID))
                            .putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID,
                                    uri.build().getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID))
                            .putExtra(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                    uri.build().getQueryParameter(PARAM_SOURCE))
                            .putExtras(extras);
                } else {
                    return ((TopAdsManagementRouter)context.getApplicationContext()).getTopAdsDashboardIntent(context)
                            .putExtras(extras);
                }
            } else {
                Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                return getCallingIntent(context)
                        .setData(uri.build())
                        .putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, uri.build().getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID))
                        .putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID,
                                uri.build().getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID))
                        .putExtra(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                uri.build().getQueryParameter(PARAM_SOURCE))
                        .putExtras(extras);
            }
        } else {
            Intent launchIntent = ApplinkUtils.getSellerAppApplinkIntent(context, extras);
            Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
            String itemId = uri.getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID);
            launchIntent.putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, itemId)
                    .putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID,
                            uri.getQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID))
                    .putExtra(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                            uri.getQueryParameter(PARAM_SOURCE));
            return launchIntent;
        }
    }

    public static Intent createIntent(Context context, String shopId, String itemId){
        Intent intent = getCallingIntent(context);
        intent.putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopId);
        intent.putExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, itemId);
        return intent;
    }

    public static Intent createIntent(Context context, String shopId, String itemId, String source){
        Intent intent = createIntent(context, shopId, itemId);
        intent.putExtra(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE, source);
        return intent;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsCheckProductPromoActivity.class);
    }

    private void initFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            shopId = getIntent().getStringExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID);
            itemId = getIntent().getStringExtra(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID);
            source = getIntent().getStringExtra(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        initFromIntent();
        return TopAdsCheckProductPromoFragment.createInstance(shopId, itemId, source);
    }

    @Override
    protected String getTagFragment() {
        return TopAdsCheckProductPromoFragment.class.getSimpleName();
    }

    @Override
    public TopAdsComponent getComponent() {
        return TopAdsComponentInstance.getComponent(getApplication());
    }
}
