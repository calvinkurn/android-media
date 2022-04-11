package com.tokopedia.attachvoucher.stub.view

import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.test.base.AttachVoucherTest
import com.tokopedia.attachvoucher.view.activity.AttachVoucherActivity

class AttachVoucherActivityStub : AttachVoucherActivity() {

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getComponent(): AttachVoucherComponent {
        return AttachVoucherTest.attachVoucherComponentStub!!
    }

    override fun getTagFragment(): String {
        return TAG_STUB_FRAGMENT
    }

    companion object {
        const val TAG_STUB_FRAGMENT = "TAG_STUB_FRAGMENT"
    }
}