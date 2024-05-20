package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.bottomsheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomSheetPofBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.di.PofComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapter
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactoryImpl
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofBottomSheetSummaryUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofFooterUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel.PofViewModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.widget.WidgetPofFooter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.google.android.material.R as materialR

class PofBottomSheet : BottomSheetUnify(),
    PofAdapterTypeFactory.Listener,
    WidgetPofFooter.Listener {

    companion object {
        val TAG = PofBottomSheet::class.java.name

        fun createInstance(orderId: Long, pofStatus: Int): PofBottomSheet {
            return PofBottomSheet().apply {
                val fragmentArguments = Bundle()
                fragmentArguments.putLong(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID, orderId)
                fragmentArguments.putInt(DeeplinkMapperOrder.Pof.INTENT_PARAM_POF_STATUS, pofStatus)
                arguments = fragmentArguments
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazyThreadSafetyNone {
        ViewModelProvider(this, viewModelFactory)[PofViewModel::class.java]
    }
    private val adapter by lazyThreadSafetyNone {
        PofAdapter(PofAdapterTypeFactoryImpl(this))
    }
    private val orderId by lazyThreadSafetyNone {
        arguments?.getLong(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID).orZero()
    }
    private val pofStatus by lazyThreadSafetyNone {
        arguments?.getInt(DeeplinkMapperOrder.Pof.INTENT_PARAM_POF_STATUS).orZero()
    }

    private var binding by viewBinding(BottomSheetPofBinding::bind)

    init {
        clearContentPadding = true
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? HasComponent<PofComponent>)?.component?.inject(this)
        binding = BottomSheetPofBinding.inflate(inflater).also { setChild(it.root) }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFooter()
        setupBottomSheet()
        collectUiState()
        collectToasterQueue()
        collectUiEffect()
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.finish()
    }

    private fun setupBottomSheet() {
        setCloseClickListener { viewModel.onEvent(UiEvent.OnClickDismissPofBottomSheet) }
        isCancelable = false
        dialog?.setCanceledOnTouchOutside(false)
        dialog
            ?.window
            ?.decorView
            ?.findViewById<View>(materialR.id.touch_outside)
            ?.setOnClickListener { viewModel.onEvent(UiEvent.OnClickDismissPofBottomSheet) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onEvent(UiEvent.SaveState(outState))
    }

    override fun onEvent(event: UiEvent) {
        viewModel.onEvent(event)
    }

    private fun setupRecyclerView() {
        binding?.rvPof?.layoutManager = LinearLayoutManager(context)
        binding?.rvPof?.adapter = adapter
    }

    private fun setupFooter() {
        binding?.footerPof?.attachListener(this)
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { uiState ->
                Handler(Looper.getMainLooper()).post {
                    if (activity?.isFinishing == false && isAdded && !isDetached) {
                        setTitle(uiState.title.getString(context))
                        updateResetButton(uiState.showResetButton)
                        updateBody(uiState.items)
                        updateFooter(uiState.footerUiState)
                        updateSummaryBottomSheetUi(uiState.bottomSheetSummaryUiState)
                    }
                }
            }
        }
    }

    private fun collectToasterQueue() {
        lifecycleScope.launchWhenResumed {
            viewModel.toasterQueue.collectLatest { toaster ->
                binding?.clPof?.let { view ->
                    Toaster
                        .build(
                            view = view,
                            text = toaster.text.getString(view.context),
                            duration = toaster.duration,
                            type = toaster.type
                        ).show()
                }
            }
        }
    }

    private fun collectUiEffect() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiEffect.collectLatest { uiEffect ->
                when (uiEffect) {
                    is UiEffect.FinishActivity -> onCloseBottomSheet(uiEffect.result)
                }
            }
        }
    }

    private fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.onEvent(UiEvent.OpenScreen(orderId = orderId, initialPofStatus = pofStatus))
        } else {
            viewModel.onEvent(
                UiEvent.RestoreState(savedInstanceState) {
                    init(null)
                }
            )
        }
    }

    private fun onCloseBottomSheet(result: Int) {
        activity?.setResult(result)
        dismiss()
    }

    private fun updateResetButton(show: Boolean) {
        if (show) {
            setAction(getString(R.string.som_pof_action_reset)) { viewModel.onEvent(UiEvent.OnClickResetPofForm) }
        } else {
            clearAction()
        }
    }

    private fun updateBody(items: List<PofVisitable>) {
        adapter.update(items)
    }

    private fun updateFooter(footerUiState: PofFooterUiState) {
        binding?.footerPof?.updateUi(footerUiState)
    }

    private fun updateSummaryBottomSheetUi(uiState: PofBottomSheetSummaryUiState) {
        when(uiState) {
            is PofBottomSheetSummaryUiState.Hidden -> hideSummaryBottomSheet()
            is PofBottomSheetSummaryUiState.Showing -> showSummaryBottomSheet(uiState.items)
        }
    }

    private fun hideSummaryBottomSheet() {
        findSummaryBottomSheet()?.dismiss()
    }

    private fun showSummaryBottomSheet(items: List<PofVisitable>) {
        val bottomSheet = findSummaryBottomSheet() ?: PofSummaryBottomSheet()
        bottomSheet.show(childFragmentManager, PofSummaryBottomSheet.TAG)
        bottomSheet.updateUi(items)
        bottomSheet.setCloseClickListener {
            viewModel.onEvent(UiEvent.OnClickDismissSummaryBottomSheet)
        }
        bottomSheet.setShowListener {
            bottomSheet.isCancelable = false
            bottomSheet.dialog?.setCanceledOnTouchOutside(false)
            bottomSheet.dialog?.window?.decorView?.findViewById<View>(materialR.id.touch_outside)
                ?.setOnClickListener {
                    viewModel.onEvent(UiEvent.OnClickDismissSummaryBottomSheet)
                }
        }
    }

    private fun findSummaryBottomSheet(): PofSummaryBottomSheet? {
        return childFragmentManager.findFragmentByTag(
            PofSummaryBottomSheet.TAG
        ) as? PofSummaryBottomSheet
    }
}
