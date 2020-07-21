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
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.getCustomText
import kotlinx.android.synthetic.main.activity_base_brand_category_deals.*

open class DealsBaseBrandCategoryActivity : DealsBaseActivity() {

    lateinit var dealBrandCategoryActivityViewModel: DealsBrandCategoryActivityViewModel
    lateinit var adapter: DealsFragmentPagerAdapter

    override fun getLayoutRes(): Int = R.layout.activity_base_brand_category_deals

    protected var childCategoryList: ArrayList<String?> = arrayListOf()

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
                tab?.select()
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

            childCategoryList.addAll(ArrayList(it.eventChildCategory.categories.filter { ct -> ct.isCard == 0 && ct.isHidden == 0 }
                    .map { category -> category.id }))

            val categoryId: String = intent.getStringExtra(DealsCategoryActivity.EXTRA_CATEGORY_ID) ?: ""
            val position = findCategoryPosition(categoryId)

            if (position != null) {
                it.eventChildCategory.categories.forEach { category ->
                    if (category.isCard == 0 && category.isHidden == 0) {
                        tab_deals_brand_category.addNewTab(category.title)
                        tabNameList.add(category.title)
                    }
                }

                adapter = DealsFragmentPagerAdapter(this, childCategoryList.toList(), getPageTAG(), tabNameList.toList())
                vp_deals_brand_category.offscreenPageLimit = 1
                vp_deals_brand_category.adapter = adapter

                redirectsToSpecificCategory(position)
            } else  {
                tab_deals_brand_category.hide()
                adapter = DealsFragmentPagerAdapter(this, listOf(categoryId), getPageTAG(), listOf(categoryId))
                vp_deals_brand_category.offscreenPageLimit = 1
                vp_deals_brand_category.adapter = adapter
            }
        })

        dealBrandCategoryActivityViewModel.errorMessage.observe(this, Observer {
            Toaster.make(container, it.localizedMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        })
    }

    private fun redirectsToSpecificCategory(position: Int?) {
        position?.let {
            tab_deals_brand_category.getUnifyTabLayout().isSmoothScrollingEnabled = true

            val tab = tab_deals_brand_category.getUnifyTabLayout().getTabAt(position)
            tab?.select()
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

    companion object {
        private const val ALL_ITEM_PAGE = "Semua"
    }
}