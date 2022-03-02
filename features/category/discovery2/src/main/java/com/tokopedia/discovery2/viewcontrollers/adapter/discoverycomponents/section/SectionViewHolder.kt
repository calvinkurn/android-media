package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad

class SectionViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    lateinit var viewModel: SectionViewModel
    val shimmer: ConstraintLayout = itemView.findViewById(R.id.section_shimmer)
    private var carouselEmptyState: LocalLoad = itemView.findViewById(R.id.viewEmptyState)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as SectionViewModel
        getSubComponent().inject(viewModel)
        shimmer.showWithCondition(viewModel.shouldShowShimmer())
        carouselEmptyState.showWithCondition(viewModel.shouldShowError())
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel.hideSection.observe(it,{ shouldHideSection ->
                if(shouldHideSection){
                    (fragment as DiscoveryFragment).handleHideSection(viewModel.getSectionID())
                }
            })
            viewModel.getSyncPageLiveData().observe(it, { shouldSync ->
                if (shouldSync)
                    (fragment as DiscoveryFragment).reSync()
            })

            viewModel.hideShimmerLD.observe(it, { shouldHideShimmer ->
                if (shouldHideShimmer) {
                    shimmer.hide()
                    carouselEmptyState.hide()
                }
            })

            viewModel.showErrorState.observe(it, { shouldShowError ->
                if (shouldShowError) {
                    handleError()
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel.hideSection.removeObservers(it)
            viewModel.getSyncPageLiveData().removeObservers(it)
            viewModel.hideShimmerLD.removeObservers(it)
            viewModel.showErrorState.removeObservers(it)
        }
    }

    private fun handleError() {
        carouselEmptyState.apply {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text =
                context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
            refreshBtn?.setOnClickListener {
                hide()
                shimmer.show()
                viewModel.reload()
            }
        }
        carouselEmptyState.visible()
        shimmer.hide()
    }
}