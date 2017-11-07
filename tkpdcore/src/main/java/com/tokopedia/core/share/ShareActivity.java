package com.tokopedia.core.share;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.share.fragment.ProductShareFragment;

public class ShareActivity extends TActivity {
    private ShareData data;
    public static Intent createIntent(Context context, ShareData shareData) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        return intent;
    }

    public static Intent createIntent(Context context, ShareData shareData,boolean isAddingProduct) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        intent.putExtra(ProductDetailRouter.IS_ADDING_PRODUCT,isAddingProduct);
        return intent;
    }

    public static Intent getCallingRideIntent(Context activity, ShareData shareData) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHARE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_share);
        boolean isAddingProduct=false;
        if (getIntent() != null) {
            Intent intent = getIntent();
            data = intent.getParcelableExtra(ShareData.TAG);
            isAddingProduct = intent.getBooleanExtra(ProductDetailRouter.IS_ADDING_PRODUCT,false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, ProductShareFragment.newInstance(data,isAddingProduct),
                ProductShareFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
