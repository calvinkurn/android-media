package com.tokopedia.mlp.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.mlp.adapter.MLPWidgetAdapter
import com.tokopedia.mlp.contractModel.*
import com.tokopedia.mlp.di.DaggerMerchantLendingComponent
import com.tokopedia.mlp.di.MerchantLendingComponent
import com.tokopedia.mlp.di.MerchantLendingUseCaseModule
import com.tokopedia.mlp.merchantViewModel.MerchantLendingViewModel
import kotlinx.android.synthetic.main.mlp_widget_container.*
import javax.inject.Inject

public class MerchantLendingFragment : BaseDaggerFragment() {

    lateinit var drawable: Drawable

    lateinit var merchantLendingComponent: MerchantLendingComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var merchantLendingViewModel: MerchantLendingViewModel

    private var widgetList = ArrayList<WidgetsItem>()


    override fun getScreenName(): String {
        return "MerchantLendingFragment"
    }

    override fun initInjector() {
        merchantLendingComponent = DaggerMerchantLendingComponent.builder().merchantLendingUseCaseModule(MerchantLendingUseCaseModule(this.context!!)).build()
        merchantLendingComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mlp_widget_container, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initViewContainer()
        //   initView()
        initViewModel()
        observeData()

    }

    private fun initViewContainer() {
        val linearLayoutmanager = LinearLayoutManager(context)
        val boxAdapter = MLPWidgetAdapter(widgetList, this.context!!)
        widget_container.layoutManager = linearLayoutmanager
        widget_container.adapter = boxAdapter
    }

    fun initViewModel() {
        merchantLendingViewModel = ViewModelProviders.of(this, viewModelFactory).get(MerchantLendingViewModel::class.java)
        merchantLendingViewModel.bound()
    }

    fun observeData() {
        merchantLendingViewModel.getCategoryList().observe(this, Observer<LeWidgetData> {

            val lengthDataLeWidget: Int = it?.leWidget?.widgets?.size!!

            for (widgetNo in 0 until lengthDataLeWidget) {

                it.leWidget.widgets.let {

                    widgetList.clear()
                    widgetList.addAll(it as ArrayList<WidgetsItem>)
                    widget_container.adapter.notifyDataSetChanged()

                }
            }
        })
    }

}




