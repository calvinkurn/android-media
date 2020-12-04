package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.Constant.ProductTemplate.LIST
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ShimmerProductCardViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private lateinit var shimmerProductViewModel: ShimmerViewModel
    private var parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayout)
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        shimmerProductViewModel = discoveryBaseViewModel as ShimmerViewModel
        setProductShimmerView()
    }

    private fun setProductShimmerView() {
        val layoutParams: ViewGroup.LayoutParams = parentLayout.layoutParams
        if (shimmerProductViewModel.getTemplateType() == LIST) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.width = parentLayout.context.resources.getDimensionPixelSize(R.dimen.dp_200)
        }
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        parentLayout.layoutParams = layoutParams
    }
}
