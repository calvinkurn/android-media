package com.tokopedia.core.share;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.share.listener.ShareView;

public class ShareActivity extends TActivity implements ShareView {
    private ShareData data;

    public static Intent createIntent(Context context, ShareData shareData) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        return intent;
    }

    public static Intent getCallingRideIntent(Activity activity, ShareData shareData){
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
        if (getIntent() != null){
            Intent intent = getIntent();
            data = intent.getParcelableExtra(ShareData.TAG);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, ProductShareFragment.newInstance(data),
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
}
