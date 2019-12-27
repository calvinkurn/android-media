package com.tokopedia.saldodetails.deposit.listener

import com.tokopedia.saldodetails.response.model.GqlDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse

interface MerchantFinancialStatusActionListener {

    fun hideMerchantCreditLineFragment()

    fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse)

    fun hideSaldoPrioritasFragment()

    fun hideUserFinancialStatusLayout()

    fun showSaldoPrioritasFragment(sellerDetails: GqlDetailsResponse)
}
