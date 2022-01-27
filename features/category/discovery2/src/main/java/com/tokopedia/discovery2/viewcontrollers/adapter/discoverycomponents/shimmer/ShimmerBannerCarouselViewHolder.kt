package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ShimmerBannerCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private lateinit var shimmerBannerCarouselViewModel: ShimmerViewModel
    private var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        shimmerBannerCarouselViewModel = discoveryBaseViewModel as ShimmerViewModel
        setProductShimmerView()
    }

    private fun setProductShimmerView() {
        val layoutParams: ViewGroup.LayoutParams = parentLayout.layoutParams
        layoutParams.width = parentLayout.context.resources.getDimensionPixelSize(R.dimen.dp_200)
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        parentLayout.layoutParams = layoutParams
    }
}
