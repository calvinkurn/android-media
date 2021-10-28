package com.tokopedia.cart.old

import android.os.Bundle
import com.tokopedia.cart.old.view.CartFragment

class OldCartFragment : CartFragment() {

    companion object {
        fun newInstance(bundle: Bundle?, args: String): OldCartFragment {
            var tmpBundle = bundle
            if (tmpBundle == null) {
                tmpBundle = Bundle()
            }
            tmpBundle.putString(CartFragment::class.java.simpleName, args)
            val fragment = OldCartFragment()
            fragment.arguments = tmpBundle
            return fragment
        }
    }

    override fun isBundleToggleChanged(): Boolean {
        return false
    }
}