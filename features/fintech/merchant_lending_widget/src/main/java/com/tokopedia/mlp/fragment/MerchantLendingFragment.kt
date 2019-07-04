package com.tokopedia.mlp.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.support.v7.widget.SimpleItemAnimator
import android.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.mlp.callback.MLPWidgetAdapterCallBack


class MerchantLendingFragment : BaseDaggerFragment(),MLPWidgetAdapterCallBack {

    lateinit var drawable: Drawable
    lateinit var merchantLendingComponent: MerchantLendingComponent
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var merchantLendingViewModel: MerchantLendingViewModel
    private val widgetList = ArrayList<WidgetsItem>()
    private var isExpanded: Boolean = false
    private val animateDuration:Long=300
    private lateinit var toggleSaldoPrioritasListener: MLPWidgetAdapter.ToggleSaldoPrioritasLisneter

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
        initViewModel()
        observeData()
        checkExpanded()
        observeSPData()
    }

    override fun toggleSaldoPrioritas( listener: MLPWidgetAdapter.ToggleSaldoPrioritasLisneter) {
        merchantLendingViewModel.executeUseCaseToggle()
        toggleSaldoPrioritasListener = listener
    }

    private fun checkExpanded() {
        iv_collapsewidget.setOnClickListener {
            isExpanded = !isExpanded;
            if (isExpanded) {
                iv_collapsewidget.animate().rotation(180f).duration = animateDuration
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(widget_container)
                }
            } else {
                iv_collapsewidget.animate().rotation(0f).duration = animateDuration
            }
            val mlpWidgetAdapter = widget_container.adapter as MLPWidgetAdapter
            mlpWidgetAdapter.isexpanded = isExpanded
            widget_container.adapter.notifyDataSetChanged()
        }
    }

    private fun initViewContainer() {
        (widget_container.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true

        val linearLayoutManager = LinearLayoutManager(context)
        val boxAdapter = MLPWidgetAdapter(widgetList, this.context!!, isExpanded,this)
        widget_container.layoutManager = linearLayoutManager
        widget_container.adapter = boxAdapter
    }

    fun initViewModel() {
        merchantLendingViewModel = ViewModelProviders.of(this, viewModelFactory).get(MerchantLendingViewModel::class.java)
        merchantLendingViewModel.executeUseCase()
    }

    fun observeData() {
        merchantLendingViewModel.getCategoryList().observe(this, Observer { leWidgetData ->
            if (leWidgetData?.leWidget?.widgets?.isEmpty()!!){
                line.hide()
                tv_title_widget.hide()
                iv_collapsewidget.hide()
                ll_recyclercontainer.hide()
                line2.hide()
                parentContainer.hide()
            }
            else {
                leWidgetData.leWidget.widgets.let {
                    val lengthDataLeWidget: Int = it.size

                    for (widgetNo in 0 until lengthDataLeWidget) {

                        widgetList.clear()
                        widgetList.addAll(it as List<WidgetsItem>)
                        widget_container.adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    fun observeSPData(){
        merchantLendingViewModel.getSPUpdateData().observe(this, Observer {
            toggleSaldoPrioritasListener.onSuccessToggleSaldo(it!!)

        })
    }

}




