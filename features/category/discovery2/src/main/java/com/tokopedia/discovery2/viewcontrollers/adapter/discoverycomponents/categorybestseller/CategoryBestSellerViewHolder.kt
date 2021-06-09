package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.LihatSemua
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator

class CategoryBestSellerViewHolder (itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner)  {

    private var mProductCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.products_rv)
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var categoryBestSellerViewModel: CategoryBestSellerViewModel
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator(fragment.context?.resources?.getDimensionPixelSize(R.dimen.dp_12))

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        categoryBestSellerViewModel = discoveryBaseViewModel as CategoryBestSellerViewModel
        getSubComponent().inject(categoryBestSellerViewModel)
        addShimmer()
        addDefaultItemDecorator()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            categoryBestSellerViewModel.getProductCarouselItemsListData().observe(lifecycle, { item ->
                if(item.isNotEmpty())
                    addCardHeader(item[0].lihatSemua)
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            categoryBestSellerViewModel.syncData.observe(lifecycle, { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
            categoryBestSellerViewModel.getProductCardMaxHeight().observe(lifecycle, { height ->
                setMaxHeight(height)
            })
            categoryBestSellerViewModel.getProductLoadState().observe(lifecycle, {
                if (it) handleErrorState()
            })
        }
    }

    private fun addDefaultItemDecorator() {
        if (mProductCarouselRecyclerView.itemDecorationCount > 0)
            mProductCarouselRecyclerView.removeItemDecorationAt(0)
        mProductCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }

    private fun setMaxHeight(height: Int) {
        val carouselLayoutParams = mProductCarouselRecyclerView.layoutParams
        carouselLayoutParams?.height = height
        mProductCarouselRecyclerView.layoutParams = carouselLayoutParams
    }

    private fun addCardHeader(lihatSemua: LihatSemua?) {
        mHeaderView.removeAllViews()
        val lihatSemuaDataItem = DataItem(title = lihatSemua?.header,
                subtitle = lihatSemua?.subheader, btnApplink = lihatSemua?.applink)
        val lihatSemuaComponentData = ComponentsItem(
                name = ComponentsList.CategoryBestSeller.componentName,
                data = listOf(lihatSemuaDataItem))
        mHeaderView.addView(CustomViewCreator.getCustomViewObject(itemView.context,
                ComponentsList.LihatSemua, lihatSemuaComponentData, fragment))
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun handleErrorState() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        mDiscoveryRecycleAdapter.setDataList(list)
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        if (mHeaderView.childCount > 0)
            mHeaderView.removeAllViews()
    }

    override fun getInnerRecycleView(): RecyclerView {
        return mProductCarouselRecyclerView
    }
}