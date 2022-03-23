package com.tokopedia.deals.common.ui.activity

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.deals.R
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.common.ui.adapter.DealsFragmentPagerAdapter
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.deals.databinding.ActivityBaseBrandCategoryDealsBinding
import com.tokopedia.deals.databinding.ActivityBaseDealsBinding
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.getCustomText
import java.lang.Math.abs

open class DealsBaseBrandCategoryActivity : DealsBaseActivity() {

    lateinit var binding: ActivityBaseBrandCategoryDealsBinding

    lateinit var dealBrandCategoryActivityViewModel: DealsBrandCategoryActivityViewModel
    lateinit var adapter: DealsFragmentPagerAdapter

    override fun getLayoutRes(): Int = R.layout.activity_base_brand_category_deals

    protected var childCategoryList: ArrayList<String?> = arrayListOf()

    var isEnableTabClickAnalytics = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBrandCategoryDealsBinding.bind(viewGroup)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        dealBrandCategoryActivityViewModel = viewModelProvider.get(DealsBrandCategoryActivityViewModel::class.java)
        dealBrandCategoryActivityViewModel.getCategoryCombindedData()

        observerLayout()
        setUpScrollView()
    }

    private fun observerLayout() {

        binding.tabDealsBrandCategory.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.select()
                binding.vpDealsBrandCategory.currentItem = tab.position

                if (isEnableTabClickAnalytics) tabAnalytics(tab.getCustomText(), tab.position)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }
        })

        binding.vpDealsBrandCategory?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val tab = binding.tabDealsBrandCategory?.getUnifyTabLayout()?.getTabAt(position)
                tab?.select()
            }
        })

        dealBrandCategoryActivityViewModel.curatedData.observe(this, Observer {
            binding.tabsShimmering.root.hide()
            val tabNameList: ArrayList<String> = arrayListOf()

            if (showAllItemPage()) {
                childCategoryList.add(null)
                binding.tabDealsBrandCategory.addNewTab(ALL_ITEM_PAGE)
                tabNameList.add(ALL_ITEM_PAGE)
            }

            childCategoryList.addAll(ArrayList(it.eventChildCategory.categories.filter { ct -> ct.isCard == 0 && ct.isHidden == 0 }
                    .map { category -> category.id }))

            val categoryId: String = intent.getStringExtra(DealsCategoryActivity.EXTRA_CATEGORY_ID)
                    ?: ""
            val position = findCategoryPosition(categoryId)

            if (position != null) {
                it.eventChildCategory.categories.forEach { category ->
                    if (category.isCard == 0 && category.isHidden == 0) {
                        binding.tabDealsBrandCategory.addNewTab(category.title)
                        tabNameList.add(category.title)
                    }
                }

                adapter = DealsFragmentPagerAdapter(this, childCategoryList.toList(), getPageTAG(), tabNameList.toList(), false)
                binding.vpDealsBrandCategory.offscreenPageLimit = 1
                binding.vpDealsBrandCategory.adapter = adapter

                redirectsToSpecificCategory(position)
            } else {
                binding.tabDealsBrandCategory.hide()
                adapter = DealsFragmentPagerAdapter(this, listOf(categoryId), getPageTAG(), listOf(categoryId), true)
                binding.vpDealsBrandCategory.offscreenPageLimit = 1
                binding.vpDealsBrandCategory.adapter = adapter
            }
        })

        dealBrandCategoryActivityViewModel.errorMessage.observe(this, Observer {
            Toaster.build(binding.container, it.localizedMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        })
    }

    private fun redirectsToSpecificCategory(position: Int?) {
        val tabLayout = binding.tabDealsBrandCategory.getUnifyTabLayout()
        val observer = binding.tabDealsBrandCategory.getUnifyTabLayout().viewTreeObserver
        if (observer.isAlive) {
            observer.dispatchOnGlobalLayout()
            observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.tabDealsBrandCategory.getUnifyTabLayout().viewTreeObserver.removeOnGlobalLayoutListener(this)
                    isEnableTabClickAnalytics = true
                    position?.let { tabLayout.getTabAt(it)?.select() }
                }
            })
        }
    }

    open fun findCategoryPosition(categoryId: String): Int? {
        if (categoryId.isEmpty()) return 0
        childCategoryList.forEachIndexed { index, s ->
            if (s == categoryId) {
                return index
            }
        }
        return 0
    }

    override fun getNewFragment(): Fragment? = null

    open fun showAllItemPage(): Boolean = false

    open fun getPageTAG(): String = ""

    open fun tabAnalytics(categoryName: String, position: Int) {
        //no-op
    }

    private fun setUpScrollView() {
        binding.appBarLayoutSearchContent?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange >= - binding.contentBaseDealsSearchBar.searchBarDealsBaseSearch.height ) {
                //collapse
                ViewCompat.setElevation(appBarLayout,0f)
                binding.contentBaseToolbar.imgDealsSearchIcon.show()
            } else {
                binding.contentBaseToolbar.imgDealsSearchIcon.hide()
            }
        })

        binding.contentBaseToolbar.imgDealsSearchIcon.setOnClickListener {
            searchBarActionListener?.onClickSearchBar()
        }
    }

    override fun getParentViewResourceID(): Int = R.id.ll_main

    companion object {
        private const val ALL_ITEM_PAGE = "Semua"
    }
}