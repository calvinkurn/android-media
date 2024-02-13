package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.databinding.BottomsheetTransparencyFeeBinding
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailTransparencyFeeComponent
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailTransparencyFeeAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeUiModelWrapper
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailTransparencyFeeViewModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeSummary
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SomDetailTransparencyFeeBottomSheet : BottomSheetUnify(),
    DetailTransparencyFeeAdapterFactoryImpl.ActionListener, WidgetTransparencyFeeSummary.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val typeFactory by lazy(LazyThreadSafetyMode.NONE) {
        DetailTransparencyFeeAdapterFactoryImpl(this)
    }

    private val orderId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(ORDER_ID_KEY).orEmpty()
    }

    private val somDetailTransparencyFeeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SomDetailTransparencyFeeAdapter(typeFactory)
    }

    private val viewModel: SomDetailTransparencyFeeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(SomDetailTransparencyFeeViewModel::class.java)
    }

    private var binding by autoClearedNullable<BottomsheetTransparencyFeeBinding>()

    init {
        clearContentPadding = true
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
        binding = BottomsheetTransparencyFeeBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupTransparencyFeeSummaryWidget()
        setShowListener { observeTransparencyFee() }
        fetchTransparencyFee()
    }

    override fun onErrorActionClicked() {
        somDetailTransparencyFeeAdapter.hideError()
        fetchTransparencyFee()
    }

    override fun onTransparencyInfoIconClicked(title: String, desc: String) {
        val transparencyFeeInfoBottomSheet = TransparencyFeeInfoBottomSheet.newInstance(title, desc)
        transparencyFeeInfoBottomSheet.show(childFragmentManager)
    }

    override fun onClickNoteLink(url: String) {
        SomAnalytics.eventNoteLinkClicked()
        RouteManager.route(context, url)
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun initInjector() {
        activity?.application?.let { application ->
            DaggerSomDetailTransparencyFeeComponent
                .builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
                .inject(this)
        }
    }

    private fun setupRecyclerView() {
        binding?.rvTransparancyFee?.run {
            if (adapter != somDetailTransparencyFeeAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = somDetailTransparencyFeeAdapter
                itemAnimator = null
            }
        }
    }

    private fun setupTransparencyFeeSummaryWidget() {
        binding?.footerTransparencyFee?.setListener(this)
    }

    private fun observeTransparencyFee() {
        removeObservers(viewModel.transparencyFee)
        observe(viewModel.transparencyFee, ::transparencyFeeObserver)
    }

    private fun fetchTransparencyFee() {
        if (orderId.isNotBlank()) {
            somDetailTransparencyFeeAdapter.showLoadingShimmer()
            viewModel.fetchTransparencyFee(orderId)
        }
    }

    private fun setBottomSheetTitle(title: String) {
        setTitle(title)
    }

    private fun transparencyFeeObserver(result: Result<TransparencyFeeUiModelWrapper>) {
        somDetailTransparencyFeeAdapter.hideLoadingShimmer()
        when (result) {
            is Success -> {
                setBottomSheetTitle(result.data.bottomSheetTitle)
                somDetailTransparencyFeeAdapter.updateItems(result.data.transparencyFeeList)
                binding?.footerTransparencyFee?.updateUI(result.data.summary)
            }
            is Fail -> {
                somDetailTransparencyFeeAdapter.showError(TransparencyFeeErrorStateUiModel(result.throwable))
            }
        }
    }

    companion object {

        private val TAG = SomDetailTransparencyFeeBottomSheet::class.java.simpleName

        private const val ORDER_ID_KEY = "orderId"

        fun newInstance(orderId: String): SomDetailTransparencyFeeBottomSheet {
            return SomDetailTransparencyFeeBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ORDER_ID_KEY, orderId)
                }
            }
        }
    }
}
