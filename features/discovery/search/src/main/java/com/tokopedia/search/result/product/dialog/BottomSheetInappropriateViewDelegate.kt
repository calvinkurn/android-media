package com.tokopedia.search.result.product.dialog

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.search.R
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class BottomSheetInappropriateViewDelegate @Inject constructor(
    @SearchContext
    context: Context,
    fragmentProvider: FragmentProvider,
    private val viewUpdater: ViewUpdater,
) : BottomSheetInappropriateView,
    ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider{

    override fun openInappropriateWarningBottomSheet(
        isAdult: Boolean,
        onButtonConfirmationClicked: () -> Unit,
    ) {
        val descMessageInappropriate = generateDescMessageInappropriate(isAdult)
        val bottomSheet = BottomSheetInappropriate.newInstance(isAdult, descMessageInappropriate)
        bottomSheet.setOnButtonConfirmationClicked {
            onButtonConfirmationClicked.invoke()
            showMessageSuccessInactiveSafeProduct()
            viewUpdater.unBlurItem()
        }
        bottomSheet.show(
            getFragment().parentFragmentManager,
            bottomSheet.tag
        )
    }

    private fun generateDescMessageInappropriate(isAdult: Boolean): String {
        return if(isAdult) {
            context?.getString(R.string.search_result_product_inappropriate_description_twenty_one)
        } else {
            context?.getString(R.string.search_result_product_inappropriate_description_under_twenty_one)
        }.orEmpty()
    }

    private fun showMessageSuccessInactiveSafeProduct() {
        val view = getFragment().view ?: return
        val message = view.context.getString(R.string.search_result_product_inappropriate_success_inactive)
        view.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }
}
