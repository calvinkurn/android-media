package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

class CarouselErrorLoadViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var carouselErrorLoadViewModel: CarouselErrorLoadViewModel? = null
    private var errorLoadMore: ImageUnify = itemView.findViewById(R.id.errorLoadMore)
    private var progressLoader: LoaderUnify = itemView.findViewById(R.id.progressLoader)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselErrorLoadViewModel = discoveryBaseViewModel as CarouselErrorLoadViewModel
        carouselErrorLoadViewModel?.let {
            getSubComponent().inject(it)
        }
        setLoaderView()
    }

    private fun setLoaderView() {
        errorLoadMore.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE
        errorLoadMore.setOnClickListener {
            carouselErrorLoadViewModel?.loadData()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            carouselErrorLoadViewModel?.getSyncPageLiveData()?.observe(lifecycleOwner) {
                carouselErrorLoadViewModel?.getParentComponentPosition()?.let { it1 -> (fragment as DiscoveryFragment).refreshCarouselData(it1) }
            }
            carouselErrorLoadViewModel?.getShowLoaderStatus()?.observe(lifecycleOwner) {
                if (it) {
                    errorLoadMore.visibility = View.GONE
                    progressLoader.visibility = View.VISIBLE
                } else {
                    errorLoadMore.visibility = View.VISIBLE
                    progressLoader.visibility = View.GONE
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            carouselErrorLoadViewModel?.getSyncPageLiveData()?.removeObservers(it)
            carouselErrorLoadViewModel?.getShowLoaderStatus()?.removeObservers(it)
        }
    }
}
