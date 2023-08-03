package com.tokopedia.common.topupbills.view.bottomsheet.callback

import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute

interface AddSmartBillsInquiryCallBack {
    fun onInquiryClicked(attributes: TopupBillsEnquiryAttribute)
    fun onInquiryClose()
}
