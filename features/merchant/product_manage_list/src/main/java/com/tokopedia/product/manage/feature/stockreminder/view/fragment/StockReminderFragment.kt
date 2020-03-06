package com.tokopedia.product.manage.feature.stockreminder.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_stock_reminder.*
import javax.inject.Inject

class StockReminderFragment: BaseDaggerFragment() {

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        private const val ARG_PRODUCT_NAME = "product_name"

        fun createInstance(productId: Long, productName: String): Fragment {
            val fragment = StockReminderFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId.toString())
                putString(ARG_PRODUCT_NAME, productName)
            }
            return fragment
        }
    }

    private var productId: String? = null
    private var productName: String? = null
    private var warehouseId: String? = null
    private var threshold: Int? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(StockReminderViewModel::class.java) }

    override fun getScreenName(): String = javaClass.simpleName

    override fun initInjector() {
        activity?.let {
            DaggerStockReminderComponent
                    .builder()
                    .productManageComponent(ProductManageInstance.getComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARG_PRODUCT_ID)?.let{ productId = it }
        arguments?.getString(ARG_PRODUCT_NAME)?.let { productName = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_stock_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLogin()

        viewModel.getStockReminderLiveData.observe(viewLifecycleOwner, getStockReminderObserver())
        viewModel.createStockReminderLiveData.observe(viewLifecycleOwner, createStockReminderObserver())
        viewModel.updateStockReminderLiveData.observe(viewLifecycleOwner, updateStockReminderObserver())

        getStockReminder()

        swStockReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                containerStockReminder.visibility = View.VISIBLE
            } else {
                containerStockReminder.visibility = View.GONE
            }
        }

        btnSaveReminder.setOnClickListener {
            if(qeStock.getValue() == 0) qeStock.setValue(1)
            else if(qeStock.getValue() > 100) qeStock.setValue(100)

            if (threshold == 0) {
                threshold = qeStock.getValue()
                createStockReminder()
            } else {
                threshold = qeStock.getValue()
                updateStockReminder()
            }
        }
    }

    private fun checkLogin() {
        if (!userSession.isLoggedIn) {
            RouteManager.route(context, ApplinkConst.LOGIN)
            activity?.finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(context, ApplinkConst.HOME)
            activity?.finish()
        }
    }

    private fun showLoading() {
        ImageHandler.loadGif(ivLoadingStockReminder, R.drawable.ic_loading_indeterminate, R.drawable.ic_loading_indeterminate)
        loadingStockReminder.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        ImageHandler.clearImage(ivLoadingStockReminder)
        loadingStockReminder.visibility = View.GONE
    }

    private fun doResultIntent() {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_PRODUCT_NAME, productName)
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        activity?.finish()
    }

    private fun getStockReminder() {
        showLoading()
        productId?.let { viewModel.getStockReminder(it) }

    }

    private fun createStockReminder() {
        showLoading()
        productId?.let { productId ->
            warehouseId?.let { warehouseId ->
                viewModel.createStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            }
        }
    }

    private fun updateStockReminder() {
        showLoading()
        productId?.let { productId ->
            warehouseId?.let { warehouseId ->
                viewModel.updateStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            }
        }
    }

    private fun getStockReminderObserver() = Observer<Result<GetStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> {
                warehouseId = stockReminderData.data.getByProductIds.data.getOrNull(0)?.productsWareHouse?.getOrNull(0)?.wareHouseId
                threshold = stockReminderData.data.getByProductIds.data.getOrNull(0)?.productsWareHouse?.getOrNull(0)?.threshold

                threshold?.let { qeStock.setValue(it) }
                if (threshold != 0) swStockReminder.isChecked = true
            }
            is Fail -> {
                globalErrorStockReminder.visibility = View.VISIBLE
                geStockReminder.setType(GlobalError.SERVER_ERROR)
                geStockReminder.setActionClickListener {
                    globalErrorStockReminder.visibility = View.GONE
                    getStockReminder()
                }
            }
        }
    }

    private fun createStockReminderObserver()= Observer<Result<CreateStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> { doResultIntent() }
            is Fail -> {
                Toaster.make(layout, getString(R.string.product_stock_reminder_toaster_failed_desc), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.product_stock_reminder_toaster_action_text))
                Toaster.onCTAClick = View.OnClickListener { createStockReminder() }
            }
        }
    }

    private fun updateStockReminderObserver() = Observer<Result<UpdateStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> { doResultIntent() }
            is Fail -> {
                Toaster.make(layout, getString(R.string.product_stock_reminder_toaster_failed_desc), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.product_stock_reminder_toaster_action_text))
                Toaster.onCTAClick = View.OnClickListener { updateStockReminder() }
            }
        }
    }

}