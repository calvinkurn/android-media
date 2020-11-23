package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.childcategories

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.category.navbottomsheet.view.CategoryNavBottomSheet
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.SpaceItemDecoration
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setMargin

class ChildCategoriesViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), CategoryNavBottomSheet.CategorySelected {
    private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private val dropdownArrow: ImageView = itemView.findViewById(R.id.dropdown_arrow)
    private var categoriesRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)
    private lateinit var childCategoriesViewModel: ChildCategoriesViewModel

    init {
        attachRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        childCategoriesViewModel = discoveryBaseViewModel as ChildCategoriesViewModel
        getSubComponent().inject(childCategoriesViewModel)
        dropdownArrow.setOnClickListener {
            CategoryNavBottomSheet(this, childCategoriesViewModel.components.pageEndPoint, true).show(fragment.childFragmentManager, "")
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        childCategoriesViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            categoriesRecycleAdapter.setDataList(item)
            categoriesRecycleAdapter.notifyDataSetChanged()
        })
        childCategoriesViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            if (item) {
                (fragment as DiscoveryFragment).reSync()
            }
        })
    }

    private fun attachRecyclerView() {
        categoriesRecyclerView.apply {
            adapter = categoriesRecycleAdapter
            val chipsLayoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = chipsLayoutManager
            setMargin(resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8),
                    resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8))
            addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), LinearLayoutManager.HORIZONTAL))
        }
    }

    override fun onCategorySelected(catId: String, appLink: String?, depth: Int) {
        RouteManager.route(itemView.context, appLink)
    }

}