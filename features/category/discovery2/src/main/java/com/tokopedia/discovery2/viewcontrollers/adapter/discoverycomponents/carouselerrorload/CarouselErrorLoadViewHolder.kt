package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

class CarouselErrorLoadViewHolder(itemView: View, private val fragment: Fragment) :
        AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var carouselErrorLoadViewModel: CarouselErrorLoadViewModel
    private var errorLoadMore: ImageUnify = itemView.findViewById(R.id.errorLoadMore)
    private var progressLoader: LoaderUnify = itemView.findViewById(R.id.progressLoader)


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselErrorLoadViewModel = discoveryBaseViewModel as CarouselErrorLoadViewModel
        with(itemView.context) {
            if (this is DiscoveryActivity) {
                this.discoveryComponent.provideSubComponent()
                        .inject(carouselErrorLoadViewModel)
            }
        }
        setLoaderView()
    }

    private fun setLoaderView() {
        errorLoadMore.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE
        errorLoadMore.setOnClickListener {
            carouselErrorLoadViewModel.loadData()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            carouselErrorLoadViewModel.getSyncPageLiveData().observe(lifecycleOwner, {
                (fragment as DiscoveryFragment).refreshCarouselData(carouselErrorLoadViewModel.getParentComponentPosition())
            })
            carouselErrorLoadViewModel.getShowLoaderStatus().observe(lifecycleOwner, {
                if (it) {
                    errorLoadMore.visibility = View.GONE
                    progressLoader.visibility = View.VISIBLE
                } else {
                    errorLoadMore.visibility = View.VISIBLE
                    progressLoader.visibility = View.GONE
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            carouselErrorLoadViewModel.getSyncPageLiveData().removeObservers(it)
            carouselErrorLoadViewModel.getShowLoaderStatus().removeObservers(it)
        }
    }
}