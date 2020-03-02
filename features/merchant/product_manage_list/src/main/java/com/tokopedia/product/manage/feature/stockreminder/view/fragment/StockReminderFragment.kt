package com.tokopedia.product.manage.feature.stockreminder.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.product.manage.oldlist.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_stock_reminder.*
import javax.inject.Inject

class StockReminderFragment: BaseDaggerFragment() {

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        fun createInstance(productId: Long): Fragment {
            val fragment = StockReminderFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId.toString())
            }
            return fragment
        }
    }

    private var productId: String = ""
    private var warehouseId: String = ""

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stock_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stockReminderLiveData.observe(viewLifecycleOwner, stockReminderObserver)
        viewModel.stockReminderCreateLiveData.observe(viewLifecycleOwner, stockReminderCreateObserver)
        viewModel.getStockReminder(productId)

        swStockReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                containerStockReminder.visibility = View.VISIBLE
            } else {
                containerStockReminder.visibility = View.GONE
            }
        }

        btnSaveReminder.setOnClickListener {
            viewModel.createStockReminder(userSession.shopId, productId, warehouseId, "5")
        }
    }

    private val stockReminderObserver = Observer<Result<GetStockReminderResponse>> { stockReminderData ->
        when(stockReminderData) {
            is Success -> {
                warehouseId = stockReminderData.data.getByProductIds.data[0].productsWareHouse[0].wareHouseId
                Log.d("Hello", warehouseId)
            }
            is Fail -> {
                Log.d("Hello Failed", "Failed")
            }
        }
    }

    private val stockReminderCreateObserver = Observer<Result<CreateStockReminderResponse>> { stockReminderData ->
        when(stockReminderData) {
            is Success -> {
                Log.d("Hello Create", stockReminderData.data.createStockAlertThreshold.data[0].productId)
                Log.d("Hello Create", stockReminderData.data.createStockAlertThreshold.data[0].wareHouseId)
                Log.d("Hello Create", stockReminderData.data.createStockAlertThreshold.data[0].threshold)
            }
            is Fail -> {
                Log.d("Hello Failed", "Failed")
            }
        }
    }

}