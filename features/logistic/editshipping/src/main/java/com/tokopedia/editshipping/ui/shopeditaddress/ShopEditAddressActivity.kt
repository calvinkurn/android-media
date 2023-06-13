package com.tokopedia.editshipping.ui.shopeditaddress

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ShopEditAddressActivity : BaseSimpleActivity() {

    override fun getNewFragment(): ShopEditAddressFragment? {
        var fragment: ShopEditAddressFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = ShopEditAddressFragment.newInstance(bundle ?: Bundle())
        }
        return fragment
    }
}
