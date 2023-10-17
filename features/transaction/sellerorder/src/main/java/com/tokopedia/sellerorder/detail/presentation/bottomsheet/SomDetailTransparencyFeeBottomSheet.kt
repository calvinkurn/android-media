package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.sellerorder.databinding.BottomsheetTransparencyFeeBinding
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailTransparencyFeeAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailTransparencyFeeViewModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeSummary
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
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

    private val somDetailTransparencyFeeAdapter by lazy {
        SomDetailTransparencyFeeAdapter(typeFactory)
    }

    private val viewModel: SomDetailTransparencyFeeViewModel by lazy {
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
        observeTransparencyFee()
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
        RouteManager.route(context, url)
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun initInjector() {
        (activity as? HasComponent<SomDetailComponent>)?.let { activityComponent ->
            activityComponent.component.inject(this)
        }
    }

    private fun setupRecyclerView() {
        binding?.rvTransparancyFee?.run {
            if (adapter != somDetailTransparencyFeeAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = somDetailTransparencyFeeAdapter
            }
        }
    }

    private fun setupTransparencyFeeSummaryWidget() {
        binding?.footerTransparencyFee?.setListener(this)
    }

    private fun observeTransparencyFee() {
        observe(viewModel.transparencyFee) {
            somDetailTransparencyFeeAdapter.hideLoadingShimmer()
            when (it) {
                is Success -> {
                    setBottomSheetTitle(it.data.bottomSheetTitle)
                    somDetailTransparencyFeeAdapter.updateItems(it.data.transparencyFeeList)
                    binding?.footerTransparencyFee?.updateUI(it.data.summary)
                }

                is Fail -> {
                    somDetailTransparencyFeeAdapter.showError(TransparencyFeeErrorStateUiModel(it.throwable))
                }
            }
        }
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
