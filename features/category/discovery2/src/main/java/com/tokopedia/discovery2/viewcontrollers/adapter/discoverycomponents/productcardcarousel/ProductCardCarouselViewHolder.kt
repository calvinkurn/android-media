package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator

class ProductCardCarouselViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mProductCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.products_rv)
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductCarouselComponentViewModel: ProductCardCarouselViewModel
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator()
    private var componentName:String = ""

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductCarouselComponentViewModel = discoveryBaseViewModel as ProductCardCarouselViewModel
        addShimmer()
        addDefaultItemDecorator()
    }

    private fun addCardHeader(componentsItem: ComponentsItem) {
        mHeaderView.addView(CustomViewCreator.getCustomViewObject(itemView.context, ComponentsList.LihatSemua, componentsItem, fragment))
    }

    private fun addDefaultItemDecorator() {
        if (mProductCarouselRecyclerView.itemDecorationCount > 0)
            mProductCarouselRecyclerView.removeItemDecorationAt(0)
        mProductCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductCarouselComponentViewModel.getProductCardHeaderData().observe(it, Observer { component ->
                componentName = component.name ?: ""
                addCardHeader(component)
            })
            mProductCarouselComponentViewModel.getProductCarouselItemsListData().observe(it, Observer { item ->
                if(componentName == ComponentsList.ProductCardCarousel.componentName) {
                    val productCardWidth = getDisplayMetric(fragment.context).widthPixels /2.3.toInt()
                    mProductCarouselComponentViewModel.getMaxHeightProductCard(item, productCardWidth)
                }
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            mProductCarouselComponentViewModel.syncData.observe(it, Observer { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
            mProductCarouselComponentViewModel.getProductCardMaxHeight().observe(it, Observer {
                setMaxHeight(it)
            })
        }
    }

    private fun setMaxHeight(height: Int) {
        height.let {
            val carouselLayoutParams = mProductCarouselRecyclerView.layoutParams
            carouselLayoutParams?.height = it
            mProductCarouselRecyclerView.layoutParams = carouselLayoutParams
        }
    }

    private fun getDisplayMetric(context: Context?): DisplayMetrics {
        val displayMetrics = getDisplayMetric(context)
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (mProductCarouselComponentViewModel.getProductCarouselItemsListData().hasObservers()) {
            lifecycleOwner?.let { mProductCarouselComponentViewModel.getProductCarouselItemsListData().removeObservers(it) }
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }


    override fun getInnerRecycleView(): RecyclerView? {
        return mProductCarouselRecyclerView
    }
}