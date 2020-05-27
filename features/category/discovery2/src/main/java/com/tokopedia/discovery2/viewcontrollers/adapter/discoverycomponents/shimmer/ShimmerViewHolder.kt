package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.unifycomponents.LoaderUnify


class ShimmerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private lateinit var viewModel: ShimmerViewModel
    private val shimmer: LoaderUnify = itemView.findViewById(R.id.shimmer_view)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ShimmerViewModel
        viewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            if(componentItem.constraintRatio.isNullOrEmpty()) {
                shimmer.layoutParams.height = componentItem.shimmerHeight
                shimmer.layoutParams.width = componentItem.shimmerWidth
            } else {
                (shimmer.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = componentItem.constraintRatio
            }
        })
    }
}
