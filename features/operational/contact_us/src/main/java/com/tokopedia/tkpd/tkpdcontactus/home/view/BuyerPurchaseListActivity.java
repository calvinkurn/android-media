package com.tokopedia.tkpd.tkpdcontactus.home.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.tkpdcontactus.home.view.fragment.BuyerPurchaseFragment;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public class BuyerPurchaseListActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return BuyerPurchaseFragment.newInstance();
    }

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context,BuyerPurchaseListActivity.class);
        return intent;
    }
}
