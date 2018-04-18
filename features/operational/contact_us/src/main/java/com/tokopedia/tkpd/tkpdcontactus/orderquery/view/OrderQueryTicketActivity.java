package com.tokopedia.tkpd.tkpdcontactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment.OrderQueryTicketFragment;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class OrderQueryTicketActivity extends BaseSimpleActivity {
    public static final String KEY_BUYER_PURCHASE_LIST = "BUYER_PURCHASE_LIST";
    private FragmentManager mFragmentManager;

    public static Intent getOrderQueryTicketIntent(Context context, BuyerPurchaseList buyerPurchaseList) {
        Intent intent = new Intent(context,OrderQueryTicketActivity.class);
        intent.putExtra(KEY_BUYER_PURCHASE_LIST,buyerPurchaseList);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return OrderQueryTicketFragment.newInstance((BuyerPurchaseList) getIntent().getSerializableExtra(KEY_BUYER_PURCHASE_LIST));
    }

}
