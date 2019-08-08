package com.tokopedia.tradein.view.viewcontrollers;

import android.arch.lifecycle.ViewModelProvider;
import android.view.MenuItem;

import com.tokopedia.tradein.R;
import tradein_common.TradeInUtils;
import com.tokopedia.tradein.viewmodel.TradeInVMFactory;
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity;

import java.util.Map;


public abstract class BaseTradeInActivity extends BaseViewModelActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    public static final int TRADEIN_HOME_REQUEST = 22345;
    public static final int APP_SETTINGS = 9988;

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

    protected String getDeviceId() {
        return TradeInUtils.getDeviceId(this);
    }

    @Override
    protected ViewModelProvider.NewInstanceFactory getVMFactory() {
        return TradeInVMFactory.getInstance(this);
    }
}
