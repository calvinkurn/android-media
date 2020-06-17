package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder


class SpacingViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var parentView: LinearLayout
    private lateinit var spacingViewModel: SpacingViewModel
    private lateinit var layoutParams: ViewGroup.LayoutParams

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        spacingViewModel = discoveryBaseViewModel as SpacingViewModel
        initView()
    }

    private fun initView() {
        parentView = itemView.findViewById(R.id.parent_view)
        layoutParams = parentView.layoutParams
        setUpObserver()
    }

    private fun setUpObserver() {
        spacingViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer {
            spacingViewModel.setupSpacingView()
        })

        spacingViewModel.getViewHeight().observe(fragment.viewLifecycleOwner, Observer {
            layoutParams.height = it
            parentView.layoutParams = layoutParams
        })

        spacingViewModel.getViewBackgroundColor().observe(fragment.viewLifecycleOwner, Observer {
            parentView.setBackgroundColor(Color.parseColor(it))
        })
    }
}