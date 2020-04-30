package com.tokopedia.vouchercreation.detail

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailActivity : BaseSimpleActivity() {

    override fun getScreenName(): String = this::class.java.simpleName

    override fun getNewFragment(): Fragment? {
        return VoucherDetailFragment.newInstance()
    }
}