package com.tokopedia.pms.payment.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.view.fragment.PaymentListFragment;

public class PaymentListActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return PaymentListFragment.createInstance();
    }

    public static Intent createIntent(Activity context) {
        return new Intent(context, PaymentListActivity.class);
    }

    @DeepLink(Constant.PURCHASE_VERIFICATION)
    public static Intent getCallingIntentPurchaseVerification(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PaymentListActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }
}
