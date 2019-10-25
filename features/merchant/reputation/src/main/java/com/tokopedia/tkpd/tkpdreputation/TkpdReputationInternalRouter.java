package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopInfoFragment;


public class TkpdReputationInternalRouter {

    public static Intent getProductReviewIntent(Context context, String productId, String productName) {
        return ReviewProductActivity.createIntent(context, productId, productName);
    }

    public static Fragment getReviewShopFragment(String shopId, String shopDomain) {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    public static Fragment getReviewShopInfoFragment(String shopId, String shopDomain) {
        return ReviewShopInfoFragment.createInstance(shopId, shopDomain);
    }

    public static Intent getInboxReputationActivityIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
    }

}
