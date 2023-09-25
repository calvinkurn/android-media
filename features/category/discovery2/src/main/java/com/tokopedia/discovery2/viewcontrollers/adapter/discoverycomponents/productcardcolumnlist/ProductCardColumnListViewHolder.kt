package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.carouselproductcard.paging.CarouselPagingSelectedGroupModel
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ProductCardColumnListViewHolder(
    itemView: View,
    val fragment: Fragment
): AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var carouselPagingProductCard: CarouselPagingProductCardView = itemView.findViewById(R.id.carousel_paging_product_card)

    private var viewModel: ProductCardColumnListViewModel? = null

    override fun bindView(
        discoveryBaseViewModel: DiscoveryBaseViewModel
    ) {
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
            }
        }
    }

    private fun initCarouselPaging(carouselPagingGroupProductModel: CarouselPagingGroupProductModel) {
        carouselPagingProductCard.setPagingModel(
            model = CarouselPagingModel(
                productCardGroupList = listOf(carouselPagingGroupProductModel)
            ),
            listener = object: CarouselPagingProductCardView.CarouselPagingListener {
                override fun onGroupChanged(selectedGroupModel: CarouselPagingSelectedGroupModel) {

                }

                override fun onItemImpress(
                    groupModel: CarouselPagingGroupModel,
                    itemPosition: Int
                ) {

                }

                override fun onItemClick(groupModel: CarouselPagingGroupModel, itemPosition: Int) {
                    RouteManager.route(itemView.context, getProductAppLink(itemPosition))
                }
            }
        )
    }

    private fun getProductAppLink(itemPosition: Int): String = viewModel?.getProduct(itemPosition)?.applinks.orEmpty()
}
