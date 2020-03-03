package com.tokopedia.product.manage.feature.stockreminder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
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

    private var productId: String = ""
    private var productName: String = ""
    private var warehouseId: String = ""
    private var threshold: Int = 0

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(StockReminderViewModel::class.java) }

    override fun getScreenName(): String = javaClass.simpleName

    override fun initInjector() {
        DaggerStockReminderComponent
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARG_PRODUCT_ID)?.let{
            productId = it
        }
        arguments?.getString(ARG_PRODUCT_NAME)?.let {
            productName = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_stock_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStockReminderLiveData.observe(viewLifecycleOwner, getStockReminderObserver)
        viewModel.createStockReminderLiveData.observe(viewLifecycleOwner, createStockReminderObserver)
        viewModel.updateStockReminderLiveData.observe(viewLifecycleOwner, updateStockReminderObserver)
        viewModel.getStockReminder(productId)

        swStockReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                containerStockReminder.visibility = View.VISIBLE
            } else {
                containerStockReminder.visibility = View.GONE
            }
        }

        qeStock.setValueChangedListener { _, _, _ ->
            btnSaveReminder.isEnabled =  qeStock.getValue() != 0
        }
        
        btnSaveReminder.setOnClickListener {
            if (threshold == 0) {
                threshold = qeStock.getValue()
                viewModel.createStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            } else {
                threshold = qeStock.getValue()
                viewModel.updateStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            }
            activity?.finish()
        }
    }

    private val getStockReminderObserver = Observer<Result<GetStockReminderResponse>> { stockReminderData ->
        when(stockReminderData) {
            is Success -> {
                warehouseId = stockReminderData.data.getByProductIds.data[0].productsWareHouse[0].wareHouseId
                threshold = stockReminderData.data.getByProductIds.data[0].productsWareHouse[0].threshold

                qeStock.setValue(threshold)

                if(threshold != 0) {
                    swStockReminder.isChecked = true
                }
            }
            is Fail -> { }
        }
    }

    private val createStockReminderObserver = Observer<Result<CreateStockReminderResponse>> { stockReminderData ->
        when(stockReminderData) {
            is Success -> {
                Toaster.make(layout, getString(R.string.product_stock_reminder_toaster_success_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            }
            is Fail -> { }
        }
    }

    private val updateStockReminderObserver = Observer<Result<UpdateStockReminderResponse>> { stockReminderData ->
        when(stockReminderData) {
            is Success -> {
                Toaster.make(layout, getString(R.string.product_stock_reminder_toaster_success_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            }
            is Fail -> { }
        }
    }

}