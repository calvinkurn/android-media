package com.tokopedia.homenav.category.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.category.analytics.CategoryTracking
import com.tokopedia.homenav.category.view.adapter.CategoryListAdapter
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactory
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactoryImpl
import com.tokopedia.homenav.category.view.di.DaggerCategoryListComponent
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_nav_category.view.*
import javax.inject.Inject

class CategoryListFragment: BaseDaggerFragment(), HomeNavListener {
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val typeFactory: CategoryListTypeFactory = CategoryListTypeFactoryImpl(this)
    private val adapter: CategoryListAdapter = CategoryListAdapter(typeFactory)
    private lateinit var menuViewModel: HomeNavMenuViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nav_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        arguments?.get(BUNDLE_MENU_ITEM)?.let {
            menuViewModel = it as HomeNavMenuViewModel
            adapter.submitList(it.submenu)
        }
    }

    override fun getScreenName(): String {
        return PAGE_NAME
    }

    override fun initInjector() {
        val baseNavComponent
                = DaggerBaseNavComponent.builder()
                .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
                .build() as DaggerBaseNavComponent
        DaggerCategoryListComponent.builder().baseNavComponent(baseNavComponent).build().inject(this)
    }

    override fun onRefresh() {}

    override fun onMenuClick(homeNavMenuViewModel: HomeNavMenuViewModel) {
        arguments?.getString(TITLE_ARGS, "")?.let {
            if(!it.contains(OTHER)){
                CategoryTracking.onClickItem(homeNavMenuViewModel.id.toString(), userSessionInterface.userId)
            } else {
                CategoryTracking.onClickLainnyaItem(homeNavMenuViewModel.itemTitle, userSessionInterface.userId)
            }
            RouteManager.route(context, homeNavMenuViewModel.applink)
        }
    }

    private fun initRecyclerView(view: View) {
        view.recycler_view?.adapter = adapter
    }

    companion object {
        const val PAGE_NAME = "CATEGORY_LIST"
        const val OTHER = "lainnya"
        private const val TITLE_ARGS = "title"
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
    }

}