package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.analytic.BrandlistTracking
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
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
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

    private var brandlistTracking: BrandlistTracking? = null
    private var rootView: View? = null
    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var tabLayout: BrandlistCategoryTabLayout? = null
    private var loadingLayout: View? = null
    private var viewPager: ViewPager? = null
    private var appbarCategory: AppBarLayout? = null
    private var currentCategoryName = ""
    private var targetCategoryName = ""


    private var categorySlug = "0"

    private val tabAdapter: BrandlistContainerAdapter by lazy {
        BrandlistContainerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            brandlistTracking = BrandlistTracking(it)
        }
        arguments?.let {
            categorySlug = it.getString(CATEGORY_EXTRA_APPLINK, "0")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_brandlist_category, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        observeBrandListCategoriesData()
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

        mainToolbar = view.findViewById(R.id.maintoolbar)
        tabLayout = view.findViewById(R.id.tablayout)
        loadingLayout = view.findViewById(R.id.view_category_tab_loading)
        viewPager = view.findViewById(R.id.viewpager)
        appbarCategory = view.findViewById(R.id.appbarLayout)
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)

        mainToolbar?.let {
            configMainToolbar(it)
        }
    }

    private fun observeBrandListCategoriesData() {
        viewModel.brandlistCategoriesResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    removeLoading()
                    populateCategoriesData(it.data)
                }
                is Fail -> {
                    NetworkErrorHelper.showEmptyState(context, view) {
                        viewModel.getBrandlistCategories()
                    }
                }
            }
        })
    }

    private fun populateCategoriesData(brandListCategories: BrandlistCategories) {
        brandListCategories.categories.forEachIndexed { _, category ->
            tabAdapter.categories.add(category)
        }
        tabAdapter.notifyDataSetChanged()
        tabLayout?.setup(viewPager, convertToCategoryTabModels(brandListCategories.categories), appbarCategory)
        tabLayout?.getTabAt(0)?.select()

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentCategoryName = it.text.toString()
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    targetCategoryName = it.text.toString()
                    brandlistTracking?.clickCategory(targetCategoryName, currentCategoryName)
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int? = rootView?.systemUiVisibility
            flags = flags?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            flags?.let { rootView?.setSystemUiVisibility(it) }
            activity?.window?.statusBarColor = Color.WHITE
        }

        if (Build.VERSION.SDK_INT in 19..20) {
            activity?.let { setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true) }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity?.let { setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false) }
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun removeLoading() {
        loadingLayout?.visibility = View.GONE
        tabLayout?.visibility = View.VISIBLE
    }

    private fun configMainToolbar(mainToolbar: MainToolbar) {

        activity?.let {
            (it as? AppCompatActivity)?.let {
                it.setSupportActionBar(mainToolbar)
                it.supportActionBar?.setDisplayShowTitleEnabled(false)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            mainToolbar.setNavigationIcon(R.drawable.brandlist_icon_arrow_black)
            mainToolbar.searchApplink = ApplinkConstInternalMechant.BRANDLIST_SEARCH
            mainToolbar.setQuerySearch(getString(R.string.brandlist_search_view_hint))
            mainToolbar.btnWishlist?.hide()
            mainToolbar.btnInbox?.hide()
            mainToolbar.btnNotification?.hide()
            mainToolbar.setOnClickListener {
                val keywordSearch = ""
                brandlistTracking?.clickSearchBox(targetCategoryName, keywordSearch, false)
            }
        }
    }
}