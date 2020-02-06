package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import java.util.List;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoActivity extends BaseSimpleActivity {

    private static final String DEFAULT_SHOP_ID = "0";
    private String shopId;
    private String shopDomain;

    public static Intent createIntent(Context context, String shopId, String shopDomain){
        Intent intent = new Intent(context, ReviewShopInfoActivity.class);
        intent.putExtra(ReviewShopFragment.SHOP_ID, shopId);
        intent.putExtra(ReviewShopFragment.SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            setupOpenReviewShopInfo(getIntent().getData(), getIntent().getExtras());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    private void setupOpenReviewShopInfo(Uri intentData, Bundle intentExtras) {
        if(intentData != null && intentExtras != null) {
            List<String> pathSegments = intentData.getPathSegments();
            int pathSegmentSize = pathSegments.size();
            boolean isFromApplink = (pathSegmentSize > 1);
            if(isFromApplink) {
                shopId = pathSegments.get(pathSegmentSize - 2);
            } else {
                shopId = intentExtras.getString(ReviewShopFragment.SHOP_ID);
                shopDomain = intentExtras.getString(ReviewShopFragment.SHOP_DOMAIN);
            }
        } else {
            shopId = DEFAULT_SHOP_ID;
        }
    }
}
