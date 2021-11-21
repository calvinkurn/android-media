package com.tokopedia.cmhomewidget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.cmhomewidget.communicator.CMHomeWidgetCommunicator
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProduct
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductListener
import com.tokopedia.cmhomewidget.presentation.adapter.CMHomeWidgetAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import timber.log.Timber
import javax.inject.Inject

class CMHomeWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), CMHomeWidgetCommunicator,
    CMHomeWidgetProductListener, CMHomeWidgetCardListener {

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
        binding.rvCmHomeWidget.layoutManager = LinearLayoutManager(
            this.context, LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvCmHomeWidget.adapter = adapter.get()
    }

    override fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData) {
        this.cmHomeWidgetData = cmHomeWidgetData
        setUpUi()
    }

    private fun setUpUi() {
        showCMHomeWidget()
        setHeading()
        cmHomeWidgetData.CMHomeWidgetProducts?.let {
            adapter.get().loadData(it)
        }
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

    override fun onCardClick(item: CMHomeWidgetCard) {
        startRequiredActivity(item.appLink)
    }

    override fun onProductClick(item: CMHomeWidgetProduct) {
        startRequiredActivity(item.appLink)
    }

    private fun startRequiredActivity(appLink: String?) {
        val intent = RouteManager.getIntent(context, appLink)
        context.startActivity(intent)
    }
}