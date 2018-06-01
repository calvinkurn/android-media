package com.tokopedia.digital_deals.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.oms.scrooge.ScroogePGUtil;

public class CheckoutActivity extends BaseSimpleActivity {


    @Override
    protected Fragment getNewFragment() {
        return CheckoutHomeFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String orderId = Utils.fetchOrderId(data.getStringExtra(ScroogePGUtil.SUCCESS_MSG_URL));
        Intent intent = ((TkpdCoreRouter) getApplication())
                .getOrderListDetailActivity(this, "RIDE", orderId);
        this.startActivity(intent);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

