package com.tokopedia.product.manage.stock_reminder.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.stock_reminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.stock_reminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_stock_reminder.*
import javax.inject.Inject

class StockReminderFragment: BaseDaggerFragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stock_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stockReminderLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    println("data : ${it.data.getByProductIds.data[0].productId}")
                }
                is Fail -> {
                    Log.d("Hello", "Failed")
                }
            }
        })
        viewModel.getStockReminderData()

        swStockReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                containerStockReminder.visibility = View.VISIBLE
            } else {
                containerStockReminder.visibility = View.GONE
            }
        }
    }


}