package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.animation.AnimatorSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.Utils.Companion.verticalScrollAnimation
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.databinding.ItemDiscoveryShopOfferHeroBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.PRODUCT_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandComponentExtension.getErrorStateComponent
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.TierData
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
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopOfferHeroBrandViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        private const val PREFETCH_ITEM_COUNT = 4
        private const val SHOP_TIER_DURATION_ANIMATION = 2000L
    }

    internal val mLayoutManager: LinearLayoutManager
        by lazy { LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false) }

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy { DiscoveryRecycleAdapter(fragment) }

    private val binding: ItemDiscoveryShopOfferHeroBrandLayoutBinding?
        by viewBinding()

    private var viewModel: ShopOfferHeroBrandViewModel? = null

    init {
        binding?.apply {
            setupRecyclerView()
            setupShopTierWordingAnimation()
        }
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
            syncData.observe(lifecycleOwner) { sync ->
                if (sync) {
                    syncData()
                }
            }

            header.observe(lifecycleOwner) { header ->
                binding?.showHeader(header)
            }

            productList.observe(lifecycleOwner) { item ->
                when (item) {
                    is Success -> handleSuccessState(item.data)
                    is Fail -> handleFailState()
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

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.setupShopTierWordingAnimation() {
        tpShopTierWording.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (tpShopTierWording.lineCount.isMoreThanZero()) {
                        val animatorSet = AnimatorSet()

                        val firstAnimation = tpShopTierWording.verticalScrollAnimation(
                            duration = SHOP_TIER_DURATION_ANIMATION,
                            isReverse = false
                        )
                        val secondAnimation = tpShopTierWording.verticalScrollAnimation(
                            duration = SHOP_TIER_DURATION_ANIMATION,
                            isReverse = true
                        )

                        animatorSet.playSequentially(
                            firstAnimation,
                            secondAnimation
                        )

                        animatorSet.start()
                    }
                    tpShopTierWording.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.addScrollListener() {
        if (viewModel?.hasHeader() == false) return

        rvProductCarousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = mLayoutManager.childCount
                    val totalItemCount: Int = mLayoutManager.itemCount
                    val firstVisibleItemPosition: Int = mLayoutManager.findFirstVisibleItemPosition()
                    viewModel?.let { mProductCarouselComponentViewModel ->
                        if (!mProductCarouselComponentViewModel.isLoading && mProductCarouselComponentViewModel.hasNextPage()) {
                            if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= PRODUCT_PER_PAGE) {
                                mProductCarouselComponentViewModel.loadMore()
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
            tpShopTierWording.text = header.offerTiers?.joinToString("\n") { MethodChecker.fromHtml(it.tierWording) }

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
                progressBarLayout.setBackgroundColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN0))
            } else {
                iuProgressBarIcon.show()
                iuProgressBarIcon.loadImage(IMG_DISCO_SHOP_OFFER_BRAND_SEE_MORE_BUY_MORE)

                tpProgressBarTierWording.show()
                tpProgressBarTierWording.text = tierData.tierWording

                progressBarSpace.show()
                progressBarShimmer.hide()
                progressBarLayout.setBackgroundColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_TN100))
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

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.showLocalLoad() {
        localLoad.run {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
            refreshBtn?.setOnClickListener {
                progressState = !progressState
                reloadComponent()
            }
            show()
        }
        rvProductCarousel.gone()
        errorHolder.gone()
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.reloadComponent() {
        rvProductCarousel.show()
        localLoad.hide()
        viewModel?.apply {
            resetComponent()
            loadFirstPageProductCarousel()
        }
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

    private fun handleSuccessState(components: ArrayList<ComponentsItem>) {
        if (components.isNotEmpty()) mAdapter.setDataList(components) else binding?.root?.hide()
    }

    private fun handleFailState() {
        binding?.apply {
            viewModel?.apply {
                val productList = getProductList()
                if (!productList.isNullOrEmpty()) {
                    showLocalLoad()
                } else if (productList.isNullOrEmpty() && areFiltersApplied()) {
                    if (errorHolder.childCount.isMoreThanZero()) {
                        errorHolder.removeAllViews()
                    }
                    errorHolder.addView(
                        getErrorStateComponent(component).let {
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
    }
}
