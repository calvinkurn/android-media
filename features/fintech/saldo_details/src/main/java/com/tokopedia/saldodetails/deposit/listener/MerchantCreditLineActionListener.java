package com.tokopedia.saldodetails.deposit.listener;

import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse;

public interface MerchantCreditLineActionListener {

    void hideMerchantCreditLineFragment();

    void showMerchantCreditLineFragment(GqlMerchantCreditDetailsResponse response);

}
