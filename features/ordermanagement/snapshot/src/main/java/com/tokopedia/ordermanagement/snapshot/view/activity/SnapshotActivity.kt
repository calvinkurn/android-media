package com.tokopedia.ordermanagement.snapshot.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.ordermanagement.snapshot.view.fragment.SnapshotFragment

class SnapshotActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return SnapshotFragment.newInstance()
    }
}