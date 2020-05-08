package com.tokopedia.tokopoints.view.pointhistory

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.PointhistoryPlt.Companion.POINTHISTORY_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.PointhistoryPlt.Companion.POINTHISTORY_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.PointhistoryPlt.Companion.POINTHISTORY_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.PointhistoryPlt.Companion.POINTHISTORY_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity
import com.tokopedia.tokopoints.view.util.*
import kotlinx.android.synthetic.main.layout_tp_server_error.view.*
import kotlinx.android.synthetic.main.tp_content_point_history.*
import kotlinx.android.synthetic.main.tp_content_point_history.view.*
import kotlinx.android.synthetic.main.tp_content_point_history.view.rv_history_point
import kotlinx.android.synthetic.main.tp_content_point_history_header.view.*
import kotlinx.android.synthetic.main.tp_fragment_point_history.view.*
import kotlinx.android.synthetic.main.tp_history_point_header.*

import javax.inject.Inject

class PointHistoryFragment : BaseDaggerFragment(), PointHistoryContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {

    private var mStrPointExpInfo: String? = null
    private var mStrLoyaltyExpInfo: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAdapter: PointHistoryListAdapter

    private val mPresenter: PointHistoryViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(PointHistoryViewModel::class.java) }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null


    override fun getScreenName() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        return inflater.inflate(R.layout.tp_fragment_point_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initobserver()
    }

    private fun init(view: View) {
        if (view.rv_history_point.itemDecorationCount == 0) {
            view.rv_history_point.addItemDecoration(SpacesItemDecoration(activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_2), 0, 0))
        }
        view.rv_history_point.adapter = mAdapter
        view.btn_history_info.setOnClickListener { this.onClick(it) }
        view.text_failed_action.setOnClickListener { this.onClick(it) }
    }

    private fun initobserver() {
        addDataObeserver()
        addListLoadingObserver()
    }

    private fun addListLoadingObserver() {
        mPresenter.listLoading.observe(this, Observer {
            it?.let {
                when (it) {
                    is Loading -> mAdapter.startDataLoading()
                    is ErrorMessage -> {
                        if (it.data.isEmpty()) {
                            onEmptyList()
                            return@let
                        }
                        onError(it.data)
                    }
                    is Success -> {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        setOnRecyclerViewLayoutReady()
                        mAdapter.showData(it.data)
                    }
                }
            }
        })
    }

    private fun addDataObeserver() {
        mPresenter.data.observe(this, Observer {
            it?.let {
                when (it) {
                    is Loading -> showLoading()
                    is ErrorMessage -> {
                        onError(it.data)
                    }
                    is Success -> onSuccess(it.data.status?.points)

                }
            }
        })
    }

    private fun onEmptyList() {
        view?.apply {
            container_main.displayedChild = CONTAINER_ERROR
            img_error.setImageResource(R.drawable.tp_ic_empty_list)
            text_title_error.setText(R.string.tp_history_empty_title)
            text_label_error.setText(R.string.tp_history_empty_desc)
            text_failed_action.setText(R.string.tp_history_btn_action)
        }
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun showLoading() {
        view?.container_main?.displayedChild = CONTAINER_LOADER
    }

    override fun onSuccess(data: TokoPointStatusPointsEntity?) {
        view?.apply {
            data?.let { data ->
                con_header.visibility = View.VISIBLE
                text_my_points_value.text = CurrencyFormatUtil.convertPriceValue(data.reward.toDouble(), false)
                text_loyalty_value.text = CurrencyFormatUtil.convertPriceValue(data.loyalty.toDouble(), false)
                mStrPointExpInfo = data.rewardExpiryInfo
                mStrLoyaltyExpInfo = data.loyaltyExpiryInfo
                container_main.displayedChild = CONTAINER_DATA
            }

        }
    }

    override fun onError(error: String) {
        view?.container_main?.displayedChild = CONTAINER_ERROR
        view?.server_error_view?.showErrorUi(NetworkDetector.isConnectedToInternet(activityContext))
    }

    override fun getAppContext(): Context? {
        return activity
    }

    override fun getActivityContext(): Context? {
        return activity
    }


    override fun onClick(v: View) {
        if (v.id == R.id.btn_history_info) {
            showHistoryExpiryBottomSheet(mStrPointExpInfo, mStrLoyaltyExpInfo)
        } else if (v.id == R.id.text_failed_action) {
            val btnActionFailed = v as TextView
            mPresenter.onErrorButtonClicked(btnActionFailed.text.toString(), v.context)

        }
    }

    private fun showHistoryExpiryBottomSheet(pointInfo: String?, loyaltyInfo: String?) {
        val dialog = CloseableBottomSheetDialog.createInstanceRounded(activity)
        val view = layoutInflater.inflate(R.layout.tp_point_history_info, null, false)
        val textPoint = view.findViewById<TextView>(R.id.text_point_exp_info)
        val textLoyalty = view.findViewById<TextView>(R.id.text_loyalty_exp_info)
        textPoint.text = MethodChecker.fromHtml(pointInfo)
        textLoyalty.text = MethodChecker.fromHtml(loyaltyInfo)
        view.findViewById<View>(R.id.btn_help_history).setOnClickListener { v -> RouteManager.route(context, CommonConstant.WebLink.INFO_EXPIRED_POINTS, getString(R.string.tp_title_tokopoints)) }
        dialog.setCustomContentView(view, getString(R.string.tp_title_history_bottomshet), false)
        dialog.show()
        view.findViewById<View>(R.id.close_button).setOnClickListener { v -> dialog.dismiss() }
    }

    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2

        fun newInstance(extras: Bundle?): Fragment {
            val fragment = PointHistoryFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                POINTHISTORY_TOKOPOINT_PLT_PREPARE_METRICS,
                POINTHISTORY_TOKOPOINT_PLT_NETWORK_METRICS,
                POINTHISTORY_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(POINTHISTORY_TOKOPOINT_PLT)
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

    private fun setOnRecyclerViewLayoutReady() {
        rv_history_point.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        rv_history_point.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }
}
