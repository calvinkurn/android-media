package com.tokopedia.attachproduct.stub.view

import androidx.fragment.app.Fragment
import com.tokopedia.attachproduct.stub.view.fragment.AttachProductTestFragment
import com.tokopedia.attachproduct.view.activity.AttachProductActivity

class AttachProductTestActivity : AttachProductActivity() {

    override fun getNewFragment(): Fragment {
        return AttachProductTestFragment.newInstance(
            this, isSeller, source, maxChecked, hiddenProducts,
            warehouseId, shopId
        )
    }

}