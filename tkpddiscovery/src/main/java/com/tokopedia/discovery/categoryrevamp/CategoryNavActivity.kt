package com.tokopedia.discovery.categoryrevamp

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.view.ViewTreeObserver
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.fragments.CatalogNavFragment
import com.tokopedia.discovery.categoryrevamp.fragments.ProductNavFragment
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel
import com.tokopedia.discovery.newdiscovery.search.adapter.SearchSectionPagerAdapter
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem
import kotlinx.android.synthetic.main.activity_category_nav.*
import java.util.*


class CategoryNavActivity : BaseActivity() {

    private var searchSectionPagerAdapter: SearchSectionPagerAdapter? = null
    private var isForceSwipeToShop: Boolean = false
    private var activeTabPosition: Int = 0

    private val EXTRA_CATEGORY_HEADER_VIEW_MODEL = "CATEGORY_HADES_MODEL"
    private val EXTRA_TRACKER_ATTRIBUTION = "EXTRA_TRACKER_ATTRIBUTION"

    private var departmentId: String? = null
    private var categoryName: String? = null
    private var categoryUrl: String? = null
    private var trackerAttribution: String? = null

    private lateinit var categoryHeaderModel: CategoryHeaderModel

    val searchSectionItemList = ArrayList<SearchSectionItem>()

    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_nav)
        prepareView()

    }

    private fun initSwitchButton() {
        img_display_button.tag = STATE_GRID
        img_display_button.setOnClickListener {
            (searchSectionItemList[0].fragment as ProductNavFragment).switchLayout()

            when (img_display_button.tag) {

                STATE_GRID -> {
                    img_display_button.tag = STATE_LIST
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
                }
            }
        }
    }

    private fun prepareView() {
        fetchBundle()
        initToolbar()
        initViewPager()
        loadSection()
        initSwitchButton()
    }

    private fun fetchBundle() {
        val bundle = intent.extras
        if (bundle.getParcelable<Parcelable>(EXTRA_CATEGORY_HEADER_VIEW_MODEL) != null) run {
            categoryHeaderModel = bundle.getParcelable<CategoryHeaderModel>(EXTRA_CATEGORY_HEADER_VIEW_MODEL)
            //trackerAttribution = bundle.getString(EXTRA_TRACKER_ATTRIBUTION, "")
            //departmentId = categoryHeaderModel!!.getDepartementId()
            //categoryName = categoryHeaderModel!!.getHeaderModel().getCategoryName()
            //categoryPresenter.getCategoryPage1(categoryHeaderModel)
        }
    }

    private fun loadSection() {
        populateTab(searchSectionItemList)

        searchSectionPagerAdapter = SearchSectionPagerAdapter(supportFragmentManager)
        searchSectionPagerAdapter!!.setData(searchSectionItemList)
        pager.adapter = searchSectionPagerAdapter
        tabs.setupWithViewPager(pager)

        setActiveTab()
    }

    private fun populateTab(searchSectionItemList: ArrayList<SearchSectionItem>) {
        initFragments()
        addFragmentsToList(searchSectionItemList)

    }

    private fun addFragmentsToList(searchSectionItemList: ArrayList<SearchSectionItem>) {
        searchSectionItemList.add(SearchSectionItem("Produk", ProductNavFragment.newInstance(categoryHeaderModel)))
        searchSectionItemList.add(SearchSectionItem("Katalog", CatalogNavFragment.newInstance(categoryHeaderModel)))
    }

    private fun initFragments() {
    }

    private fun setActiveTab() {
        pager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                pager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                pager.currentItem = 0
            }
        })
    }

    private fun initViewPager() {
        pager.offscreenPageLimit = 3
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //  bottomSheetFilterView.closeView()
            }

            override fun onPageSelected(position: Int) {
                onPageSelectedCalled(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    private fun onPageSelectedCalled(position: Int) {
        this.isForceSwipeToShop = false
        this.activeTabPosition = position
    }

    private fun initToolbar() {
        // setSupportActionBar(toolbar)
/*
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }*/

        // toolbar.setOnClickListener { moveToAutoCompleteActivity() }
    }


    /* private fun moveToAutoCompleteActivity() {
         ActivityCompat.startActivityForResult(
                 this,
                 getAutoCompleteIntent(),
                 AUTO_COMPLETE_ACTIVITY_REQUEST_CODE,
                 getOptionsForTransitionAnimation().toBundle())
     }*/


    /* private fun getAutoCompleteIntent(): Intent {
         val autoCompleteSearchParameter = SearchParameter()
         autoCompleteSearchParameter.setSearchQuery(searchParameter.getSearchQuery())

         val intent = RouteManager.getIntent(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
         intent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, autoCompleteSearchParameter)

         return intent
     }*/

    /*  private fun findViews() {
          var toolbar = findViewById<Toolbar>(R.id.toolbar)
          var container = findViewById<FrameLayout>(R.id.container)
          var loadingView = findViewById<ProgressBar>(R.id.progressBar)
          var tabLayout = findViewById<TabLayout>(R.id.tabs)
          var viewPager = findViewById<ViewPager>(R.id.pager)
          //bottomSheetFilterView = findViewById<BottomSheetFilterView>(R.id.bottomSheetFilter)
          var buttonFilter = findViewById<TextView>(R.id.button_filter)
          var iconFilter = findViewById<View>(R.id.icon_filter)
          var buttonSort = findViewById<TextView>(R.id.button_sort)
          var iconSort = findViewById<View>(R.id.icon_sort)
          var searchNavDivider = findViewById<View>(R.id.search_nav_divider)
          var searchNavContainer = findViewById<View>(R.id.search_nav_container)
      }*/
}
