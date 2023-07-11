package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.LocalLoad

class ProductCardSingleViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    var viewModel: ProductCardSingleViewModel? = null
    private val masterProdFrameLayout: FrameLayout = itemView.findViewById(R.id.master_prod)
    private val backgroundImage: ImageView = itemView.findViewById(R.id.master_prod_bg)
    private val shimmer: CardView = itemView.findViewById(R.id.shimmer_card)
    private var carouselEmptyState: LocalLoad = itemView.findViewById(R.id.viewEmptyState)

    private var productViewHolder: AbstractViewHolder =
        DiscoveryHomeFactory.createViewHolder(
            View.inflate(
                fragment.context,
                ComponentsList.ProductCardSingleItem.id,
                masterProdFrameLayout
            ),
            ComponentsList.ProductCardSingleItem.ordinal,
            fragment
        ) as AbstractViewHolder
    private var productViewModel: DiscoveryBaseViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ProductCardSingleViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }
        shimmer.show()
        masterProdFrameLayout.hide()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel?.getProductData()?.observe(lifecycle) {
                handleProductData(it)
            }

            viewModel?.getMixLeftData()?.observe(lifecycle) { mixLeft ->
                setupBackgroundData(mixLeft)
            }

            viewModel?.showErrorState?.observe(lifecycle) { showError ->
                if (showError) {
                    handleError()
                }
            }
            viewModel?.hideView?.observe(lifecycle) { hideView ->

                if (hideView) {
                    hideViewAndShimmer()
                }
            }
        }
    }

    private fun hideViewAndShimmer() {
        shimmer.hide()
        masterProdFrameLayout.hide()
    }

    private fun setupBackgroundData(mixLeft: MixLeft?) {
        if (mixLeft != null && !(mixLeft.backgroundImageUrl.isNullOrEmpty())) {
            try {
                backgroundImage.loadImageWithoutPlaceholder(mixLeft.backgroundImageUrl)
                if (!mixLeft.backgroundColor.isNullOrEmpty()) {
                    backgroundImage.setColorFilter(Color.parseColor(mixLeft.backgroundColor))
                }
                backgroundImage.show()
            } catch (e: Exception) {
                backgroundImage.hide()
            }
        } else {
            backgroundImage.hide()
        }
    }

    private fun handleProductData(productData: ComponentsItem?) {
        if (productData == null) {
            masterProdFrameLayout.hide()
            shimmer.hide()
        } else {
            if (fragment.context?.applicationContext as? Application != null) {
                masterProdFrameLayout.show()
                shimmer.hide()
                productViewModel =
                    DiscoveryHomeFactory.createViewModel(ComponentsList.ProductCardSingleItem.ordinal)(
                        fragment.context?.applicationContext as Application,
                        productData,
                        0
                    )
                productViewModel?.let {
                    productViewHolder.bindView(it)
                }
                productViewModel?.onAttachToViewHolder()
                productViewHolder.onViewAttachedToWindow()
                productViewHolder.setUpObservers(fragment.viewLifecycleOwner)
            }
        }
    }

    private fun handleError() {
        hideViewAndShimmer()
        carouselEmptyState.apply {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text =
                context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
            refreshBtn?.setOnClickListener {
                carouselEmptyState.hide()
                shimmer.show()
                viewModel?.reload()
            }
        }
        carouselEmptyState.visible()
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getProductData()?.removeObservers(it)
            viewModel?.getMixLeftData()?.removeObservers(it)
            viewModel?.showErrorState?.removeObservers(it)
            viewModel?.hideView?.removeObservers(it)
            if (productViewModel != null) {
                productViewHolder.removeObservers(it)
            }
        }
    }
}
