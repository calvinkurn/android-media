package com.tokopedia.smartbills.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_smart_bills_add_telco.*
import javax.inject.Inject

class SmartBillsAddTelcoFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsAddTelcoViewModel

    private var templateTelco: String? = null
    private var categoryId: String? = null
    private var menuId: String? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        templateTelco = (activity as SmartBillsAddTelcoActivity).getTemplateTelco()
        categoryId = (activity as SmartBillsAddTelcoActivity).getCategoryId()
        menuId = (activity as SmartBillsAddTelcoActivity).getMenuId()
        return inflater.inflate(R.layout.fragment_smart_bills_add_telco, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SmartBillsAddTelcoViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoader()
        getMenuDetailTicker()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeTicker()
    }

    private fun getMenuDetailTicker(){
        viewModel.getMenuDetailAddTelco(viewModel.createMenuDetailAddTelcoParams(getMenuID()))
    }

    private fun getMenuID(): Int{
        return menuId.toIntOrZero()
    }

    private fun observeTicker(){
        observe(viewModel.listTicker){
            when(it){
                is Success -> {
                    if (!it.data.isNullOrEmpty()){
                        showTicker(it.data)
                    } else {
                        hideTicker()
                    }
                }

                is Fail -> {
                    hideTicker()
                }
            }
            hideLoader()
        }
    }

    private fun hideTicker(){
        ticker_sbm_add_telco.hide()
    }

    private fun showTicker(listTicker: List<TopupBillsTicker>){
        ticker_sbm_add_telco.show()
        val messages = ArrayList<TickerData>()
        for (item in listTicker) {
            messages.add(TickerData(item.name, item.content,
                    when (item.type) {
                        "warning" -> Ticker.TYPE_WARNING
                        "info" -> Ticker.TYPE_INFORMATION
                        "success" -> Ticker.TYPE_ANNOUNCEMENT
                        "error" -> Ticker.TYPE_ERROR
                        else -> Ticker.TYPE_INFORMATION
                    }, isFromHtml = true))
        }
        context?.run {
            val tickerAdapter = TickerPagerAdapter(this, messages)
            tickerAdapter.setDescriptionClickEvent(object: TickerCallback{
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(context, linkUrl.toString())
                }

                override fun onDismiss() {

                }
            })

            ticker_sbm_add_telco.addPagerView(tickerAdapter, messages)
        }
    }

    private fun showLoader(){
        loader_sbm_add_telco.show()
        hideTicker()
    }

    private fun hideLoader(){
        loader_sbm_add_telco.hide()
    }

    companion object {
        fun newInstance() = SmartBillsAddTelcoFragment()
    }
}