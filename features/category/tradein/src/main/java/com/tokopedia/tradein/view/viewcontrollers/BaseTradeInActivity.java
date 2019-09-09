package com.tokopedia.tradein.view.viewcontrollers;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.viewmodel.TradeInVMFactory;
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity;

import tradein_common.TradeInUtils;


public abstract class BaseTradeInActivity extends BaseViewModelActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    public static final int TRADEIN_HOME_REQUEST = 22345;
    public static final int APP_SETTINGS = 9988;
    public static final int TRADEIN_OFFLINE = 0;
    public static final int TRADEIN_ONLINE = 1;
    public static final int TRADEIN_MONEYIN = 2;
    public static final String TRADEIN_EXCHANGE = "exchange";
    public static final String TRADEIN_MONEY_IN = "money-in";
    protected String TRADEIN_TEST_TYPE = TRADEIN_EXCHANGE;
    protected int TRADEIN_TYPE = TRADEIN_OFFLINE;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_show_tnc) {
            sendGeneralEvent("clickTradeIn",
                    "trade in start page",
                    "click icon syarat dan ketentuan",
                    "");
            showTnC(R.string.tradein_tnc);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null && uri.toString().contains("money_in/device_validation")) {
            TRADEIN_TYPE = TRADEIN_MONEYIN;
        } else {
            if (intent.hasExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE))
                TRADEIN_TYPE = intent.getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, TRADEIN_OFFLINE);
        }
        super.onCreate(savedInstanceState);
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            toolbar.setTitle(R.string.money_in);
            TRADEIN_TEST_TYPE = TRADEIN_MONEY_IN;
        }
        setSupportActionBar(toolbar);
    }

    protected String getDeviceId() {
        return TradeInUtils.getDeviceId(this);
    }

    @Override
    protected ViewModelProvider.AndroidViewModelFactory getVMFactory() {
        return TradeInVMFactory.getInstance(this.getApplication(), getIntent());
    }
}
