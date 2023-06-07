package com.tokopedia.product.detail.postatc.view.component.addons

import com.google.android.material.snackbar.Snackbar
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.postatc.base.BaseCallbackImpl
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.unifycomponents.Toaster

interface AddonsCallback {
    fun onLoadingSaveAddons()
    fun onSuccessSaveAddons(itemCount: Int)
    fun onFailedSaveAddons(message: String)
}

class AddonsCallbackImpl(
    fragment: PostAtcBottomSheet
) : BaseCallbackImpl(fragment), AddonsCallback {
    override fun onLoadingSaveAddons() {
        val fragment = fragment ?: return
        fragment.binding?.apply {
            val context = fragment.context ?: return
            postAtcFooterInfo.text = context.getString(R.string.pdp_post_atc_footer_info_loading)
        }
    }

    override fun onSuccessSaveAddons(itemCount: Int) {
        val fragment = fragment ?: return
        fragment.binding?.apply {
            val context = fragment.context ?: return
            postAtcFooterInfo.text = context.getString(R.string.pdp_post_atc_footer_info_added, "1")
        }
    }

    override fun onFailedSaveAddons(message: String) {
        val view = fragment?.view ?: return
        Toaster.make(view, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
    }
}
