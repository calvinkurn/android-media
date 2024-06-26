package com.tokopedia.tokopoints.view.couponlisting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.databinding.TpActivityStackedCouponListBinding
import com.tokopedia.tokopoints.di.BundleModule
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment.Companion.REQUEST_CODE_STACKED_ADAPTER
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment.Companion.REQUEST_CODE_STACKED_IN_ADAPTER
import com.tokopedia.tokopoints.view.model.CouponFilterItem
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.TAB_SETUP_DELAY_MS
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class CouponListingStackedActivity : BaseSimpleActivity(), StackedCouponActivityContract.View, HasComponent<TokopointBundleComponent> {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var mTabsFilter: TabLayout? = null
    private var mAdapter: StackedCouponFilterPagerAdapter? = null
    private var bundle: Bundle? = null

    @Inject
    internal lateinit var factory: ViewModelFactory

    private var binding: TpActivityStackedCouponListBinding? by viewBinding()

    private val mPresenter: StackedCouponActivtyViewModel by lazy { ViewModelProviders.of(this, factory)[StackedCouponActivtyViewModel::class.java] }

    override val activityContext: Context?
        get() = this

    override val appContext: Context?
        get() = applicationContext

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_coupon_listing_tokopoint
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        forDeeplink()
        super.onCreate(savedInstanceState)
        val userSession = UserSession(this)
        component.inject(this)
        addObserver()
        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        } else {
            mPresenter.getFilter()
        }
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_COUPON_LISTING, intent.data, bundle)
        }
    }

    private fun addObserver() {
        mPresenter.couponFilterViewModel.observe(
            this,
            Observer {
                it.let {
                    when (it) {
                        is Loading -> showLoading()
                        is ErrorMessage -> onError(it.data, NetworkDetector.isConnectedToInternet(this@CouponListingStackedActivity))
                        is Success -> {
                            hideLoading()
                            onSuccess(it.data.filter.categories)
                        }
                        else -> {
                            // no-op
                        }
                    }
                }
            }
        )
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        // Intermittent crash on Marshmallow
        try {
            binding = TpActivityStackedCouponListBinding.inflate(layoutInflater)
            setContentView(binding?.root)
        } catch (e: Exception) {
            finish()
            return
        }
        toolbar = findViewById<View>(toolbarResourceID) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = this.title
        }
        updateTitle(getString(R.string.tp_label_my_coupon_new))
        binding?.serverErrorView?.setErrorButtonClickListener {
            mPresenter.getFilter()
        }
        initViews()
    }

    override fun getLayoutRes(): Int {
        return R.layout.tp_activity_stacked_coupon_list
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector() =
        DaggerTokopointBundleComponent.builder()
            .bundleModule(BundleModule(bundle ?: Bundle()))
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokopointsQueryModule(TokopointsQueryModule(this))
            .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            mPresenter.getFilter()
        } else if ((requestCode == REQUEST_CODE_STACKED_IN_ADAPTER || requestCode == REQUEST_CODE_STACKED_ADAPTER) && resultCode == Activity.RESULT_OK) {
            binding?.apply {
                val fragment = mAdapter?.getRegisteredFragment(viewPagerSortType.currentItem)
                data?.let {
                    fragment?.onActivityResult(requestCode, resultCode, it)
                }
            }
        } else {
            finish()
        }
    }

    override fun showLoading() {
        binding?.container?.displayedChild = 0
    }

    override fun hideLoading() {
        binding?.container?.displayedChild = 1
    }

    override fun onSuccess(data: List<CouponFilterItem>) {
        binding?.viewPagerSortType?.apply {
            mAdapter = StackedCouponFilterPagerAdapter(supportFragmentManager, data)

            adapter = mAdapter
            mTabsFilter!!.setupWithViewPager(this)
            mTabsFilter!!.visibility = View.VISIBLE

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    val fragment = mAdapter?.getRegisteredFragment(position) as CouponListingStackedFragment?

                    fragment?.apply {
                        if (isAdded) {
                            presenter.getCoupons(data[position].id)
                        }
                    }
                    AnalyticsTrackerUtil.sendEvent(
                        activityContext,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                        "click " + data[position],
                        ""
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })

            postDelayed({
                val ids = getSelectedCategoryId(data)
                currentItem = ids[1]
                if (ids[0] == FIRST_TAB_NUM) {
                    loadFirstTab(ids[0])
                }
            }, TAB_SETUP_DELAY_MS.toLong())
        }
        // Setting up sort types tabsK
    }

    override fun onError(error: String, hasInternet: Boolean) {
        binding?.apply {
            container.displayedChild = 2
            serverErrorView.showErrorUi(hasInternet)
        }
    }

    override fun getStringRaw(id: Int): String {
        return GraphqlHelper.loadRawString(resources, id)
    }

    private fun initViews() {
        mTabsFilter = findViewById(R.id.tabs_sort_type)
    }

    fun loadFirstTab(categoryId: Int) {
        binding?.viewPagerSortType?.apply {
            val fragment = mAdapter?.getRegisteredFragment(currentItem) as? CouponListingStackedFragment?
            fragment?.apply {
                if (isAdded) {
                    presenter.getCoupons(categoryId)
                }
            }
        }
    }

    private fun getSelectedCategoryId(data: List<CouponFilterItem>): IntArray {
        val ids = intArrayOf(0, 0)
        var counter = 0

        for (item in data) {
            if (item.isSelected) {
                ids[0] = item.id
                ids[1] = counter
                break
            }
            counter++
        }

        return ids
    }

    companion object {
        private val REQUEST_CODE_LOGIN = 1
        private const val FIRST_TAB_NUM = -1

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, CouponListingStackedActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, CouponListingStackedActivity::class.java)
        }
    }
}
