package com.tokopedia.sellerorder.list.presentation.bottomsheets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.list.di.bulkaccept.DaggerSomBulkAcceptOrderComponent
import com.tokopedia.sellerorder.list.di.bulkaccept.SomBulkAcceptOrderComponent
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkAcceptOrderTypeFactory
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderProductUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListBulkAcceptOrderViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_som_list_bulk_accept_order.*
import javax.inject.Inject

class SomListBulkAcceptOrderBottomSheet : BottomSheetUnify(), HasComponent<SomBulkAcceptOrderComponent> {

    @Inject
    lateinit var viewModel: SomListBulkAcceptOrderViewModel

    private val typeFactory: SomListBulkAcceptOrderTypeFactory = SomListBulkAcceptOrderTypeFactory()
    private val adapter = BaseListAdapter<SomListBulkAcceptOrderProductUiModel, SomListBulkAcceptOrderTypeFactory>(typeFactory)
    private val orders: ArrayList<SomListOrderUiModel> = arrayListOf()

    private var listener: SomListBulkAcceptOrderBottomSheetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allowDismiss()
        setTitle(getString(R.string.som_list_bulk_accept_order_bottom_sheet_title))
        val childViews = View.inflate(context, R.layout.bottomsheet_som_list_bulk_accept_order, null)
        setChild(childViews)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setBulkAcceptOrderButtonClickListener()
        observeBulkAcceptOrderStatusResult()
    }

    override fun getComponent(): SomBulkAcceptOrderComponent? {
        return activity?.let {
            DaggerSomBulkAcceptOrderComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it.application))
                    .build()
        }
    }

    private fun inject() {
        component?.inject(this)
    }

    private fun setRecyclerView() {
        rvBulkAccept.apply {
            context?.let {
                adapter = this@SomListBulkAcceptOrderBottomSheet.adapter
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                val divider = it.getResDrawable(R.drawable.waiting_payment_tips_divider)
                addItemDecoration(SellerHomeTooltipItemDivider(divider ?: return))
            }
        }
    }

    private fun setBulkAcceptOrderButtonClickListener() {
        btnBulkAccept.setOnClickListener {
            btnBulkAccept.isLoading = true
            preventDismiss()
            viewModel.bulkAcceptOrder(orders.map { it.orderId })
        }
    }

    private fun observeBulkAcceptOrderStatusResult() {
        viewModel.bulkAcceptOrderStatusResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val successCount = result.data.data.success
                    val failedCount = result.data.data.totalOrder - successCount
                    listener?.onBulkAcceptOrderCompleted(successCount, failedCount)
                }
                is Fail -> {
                    listener?.onBulkAcceptOrderCompleted(0, orders.size)
                }
            }
            dismiss()
        })
    }

    private fun preventDismiss() {
        isCancelable = false
        isHideable = false
        overlayClickDismiss = false
    }

    private fun allowDismiss() {
        isCancelable = true
        isHideable = true
        overlayClickDismiss = true
    }

    private fun setProducts() {
        val products = orders.flatMap { it.orderProduct }.groupBy { it.productId }

        val groupedProducts = products.filter { it.value.isNotEmpty() }.map {
            SomListBulkAcceptOrderProductUiModel(
                    productName = it.value.first().productName,
                    picture = it.value.first().picture,
                    amount = it.value.size
            )
        }

        adapter.setElements(groupedProducts)
    }

    fun setOrders(orders: List<SomListOrderUiModel>) {
        this.orders.clear()
        this.orders.addAll(orders)
        setProducts()
    }

    fun setListener(listener: SomListBulkAcceptOrderBottomSheetListener) {
        this.listener = listener
    }

    interface SomListBulkAcceptOrderBottomSheetListener {
        fun onBulkAcceptOrderCompleted(totalSuccess: Int, totalFailed: Int)
    }

    class SellerHomeTooltipItemDivider(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0..childCount - 2) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + mDivider.intrinsicHeight

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider.draw(canvas)
            }
        }
    }
}