package com.tokopedia.cod

import javax.inject.Inject

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodPresenter @Inject constructor() : CodContract.Presenter {

    lateinit var mView: CodContract.View

    override fun confirmPayment() {
        val dumb = "tokopedia://thankyou/marketplace/cod?bill_amount=301.096&gateway_code=COD&gateway_logo=cod.png&gateway_name=Cash+On+Delivery&payment_detail=Total+Tagihan%2CRp+320.596%3BBiaya+Layanan%2CRp+2.500%3BPenggunaan+Voucher%2C-+Rp+22.000&shipping_duration=2-4+Hari.&shipping_logo=cod.png&total_amount=301.096&transaction_id=300922467"
        mView.navigateToThankYouPage(dumb)
    }

    override fun setView(view: CodContract.View) {
        mView = view
    }
}