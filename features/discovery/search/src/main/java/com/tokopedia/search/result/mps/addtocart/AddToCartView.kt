package com.tokopedia.search.result.mps.addtocart

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.State
import com.tokopedia.search.R
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.mvvm.RefreshableView
import com.tokopedia.unifycomponents.Toaster

class AddToCartView(
    private val viewModel: AddToCartViewModel?,
    context: Context?,
    private val rootView: View?,
): ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    RefreshableView<AddToCartState> {

    override fun refresh(state: AddToCartState) {
        val rootView = rootView ?: return
        val addToCartState = state.state ?: return

        val isSuccess = addToCartState is State.Success
        val message = getAddToCartStateMessage(addToCartState)
        val actionText = getAddToCartActionText(isSuccess)

        Toaster.build(
            rootView,
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
        if (isSuccess) context?.resources?.getString(R.string.search_see_cart) ?: "" else ""

    private fun addToCartToasterAction(isSuccess: Boolean) {
        if (isSuccess) openApplink(context, ApplinkConst.CART)
    }

    private fun addToCartToasterCallback() = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            viewModel?.onAddToCartMessageDismissed()
        }
    }
}
