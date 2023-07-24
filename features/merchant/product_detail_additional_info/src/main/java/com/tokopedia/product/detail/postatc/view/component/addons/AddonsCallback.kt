package com.tokopedia.product.detail.postatc.view.component.addons

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
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

    private var latestInfo = ""
    override fun onLoadingSaveAddons() {
        val fragment = fragment ?: return
        fragment.footer?.apply {
            val context = fragment.context ?: return
            val loadingText = context.getString(R.string.pdp_post_atc_footer_info_loading)
            postAtcFooterInfo.text = loadingText
            val transition = AutoTransition()
            transition.duration = 150
            transition.interpolator = AccelerateDecelerateInterpolator()
            TransitionManager.beginDelayedTransition(root, transition)
            postAtcFooterInfo.show()
        }
    }

    override fun onSuccessSaveAddons(itemCount: Int) {
        val fragment = fragment ?: return
        fragment.footer?.apply {
            val transition = AutoTransition()
            transition.duration = 150
            transition.interpolator = AccelerateDecelerateInterpolator()
            TransitionManager.beginDelayedTransition(root, transition)
            if (itemCount == 0) {
                latestInfo = ""
                postAtcFooterInfo.hide()
            } else {
                val context = fragment.context ?: return
                context.getString(
                    R.string.pdp_post_atc_footer_info_added,
                    itemCount.toString()
                ).let {
                    latestInfo = it
                    postAtcFooterInfo.text = it
                }
                postAtcFooterInfo.show()
            }
        }
    }

    override fun onFailedSaveAddons(message: String) {
        val fragment = fragment ?: return
        val latestInfo = latestInfo
        fragment.footer?.postAtcFooterInfo?.showIfWithBlock(latestInfo.isNotEmpty()) {
            text = latestInfo
        }
        val view = fragment.binding?.root?.rootView ?: return
        Toaster.build(view, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }
}
