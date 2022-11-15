package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.adapter.CatalogListAdapter
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistItemPlt.Companion.CATALOGLISTITEM_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistItemPlt.Companion.CATALOGLISTITEM_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistItemPlt.Companion.CATALOGLISTITEM_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistItemPlt.Companion.CATALOGLISTITEM_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.model.CatalogEntity
import com.tokopedia.tokopoints.view.model.CatalogStatusItem
import com.tokopedia.tokopoints.view.sendgift.SendGiftFragment
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.TokoPointsRemoteConfig.Companion.instance
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CatalogListItemFragment : BaseDaggerFragment(), CatalogListItemContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mContainer: ViewFlipper? = null
    private var serverErrorView: ServerErrorView? = null
    private var mRecyclerViewCatalog: RecyclerView? = null
    private var mAdapter: CatalogListAdapter? = null
    private var mRefreshTime: Long = 0
    private var mTimer: Timer? = null
    private var mHandler: Handler? = Handler()
    var finalCatalogList: ArrayList<Any> = ArrayList()

    private var mRunnableUpdateCatalogStatus: Runnable? = Runnable {
        val items: MutableList<Int> = ArrayList()
        for (each in finalCatalogList) {
            if (each is CatalogsValueEntity) {
                if (each.catalogType == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                    items.add(each.id)
                }
            }
        }
        viewModel.fetchLatestStatus(items)
    }

    @Inject
    lateinit var viewModel: CatalogListItemViewModel
    private var mSwipeToRefresh: SwipeToRefresh? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override val activityContext: Context
        get() = requireActivity()

    override val appContext: Context
        get() = requireContext()

    override val currentCategoryId: Int
        get() {
            return if (arguments != null) {
              requireArguments().getInt(CommonConstant.ARGS_CATEGORY_ID)
            } else CommonConstant.DEFAULT_CATEGORY_TYPE
        }

    override val currentSubCategoryId: Int
        get(){
            return if (arguments != null) {
                requireArguments().getInt(CommonConstant.ARGS_SUB_CATEGORY_ID)
            } else CommonConstant.DEFAULT_CATEGORY_TYPE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        fetchRemoteConfig()
        val rootView = inflater.inflate(R.layout.tp_fragment_catalog_tabs_item, container, false)
        mRecyclerViewCatalog = rootView.findViewById(R.id.list_catalog_item)
        mSwipeToRefresh = rootView.findViewById(R.id.swipe_refresh_layout)
        if (pointsAvailability) { // set padding of recycler view according to membershipdata availability
            mRecyclerViewCatalog?.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.tp_margin_bottom_membership_and_egg))
        } else {
            mRecyclerViewCatalog?.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.tp_margin_bottom_egg))
        }
        mContainer = rootView.findViewById(R.id.container)
        serverErrorView = rootView.findViewById(R.id.server_error_view)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.text_failed_action).setOnClickListener(this)
        view.findViewById<View>(R.id.text_empty_action).setOnClickListener(this)
        mSwipeToRefresh?.setOnRefreshListener {
            getCatalogList(currentCategoryId, currentSubCategoryId)
        }
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initObserver()
    }

    private fun initObserver() {
        addLatestStatusObserver()
        addCatalogListObserver()
    }

    private fun addCatalogListObserver() = viewModel.listCatalogItem.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it.let {
            when (it) {
                is Loading -> showLoader()
                is ErrorMessage -> showError()
                is Success -> {
                    hideLoader()
                    populateCatalog(it.data)
                }
            }
        }
    })

    private fun addLatestStatusObserver() = viewModel.latestStatusLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            stopNetworkRequestPerformanceMonitoring()
            startRenderPerformanceMonitoring()
            refreshCatalog(it)
            stopRenderPerformanceMonitoring()
            stopPerformanceMonitoring()
        }
    })

    override fun onClick(view: View) {
        if (view.id == R.id.text_failed_action) {
            getCatalogList(currentCategoryId, currentSubCategoryId)
        } else if (view.id == R.id.text_empty_action) {
            openWebView(CommonConstant.WebLink.INFO)
        }
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun showLoader() {
        mContainer!!.displayedChild = CONTAINER_LOADER
        mSwipeToRefresh!!.isRefreshing = false
    }

    override fun showError() {
        mContainer!!.displayedChild = CONTAINER_ERROR
        mSwipeToRefresh!!.isRefreshing = false
        serverErrorView!!.showErrorUi(NetworkDetector.isConnectedToInternet(appContext))
    }

    override fun onEmptyCatalog() {
        mContainer!!.displayedChild = CONTAINER_EMPTY
        view?.findViewById<TextView>(R.id.text_title_error2)?.text = getString(R.string.tp_catalog_empty_title)
        view?.findViewById<TextView>(R.id.text_label_error2)?.text = getString(R.string.tp_catalog_empty_description)
        val btnCOntinue = view?.findViewById<TextView>(R.id.button_continue)
        btnCOntinue?.text = getString(R.string.tp_catalog_empty_button)
        btnCOntinue?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.TOKOPEDIA_REWARD)
        }
        view?.findViewById<Button>(R.id.text_empty_action)?.hide()
    }

    override fun openWebView(url: String) {
        if (!TextUtils.isEmpty(url)) {
            RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        }
    }

    override fun hideLoader() {
        mContainer!!.displayedChild = CONTAINER_DATA
        mSwipeToRefresh!!.isRefreshing = false
    }

    val pointsAvailability: Boolean
        get() = if (arguments != null) {
            requireArguments().getBoolean(CommonConstant.ARGS_POINTS_AVAILABILITY, false)
        } else false

    fun getCatalogList(currentCategoryId: Int, currentSubCategoryId: Int) {
        viewModel.getCataloglistItem(currentCategoryId, currentSubCategoryId, viewModel.pointRange)
    }

    private fun refreshCatalog(items: List<CatalogStatusItem>) {
        if (items == null || items.isEmpty()) {
            return
        }
        for (each in items) {
            if (each == null) {
                continue
            }
            if (mAdapter?.itemCount != null) {
                for (i in 0 until mAdapter?.itemCount!!) {
                    val item = finalCatalogList[i]
                    if (item is CatalogsValueEntity) {
                        item?.let {
                            if (each.catalogID == item.id) {
                                item.isDisabled = each.isDisabled
                                item.isDisabledButton = each.isDisabledButton
                                item.upperTextDesc = each.upperTextDesc
                                item.quota = each.quota
                            }
                        }
                    }
                }
            }
        }
        if (mAdapter != null) {
            mAdapter!!.notifyDataSetChanged()
        }
    }

    private fun decorateDialog(dialog: AlertDialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activityContext,
                    com.tokopedia.unifyprinciples.R.color.Unify_G400))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }
        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activityContext,
                    com.tokopedia.unifyprinciples.R.color.Unify_N200))
        }
    }

    /*This section is exclusively for handling flash-sale timer*/
    private fun startUpdateCatalogStatusTimer() {
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (mHandler != null) {
                    mRunnableUpdateCatalogStatus?.let { it ->
                        mHandler!!.post(it)
                    }
                }
            }
        }, 0, if (mRefreshTime > 0) mRefreshTime else CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong())
    }


    private fun fetchRemoteConfig() {
        mRefreshTime = instance(requireContext()).getLongRemoteConfig(CommonConstant.TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S, CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
        if (mHandler != null) {
            mRunnableUpdateCatalogStatus?.let { it ->
                mHandler!!.removeCallbacks(it)
            }
            mHandler = null
        }
        mRunnableUpdateCatalogStatus = null
    }

    override fun onPreValidateError(title: String, message: String) {
        val adb = AlertDialog.Builder(activityContext)
        adb.setTitle(title)
        adb.setMessage(message)
        adb.setPositiveButton(R.string.tp_label_ok
        ) { _,_ -> }
        val dialog = adb.create()
        dialog.show()
        decorateDialog(dialog)
    }

    override fun gotoSendGiftPage(id: Int, title: String, pointStr: String) {
        val bundle = Bundle()
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id)
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title)
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr)
        val sendGiftFragment = SendGiftFragment()
        sendGiftFragment.arguments = bundle
        sendGiftFragment.show(childFragmentManager, CommonConstant.FRAGMENT_DETAIL_TOKOPOINT)
    }

    private fun populateCatalog(catalogEntity: CatalogEntity) {

        if (catalogEntity.catalogs.isNullOrEmpty()) {
            onEmptyCatalog()
        } else {
            finalCatalogList.clear()
            catalogEntity.catalogs?.let { finalCatalogList.addAll(it) }
            if (catalogEntity.countDownInfo?.isShown != null && catalogEntity.countDownInfo.isShown) {
                catalogEntity.countDownInfo.let { finalCatalogList.add(0, it) }
            }
            mAdapter = CatalogListAdapter(finalCatalogList)
            if (mRecyclerViewCatalog?.itemDecorationCount == 0) {
                mRecyclerViewCatalog?.addItemDecoration(SpacesItemDecoration(activityContext.resources.getDimensionPixelOffset(R.dimen.dp_10),
                        activityContext.resources.getDimensionPixelOffset(R.dimen.dp_14),
                        activityContext.resources.getDimensionPixelOffset(R.dimen.dp_14)))
            }
            mRecyclerViewCatalog?.adapter = mAdapter
            mAdapter?.notifyDataSetChanged()
            if (mTimer == null) {
                startUpdateCatalogStatusTimer()
            }
        }
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2
        private const val CONTAINER_EMPTY = 3
        fun newInstance(categoryId: Int, subCategoryId: Int, isPointsAvailable: Boolean): Fragment {
            val fragment: Fragment = CatalogListItemFragment()
            val bundle = Bundle()
            bundle.putInt(CommonConstant.ARGS_CATEGORY_ID, categoryId)
            bundle.putInt(CommonConstant.ARGS_SUB_CATEGORY_ID, subCategoryId)
            bundle.putBoolean(CommonConstant.ARGS_POINTS_AVAILABILITY, isPointsAvailable)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                CATALOGLISTITEM_TOKOPOINT_PLT_PREPARE_METRICS,
                CATALOGLISTITEM_TOKOPOINT_PLT_NETWORK_METRICS,
                CATALOGLISTITEM_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(CATALOGLISTITEM_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }
}
