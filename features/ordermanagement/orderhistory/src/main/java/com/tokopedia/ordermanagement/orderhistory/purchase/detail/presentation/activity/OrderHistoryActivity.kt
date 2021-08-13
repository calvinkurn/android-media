package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.ordermanagement.orderhistory.R
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.adapter.OrderHistoryAdapter
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.customview.OrderHistoryStepperLayout
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.di.DaggerOrderHistoryComponent
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.OrderHistoryResult
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel.OrderHistoryViewModel
import javax.inject.Inject

/**
 * Created by kris on 11/7/17. Tokopedia
 */
class OrderHistoryActivity : BaseSimpleActivity() {

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        private const val EXTRA_USER_MODE = "EXTRA_USER_MODE"
    }

    private var extraOrderId: String = ""
    private var extraUserMode: Int = 0

    private var mainView: View? = null
    private var mainViewContainer: View? = null
    private var mainProgressDialog: ProgressDialog? = null
    private var recyclerView: RecyclerView? = null

    @Inject
    lateinit var viewModel: OrderHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initInjector()
        getDataFromIntent()
        observeOrderHistory()
        viewModel.getOrderHistory(extraOrderId, extraUserMode)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun inflateFragment() {
        //no op
    }

    override fun getLayoutRes(): Int {
        return R.layout.order_history_layout
    }

    private fun initInjector() {
        val component = DaggerOrderHistoryComponent
                .builder()
                .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
                .build()
        component.inject(this)
    }

    private fun initView() {
        mainView = findViewById(R.id.main_view)
        mainProgressDialog = ProgressDialog(this)
        mainViewContainer = findViewById(R.id.main_container)
        recyclerView = findViewById(R.id.order_history_list)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun observeOrderHistory() {
        viewModel.orderHistory.observe(this, Observer {
            when(it) {
                is OrderHistoryResult.OrderHistoryLoading -> {
                    showMainViewLoadingPage()
                }
                is OrderHistoryResult.OrderHistorySuccess -> {
                    hideMainViewLoadingPage()
                    setData(it.response)
                }
                is OrderHistoryResult.OrderHistoryFail -> {
                    hideMainViewLoadingPage()
                    onLoadError(it.throwable)
                }
            }
        })
    }

    private fun setData(data: OrderHistoryData) {
        val stepperLayout = findViewById<OrderHistoryStepperLayout>(R.id.order_history_stepper_layout)
        stepperLayout.setStepperStatus(data)
        recyclerView?.adapter = OrderHistoryAdapter(data.orderListData)
    }

    private fun showMainViewLoadingPage() {
        mainProgressDialog?.show()
        mainViewContainer?.visibility = View.GONE
    }

    private fun hideMainViewLoadingPage() {
        mainProgressDialog?.dismiss()
        mainViewContainer?.visibility = View.VISIBLE
    }

    private fun onLoadError(throwable: Throwable) {
        logExceptionToCrashlytics(throwable)
        NetworkErrorHelper.showEmptyState(this, mainView) {
            viewModel.getOrderHistory(extraOrderId, extraUserMode)
        }
    }

    private fun getDataFromIntent() {
        extraOrderId = intent.extras?.getString(EXTRA_ORDER_ID) ?: ""
        extraUserMode = intent.extras?.getInt(EXTRA_USER_MODE) ?: 0
    }

    private fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}