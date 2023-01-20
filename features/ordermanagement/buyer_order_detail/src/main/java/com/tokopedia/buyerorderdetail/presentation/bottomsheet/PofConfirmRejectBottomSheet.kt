package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorderdetail.databinding.PofConfirmRejectBottomsheetBinding
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.PartialOrderFulfillmentActivity
import com.tokopedia.buyerorderdetail.presentation.viewmodel.PartialOrderFulfillmentViewModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PofConfirmRejectBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<PofConfirmRejectBottomsheetBinding>()

    private val orderId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(POF_REJECT_ORDER_ID_KEY)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PartialOrderFulfillmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PartialOrderFulfillmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PofConfirmRejectBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        showCloseIcon = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeRejectPartialOrderFulfillment()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    private fun setupViews() {
        binding?.run {
            ivPovConfirmReject.loadImage("")
            setBtnPrimaryCancellation()
            setBtnSecondaryBack()
        }
    }

    private fun setBtnPrimaryCancellation() {
        binding?.btnSecondaryBack?.setOnClickListener {
            dismiss()
        }
    }

    private fun setBtnSecondaryBack() {
        binding?.btnPrimaryCancellation?.setOnClickListener {
            orderId?.let {
                showBtnPrimaryLoading()
                viewModel.rejectPartialOrderFulfillment(it)
            }
        }
    }

    private fun showBtnPrimaryLoading() {
        binding?.btnPrimaryCancellation?.isLoading = true
    }

    private fun hideBtnPrimaryLoading() {
        binding?.btnPrimaryCancellation?.isLoading = false
    }

    private fun observeRejectPartialOrderFulfillment() {
        observe(viewModel.rejectPartialOrderFulfillment) {
            hideBtnPrimaryLoading()
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        dismiss()
                        (activity as? PartialOrderFulfillmentActivity)?.setResultFinish(
                            Activity.RESULT_OK
                        )
                    } else {
                        showToasterError()
                    }
                }
                is Fail -> {
                    showToasterError()
                }
            }
        }
    }

    private fun showToasterError() {
        val message =
            context?.getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_error_request)

        if (message?.isNotBlank() == true) {
            view?.run {
                Toaster.build(
                    view = this,
                    text = message,
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val POF_REJECT_ORDER_ID_KEY = "pof_reject_order_id_key"

        private val TAG = PofConfirmRejectBottomSheet::class.java.simpleName

        fun newInstance(orderId: String): PofConfirmRejectBottomSheet {
            return PofConfirmRejectBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(POF_REJECT_ORDER_ID_KEY, orderId)
                }
            }
        }
    }
}
