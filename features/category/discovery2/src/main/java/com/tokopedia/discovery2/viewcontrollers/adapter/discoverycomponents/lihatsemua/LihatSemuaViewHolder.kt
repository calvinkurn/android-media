package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: Typography = itemView.findViewById(R.id.lihat_semua_tv)
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title_tv)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.sub_header_tv)
    private var onLihatSemuaClickListener: OnLihatSemuaClickListener? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatSemuaViewModel = discoveryBaseViewModel as LihatSemuaViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatSemuaViewModel.getComponentData().observe(it, Observer { componentItem ->
            componentItem.data?.firstOrNull()?.let { data ->
                lihatTitleTextView.setTextAndCheckShow(data.title)
                lihatSubTitleTextView.setTextAndCheckShow(data.subtitle)
                lihatTextView.visibility = if (data.btnApplink.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                lihatTextView.setOnClickListener {
                    navigateToAppLink(data)
                    sendGtmEvent(componentItem)
                }
            }
        })
        }
    }

    private fun sendGtmEvent(componentsItem: ComponentsItem) {
        if (componentsItem.name == ComponentNames.ProductCardCarousel.componentName) {
            onLihatSemuaClickListener?.onProductCardHeaderClick(componentsItem)
        } else {
            componentsItem.data?.first()?.let {
                onLihatSemuaClickListener?.onLihatSemuaClick(it)
            }
        }
    }

    private fun navigateToAppLink(data: DataItem) {
        lihatSemuaViewModel.navigate(fragment.activity, data.btnApplink)
    }

    interface OnLihatSemuaClickListener {
        fun onProductCardHeaderClick(componentsItem: ComponentsItem)
        fun onLihatSemuaClick(data: DataItem)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        if(fragment is OnLihatSemuaClickListener){
            onLihatSemuaClickListener = fragment
        }
    }
}