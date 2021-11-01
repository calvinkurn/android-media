package com.tokopedia.attachproduct.fake.view

import androidx.fragment.app.Fragment
import com.tokopedia.attachproduct.view.activity.AttachProductActivity

class AttachProductTestActivity : AttachProductActivity() {
    override fun getNewFragment(): Fragment {
        return super.getNewFragment()
    }
}