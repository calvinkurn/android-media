package com.tokopedia.buyerorder.recharge.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorder.databinding.FragmentRechargeOrderDetailBinding
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.di.RechargeOrderDetailComponent
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailAdapter
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailTypeFactory
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.*
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailStaticButtonModel
import com.tokopedia.buyerorder.recharge.presentation.viewmodel.RechargeOrderDetailViewModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailFragment : BaseDaggerFragment(),
        RechargeOrderDetailTopSectionViewHolder.ActionListener,
        RechargeOrderDetailProductViewHolder.ActionListener,
        RechargeOrderDetailDigitalRecommendationViewHolder.ActionListener,
        RechargeOrderDetailStaticButtonViewHolder.ActionListener,
        RechargeOrderDetailAboutOrderViewHolder.ActionListener {

    private lateinit var binding: FragmentRechargeOrderDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val rechargeViewModel: RechargeOrderDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RechargeOrderDetailViewModel::class.java)
    }

    private val digitalRecommendationData: DigitalRecommendationData
        get() = DigitalRecommendationData(
                viewModelFactory,
                viewLifecycleOwner,
                DigitalRecommendationAdditionalTrackingData(
                        userType = "",
                        widgetPosition = "",
                        pgCategories = emptyList()
                ),
                DigitalRecommendationPage.DIGITAL_GOODS
        )
    private val typeFactory: RechargeOrderDetailTypeFactory by lazy {
        RechargeOrderDetailTypeFactory(digitalRecommendationData,
                this,
                this,
                this,
                this,
                this)
    }
    private val adapter: RechargeOrderDetailAdapter by lazy {
        RechargeOrderDetailAdapter(typeFactory)
    }

    private var orderId: String = ""
    private var orderCategory: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(RechargeOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
        }

        arguments?.let {
            if (it.containsKey(EXTRA_ORDER_ID))
                orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            if (it.containsKey(EXTRA_ORDER_CATEGORY))
                orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        showLoading()
        observeData()

        rechargeViewModel.fetchData(
                RechargeOrderDetailRequest(
                        orderCategory = orderCategory,
                        orderId = orderId
                )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_ORDER_ID, orderId)
        outState.putString(EXTRA_ORDER_CATEGORY, orderCategory)
    }

    override fun onCopyInvoiceNumberClicked(invoiceRefNum: String) {
//        TODO("Not yet implemented")
    }

    override fun onCopyCodeClicked(label: String, value: String) {
//        TODO("Not yet implemented")
    }

    override fun hideDigitalRecommendation() {
        adapter.removeDigitalRecommendation()
    }

    override fun onClickStaticButton(staticButtonModel: RechargeOrderDetailStaticButtonModel) {
//        TODO("Not yet implemented")
    }

    override fun onClickHelp(helpUrl: String) {
//        TODO("Not yet implemented")
    }

    private fun setupViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvRechargeOrderDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvRechargeOrderDetail.adapter = adapter
        }
    }

    private fun observeData() {
        rechargeViewModel.orderDetailData.observe(viewLifecycleOwner, {
            hideLoading()
            when (it) {
                is Success -> {
                    adapter.updateItems(it.data)
                    showRecyclerView()
                }
                is Fail -> {

                }
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvRechargeOrderDetail.show()
    }

    private fun hideRecyclerView() {
        binding.rvRechargeOrderDetail.hide()
    }

    private fun showLoading() {
        binding.rechargeOrderDetailLoader.show()
    }

    private fun hideLoading() {
        binding.rechargeOrderDetailLoader.hide()
    }

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        private const val EXTRA_ORDER_CATEGORY = "EXTRA_ORDER_CATEGORY"

        fun getInstance(orderId: String,
                        orderCategory: String): RechargeOrderDetailFragment =
                RechargeOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_ORDER_CATEGORY, orderCategory)
                        putString(EXTRA_ORDER_ID, orderId)
                    }
                }
    }

}