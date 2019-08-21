package com.tokopedia.tradein.view.viewcontrollers;

import android.arch.lifecycle.ViewModelProvider;
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
    protected int TRADEIN_TYPE = 1;

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
        super.onCreate(savedInstanceState);
        TRADEIN_TYPE = getIntent().getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, 1);
        if (TRADEIN_TYPE == 2) {
            toolbar.setTitle(R.string.money_in);
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
