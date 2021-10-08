package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtension
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.dialog.OrderExtensionDialog
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.dialog.DialogUnify
import javax.inject.Inject

class BuyerOrderExtensionFragment : BaseDaggerFragment() {

    @Inject
    lateinit var buyerOrderDetailExtensionViewModel: BuyerOrderDetailExtensionViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeRespondInfo()
    }

    private fun observeRespondInfo() {
        showOrderHasBeenDialog()
    }

    private fun showOrderHasBeenDialog() {
        val confirmedCancelledOrderDialog = context?.let {
            OrderExtensionDialog(
                it,
                DialogUnify.WITH_ILLUSTRATION
            ).apply {
                setTitle(getString(R.string.order_extension_title_order_has_been_sent))
                setDescription(getString(R.string.order_extension_desc_order_has_been_sent))
                setImageUrl(BuyerOrderDetailOrderExtension.Image.ORDER_HAS_BEEN_SENT_URL)
            }
        }
        confirmedCancelledOrderDialog?.getDialog()?.run {
            setPrimaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): BuyerOrderExtensionFragment {
            return BuyerOrderExtensionFragment().apply {

            }
        }
    }
}