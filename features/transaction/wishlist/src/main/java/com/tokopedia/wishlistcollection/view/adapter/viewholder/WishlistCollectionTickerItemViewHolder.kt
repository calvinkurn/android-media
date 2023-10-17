package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.SPACE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.BG_TICKER
import com.tokopedia.wishlistcollection.util.WishlistCollectionUtils
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionTickerItemViewHolder(
    private val binding: CollectionWishlistTickerItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistCollectionTypeLayoutData, isTickerClosed: Boolean) {
        if (WishlistCollectionUtils.isAffiliateTickerEnabled()) {
            showAffiliateTicker()
            return
        }

        if (item.dataObject is GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Ticker) {
            if (item.dataObject.title.isNotEmpty() && !isTickerClosed) {
                binding.root.visible()
                val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                binding.root.layoutParams = params
                binding.wishlistCollectionTickerTitle.text = item.dataObject.title
                binding.wishlistCollectionTickerDesc.text = item.dataObject.description
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

    private fun showAffiliateTicker() {
        binding.root.show()
        binding.icCloseTickerCollectionWishlist.invisible()
        binding.wishlistCollectionTickerBg.loadImage(BG_TICKER)
        binding.wishlistCollectionTickerTitle.text =
            itemView.context.getString(R.string.wishlist_affiliate_ticker_title)
        val cta = itemView.context.getString(R.string.wishlist_affiliate_ticker_cta)
        val description = itemView.context.getString(R.string.wishlist_affiliate_ticker_desc)
        binding.wishlistCollectionTickerDesc.apply {
            text = buildSpannedString {
                append(description)
                append(String.SPACE)
                inSpans(
                    spans = arrayOf(
                        setOnClickListener {
                            actionListener?.onAffiliateTickerCtaClick()
                        },
                        ForegroundColorSpan(
                            MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        ),
                        StyleSpan(Typeface.BOLD)
                    ),
                    builderAction = { append(cta) }
                )
            }
        }
    }
}
