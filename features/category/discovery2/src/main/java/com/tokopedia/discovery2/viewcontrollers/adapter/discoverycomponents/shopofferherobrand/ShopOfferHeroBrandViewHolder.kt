package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.animation.AnimatorSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Space
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.flipImage
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.Utils.Companion.verticalScrollAnimation
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.PRODUCT_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandComponentExtension.getErrorStateComponent
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.BmGmTierData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.TierData
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_DISCO_SHOP_OFFER_BRAND_SEE_MORE_BUY_MORE
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.ANDROID_MAIN_APP_ENABLE_DISCO_SHOP_OFFER_HERO_BRAND
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopOfferHeroBrandViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        private const val PREFETCH_ITEM_COUNT = 4
        private const val SHOP_TIER_WORDING_ANIMATION_DURATION = 2000L
        private const val SHOP_TIER_WORDING_SEPARATOR = "<br/>"
        private const val SHOP_TIER_WORDING_DELAY_DURATION = 500L
        private const val PROGRESS_BAR_TIER_DELAY = 2500L
        private const val PROGRESS_BAR_SHIMMERING_DELAY = 500L
        private const val NO_ANIMATION_SIZE = 1
    }

    internal val mLayoutManager: LinearLayoutManager
        by lazy { LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false) }

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy { DiscoveryRecycleAdapter(fragment) }

    private val coroutineScope
        by lazy { CoroutineScope(Dispatchers.Main) }

    private val remoteConfig: RemoteConfig by lazy(LazyThreadSafetyMode.NONE) {
        FirebaseRemoteConfigImpl(fragment.context)
    }

    private val rootLayout = itemView.findViewById<ConstraintLayout>(R.id.layout_root)
    private val progressBarLayout = itemView
        .findViewById<ConstraintLayout>(R.id.progress_bar_layout)
    private val errorHolder = itemView.findViewById<FrameLayout>(R.id.error_holder)

    private val rvProductCarousel = itemView.findViewById<RecyclerView>(R.id.rv_product_carousel)
    private val sivShopIcon = itemView.findViewById<ShapeableImageView>(R.id.siv_shop_icon)
    private val sivShopIconRounded =
        itemView.findViewById<ShapeableImageView>(R.id.siv_shop_icon_rounded)
    private val sivShadowShopIcon =
        itemView.findViewById<ShapeableImageView>(R.id.siv_shadow_shop_icon)
    private val layoutShopIcon = itemView.findViewById<ConstraintLayout>(R.id.layout_shop_icon)
    private val sivShopBadge = itemView.findViewById<ShapeableImageView>(R.id.siv_shop_badge)
    private val layoutShopInfo = itemView.findViewById<ConstraintLayout>(R.id.layout_shop_info)
    private val sivSeeAll = itemView.findViewById<ShapeableImageView>(R.id.siv_see_all)
    private val headerSpace = itemView.findViewById<Space>(R.id.header_space)
    private val progressBar = itemView.findViewById<CardView>(R.id.progress_bar)
    private val progressBarSpace = itemView.findViewById<Space>(R.id.progress_bar_space)

    private val tpShopName = itemView.findViewById<Typography>(R.id.tp_shop_name)
    private val tpShopTierWording = itemView.findViewById<Typography>(R.id.tp_shop_tier_wording)
    private val tpProgressBarTierWording = itemView
        .findViewById<Typography>(R.id.tp_progress_bar_tier_wording)

    private val localLoad = itemView.findViewById<LocalLoad>(R.id.local_load)
    private val progressBarShimmer = itemView.findViewById<LoaderUnify>(R.id.progress_bar_shimmer)
    private val iuProgressBarIcon = itemView.findViewById<ImageUnify>(R.id.iu_progress_bar_icon)

    private var viewModel: ShopOfferHeroBrandViewModel? = null
    private var progressBarJob: Job? = null
    private var hasRunScrollingAnimation: Boolean = false
    private var hasRunFlipGwpAnimation: Boolean = false
    private var currentBmGmTierData: BmGmTierData? = null

    init {
        if (isShopOfferHeroBrandEnabled()) {
            setupRecyclerView()
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        if (isShopOfferHeroBrandEnabled()) {
            viewModel = discoveryBaseViewModel as ShopOfferHeroBrandViewModel
            viewModel?.let {
                getSubComponent().inject(it)
            }

            if (mAdapter.itemCount.isZero() || viewModel?.getProductList().isNullOrEmpty()) {
                addShimmer()
            }

            showProductCarousel()
        }
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
                showHeader(header)
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
                executeShopTierWordingAnimation()
                showProgressBar(it)
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

    private fun setupRecyclerView() {
        mAdapter.setHasStableIds(true)
        rvProductCarousel.layoutManager = mLayoutManager
        rvProductCarousel.adapter = mAdapter
        mLayoutManager.initialPrefetchItemCount = PREFETCH_ITEM_COUNT
        addItemDecorator()
        addScrollListener()
    }

    private fun executeShopTierWordingAnimation() {
        if (!hasRunScrollingAnimation || !hasRunFlipGwpAnimation) {
            tpShopTierWording.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (tpShopTierWording.lineCount.isMoreThanZero()) {
                            tpShopTierWording.postDelayed({
                                val animatorSet = AnimatorSet()
                                val firstAnimation = tpShopTierWording.verticalScrollAnimation(
                                    duration = SHOP_TIER_WORDING_ANIMATION_DURATION,
                                    isReverse = false,
                                    isFromHtml = true
                                )
                                val secondAnimation = tpShopTierWording.verticalScrollAnimation(
                                    duration = SHOP_TIER_WORDING_ANIMATION_DURATION,
                                    isReverse = viewModel?.isGwp() == false,
                                    isFromHtml = true
                                )
                                if (viewModel?.isGwp() == true) {
                                    animatorSet.play(secondAnimation)
                                } else {
                                    animatorSet.playSequentially(firstAnimation, secondAnimation)
                                }
                                animatorSet.start()
                                hasRunFlipGwpAnimation = viewModel?.isGwp() == true
                                hasRunScrollingAnimation = true
                            }, SHOP_TIER_WORDING_DELAY_DURATION)
                        }
                        tpShopTierWording.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )
        }
    }

    private fun addScrollListener() {
        if (viewModel?.hasHeader() == false) return

        rvProductCarousel.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = mLayoutManager.childCount
                    val totalItemCount: Int = mLayoutManager.itemCount
                    val firstVisibleItemPosition: Int =
                        mLayoutManager.findFirstVisibleItemPosition()
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

    private fun addItemDecorator() {
        if (rvProductCarousel.itemDecorationCount.isMoreThanZero()) {
            rvProductCarousel.removeAllItemDecoration()
        }
        rvProductCarousel.addItemDecoration(CarouselProductCardItemDecorator())
    }

    private fun showHeader(
        header: Properties.Header?
    ) {
        if (header == null) {
            sivShopIcon.hide()
            sivShopIconRounded.hide()
            tpShopName.hide()
            tpShopTierWording.hide()
            sivSeeAll.hide()
            headerSpace.hide()
        } else {
            renderShopIcon(header)
            tpShopName.show()
            tpShopTierWording.show()
            sivSeeAll.show()
            headerSpace.show()
            tpShopName.text = header.shopName.orEmpty().toDecodedString()
            if (viewModel?.isGwp() == false) {
                tpShopTierWording.text = getFirstLastOfferTiers(header.offerTiers)
                executeShopTierWordingAnimation()
            } else {
                tpShopTierWording.text = setFlippingWordGwp(
                    listOf(
                        viewModel?.header?.value?.offerTiers?.firstOrNull()?.tierWording.orEmpty(),
                        viewModel?.header?.value?.atcWording.orEmpty()
                    )
                )
            }
            if (header.shopBadge.isNullOrEmpty()) {
                sivShopBadge.gone()
            } else {
                sivShopBadge.loadImage(header.shopBadge)
            }

            if (header.applink.isNullOrBlank()) return

            sivSeeAll.route(header.applink)
            tpShopName.route(header.applink)
            tpShopTierWording.route(header.applink)
        }
    }

    private fun renderShopIcon(header: Properties.Header) {
        if (viewModel?.isGwp() == false) {
            sivShopIcon.show()
            sivShadowShopIcon.gone()
            sivShopIconRounded.gone()
            sivShopIcon.route(header.applink)
            sivShopIcon.loadImage(header.shopIcon.orEmpty())
        } else {
            sivShadowShopIcon.isVisible = (header.offerTiers?.size ?: 0) > 1
            sivShopIconRounded.show()
            sivShadowShopIcon.visible()
            sivShopIconRounded.loadImage(header.offerTiers?.firstOrNull()?.image.orEmpty())
            sivShopIconRounded.route(header.applink)
            currentBmGmTierData = BmGmTierData(
                offerMessages = listOf(),
                flipTierImage = header.offerTiers?.firstOrNull()?.image.orEmpty(),
                flipTierWording = header.offerTiers?.firstOrNull()?.tierWording.orEmpty()
            )
        }
    }

    private fun flipImageTier(tierImage: String) {
        layoutShopIcon.flipImage(sivShopIconRounded, tierImage)
    }

    private fun showProgressBar(
        tierData: TierData
    ) {
        progressBarJob?.cancel()
        if (tierData.isProgressBarShown) {
            when {
                tierData.isShimmerShown -> {
                    showProgressBarShimmering()
                }

                tierData.offerMessages.isNullOrEmpty() -> {
                    hideProgressBar()
                }

                else -> {
                    showProgressBarInfo()
                    tpProgressBarTierWording.text =
                        MethodChecker.fromHtml(tierData.offerMessages.firstOrNull())
                    if (tierData.offerMessages.size.isMoreThanZero()) {
                        changeProgressBarInfoWithDelay(tierData.offerMessages)
                    }
                    if (viewModel?.isGwp() == true) {
                        flipTier(tierData)
                    }
                }
            }
        } else {
            hideProgressBar()
        }
    }

    private fun flipTier(tierData: TierData) {
        if (currentBmGmTierData?.flipTierImage != tierData.flipTierImage) {
            flipImageTier(tierData.flipTierImage)
        }
        currentBmGmTierData = BmGmTierData(
            offerMessages = tierData.offerMessages,
            flipTierImage = tierData.flipTierImage,
            flipTierWording = tierData.flipTierWording
        )
    }

    private fun showProgressBarShimmering() {
        progressBar.show()
        progressBarSpace.show()
        iuProgressBarIcon.hide()
        tpProgressBarTierWording.hide()
        progressBarShimmer.show()
        progressBarLayout.setBackgroundColor(
            MethodChecker.getColor(
                itemView.context,
                unifyprinciplesR.color.Unify_NN0
            )
        )
    }

    private fun showProgressBarInfo() {
        progressBar.show()
        progressBarSpace.show()

        iuProgressBarIcon.show()
        iuProgressBarIcon.loadImage(IMG_DISCO_SHOP_OFFER_BRAND_SEE_MORE_BUY_MORE)

        progressBarSpace.show()
        progressBarShimmer.hide()
        progressBarLayout.setBackgroundColor(
            MethodChecker.getColor(
                itemView.context,
                unifyprinciplesR.color.Unify_TN100
            )
        )

        tpProgressBarTierWording.show()
    }

    private fun hideProgressBar() {
        progressBar.hide()
        progressBarSpace.hide()
    }

    private fun changeProgressBarInfoWithDelay(
        list: List<String>?
    ) {
        progressBarJob = coroutineScope.launch {
            list?.forEachIndexed { index, offerMessage ->
                if (index.isMoreThanZero()) {
                    delay(PROGRESS_BAR_TIER_DELAY)

                    showProgressBarShimmering()

                    delay(PROGRESS_BAR_SHIMMERING_DELAY)

                    showProgressBarInfo()

                    tpProgressBarTierWording.text = MethodChecker.fromHtml(offerMessage)
                }
            }
        }
    }

    private fun showProductCarousel() {
        rvProductCarousel.show()
        localLoad.hide()
        errorHolder.gone()
    }

    private fun showLocalLoad() {
        localLoad.run {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text =
                context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
            refreshBtn?.setOnClickListener {
                progressState = !progressState
                reloadComponent()
            }
            show()
        }
        rvProductCarousel.gone()
        errorHolder.gone()
    }

    private fun reloadComponent() {
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
            viewModel?.let {
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackEventProductBmGmClickSeeMore(it.component)
            }
            RouteManager.route(fragment.context, appLink)
        }
    }

    private fun getFirstLastOfferTiers(
        list: List<Properties.Header.OfferTier>?
    ): CharSequence? {
        val offerTiers = list?.map { it.tierWording } ?: return String.EMPTY

        return if (offerTiers.size > NO_ANIMATION_SIZE) {
            val newOfferTiers = arrayListOf(offerTiers.first(), offerTiers.last())
            MethodChecker.fromHtml(newOfferTiers.joinToString(SHOP_TIER_WORDING_SEPARATOR))
        } else {
            MethodChecker.fromHtml(offerTiers.joinToString())
        }
    }

    private fun setFlippingWordGwp(listWords: List<String>): CharSequence {
        val flippingWording = arrayListOf(listWords.first(), listWords.last())
        return MethodChecker.fromHtml(flippingWording.joinToString(SHOP_TIER_WORDING_SEPARATOR))
    }

    private fun setMaxHeight(height: Int) {
        val carouselLayoutParams = rvProductCarousel.layoutParams
        carouselLayoutParams?.height = height
        rvProductCarousel.layoutParams = carouselLayoutParams
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mAdapter.setDataList(list)
    }

    private fun handleSuccessState(components: ArrayList<ComponentsItem>) {
        if (components.isNotEmpty()) {
            mAdapter.setDataList(components)
            viewModel?.getHeader()
        } else {
            rootLayout.hide()
        }
    }

    private fun handleFailState() {
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
            } else {
                errorHolder.gone()
                localLoad.gone()
                rvProductCarousel.gone()
            }
        }
    }

    private fun isShopOfferHeroBrandEnabled(): Boolean {
        return remoteConfig.getBoolean(ANDROID_MAIN_APP_ENABLE_DISCO_SHOP_OFFER_HERO_BRAND, true)
    }
}
