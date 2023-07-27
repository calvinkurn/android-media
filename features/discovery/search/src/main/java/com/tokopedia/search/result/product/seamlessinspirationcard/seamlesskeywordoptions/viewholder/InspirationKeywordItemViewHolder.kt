package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessItemKeywordBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.utils.view.binding.viewBinding

abstract class InspirationKeywordItemViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(inspirationKeywordDataView: InspirationKeywordDataView)
}
