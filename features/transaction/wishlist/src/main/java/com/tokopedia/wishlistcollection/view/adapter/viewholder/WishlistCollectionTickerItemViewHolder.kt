package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.util.TickerWrapper
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.BG_TICKER
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionTickerItemViewHolder(
    private val binding: CollectionWishlistTickerItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistCollectionTypeLayoutData, isTickerClosed: Boolean) {
        if (item.dataObject is TickerWrapper) {
            if (item.dataObject.ticker.title.isNotEmpty() && !isTickerClosed) {
                binding.root.visible()
                val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                binding.root.layoutParams = params
                binding.wishlistCollectionTickerTitle.text = item.dataObject.ticker.title

                val wishlistTickerDescription = "${item.dataObject.ticker.description} ${item.dataObject.ctaText}"
                binding.wishlistCollectionTickerDesc.movementMethod = LinkMovementMethod.getInstance()
                binding.wishlistCollectionTickerDesc.text = getWishlistDescriptionSpan(wishlistTickerDescription, item.dataObject.ctaText)

                binding.icCloseTickerCollectionWishlist.bringToFront()
                binding.icCloseTickerCollectionWishlist.setOnClickListener { actionListener?.onCloseTicker() }
                binding.wishlistCollectionTickerBg.loadImage(BG_TICKER)
            } else {
                binding.root.gone()
                val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                    height = 0
                    width = 0
                }
                binding.root.layoutParams = params
            }
        }
    }

    private fun getWishlistDescriptionSpan(wishlistDescriptionFullText: String, ctaText: String): SpannableString {
        val spannableWishlistDescription = SpannableString(wishlistDescriptionFullText)

        val ctaColor = ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val ctaStartIdx = wishlistDescriptionFullText.length - ctaText.length
        val ctaEndIdx = wishlistDescriptionFullText.length

        spannableWishlistDescription.setSpan(ForegroundColorSpan(ctaColor), ctaStartIdx, ctaEndIdx, SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableWishlistDescription.setSpan(StyleSpan(android.graphics.Typeface.BOLD), ctaStartIdx, ctaEndIdx, SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableWishlistDescription.setSpan(
            object : ClickableSpan() {
                override fun onClick(textView: View) {
                    actionListener?.onTickerActionClicked(true)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            },
            ctaStartIdx, ctaEndIdx, SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableWishlistDescription
    }
}
