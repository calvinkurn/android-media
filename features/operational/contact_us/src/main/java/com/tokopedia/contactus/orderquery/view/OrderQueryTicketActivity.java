package com.tokopedia.contactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.orderquery.view.broadcastreceiver.FinishActivityReceiver;
import com.tokopedia.contactus.orderquery.view.fragment.OrderQueryTicketFragment;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class OrderQueryTicketActivity extends BaseSimpleActivity {
    public static final String KEY_BUYER_PURCHASE_LIST = "BUYER_PURCHASE_LIST";
    private FragmentManager mFragmentManager;
    private FinishActivityReceiver finishReceiver = new FinishActivityReceiver(this);

    public static Intent getOrderQueryTicketIntent(Context context, BuyerPurchaseList buyerPurchaseList) {
        Intent intent = new Intent(context, OrderQueryTicketActivity.class);
        intent.putExtra(KEY_BUYER_PURCHASE_LIST, buyerPurchaseList);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return OrderQueryTicketFragment.newInstance((BuyerPurchaseList) getIntent().getSerializableExtra(KEY_BUYER_PURCHASE_LIST));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ContactUsConstant.ACTION_CLOSE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver);
    }
}