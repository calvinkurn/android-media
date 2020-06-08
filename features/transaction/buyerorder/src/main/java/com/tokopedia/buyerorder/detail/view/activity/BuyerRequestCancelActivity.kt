package com.tokopedia.buyerorder.detail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.buyerorder.detail.view.fragment.BuyerRequestCancelFragment

/**
 * Created by fwidjaja on 08/06/20.
 */
class BuyerRequestCancelActivity: BaseSimpleActivity() {
    override fun getNewFragment(): BuyerRequestCancelFragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        }
        return BuyerRequestCancelFragment.newInstance(bundle)
    }
}