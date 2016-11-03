package com.tokopedia.tkpd.share;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.product.model.share.ShareData;
import com.tokopedia.tkpd.share.fragment.ProductShareFragment;
import com.tokopedia.tkpd.share.listener.ShareView;

public class ShareActivity extends TActivity implements ShareView {
    private ShareData data;

    public static Intent createIntent(Context context, ShareData shareData) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        return intent;
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
        fragmentTransaction.addToBackStack(null);
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
