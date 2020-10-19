package com.tokopedia.tkpd.tkpdreputation.review.product.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/15/18.
 *
 * For navigating
 * use ApplinkConstInternalMarketplace.PRODUCT_REVIEW
 *
 */
public class ReviewProductActivity extends BaseSimpleActivity implements HasComponent<BaseAppComponent> {

    public static final String EXTRA_PRODUCT_NAME = "x_prd_nm";

    public static Intent createIntent(Context context, String productId, String productName) {
        Intent intent = new Intent(context, ReviewProductActivity.class);
        intent.putExtra(ReviewProductFragment.EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_PRODUCT_NAME, productName);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String productId;
        Uri uri = getIntent().getData();
        if (uri != null){
            List<String> segments = uri.getPathSegments();
            productId = segments.get(segments.size() - 2);
        } else {
            productId = getIntent().getExtras().getString(ReviewProductFragment.EXTRA_PRODUCT_ID);
        }

        return ReviewProductFragment.getInstance(productId);
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
}
