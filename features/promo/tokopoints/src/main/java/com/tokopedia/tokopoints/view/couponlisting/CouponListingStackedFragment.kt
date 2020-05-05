package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingActivity
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.*
import kotlinx.android.synthetic.main.tp_fragment_stacked_coupon_listing.*
import kotlinx.android.synthetic.main.tp_fragment_stacked_coupon_listing.view.*
import javax.inject.Inject

class CouponListingStackedFragment : BaseDaggerFragment(), CouponListingStackedContract.View, View.OnClickListener, AdapterCallback {

    private var mItemDecoration: SpacesItemDecoration? = null

    @Inject
    lateinit var factory: ViewModelFactory

    val presenter: CouponLisitingStackedViewModel by lazy { ViewModelProviders.of(this, factory)[CouponLisitingStackedViewModel::class.java] }

    private val mAdapter: CouponListStackedBaseAdapter by lazy { CouponListStackedBaseAdapter(presenter, this) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tp_fragment_stacked_coupon_listing, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun getAppContext(): Context? {
        return context
    }

    override fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun getActivityContext(): Context? {
        return activity
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.MY_COUPON_LISTING_SCREEN_NAME
    }

    override fun initInjector() {
        DaggerTokopointBundleComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .tokopointsQueryModule(TokopointsQueryModule(requireActivity()))
            .build().inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_failed_action) {
            showLoader()
            mAdapter.loadData(mAdapter.currentPageIndex)
        }
    }

    private fun initViews(view: View) {
        mItemDecoration = SpacesItemDecoration(0,
                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_0),
                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_0))
        if (view.recycler_view_coupons.itemDecorationCount > 0) {
            view.recycler_view_coupons.removeItemDecoration(mItemDecoration!!)
        }
        view.recycler_view_coupons.addItemDecoration(mItemDecoration!!)
        view.recycler_view_coupons.adapter = mAdapter

    }

    private fun initListener() {
        if (view == null) {
            return
        }

        view!!.findViewById<View>(R.id.text_failed_action).setOnClickListener(this)
        view!!.findViewById<View>(R.id.button_continue).setOnClickListener { view12 ->
            val bundle = Bundle()
            bundle.putInt(CommonConstant.EXTRA_COUPON_COUNT, 0)
            startActivity(CatalogListingActivity.getCallingIntent(activityContext, bundle))
        }
        view!!.findViewById<View>(R.id.text_empty_action).setOnClickListener { v -> RouteManager.route(activityContext, ApplinkConstInternalGlobal.WEBVIEW, CommonConstant.WebLink.INFO) }

        swipe_refresh_layout.setOnRefreshListener {
            val id = presenter.category
            id?.let {  presenter.getCoupons(id) }
        }

        addListObserver()
        addInStackedObserverList()
    }

    private fun addInStackedObserverList() = presenter.inStackedAdapter.observe(this, Observer {
        it?.let {
            showCouponInStackBottomSheet(it)
        }
    })

    private fun addListObserver() = presenter.startAdapter.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mAdapter.resetAdapter()
                    mAdapter.notifyDataSetChanged()
                    mAdapter.startDataLoading()
                }
                is Success -> mAdapter.onSuccess(it.data)
                is ErrorMessage -> mAdapter.onError()
            }
        }
    })


    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }


    override fun emptyCoupons(errors: Map<String, String>?) {
        if (view == null || errors == null) {
            return
        }

        (view!!.findViewById<View>(R.id.img_error2) as ImageView).setImageResource(R.drawable.ic_tp_empty_pages)
        (view!!.findViewById<View>(R.id.text_title_error2) as TextView).text = errors[CommonConstant.CouponMapKeys.TITLE]
        (view!!.findViewById<View>(R.id.text_label_error2) as TextView).text = errors[CommonConstant.CouponMapKeys.SUB_TITLE]
        view!!.findViewById<View>(R.id.button_continue).visibility = View.VISIBLE

        container.displayedChild = CONTAINER_EMPTY
    }

    override fun onRetryPageLoad(pageNumber: Int) {}

    override fun onEmptyList(rawObject: Any) {
        hideLoader()
        emptyCoupons((rawObject as TokoPointPromosEntity).coupon.emptyMessage)
    }

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onFinishFirstPageLoad(count: Int, rawObject: Any?) {
        view!!.postDelayed({ hideLoader() }, CommonConstant.UI_SETTLING_DELAY_MS.toLong())
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        swipe_refresh_layout.isRefreshing = false
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            container.displayedChild = CONTAINER_ERROR
        }
        swipe_refresh_layout.isRefreshing = false
    }

    fun showCouponInStackBottomSheet(data: TokoPointPromosEntity) {
        val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(activity)
        val view = layoutInflater.inflate(R.layout.tp_bottosheet_coupon_in_stack, null, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_coupon_in_stack)

        if (mItemDecoration != null) {
            recyclerView.addItemDecoration(mItemDecoration!!)
        }

        val mStackedInadapter = CouponInStackBaseAdapter(object : AdapterCallback {
            override fun onRetryPageLoad(pageNumber: Int) {

            }

            override fun onEmptyList(rawObject: Any) {

            }

            override fun onStartFirstPageLoad() {

            }

            override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
                closeableBottomSheetDialog.show()
            }

            override fun onStartPageLoad(pageNumber: Int) {

            }

            override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {

            }

            override fun onError(pageNumber: Int) {

            }
        }, data)

        recyclerView.adapter = mStackedInadapter
        closeableBottomSheetDialog.setContentView(view)
        mStackedInadapter.startDataLoading()

    }

    override fun onDestroyView() {
        mAdapter.onDestroyView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val code = data!!.getStringExtra(CommonConstant.EXTRA_COUPON_CODE)
        if (requestCode == REQUEST_CODE_STACKED_ADAPTER) {
            mAdapter.couponCodeVisible(code, false)
        } else if (requestCode == REQUEST_CODE_STACKED_IN_ADAPTER) {
            mAdapter.couponStackedVisible()
        }
    }

    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2
        private val CONTAINER_EMPTY = 3
        val REQUEST_CODE_STACKED_IN_ADAPTER = 4
        val REQUEST_CODE_STACKED_ADAPTER = 5

        fun newInstance(): CouponListingStackedFragment {
            return CouponListingStackedFragment()
        }
    }
}
