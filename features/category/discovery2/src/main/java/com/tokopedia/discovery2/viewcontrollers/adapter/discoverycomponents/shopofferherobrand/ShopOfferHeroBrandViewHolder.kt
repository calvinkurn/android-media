package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.databinding.ItemDiscoveryShopOfferHeroBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_DISCO_SHOP_OFFER_BRAND_SEE_MORE_BUY_MORE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import okhttp3.Route
import kotlin.collections.ArrayList

class ShopOfferHeroBrandViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        const val PREFETCH_ITEM_COUNT = 4
    }

    internal val mLayoutManager: LinearLayoutManager
        by lazy { LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false) }

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy { DiscoveryRecycleAdapter(fragment) }

    private val binding: ItemDiscoveryShopOfferHeroBrandLayoutBinding?
        by viewBinding()

    private var viewModel: ShopOfferHeroBrandViewModel? = null

    init {
        binding?.setupRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ShopOfferHeroBrandViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }

        if (mAdapter.itemCount.isZero() || viewModel?.getProductList().isNullOrEmpty()) {
            addShimmer()
        }

        binding?.showProductCarousel()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.apply {
            header.observe(lifecycleOwner) { header ->
                binding?.showHeader(header)
            }

            productList.observe(lifecycleOwner) { item ->
                when (item) {
                    is Success -> mAdapter.setDataList(item.data)
                    is Fail -> handleErrorState()
                }
            }

            syncData.observe(lifecycleOwner) { sync ->
                if (sync) {
                    mAdapter.notifyDataSetChanged()
                }
            }

            productMaxHeight.observe(lifecycleOwner) { height ->
                setMaxHeight(height)
            }

            tierChange.observe(lifecycleOwner) {
                binding?.showProgressBar(it)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.apply {
            productList.removeObservers(lifecycleOwner)
            productMaxHeight.removeObservers(lifecycleOwner)
            header.removeObservers(lifecycleOwner)
        }
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.setupRecyclerView() {
        mAdapter.setHasStableIds(true)
        rvProductCarousel.layoutManager = mLayoutManager
        rvProductCarousel.adapter = mAdapter
        mLayoutManager.initialPrefetchItemCount = PREFETCH_ITEM_COUNT
        addItemDecorator()
        addScrollListener()
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.addScrollListener() {
        if (viewModel?.shouldShowViewAllCard() == true) return

        rvProductCarousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = mLayoutManager.childCount
                    val totalItemCount: Int = mLayoutManager.itemCount
                    val firstVisibleItemPosition: Int = mLayoutManager.findFirstVisibleItemPosition()
                    viewModel?.let { mProductCarouselComponentViewModel ->
                        if (!mProductCarouselComponentViewModel.isLoadingData() && mProductCarouselComponentViewModel.hasNextPage()) {
                            if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= mProductCarouselComponentViewModel.getPageSize()) {
                                mProductCarouselComponentViewModel.fetchCarouselPaginatedProducts()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.addItemDecorator() {
        if (rvProductCarousel.itemDecorationCount.isMoreThanZero()) {
            rvProductCarousel.removeAllItemDecoration()
        }
        rvProductCarousel.addItemDecoration(CarouselProductCardItemDecorator())
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.showHeader(
        header: Properties.Header?
    ) {
        if (header == null) {
            sivShopIcon.hide()
            tpShopName.hide()
            tpShopTierWording.hide()
            sivSeeAll.hide()
            headerSpace.hide()
        } else {
            sivShopIcon.show()
            tpShopName.show()
            tpShopTierWording.show()
            sivSeeAll.show()
            headerSpace.show()

            sivShopIcon.loadImage(header.shopIcon.orEmpty())
            tpShopName.text = header.shopName.orEmpty().toDecodedString()
            tpShopTierWording.text = header.offerTiers?.firstOrNull()?.tierWording.orEmpty().toDecodedString()

            if (header.applink.isNullOrBlank()) return

            sivSeeAll.route(header.applink)
            sivShopIcon.route(header.applink)
            tpShopName.route(header.applink)
            tpShopTierWording.route(header.applink)
        }
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.showProgressBar(
        tierData: TierData
    ) {
        if (tierData.isProgressBarShown) {
            progressBar.show()
            progressBarSpace.show()
            if (tierData.isShimmerShown) {
                iuProgressBarIcon.hide()
                tpProgressBarTierWording.hide()

                progressBarShimmer.show()
                progressBarLayout.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            } else {
                iuProgressBarIcon.show()
                iuProgressBarIcon.loadImage(IMG_DISCO_SHOP_OFFER_BRAND_SEE_MORE_BUY_MORE)

                tpProgressBarTierWording.show()
                tpProgressBarTierWording.text = tierData.tierWording

                progressBarSpace.show()
                progressBarShimmer.hide()
                progressBarLayout.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_TN100))
            }
        } else {
            progressBar.hide()
            progressBarSpace.hide()
        }
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.showProductCarousel() {
        rvProductCarousel.show()
        localLoad.hide()
        errorHolder.gone()
    }

    private fun View.route(
        appLink: String?
    ) {
        if (appLink.isNullOrBlank()) return

        setOnClickListener {
            RouteManager.route(fragment.context, appLink)
        }
    }

    private fun setMaxHeight(height: Int) {
        binding?.apply {
            val carouselLayoutParams = rvProductCarousel.layoutParams
            carouselLayoutParams?.height = height
            rvProductCarousel.layoutParams = carouselLayoutParams
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mAdapter.setDataList(list)
    }

    private fun handleErrorState() {
        binding?.apply {
            addShimmer()
            mAdapter.notifyDataSetChanged()

            if (viewModel?.getProductList() == null) {
                localLoad.run {
                    title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                    description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                    refreshBtn?.setOnClickListener {
                        reloadComponent()
                    }
                    localLoad?.visible()
                    rvProductCarousel.gone()
                    errorHolder.gone()
                }
            } else if (viewModel?.getProductList()?.isEmpty() == true &&
                viewModel?.areFiltersApplied() == true
            ) {
                if (errorHolder.childCount > 0) {
                    errorHolder.removeAllViews()
                }
                errorHolder.addView(
                    viewModel?.getErrorStateComponent()?.let {
                        CustomViewCreator.getCustomViewObject(
                            itemView.context,
                            ComponentsList.ProductListEmptyState,
                            it,
                            fragment
                        )
                    }
                )
                errorHolder.show()
                localLoad.gone()
                rvProductCarousel.gone()
            }
        }
    }

    private fun reloadComponent() {
        binding?.rvProductCarousel?.visible()
        binding?.localLoad?.gone()
        viewModel?.resetComponent()
        viewModel?.loadFirstPageProductCarousel()
    }
}
