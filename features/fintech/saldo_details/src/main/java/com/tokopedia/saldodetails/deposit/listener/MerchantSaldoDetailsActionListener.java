package com.tokopedia.saldodetails.deposit.listener;

import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;

public interface MerchantSaldoDetailsActionListener {

    void hideSaldoPrioritasFragment();

    void showSaldoPrioritasFragment(GqlMerchantSaldoDetailsResponse.Details sellerDetails);

    void finishLoading();
}
