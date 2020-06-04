package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.AddChildAdapterCallback
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero


class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)
    private lateinit var mProductRevampComponentViewModel: ProductCardRevampViewModel
    private var addChildAdapterCallback: AddChildAdapterCallback = (fragment as AddChildAdapterCallback)
    private var productDataList: ArrayList<ComponentsItem> = ArrayList()

    init {
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        addChildAdapterCallback.addChildAdapter(mDiscoveryRecycleAdapter)
        addShimmer()
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        for (i in 1..10) {
            list.add(ComponentsItem(name = "shimmer_product_card"))
        }
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
        setUpDataObserver(fragment.viewLifecycleOwner)
        mProductRevampComponentViewModel.fetchProductsData((fragment as DiscoveryFragment).pageEndPoint)
    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mProductRevampComponentViewModel.getProductCarouselItemsListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.addDataList(item)
            addChildAdapterCallback.notifyMergeAdapter()
        })
    }

//    fun removeProduct(productID: Int) {
//        var removeProductIndex = -1
//        if (!productDataList.isNullOrEmpty()) {
//            for ((index, productItem) in productDataList.withIndex()) {
//                productItem.data?.let {
//                    if(it[0].id.toIntOrZero() == productID){
//                        removeProductIndex =  index
//                    }
//                }
//            }
//
//            if(removeProductIndex >= 0 && productDataList.size > removeProductIndex){
//                productDataList.removeAt(removeProductIndex)
//                mDiscoveryRecycleAdapter.setDataList(productDataList)
//                addChildAdapterCallback.notifyMergeAdapter()
//            }
//        }
//    }
}