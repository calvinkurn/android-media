package com.tokopedia.core.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.linker.model.LinkerData;

public class ShareActivity extends TActivity {
    private LinkerData data;
    public static Intent createIntent(Context context, LinkerData shareData) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(LinkerData.TAG, shareData);
        return intent;
    }

    public static Intent createIntent(Context context, LinkerData shareData,boolean isAddingProduct) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(LinkerData.TAG, shareData);
        intent.putExtra(ProductDetailRouter.IS_ADDING_PRODUCT,isAddingProduct);
        return intent;
    }

    public static Intent getCallingRideIntent(Context activity, LinkerData shareData) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra(LinkerData.TAG, shareData);
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
            data = intent.getParcelableExtra(LinkerData.TAG);
            isAddingProduct = intent.getBooleanExtra(ProductDetailRouter.IS_ADDING_PRODUCT,false);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
