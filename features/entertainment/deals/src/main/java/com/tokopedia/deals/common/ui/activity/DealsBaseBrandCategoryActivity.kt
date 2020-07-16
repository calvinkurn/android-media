package com.tokopedia.deals.common.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.deals.R
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.common.ui.adapter.DealsFragmentPagerAdapter
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.getCustomText
import kotlinx.android.synthetic.main.activity_base_brand_category_deals.*

open class DealsBaseBrandCategoryActivity : DealsBaseActivity() {

    lateinit var dealBrandCategoryActivityViewModel: DealsBrandCategoryActivityViewModel
    lateinit var adapter: DealsFragmentPagerAdapter

    override fun getLayoutRes(): Int = R.layout.activity_base_brand_category_deals

    private val childCategoryList: ArrayList<String?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        dealBrandCategoryActivityViewModel = viewModelProvider.get(DealsBrandCategoryActivityViewModel::class.java)
        dealBrandCategoryActivityViewModel.getCategoryCombindedData()

        observerLayout()
    }

    private fun observerLayout() {
        tab_deals_brand_category.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.select()
                vp_deals_brand_category.currentItem = tab.position
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }
        })

        vp_deals_brand_category?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val tab = tab_deals_brand_category?.getUnifyTabLayout()?.getTabAt(position)
//                tab_deals_brand_category?.getUnifyTabLayout()?.selectTab(tab)
                tabAnalytics(tab?.getCustomText() ?: "", position)
            }
        })

        dealBrandCategoryActivityViewModel.curatedData.observe(this, Observer {
            tabs_shimmering?.hide()
            val tabNameList: ArrayList<String> = arrayListOf()
            if (showAllItemPage()) {
                childCategoryList.add(null)
                tab_deals_brand_category.addNewTab(ALL_ITEM_PAGE)
                tabNameList.add(ALL_ITEM_PAGE)
            }

            it.eventChildCategory.categories.forEach { category ->
                if (category.isCard == 0 && category.isHidden == 0) {
                    tab_deals_brand_category.addNewTab(category.title)
                    childCategoryList.add(category.id)
                    tabNameList.add(category.title)
                }
            }
            adapter = DealsFragmentPagerAdapter(this, childCategoryList.toList(), getPageTAG(), tabNameList.toList())

            vp_deals_brand_category.offscreenPageLimit = 1
            vp_deals_brand_category.adapter = adapter

            redirectsToSpecificCategory()
        })

        dealBrandCategoryActivityViewModel.errorMessage.observe(this, Observer {
            Toaster.make(container, it.localizedMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        })
    }

    private fun redirectsToSpecificCategory() {
        val categoryId: String? = intent.getStringExtra(DealsCategoryActivity.EXTRA_CATEGORY_ID)

        if (categoryId != null) {
            selectTab(findCategoryPosition(categoryId))
        }
    }

    private fun selectTab(position: Int) {
        tab_deals_brand_category.getUnifyTabLayout().isSmoothScrollingEnabled = true

        val tab = tab_deals_brand_category.getUnifyTabLayout().getTabAt(position)
        tab?.select()
    }

    private fun findCategoryPosition(categoryId: String): Int {
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

    companion object {
        private const val ALL_ITEM_PAGE = "Semua"
    }
}