package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemBmgmMiniCartBundledProductBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmBundledProductAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.itemdecoration.BmgmBundledProductItemDecoration
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmBundledProductViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.TierUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_bundled_product
    }

    private val binding = ItemBmgmMiniCartBundledProductBinding.bind(itemView)

    override fun bind(element: BmgmMiniCartVisitable.TierUiModel) {

        itemView.setOnClickListener { listener.setOnItemClickedListener() }
        setupProductList(element.products)
        showShiningEffect(element)

        binding.tvBmgmBundledDiscount.text = element.tierDiscountStr
    }

    private fun showShiningEffect(element: BmgmMiniCartVisitable.TierUiModel) {
        with(binding) {
            root.addOnImpressionListener(element.impressHolder) {
                val anim = AnimationUtils.loadAnimation(root.context, R.anim.shining_annimation)
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(p0: Animation?) {
                        viewBmgmShining.gone()
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
                viewBmgmShining.visible()
                viewBmgmShining.loadImage(R.drawable.bmgm_shining_effect)
                viewBmgmShining.startAnimation(anim)
            }
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