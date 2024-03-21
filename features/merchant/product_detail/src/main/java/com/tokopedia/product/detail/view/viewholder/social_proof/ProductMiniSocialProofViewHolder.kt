package com.tokopedia.product.detail.view.viewholder.social_proof

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicSocialProofBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.SocialProofAdapter
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.SocialProofAdapterFactory
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductMiniSocialProofViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductMiniSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_social_proof

        private val SPACE_BETWEEN_ITEM = 8.toPx()
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicSocialProofBinding.bind(view)
    }

    private val adapter by lazyThreadSafetyNone {
        SocialProofAdapter(factory = SocialProofAdapterFactory(listener = listener))
    }

    private var previousElement: ProductMiniSocialProofDataModel? = null

    init {
        initRecyclerView()
    }

    override fun bind(element: ProductMiniSocialProofDataModel) {
        if (previousElement.hasSameSession(newElement = element) == true) return

        renderUI(element = element)
    }

    private fun ProductMiniSocialProofDataModel?.hasSameSession(
        newElement: ProductMiniSocialProofDataModel
    ) = this?.equalsWith(newElement)

    private fun renderUI(element: ProductMiniSocialProofDataModel) {
        adapter.submitList(element.items, getComponentTrackData(element = element))

        binding.root.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun initRecyclerView() = with(binding.miniSocialProofRecyclerView) {
        adapter = this@ProductMiniSocialProofViewHolder.adapter
        layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        addItemDecoration(createSpaceBetweenItem())
    }

    private fun createSpaceBetweenItem() = object : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val marginEnd = if (parent.getChildAdapterPosition(view) < state.itemCount - Int.ONE) {
                SPACE_BETWEEN_ITEM
            } else {
                Int.ZERO
            }
            outRect.set(Int.ZERO, Int.ZERO, marginEnd, Int.ZERO)
        }
    }
}
