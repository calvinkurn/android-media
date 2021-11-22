package com.tokopedia.cmhomewidget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.cmhomewidget.communicator.CMHomeWidgetCommunicator
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.presentation.adapter.CMHomeWidgetAdapter
import com.tokopedia.cmhomewidget.presentation.adapter.decorator.HorizontalSpaceItemDecorator
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

class CMHomeWidgetCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), CMHomeWidgetCommunicator,
    CMHomeWidgetProductCardListener, CMHomeWidgetViewAllCardListener {

    @Inject
    lateinit var adapter: dagger.Lazy<CMHomeWidgetAdapter>

    @Inject
    lateinit var binding: LayoutCmHomeWidgetBinding

    private lateinit var cmHomeWidgetData: CMHomeWidgetData

    init {
        injectComponent()
        initRecyclerView()
    }

    private fun injectComponent() {
        DaggerCMHomeWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .cMHomeWidgetModule(CMHomeWidgetModule(this))
            .build().inject(this)
    }

    private fun initRecyclerView() {
        binding.rvCmHomeWidget.apply {
            layoutManager = LinearLayoutManager(
                (this@CMHomeWidgetCard).context, LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(HorizontalSpaceItemDecorator(10, 0))
            adapter = (this@CMHomeWidgetCard).adapter.get()
        }
    }

    override fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData) {
        this.cmHomeWidgetData = cmHomeWidgetData
        setUpUi()
    }

    private fun setUpUi() {
        showCMHomeWidget()
        setHeading()
        setItemsInRecyclerView()
    }

    private fun setItemsInRecyclerView() {
        val itemsList = ArrayList<CMHomeWidgetVisitable>()
        cmHomeWidgetData.cmHomeWidgetProductCardData?.let {
            itemsList.addAll(it)
        }
        cmHomeWidgetData.cmHomeWidgetViewAllCardData?.let {
            itemsList.add(it)
        }
        if (itemsList.isNotEmpty()) adapter.get().loadData(itemsList)
    }

    private fun setHeading() {
        binding.tvCmHomeWidgetHeading.text = cmHomeWidgetData.widgetTitle
    }

    override fun hideCMHomeWidget() {
        this.visibility = View.GONE
    }

    override fun showCMHomeWidget() {
        this.visibility = View.VISIBLE
    }

    override fun setOnCMHomeWidgetCloseClickListener(cmHomeWidgetCloseClickListener: CMHomeWidgetCloseClickListener) {
        binding.ivCmHomeWidgetClose.setOnClickListener {
            if (this::cmHomeWidgetData.isInitialized) {
                cmHomeWidgetCloseClickListener.onCMHomeWidgetDismissClick(
                    cmHomeWidgetData.parentId,
                    cmHomeWidgetData.campaignId
                )
            }
        }
    }

    override fun removeCMHomeWidgetDismissListener() {
        binding.ivCmHomeWidgetClose.setOnClickListener(null)
    }

    override fun onViewAllCardClick(dataItem: CMHomeWidgetViewAllCardData) {
        startRequiredActivity(dataItem.appLink)
    }

    override fun onProductCardClick(dataItem: CMHomeWidgetProductCardData) {
        startRequiredActivity(dataItem.appLink)
    }

    override fun onBuyDirectBtnClick(dataItem: CMHomeWidgetProductCardData) {
        startRequiredActivity(dataItem.cmHomeWidgetActionButtons?.get(0)?.appLink)
    }

    private fun startRequiredActivity(appLink: String?) {
        val intent = RouteManager.getIntent(context, appLink)
        context.startActivity(intent)
    }
}