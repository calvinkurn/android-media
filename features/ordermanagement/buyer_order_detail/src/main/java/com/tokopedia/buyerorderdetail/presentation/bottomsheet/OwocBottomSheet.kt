package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.OwocBottomsheetBinding
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocSectionGroupAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListHeaderListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocSectionGroupListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.OwocErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.buyerorderdetail.presentation.viewmodel.OwocViewModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OwocBottomSheet :
    BottomSheetUnify(),
    OwocSectionGroupListener,
    OwocRecyclerviewPoolListener,
    OwocProductListHeaderListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<OwocBottomsheetBinding>()

    private val viewModel: OwocViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OwocViewModel::class.java)
    }

    private val orderId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(OWOC_ORDER_ID_KEY)
    }

    private val txId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(OWOC_TX_ID_KEY)
    }

    private val navigator: BuyerOrderDetailNavigator? by lazy(LazyThreadSafetyMode.NONE) {
        activity?.let { BuyerOrderDetailNavigator(it, this) }
    }

    private val typeFactory: OwocSectionGroupTypeFactoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        OwocSectionGroupTypeFactoryImpl(navigator, this, this, this)
    }

    private val owocSectionGroupAdapter: OwocSectionGroupAdapter by lazy(LazyThreadSafetyMode.NONE) {
        OwocSectionGroupAdapter(typeFactory)
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
        clearContentPadding = true
        binding = OwocBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeGroupedOrderWrapper()
        fetchBomGroupedOrder()
    }

    override fun onErrorActionClicked() {
        owocSectionGroupAdapter.hideError()
        fetchBomGroupedOrder()
    }

    override fun goToOtherBomDetail(orderId: String) {
        BuyerOrderDetailTracker.sendClickViewDetailOnOrderGroupDetail(orderId)
        navigator?.goToBomDetailPage(orderId)
        dismiss()
    }

    override val parentPool: RecyclerView.RecycledViewPool
        get() = binding?.rvOwocList?.recycledViewPool ?: RecyclerView.RecycledViewPool()

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

    private fun setupRecyclerView() {
        binding?.rvOwocList?.run {
            if (adapter != owocSectionGroupAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = owocSectionGroupAdapter
            }
        }
    }

    private fun fetchBomGroupedOrder() {
        owocSectionGroupAdapter.showLoadingShimmer()
        orderId?.let { orderId ->
            txId?.let { txId ->
                viewModel.fetchBomGroupedOrder(txId, orderId)
            }
        }
    }

    private fun observeGroupedOrderWrapper() {
        observe(viewModel.owocGroupedOrderWrapper) {
            owocSectionGroupAdapter.hideLoadingShimmer()
            when (it) {
                is Success -> {
                    setTitle(it.data.owocTitle)
                    showOwocGroupedOrderList(it.data)
                }
                is Fail -> {
                    showOwocGroupedOrderError(it.throwable)
                }
            }
        }
    }

    private fun showOwocGroupedOrderError(throwable: Throwable) {
        owocSectionGroupAdapter.showError(OwocErrorUiModel(throwable))
    }

    private fun showOwocGroupedOrderList(data: OwocGroupedOrderWrapper) {
        owocSectionGroupAdapter.updateItems(data.owocGroupedOrderList)
    }

    companion object {

        const val OWOC_ORDER_ID_KEY = "owoc_order_id_key"
        const val OWOC_TX_ID_KEY = "owoc_tx_id_key"

        private val TAG = OwocBottomSheet::class.java.simpleName

        fun newInstance(orderId: String, txId: String): OwocBottomSheet {
            return OwocBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(OWOC_ORDER_ID_KEY, orderId)
                    putString(OWOC_TX_ID_KEY, txId)
                }
            }
        }
    }
}
