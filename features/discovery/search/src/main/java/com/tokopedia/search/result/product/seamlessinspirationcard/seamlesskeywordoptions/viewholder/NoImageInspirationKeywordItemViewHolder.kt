package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessItemKeywordNoImageBinding
import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isBigGridView
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class NoImageInspirationKeywordItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val changeListener: ChangeViewListener
) : AbstractViewHolder<InspirationKeywordDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_semless_item_keyword_no_image
    }
    private var binding: SearchInspirationSemlessItemKeywordNoImageBinding? by viewBinding()

    override fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView
    ) {
        val binding = binding ?: return
        val textKeyword = binding.textViewKeyword
        val containerCard = binding.containerCardViewOptionsKeywordCard

        containerCard.cardType = TYPE_BORDER
        containerCard.animateOnPress = ANIMATE_OVERLAY
        textKeyword.adjustRatioImage()
        textKeyword.text = inspirationKeywordDataView.keyword
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }

    private fun Typography.adjustRatioImage() {
        if(changeListener.isBigGridView()) {
            val spacingSize = this.context.getSpacingResource()
            val params: FrameLayout.LayoutParams =
                FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            params.setMargins(0, spacingSize, 0, spacingSize)
            this.layoutParams = params
        }
    }

    private fun Context.getSpacingResource(): Int =
        this
            .resources
            ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            ?: 0
}
