package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.carouselproductcard.paging.CarouselPagingSelectedGroupModel
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.asProductTrackModel
import com.tokopedia.discovery2.analytics.TrackDiscoveryRecommendationMapper.isEligibleToTrack
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide

class ProductCardColumnListViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), CarouselPagingProductCardView.CarouselPagingListener {

    private var carouselPagingProductCard: CarouselPagingProductCardView = itemView.findViewById(R.id.carousel_paging_product_card)

    private var viewModel: ProductCardColumnListViewModel? = null

    override fun bindView(
        discoveryBaseViewModel: DiscoveryBaseViewModel
    ) {
        /**
         * Set paging model with empty object is used to show carousel paging shimmering
         */
        carouselPagingProductCard.setPagingModel(
            model = CarouselPagingModel(),
            listener = this
        )

        if (viewModel == null) {
            viewModel = (discoveryBaseViewModel as ProductCardColumnListViewModel).apply {
                getSubComponent().inject(this)
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            viewModel?.apply {
                carouselPagingGroupProductModel.observe(owner) { productList ->
                    initCarouselPaging(productList)
                }

                errorState.observe(owner) {
                    carouselPagingProductCard.hide()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.apply {
                carouselPagingGroupProductModel.removeObservers(it)
                errorState.removeObservers(it)
            }
        }
    }

    override fun onGroupChanged(
        selectedGroupModel: CarouselPagingSelectedGroupModel
    ) {
        /* nothing to do */
    }

    override fun onItemImpress(
        groupModel: CarouselPagingGroupModel,
        itemPosition: Int
    ) {
        viewModel?.apply {
            val product = getProduct(itemPosition)?.also {
                AppLogRecommendation.sendProductShowAppLog(
                    it.asProductTrackModel(
                        it.parentComponentName.orEmpty()
                    )
                )
            }
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .viewProductsList(
                    componentsItems = components.getComponentItem(itemPosition) ?: ComponentsItem(),
                    isLogin = isLoggedIn(),
                    isFulFillment = components.isFulfillment(product),
                    warehouseId = components.getWarehouseId(product)
                )

            trackTopAdsImpression(itemPosition)
        }
    }

    override fun onItemClick(groupModel: CarouselPagingGroupModel, itemPosition: Int) {
        viewModel?.apply {
            val product = getProduct(itemPosition)?.also {
                if (it.isEligibleToTrack()) {
                    AppLogRecommendation.sendProductClickAppLog(
                        it.asProductTrackModel(
                            it.parentComponentName.orEmpty()
                        )
                    )
                }
            }

            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackProductCardClick(
                    componentsItems = components.getComponentItem(itemPosition) ?: ComponentsItem(),
                    isLogin = isLoggedIn(),
                    isFulFillment = components.isFulfillment(product),
                    warehouseId = components.getWarehouseId(product)
                )

            trackTopAdsClick(itemPosition)

            RouteManager.route(itemView.context, product?.applinks)
        }
    }

    private fun ProductCardColumnListViewModel.initCarouselPaging(carouselPagingGroupProductModel: CarouselPagingGroupProductModel) {
        carouselPagingProductCard.setPagingModel(
            model = CarouselPagingModel(
                productCardGroupList = listOf(carouselPagingGroupProductModel),
                itemPerPage = getItemPerPage()
            ),
            listener = this@ProductCardColumnListViewHolder
        )
        carouselPagingProductCard.addHorizontalTrackListener(
            SlideTrackObject(
                moduleName = viewModel?.components?.creativeName.orEmpty(),
                barName = viewModel?.components?.creativeName.orEmpty(),
            )
        )
    }
}
