package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin

class NavigationChipsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), CategoryNavBottomSheet.CategorySelected, CategoryNavBottomSheet.GtmProviderListener {
    private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private val chipsParentView: ConstraintLayout = itemView.findViewById(R.id.chips_parent_view)
    private val dropdownArrow: ImageView = itemView.findViewById(R.id.dropdown_arrow)
    private var categoriesRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)
    private lateinit var navigationChipsViewModel: NavigationChipsViewModel

    init {
        attachRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        navigationChipsViewModel = discoveryBaseViewModel as NavigationChipsViewModel
        getSubComponent().inject(navigationChipsViewModel)
        dropdownArrow.setOnClickListener {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickNavigationDropDown()
            CategoryNavBottomSheet.getInstance(navigationChipsViewModel.components.pageEndPoint,this,  this, true).show(fragment.childFragmentManager, "")
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        navigationChipsViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackImpressionNavigationChips(item)
            categoriesRecycleAdapter.setDataList(item)
            categoriesRecycleAdapter.notifyDataSetChanged()
            if(item.size <= 0) chipsParentView.gone()
        })
        navigationChipsViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
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
            setMargin(0,
                    resources.getDimensionPixelSize(R.dimen.dp_4),
                    resources.getDimensionPixelSize(R.dimen.dp_40),
                    resources.getDimensionPixelSize(R.dimen.dp_8))
            addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), LinearLayoutManager.HORIZONTAL))
        }
    }

    override fun onCategorySelected(catId: String, appLink: String?, depth: Int, catName: String) {
        RouteManager.route(itemView.context, appLink)
    }

    override fun onBottomSheetClosed() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCloseNavigation()
    }

    override fun onL2Expanded(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickExpandNavigationAccordion(id)
    }

    override fun onL2Collapsed(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCollapseNavigationAccordion(id)
    }

    override fun onL3Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }

    override fun onL2Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }
}