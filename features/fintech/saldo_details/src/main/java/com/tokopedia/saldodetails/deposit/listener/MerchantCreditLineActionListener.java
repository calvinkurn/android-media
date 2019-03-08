package com.tokopedia.saldodetails.deposit.listener;

import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;

public interface MerchantCreditLineActionListener {

    void hideMerchantCreditLineFragment();

    void showMerchantCreditLineFragment(GqlMerchantCreditResponse response);

}
