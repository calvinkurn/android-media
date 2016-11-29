package com.tokopedia.sellerapp.gmsubscribe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmsubscribe.view.GMSubscribeBlockFragment;
import com.tokopedia.sellerapp.gmsubscribe.view.GMSubscribeFragment;

/**
 * Created by sebastianuskh on 11/8/16.
 */

public class GMSubscribeActivity extends TActivity implements GMSubscribeFragment.GMSubscribeFragmentListener {

    private FragmentManager fragmentManager;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GM_SUBSCRIBE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_gm_subscribe, parentView);
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) ? new GMSubscribeBlockFragment() : new GMSubscribeFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void thankYouPageCount() {
        TrackingUtils.eventGoldMerchantSuccess();
    }
}
