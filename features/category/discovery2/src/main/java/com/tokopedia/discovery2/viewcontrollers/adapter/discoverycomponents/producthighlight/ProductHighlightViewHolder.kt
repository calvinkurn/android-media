package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.MultiBannerLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.LocalLoad
import kotlinx.android.synthetic.main.item_empty_error_state.view.*

class ProductHighlightViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: MultiBannerLayoutBinding = MultiBannerLayoutBinding.bind(itemView)
    private var mProductHighlightViewModel: ProductHighlightViewModel? = null
    private var bannerName: String = ""
    var productHighlightItemList: ArrayList<ProductHighlightItem> = arrayListOf()

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductHighlightViewModel = discoveryBaseViewModel as ProductHighlightViewModel
        mProductHighlightViewModel?.let { viewModel ->
            getSubComponent().inject(viewModel)
        }
        addShimmer()
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductHighlightViewModel?.getProductHighlightCardItemsListData()?.observe(it) { item ->
                if (!item.data.isNullOrEmpty()) {
                    binding.bannerContainerLayout.removeAllViews()
                    productHighlightItemList = ArrayList()
                    bannerName = item?.name ?: ""
                    addProductHighlightCard(item.data!!, item.properties?.type)
                }
            }

            mProductHighlightViewModel?.showErrorState?.observe(it) { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            }

            mProductHighlightViewModel?.hideShimmer?.observe(it) { shouldShowShimmer ->
                if (shouldShowShimmer) {
                    binding.bannerContainerLayout.removeAllViews()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductHighlightViewModel?.getProductHighlightCardItemsListData()?.removeObservers(it)
            mProductHighlightViewModel?.showErrorState?.removeObservers(it)
            mProductHighlightViewModel?.hideShimmer?.removeObservers(it)
        }
    }

    private fun addProductHighlightCard(data: List<DataItem>, compType: String?) {
        val constraintSet = ConstraintSet()
        val itemWeight = 1.0f
        val properties = mProductHighlightViewModel?.components?.properties

        for ((index, productHighlightItem) in data.withIndex()) {
            var productHighlightView: ProductHighlightItem
            val isLastItem = index == data.size - 1
            if (productHighlightItem.parentComponentName.isNullOrEmpty()) {
                productHighlightItem.parentComponentName = bannerName
            }
            mProductHighlightViewModel?.let { viewModel ->
                productHighlightItem.positionForParentItem = viewModel.position
            }
            if (index == 0) {
                productHighlightView = ProductHighlightItem(
                    productHighlightItem, properties, binding.bannerContainerLayout, constraintSet, itemWeight, index,
                    null, itemView.context, isLastItem, compType
                )
            } else {
                productHighlightView = ProductHighlightItem(
                    productHighlightItem, properties, binding.bannerContainerLayout, constraintSet, itemWeight, index,
                    productHighlightItemList[index - 1], itemView.context, isLastItem, compType
                )
            }
            productHighlightItemList.add(productHighlightView)

            setClickOnProductHighlight(productHighlightItem, index)
        }
        sendImpressionEventForProductHighlight(data)
    }

    private fun sendImpressionEventForProductHighlight(data: List<DataItem>) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackPromoProductHighlightImpression(
            data
        )
    }

    private fun setClickOnProductHighlight(itemData: DataItem, index: Int) {
        productHighlightItemList[index].productHighlightView.setOnClickListener {
            mProductHighlightViewModel?.onCardClicked(index, itemView.context)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                ?.trackProductHighlightClick(itemData, index, mProductHighlightViewModel?.components)
        }
    }

    private fun handleErrorState() {
        with(binding) {
            bannerContainerLayout.removeAllViews()
            val emptyStateParentView = bannerContainerLayout.inflateLayout(R.layout.item_empty_error_state, false)
            val emptyStateView: LocalLoad = emptyStateParentView.findViewById(R.id.viewEmptyState)
            emptyStateView.apply {
                val errorLoadUnifyView = emptyStateView.viewEmptyState
                errorLoadUnifyView.title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                errorLoadUnifyView.description?.text =
                    context?.getString(R.string.discovery_product_empty_state_description).orEmpty()
                errorLoadUnifyView.refreshBtn?.setOnClickListener {
                    hide()
                    bannerContainerLayout.removeAllViews()
                    val shimmerView = mProductHighlightViewModel?.layoutSelector()?.let { it1 -> bannerContainerLayout.inflateLayout(it1, false) }
                    bannerContainerLayout.addView(shimmerView)
                    mProductHighlightViewModel?.reload()
                }
            }
            emptyStateView.isVisible = true
            bannerContainerLayout.addView(emptyStateParentView)
        }
    }

    private fun addShimmer() {
        mProductHighlightViewModel?.let { viewModel ->
            if (viewModel.shouldShowShimmer()) {
                with(binding) {
                    bannerContainerLayout.removeAllViews()
                    val shimmerView = bannerContainerLayout.inflateLayout(viewModel.layoutSelector(), false)
                    bannerContainerLayout.addView(shimmerView)
                }
            }
        }
    }


}
