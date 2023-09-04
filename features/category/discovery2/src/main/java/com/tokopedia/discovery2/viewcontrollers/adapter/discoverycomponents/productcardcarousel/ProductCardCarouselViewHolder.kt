package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.LocalLoad
import kotlin.math.abs

class ProductCardCarouselViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mixLeftData: MixLeft? = null
    private var mProductCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.products_rv)
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private var mProductCarouselComponentViewModel: ProductCardCarouselViewModel? = null
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator()
    private var carouselEmptyState: LocalLoad? = null
    private var errorHolder: FrameLayout = itemView.findViewById(R.id.filter_error_view)
    private var mixLeftBanner: ImageView = itemView.findViewById(R.id.parallax_image)
    private var mixLeftBannerBG: ImageView = itemView.findViewById(R.id.banner_background_image)
    private var mixLeftBannerCard: CardView = itemView.findViewById(R.id.parallax_image_card)
    private var backgroundImage: ImageView = itemView.findViewById(R.id.background_image)

    init {
        linearLayoutManager.initialPrefetchItemCount = PREFETCH_ITEM_COUNT
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
        carouselEmptyState = itemView.findViewById(R.id.viewEmptyState)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductCarouselComponentViewModel = discoveryBaseViewModel as ProductCardCarouselViewModel
        mProductCarouselComponentViewModel?.let {
            getSubComponent().inject(it)
        }
        if (mDiscoveryRecycleAdapter.itemCount == 0 || mProductCarouselComponentViewModel?.getProductList().isNullOrEmpty()) {
            addShimmer()
        }
        mProductCarouselRecyclerView.show()
        carouselEmptyState?.hide()
        errorHolder.gone()
        addDefaultItemDecorator()
        handleCarouselPagination()
    }

    private fun handleCarouselPagination() {
        mProductCarouselRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
                mProductCarouselComponentViewModel?.let { mProductCarouselComponentViewModel ->
                    if (!mProductCarouselComponentViewModel.isLoadingData() && !mProductCarouselComponentViewModel.isLastPage()) {
                        if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= mProductCarouselComponentViewModel.getPageSize()) {
                            mProductCarouselComponentViewModel.fetchCarouselPaginatedProducts()
                        }
                    }
                }
            }
        })
        mProductCarouselRecyclerView.addOnScrollListener(getParallaxEffect())
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mixLeftBannerCard.isVisible) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0 && dx != 0) {
                        val firstView =
                            linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition())
                        firstView?.let {
                            val distanceFromLeft = it.left
                            val translateX = distanceFromLeft * TRANSLATION_RATIO
                            if (translateX <= 0) {
                                mixLeftBannerCard.translationX = translateX
                                val itemSize = it.width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * ALHPA_RATIO)
                                mixLeftBannerCard.alpha = 1 - alpha
                            } else {
                                mixLeftBannerCard.translationX = 0f
                                mixLeftBannerCard.alpha = 1f
                            }
                        }
                    } else if (linearLayoutManager.findFirstVisibleItemPosition() > 0 && dx != 0) {
                        mixLeftBannerCard.alpha = 0f
                    }
                }
            }
        }
    }

    private fun addCardHeader(componentsItem: ComponentsItem?) {
        mHeaderView.removeAllViews()
        checkHeaderVisibility(componentsItem)
    }

    private fun checkHeaderVisibility(componentsItem: ComponentsItem?) {
        componentsItem?.data?.firstOrNull()?.let {
            if (!it.title.isNullOrEmpty() || !it.subtitle.isNullOrEmpty()) {
                mHeaderView.addView(
                    CustomViewCreator.getCustomViewObject(
                        itemView.context,
                        ComponentsList.LihatSemua,
                        componentsItem,
                        fragment
                    )
                )
            }
        }
    }

    private fun addDefaultItemDecorator() {
        if (mProductCarouselRecyclerView.itemDecorationCount > 0) {
            mProductCarouselRecyclerView.removeItemDecorationAt(0)
        }
        mProductCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            mProductCarouselComponentViewModel?.getProductCardHeaderData()?.observe(lifecycle) { component ->
                addCardHeader(component)
            }
            mProductCarouselComponentViewModel?.getMixLeftData()?.observe(lifecycle) { mixLeft ->
                mixLeftData = mixLeft
                setupBackgroundData(mixLeft)
            }
            mProductCarouselComponentViewModel?.getProductCarouselItemsListData()?.observe(lifecycle) { item ->
                setupMixLeft(item)
                mDiscoveryRecycleAdapter.setDataList(item)
            }
            mProductCarouselComponentViewModel?.syncData?.observe(lifecycle) { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            }
            mProductCarouselComponentViewModel?.getProductCardMaxHeight()?.observe(lifecycle) { height ->
                setMaxHeight(height)
            }
            mProductCarouselComponentViewModel?.getProductLoadState()?.observe(lifecycle) {
                if (it) handleErrorState()
            }
            mProductCarouselComponentViewModel?.atcFailed?.observe(lifecycle) { position ->
                if (position >= 0) {
                    mDiscoveryRecycleAdapter.getViewModelAtPosition(position)?.let { discoveryBaseViewModel ->
                        if (discoveryBaseViewModel is MasterProductCardItemViewModel) {
                            discoveryBaseViewModel.handleATCFailed()
                        }
                    }
                }
            }
        }
    }

    private fun setupBackgroundData(mixLeft: MixLeft?) {
        if (mixLeftData != null && !(mixLeftData?.backgroundImageUrl.isNullOrEmpty())) {
            mixLeft?.let {
                try {
                    backgroundImage.loadImageWithoutPlaceholder(it.backgroundImageUrl)
                    if (!it.backgroundColor.isNullOrEmpty()) {
                        backgroundImage.setColorFilter(Color.parseColor(it.backgroundColor))
                    }
                    backgroundImage.show()
                } catch (e: Exception) {
                    backgroundImage.hide()
                }
            }
        } else {
            backgroundImage.hide()
        }
    }

    private fun setupMixLeft(item: ArrayList<ComponentsItem>) {
        if (item.isNotEmpty() && mixLeftData != null && !(mixLeftData?.bannerImageUrlMobile.isNullOrEmpty())) {
            mixLeftData?.let {
                setupMixLeftBannerBG(it.backgroundColor, it.bannerSuperGraphicImage)
                if (!it.bannerImageUrlMobile.isNullOrEmpty()) {
                    mixLeftBanner.loadImageWithoutPlaceholder(it.bannerImageUrlMobile)
                }
                mixLeftBannerCard.show()
            }
            carouselRecyclerViewDecorator.isMixLeftPresent = true
        } else {
            mixLeftBannerCard.hide()
            carouselRecyclerViewDecorator.isMixLeftPresent = false
        }
    }

    private fun setupMixLeftBannerBG(backgroundColor: String?, bannerSuperGraphicImage: String?) {
        if (backgroundColor.isNullOrEmpty() && bannerSuperGraphicImage.isNullOrEmpty()) {
            mixLeftBannerBG.hide()
            return
        }
        try {
            if (!backgroundColor.isNullOrEmpty()) {
                mixLeftBannerBG.setBackgroundColor(Color.parseColor(backgroundColor))
            }
            if (!bannerSuperGraphicImage.isNullOrEmpty()) {
                mixLeftBannerBG.loadImageWithoutPlaceholder(bannerSuperGraphicImage)
            }
            mixLeftBannerBG.show()
        } catch (e: Exception) {
            mixLeftBannerBG.hide()
        }
    }

    private fun setMaxHeight(height: Int) {
        val carouselLayoutParams = mProductCarouselRecyclerView.layoutParams
        carouselLayoutParams?.height = height
        mProductCarouselRecyclerView.layoutParams = carouselLayoutParams
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductCarouselComponentViewModel?.getProductCarouselItemsListData()?.removeObservers(it)
            mProductCarouselComponentViewModel?.getProductCardMaxHeight()?.removeObservers(it)
            mProductCarouselComponentViewModel?.getProductLoadState()?.removeObservers(it)
            mProductCarouselComponentViewModel?.getProductCardHeaderData()?.removeObservers(it)
            mProductCarouselComponentViewModel?.atcFailed?.removeObservers(it)
            mProductCarouselComponentViewModel?.getMixLeftData()?.removeObservers(it)
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun handleErrorState() {
        addShimmer()
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        mixLeftBannerCard.hide()
        if (mHeaderView.childCount > 0) {
            mHeaderView.removeAllViews()
        }

        if (mProductCarouselComponentViewModel?.getProductList() == null) {
            carouselEmptyState?.run {
                title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                carouselEmptyState?.visible()
                mProductCarouselRecyclerView.gone()
                errorHolder.gone()
            }
        } else if (mProductCarouselComponentViewModel?.getProductList()?.isEmpty() == true &&
            mProductCarouselComponentViewModel?.areFiltersApplied() == true
        ) {
            if (errorHolder.childCount > 0) {
                errorHolder.removeAllViews()
            }
            errorHolder.addView(
                mProductCarouselComponentViewModel?.getErrorStateComponent()?.let {
                    CustomViewCreator.getCustomViewObject(
                        itemView.context,
                        ComponentsList.ProductListEmptyState,
                        it,
                        fragment
                    )
                }
            )
            errorHolder.show()
            carouselEmptyState?.gone()
            mProductCarouselRecyclerView.gone()
        }
    }

    private fun reloadComponent() {
        mProductCarouselRecyclerView.visible()
        carouselEmptyState?.gone()
        mProductCarouselComponentViewModel?.resetComponent()
        mProductCarouselComponentViewModel?.fetchProductCarouselData()
    }

    override fun getInnerRecycleView(): RecyclerView {
        return mProductCarouselRecyclerView
    }

    companion object {
        const val PREFETCH_ITEM_COUNT = 4
        private const val ALHPA_RATIO = 0.8f
        private const val TRANSLATION_RATIO = 0.2f
    }
}
