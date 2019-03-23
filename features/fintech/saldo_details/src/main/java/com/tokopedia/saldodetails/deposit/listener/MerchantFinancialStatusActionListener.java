package com.tokopedia.saldodetails.deposit.listener;

import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;

public interface MerchantFinancialStatusActionListener {

    void hideMerchantCreditLineFragment();

    void showMerchantCreditLineFragment(GqlMerchantCreditResponse response);

    void hideSaldoPrioritasFragment();

    void hideUserFinancialStatusLayout();

    void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails);
}
