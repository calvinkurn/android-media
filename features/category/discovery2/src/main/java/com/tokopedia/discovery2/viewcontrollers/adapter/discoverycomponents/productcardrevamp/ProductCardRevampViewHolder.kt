package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.show

class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mProductRevampComponentViewModel: ProductCardRevampViewModel? = null
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
        mProductRevampComponentViewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mProductRevampComponentViewModel?.getSyncPageLiveData()?.observe(it) { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
            mProductRevampComponentViewModel?.getProductCarouselHeaderData()?.observe(it) { component ->
                addCardHeader(component)
            }
        }
    }

    private fun addCardHeader(componentsItem: ComponentsItem) {
        mHeaderView.show()
        mHeaderView.removeAllViews()
        mHeaderView.addView(CustomViewCreator.getCustomViewObject(itemView.context, ComponentsList.LihatSemua, componentsItem, fragment))
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductRevampComponentViewModel?.getSyncPageLiveData()?.removeObservers(it)
        }
    }
}
