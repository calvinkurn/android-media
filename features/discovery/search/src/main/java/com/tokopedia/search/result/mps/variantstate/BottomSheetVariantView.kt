package com.tokopedia.search.result.mps.variantstate

import android.content.Context
import com.tokopedia.search.result.product.addtocart.AddToCartVariantBottomSheetLauncher
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.mvvm.RefreshableView

class BottomSheetVariantView(
    private val mpsViewModel: BottomSheetVariantViewModel?,
    context: Context?,
    private val atcVariantBottomSheetLauncher: AddToCartVariantBottomSheetLauncher,
) : RefreshableView<BottomSheetVariantState>,
    ContextProvider by WeakReferenceContextProvider(context) {
    override fun refresh(state: BottomSheetVariantState) {
        if (state.isOpen) openVariantBottomSheet(state)
        else mpsViewModel?.onBottomSheetVariantDismissed()
    }

    private fun openVariantBottomSheet(variantModel : BottomSheetVariantState) {
        if(variantModel.variantModel == null) return
        atcVariantBottomSheetLauncher.launch(
            productId = variantModel.variantModel.productId,
            shopId = variantModel.variantModel.shopId,
            trackerCDListName = variantModel.variantModel.trackerCDListName,
            onCheckout = {
                mpsViewModel?.onBottomSheetVariantDismissed()
            },
            onDismiss = {
                mpsViewModel?.onBottomSheetVariantDismissed()
            }
        )
    }
}
