package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.BuildConfig
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryModule
import com.tokopedia.brandlist.brandlist_category.di.DaggerBrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.presentation.activity.BrandlistActivity.Companion.CATEGORY_EXTRA_APPLINK
import com.tokopedia.brandlist.brandlist_category.presentation.adapter.BrandlistContainerAdapter
import com.tokopedia.brandlist.brandlist_category.presentation.viewmodel.BrandlistCategoryViewModel
import com.tokopedia.brandlist.brandlist_category.presentation.widget.BrandlistCategoryTabLayout
import com.tokopedia.brandlist.common.listener.RecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.ArrayList
import javax.inject.Inject

class BrandlistContainerFragment : BaseDaggerFragment(),
        HasComponent<BrandlistCategoryComponent>,
        RecyclerViewScrollListener {

    companion object {
        @JvmStatic
        fun createInstance(category: String): Fragment {
            return BrandlistContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_EXTRA_APPLINK, category)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: BrandlistCategoryViewModel

    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var tabLayout: BrandlistCategoryTabLayout? = null
    private var loadingLayout: View? = null
    private var viewPager: ViewPager? = null
    private var appbarCategory: AppBarLayout? = null
    private var keyCategory = "0"
    private var categorySlug = "0"

    private val tabAdapter: BrandlistContainerAdapter by lazy {
        BrandlistContainerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            // Insert tracking here
        }
        arguments?.let {
            categorySlug = it.getString(CATEGORY_EXTRA_APPLINK)
            println(categorySlug)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_brandlist_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        observeBrandlistCategoriesData()
        viewModel.getBrandlistCategories()
    }

    override fun onDestroy() {
        viewModel.brandlistCategoriesResponse.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): BrandlistCategoryComponent? {
        return activity?.run {
            DaggerBrandlistCategoryComponent
                    .builder()
                    .brandlistCategoryModule(BrandlistCategoryModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }

    override fun onContentScrolled(dy: Int) {
        tabLayout?.adjustTabCollapseOnScrolled(dy)
    }

    private fun init(view: View) {
        configStatusBar(view)
        configMainToolbar(view)
        tabLayout = view.findViewById(R.id.tablayout)
        loadingLayout = view.findViewById(R.id.view_category_tab_loading)
        viewPager = view.findViewById(R.id.viewpager)
        appbarCategory = view.findViewById(R.id.appbarLayout)
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    private fun observeBrandlistCategoriesData() {
        viewModel.brandlistCategoriesResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    removeLoading()
                    populateCategoriesData(it.data)
                }
                is Fail -> {
                    if (BuildConfig.DEBUG) {
                        it.throwable.printStackTrace()
                    }

                    NetworkErrorHelper.showEmptyState(context, view) {
                        viewModel.getBrandlistCategories()
                    }
                }
            }
        })
    }

    private fun populateCategoriesData(brandlistCategories: BrandlistCategories) {
        brandlistCategories.categories.forEachIndexed { _, category ->
            tabAdapter.categories.add(category)
        }
        tabAdapter.notifyDataSetChanged()
        tabLayout?.setup(viewPager!!, convertToCategoryTabModels(brandlistCategories.categories), appbarCategory!!)
        val categorySelected = getSelectedCategory(brandlistCategories)
        tabLayout?.getTabAt(categorySelected)?.select()

        val category = tabAdapter.categories[0]
//        tracking.eventImpressionCategory(
//                category.title,
//                category.categoryId,
//                0,
//                category.icon
//        )

        tabLayout?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val categoryReselected = tabAdapter.categories.getOrNull(tab?.position.toZeroIfNull())
                categoryReselected?.let {
//                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categorySelected = tabAdapter.categories.getOrNull(tab?.position.toZeroIfNull())
                categorySelected?.let {
//                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

        })
    }

    private fun convertToCategoryTabModels(data: List<Category>): List<BrandlistCategoryTabLayout.CategoryTabModel> {
        val categoryTabModels = ArrayList<BrandlistCategoryTabLayout.CategoryTabModel>()
        data.forEach {
            categoryTabModels.add(BrandlistCategoryTabLayout.CategoryTabModel(it.title, it.icon, it.imageInactiveURL))
        }
        return categoryTabModels
    }

    private fun getSelectedCategory(brandlistCategories: BrandlistCategories): Int {
        brandlistCategories.categories.forEachIndexed { index, category ->
            if (keyCategory !== "0" && category.categoryId == keyCategory) {
                return index
            }
        }
        return 0
    }

    //status bar background compability
    private fun configStatusBar(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        activity?.let {
            statusBar?.layoutParams?.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        statusBar?.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun removeLoading() {
        loadingLayout?.visibility = View.GONE
        tabLayout?.visibility = View.VISIBLE
    }

    private fun configMainToolbar(view: View) {
        mainToolbar = view.findViewById(R.id.maintoolbar)
//        mainToolbar?.searchApplink = ApplinkConstant.OFFICIAL_SEARCHBAR
        mainToolbar?.setQuerySearch(getString(R.string.bl_query_search))
        mainToolbar?.getBtnWishlist()?.hide()
        mainToolbar?.getBtnInbox()?.hide()
        mainToolbar?.getBtnNotification()?.hide()
    }
}