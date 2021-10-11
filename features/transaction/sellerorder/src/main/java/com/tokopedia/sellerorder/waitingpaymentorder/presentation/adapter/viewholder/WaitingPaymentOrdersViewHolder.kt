package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemWaitingPaymentOrdersBinding
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersViewHolder(
        itemView: View
) : AbstractViewHolder<WaitingPaymentOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders
        const val MAX_ORDER_WHEN_COLLAPSED = 5
        const val RECYCLER_VIEW_ANIMATION_DURATION = 300L
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var iconDropdownAnimator: ValueAnimator? = null
    private var recyclerViewAnimator: ValueAnimator? = null
    private var animatorSet: AnimatorSet = AnimatorSet()

    private val animationListener = object: Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator?) {
            binding?.root?.setHasTransientState(false)
        }

        override fun onAnimationCancel(animation: Animator?) {
            binding?.root?.setHasTransientState(false)
        }

        override fun onAnimationStart(animation: Animator?) {
            binding?.root?.setHasTransientState(true)
        }

        override fun onAnimationRepeat(animation: Animator?) {
            binding?.root?.setHasTransientState(true)
        }
    }

    private val binding by viewBinding<ItemWaitingPaymentOrdersBinding>()

    init {
        animatorSet.addListener(animationListener)
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?) {
        element?.let { element ->
            binding?.run {
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                    updateToggleCollapseText(element.isExpanded)
                    setLoadUnloadMoreClickListener(element)
                }
                icLoadMoreDropDown.apply {
                    iconDropdownAnimator?.end()
                    rotation = if (element.isExpanded) -180f else Float.ZERO
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                }
                rvWaitingPaymentOrderProducts.apply {
                    recyclerViewAnimator?.end()
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    val layoutParams = layoutParams
                    layoutParams.height = getRecyclerViewHeight(element.isExpanded, element.productUiModels.size)
                    this.layoutParams = layoutParams
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(element.productUiModels)
                    setHasFixedSize(true)
                }
            }
        }
    }

    private fun getRecyclerViewHeight(isExpanded: Boolean, productUiModelsSize: Int): Int {
        return binding?.root?.run {
            val productCount = if (!isExpanded) productUiModelsSize.coerceAtMost(MAX_ORDER_WHEN_COLLAPSED) else productUiModelsSize
            productCount * (getDimens(R.dimen.waiting_order_product_height))
        }.orZero()
    }

    private fun setLoadUnloadMoreClickListener(element: WaitingPaymentOrderUiModel) {
        binding?.root?.setOnClickListener {
            animatorSet.end()
            element.isExpanded = !element.isExpanded
            setupRecyclerViewSizeAnimator(
                    binding?.rvWaitingPaymentOrderProducts?.height.orZero(),
                    getRecyclerViewHeight(element.isExpanded, element.productUiModels.size))
            setupDropdownIconAnimator(element.isExpanded)
            updateToggleCollapseText(element.isExpanded)
            animatorSet.playTogether(recyclerViewAnimator, iconDropdownAnimator)
            animatorSet.start()
        }
    }

    private fun setupRecyclerViewSizeAnimator(from: Int, to: Int) {
        recyclerViewAnimator = ValueAnimator.ofInt(from, to)
        recyclerViewAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        recyclerViewAnimator?.addUpdateListener { animation ->
            val layoutParams = binding?.rvWaitingPaymentOrderProducts?.layoutParams
            layoutParams?.let {
                layoutParams.height = animation.animatedValue as Int
                binding?.rvWaitingPaymentOrderProducts?.layoutParams = layoutParams
            }
        }
    }

    private fun setupDropdownIconAnimator(isExpanded: Boolean) {
        if (isExpanded) {
            setupDropdownIconAnimator(Float.ZERO, -180f)
        } else {
            setupDropdownIconAnimator(-180f, Float.ZERO)
        }
    }

    private fun setupDropdownIconAnimator(start: Float, end: Float) {
        iconDropdownAnimator = ValueAnimator.ofFloat(start, end)
        iconDropdownAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        iconDropdownAnimator?.addUpdateListener { animation ->
            binding?.icLoadMoreDropDown?.rotation = animation.animatedValue as Float
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        binding?.run {
            tvToggleCollapseMoreProducts.text = if (expanded) {
                root.context.getString(R.string.waiting_payment_orders_less_product)
            } else {
                root.context.getString(R.string.waiting_payment_orders_more_products)
            }
        }
    }
}