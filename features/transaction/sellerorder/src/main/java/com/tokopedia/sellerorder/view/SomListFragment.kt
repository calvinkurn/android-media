package com.tokopedia.sellerorder.view

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment() {

    companion object {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(): SomListFragment {
            return SomListFragment()
        }
    }

    override fun getScreenName(): String {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return ""
    }

    override fun initInjector() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}