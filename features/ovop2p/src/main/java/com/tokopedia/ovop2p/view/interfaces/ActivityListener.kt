package com.tokopedia.ovop2p.view.interfaces

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

interface ActivityListener {
    fun addReplaceFragment(baseDaggerFragment: BaseDaggerFragment, replace: Boolean, tag: String)
}
