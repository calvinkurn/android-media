package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmBundledProductAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmBundledProductItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartBundledProductBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmBundledProductViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.TierUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_bundled_product
        private const val SHINING_MAX_REPEAT = 3
    }

    private val binding = ItemBmgmMiniCartBundledProductBinding.bind(itemView)
    private var shiningRepeatCount = 0

    override fun bind(element: BmgmMiniCartVisitable.TierUiModel) {

        itemView.setOnClickListener { listener.setOnItemClickedListener() }
        setupProductList(element.products)
        showShiningEffect(element)

        binding.tvBmgmBundledDiscount.text = element.tierDiscountStr
    }

    private fun showShiningEffect(element: BmgmMiniCartVisitable.TierUiModel) {
        if (!element.shouldShowShiningEffect) return
        with(binding) {
            val anim = AnimationUtils.loadAnimation(root.context, R.anim.shining_annimation).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(p0: Animation?) {
                        if (shiningRepeatCount >= SHINING_MAX_REPEAT) {
                            element.shouldShowShiningEffect = false
                            viewBmgmShining.gone()
                        } else {
                            viewBmgmShining.startAnimation(this@apply)
                        }
                        shiningRepeatCount = ++shiningRepeatCount
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            }

            viewBmgmShining.visible()
            viewBmgmShining.startAnimation(anim)
        }
    }

    private fun setupProductList(products: List<BmgmMiniCartVisitable.ProductUiModel>) {
        with(binding.rvBmgmBundledProduct) {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmBundledProductItemDecoration())
            }
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }
            adapter = BmgmBundledProductAdapter(products, listener)
        }
    }
}