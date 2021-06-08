package com.tokopedia.otp.stub.verification.view.adapter

import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter

class VerificationMethodAdapterStub(
        listData: MutableList<ModeListData>,
        listener: ClickListener
) : VerificationMethodAdapter(listData, listener){
}