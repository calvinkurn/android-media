package com.tokopedia.logisticseller.ui.findingnewdriver.bottomsheet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.FindingNewDriverModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject
import com.tokopedia.logisticseller.databinding.BottomsheetFindingNewDriverBinding
import com.tokopedia.logisticseller.di.findingnewdriver.DaggerFindingNewDriverComponent
import com.tokopedia.logisticseller.di.findingnewdriver.FindingNewDriverComponent
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverAvailabilityState
import com.tokopedia.logisticseller.ui.findingnewdriver.viewmodel.FindingNewDriverViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import java.util.*

class FindingNewDriverBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<BottomsheetFindingNewDriverBinding>()

    private var orderId: String = ""

    private var mListener: FindingNewDriverListener? = null

    private val viewModel: FindingNewDriverViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FindingNewDriverViewModel::class.java)
    }

    interface FindingNewDriverListener {
        fun onClickFindNewDriver()

        fun onFailedGetNewDriverAvailability()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setChild()
    }

    private fun initInjector() {
        val component: FindingNewDriverComponent = DaggerFindingNewDriverComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    private fun setChild() {
        binding = BottomsheetFindingNewDriverBinding.inflate(LayoutInflater.from(context))
        setTitle(requireActivity().getString(R.string.label_finding_new_driver))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObserver()
        setupBtnClickListener()
        viewModel.getNewDriverAvailability(orderId)
    }

    private fun initObserver() {
        viewModel.newDriverAvailability.observe(viewLifecycleOwner) {
            when (it) {
                is NewDriverAvailabilityState.Success -> onSuccessGetNewDriverAvailability(it.data)
                is NewDriverAvailabilityState.Fail -> mListener?.onFailedGetNewDriverAvailability()
                is NewDriverAvailabilityState.Loading -> showLoadingView()
            }
        }
    }

    private fun setupBtnClickListener() {
        with(binding) {
            btnFinding.setOnClickListener {
                mListener?.onClickFindNewDriver()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun onSuccessGetNewDriverAvailability(
        data: FindingNewDriverModel
    ) {
        with(binding) {
            loaderView.gone()
            tusCountdown.gone()
            grupBtnAndInvoice.visible()
            btnFinding.isEnabled = data.isEnable
            tvInvoiceNumber.text = context?.getString(R.string.title_number_invoice, data.invoice)
            icCopy.setOnClickListener {
                copyToClipboard(data.invoice)
            }

            if (data.isEnable) {
                tvDescription.visible()
                tvDescription.text = data.message
            } else {
                tvDescription.gone()
                data.calendar?.let { calendar ->
                    renderTimer(data.message.toSpannedString(), calendar)
                }
            }
        }
    }

    private fun copyToClipboard(invoice: String) {
        val clipboardManager =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText(
                getString(R.string.label_number_invoice),
                invoice
            )
        )
        showToaster(getString(R.string.success_copy_invoice))
    }

    private fun showToaster(message: String) {
        binding.viewToaster.apply {
            Toaster.build(rootView, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                .setAnchorView(this)
                .show()
        }
    }

    private fun String.toSpannedString(): CharSequence {
        return context?.let {
            val htmlLinkHelper = HtmlLinkHelper(
                it,
                this
            )
            htmlLinkHelper.spannedString
        } ?: this
    }

    private fun renderTimer(message: CharSequence, calendar: Calendar) {
        try {
            binding.tusCountdown.timerText = message
            binding.tusCountdown.targetDate = calendar
            binding.tusCountdown.timerFormat = TimerUnifySingle.FORMAT_MINUTE
            binding.tusCountdown.onFinish = {
                viewModel.getNewDriverAvailability(orderId)
            }
            binding.tusCountdown.show()
        } catch (e: Throwable) {
            binding.tusCountdown.gone()
        }
    }

    private fun showLoadingView() {
        with(binding) {
            loaderView.visible()
            grupBtnAndInvoice.invisible()
            tvDescription.gone()
            tusCountdown.gone()
        }
    }

    companion object {
        const val TAG = "FindingNewDriverBottomSheet"

        fun showFindingNewDriverBottomSheet(
            orderId: String,
            listener: FindingNewDriverListener
        ): FindingNewDriverBottomSheet {
            val findingNewDriverBottomSheet = FindingNewDriverBottomSheet()
            findingNewDriverBottomSheet.isFullpage = false
            findingNewDriverBottomSheet.isDragable = false
            findingNewDriverBottomSheet.isHideable = true
            findingNewDriverBottomSheet.orderId = orderId
            findingNewDriverBottomSheet.mListener = listener
            return findingNewDriverBottomSheet
        }
    }
}
