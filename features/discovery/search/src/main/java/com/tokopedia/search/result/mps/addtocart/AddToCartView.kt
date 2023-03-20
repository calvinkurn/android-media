package com.tokopedia.search.result.mps.addtocart

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.State
import com.tokopedia.search.R
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.unifycomponents.Toaster

class AddToCartView(
    private val context: () -> Context?,
    private val viewModel: () -> MPSViewModel?,
): ApplinkOpener by ApplinkOpenerDelegate {

    fun refreshAddToCartToaster(view: View?, addToCartState: State<AddToCartDataModel>?) {
        view ?: return
        addToCartState ?: return

        val isSuccess = addToCartState is State.Success
        val message = getAddToCartStateMessage(addToCartState)
        val actionText = getAddToCartActionText(isSuccess)

        Toaster.build(
            view,
            message,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_NORMAL,
            actionText,
        ) {
            addToCartToasterAction(isSuccess)
        }.apply {
            addCallback(addToCartToasterCallback())
        }.show()
    }

    private fun getAddToCartStateMessage(addToCartState: State<AddToCartDataModel>) =
        when (addToCartState) {
            is State.Success -> addToCartState.data?.data?.message?.firstOrNull() ?: ""
            is State.Error -> addToCartState.message
            else -> ""
        }

    private fun getAddToCartActionText(isSuccess: Boolean): String =
        if (isSuccess) context()?.resources?.getString(R.string.search_see_cart) ?: "" else ""

    private fun addToCartToasterAction(isSuccess: Boolean) {
        if (isSuccess) openApplink(context(), ApplinkConst.CART)
    }

    private fun addToCartToasterCallback() = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            viewModel()?.onAddToCartMessageDismissed()
        }
    }
}
