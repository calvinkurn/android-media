package com.tokopedia.createpost.view.plist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import android.view.LayoutInflater as LayoutInflater1

class ShopPListSortFilterBs(val vm: ShopPageProductListViewModel) : BottomSheetUnify() {

    private var rvSort: RecyclerView? = null
    private var adapter : ShopProductSortAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(vm: ShopPageProductListViewModel): ShopPListSortFilterBs {
            return ShopPListSortFilterBs(vm)
        }
    }

    override fun onCreateView(inflater: LayoutInflater1, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addListObserver()
        initLayout()
        vm.getSortData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun addListObserver() = vm.sortLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                }
                is Success -> {
                    adapter = ShopProductSortAdapter(it.data.result)
                    rvSort?.adapter = adapter
                }
                is ErrorMessage -> {

                }
            }
        }
    })

    private fun initLayout() {
        setTitle("Urutkan")
        isDragable = true
        isHideable = true
        isKeyboardOverlap = false
        showCloseIcon = false
        showKnob = true
        val contentView = View.inflate(context,
            R.layout.bs_shop_page_plist_sort, null)
        rvSort = contentView?.findViewById(R.id.rv_sort)
        setChild(contentView)
    }
}