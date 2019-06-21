package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.qrpayment.presentation.contract.SuccessQrPaymentContract;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/22/18.
 */

public class SuccessQrPaymentPresenter extends BaseDaggerPresenter<SuccessQrPaymentContract.View>
        implements SuccessQrPaymentContract.Presenter {

    @Inject
    public SuccessQrPaymentPresenter() {
    }

    @Override
    public void deleteCacheTokoCashBalance() {
        PersistentCacheManager.instance.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }
}
