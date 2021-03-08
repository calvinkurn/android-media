package com.tokopedia.ordermanagement.orderhistory.purchase.detail.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.ordermanagement.orderhistory.R
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.adapter.OrderHistoryAdapter
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.customview.OrderHistoryStepperLayout
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.di.DaggerOrderHistoryComponent
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presenter.OrderHistoryPresenterImpl
import javax.inject.Inject

/**
 * Created by kris on 11/7/17. Tokopedia
 */
class OrderHistoryActivity : BaseSimpleActivity(), OrderHistoryView {

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        private const val EXTRA_USER_MODE = "EXTRA_USER_MODE"
        fun createInstance(context: Context?, orderId: String?, userMode: Int): Intent {
            val intent = Intent(context, OrderHistoryActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_ORDER_ID, orderId)
            bundle.putInt(EXTRA_USER_MODE, userMode)
            intent.putExtras(bundle)
            return intent
        }
    }

    private val extraOrderId: String
        get() = intent.extras?.getString(EXTRA_ORDER_ID) ?: ""

    private val extraUserMode: Int
        get() = intent.extras?.getInt(EXTRA_USER_MODE) ?: 0

    private var mainViewContainer: View? = null
    private var mainProgressDialog: ProgressDialog? = null

    @Inject
    lateinit var presenter: OrderHistoryPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainProgressDialog = ProgressDialog(this)
        mainViewContainer = findViewById(R.id.main_container)
        initInjector()
        presenter.setMainViewListener(this)
        presenter.fetchHistoryData(this, extraOrderId, extraUserMode)
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

    private fun initView(data: OrderHistoryData) {
        val stepperLayout = findViewById<OrderHistoryStepperLayout>(R.id.order_history_stepper_layout)
        stepperLayout.setStepperStatus(data)
        val orderHistoryList = findViewById<RecyclerView>(R.id.order_history_list)
        orderHistoryList.isNestedScrollingEnabled = false
        orderHistoryList.layoutManager = LinearLayoutManager(this)
        orderHistoryList.adapter = OrderHistoryAdapter(
                data.orderListData)
    }

    override fun receivedHistoryData(data: OrderHistoryData) {
        initView(data)
    }

    override fun onLoadError(message: String?) {
        NetworkErrorHelper.showEmptyState(this, mainView) {
            presenter.fetchHistoryData(this@OrderHistoryActivity,
                    extraOrderId,
                    extraUserMode)
        }
    }

    private val mainView: View
        get() = findViewById(R.id.main_view)

    override fun showMainViewLoadingPage() {
        mainProgressDialog!!.show()
        mainViewContainer!!.visibility = View.GONE
    }

    override fun hideMainViewLoadingPage() {
        mainProgressDialog!!.dismiss()
        mainViewContainer!!.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}