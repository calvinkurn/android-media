package com.tokopedia.tokofood.feature.ordertracking.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.domain.utils.FileUtilsTemp
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodOrderTrackingFragment : BaseDaggerFragment(), RecyclerViewPollerListener,
    OrderTrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fileUtilsTemp: FileUtilsTemp

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodOrderTrackingViewModel::class.java)
    }

    private val orderTrackingAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        OrderTrackingAdapterTypeFactoryImpl(this, this)
    }

    private val orderTrackingAdapter by lazy {
        OrderTrackingAdapter(orderTrackingAdapterTypeFactory)
    }

    private var binding by autoClearedNullable<FragmentTokofoodOrderTrackingBinding>()

    private var isCompletedOrder = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodOrderTrackingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwipeRefreshDisabled()
        observeOrderDetail()
        fetchOrderDetail()
    }

    override fun onDestroy() {
        viewModel.orderDetailResult.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TokoFoodOrderTrackingComponent::class.java).inject(this)
    }

    override val parentPool: RecyclerView.RecycledViewPool
        get() = binding?.rvOrderTracking?.recycledViewPool ?: RecyclerView.RecycledViewPool()

    override fun onToggleClicked(
        orderDetailToggleCtaUiModel: OrderDetailToggleCtaUiModel,
        isExpandable: Boolean
    ) {

    }

    override fun onTickerLinkClick(linkUrl: String) {
        context?.let { RouteManager.route(it, linkUrl) }
    }

    private fun observeOrderDetail() {
        observe(viewModel.orderDetailResult) {
            when (it) {
                is Success -> {
                    isCompletedOrder = it.data.isOrderCompleted
                    orderTrackingAdapter.updateOrderTracking(it.data.orderDetailList)
                }
                is Fail -> {

                }
            }
        }
    }

    private fun fetchOrderDetail() {
        context?.resources?.let {
            viewModel.fetchOrderDetail(
                fileUtilsTemp.getJsonFromRaw(it, ORDER_TRACKING_RESOURCE)
            )
        }
    }

    private fun setSwipeRefreshDisabled() {
        binding?.orderTrackingSwipeRefresh?.run {
            isEnabled = false
            isRefreshing = false
        }
    }

    companion object {

        fun newInstance(): TokoFoodOrderTrackingFragment {
            return TokoFoodOrderTrackingFragment()
        }

        private val ORDER_TRACKING_RESOURCE = R.raw.ordertracking
        private val ORDER_DETAIL_RESOURCE = R.raw.orderdetail
    }
}