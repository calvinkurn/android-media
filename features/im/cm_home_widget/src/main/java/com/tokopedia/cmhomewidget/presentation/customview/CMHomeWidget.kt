package com.tokopedia.cmhomewidget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.communicator.CMHomeWidgetCommunicator
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.domain.data.*
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.presentation.adapter.CMHomeWidgetAdapter
import com.tokopedia.cmhomewidget.presentation.adapter.decorator.HorizontalSpaceItemDecorator
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

class CMHomeWidget @JvmOverloads constructor(
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

    private var isShowShimmer = false

    init {
        initAttrs(context, attrs)
        injectComponent()
        initRecyclerView()
        handleShimmer()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.CMHomeWidget, 0, 0)
                .apply {
                    try {
                        isShowShimmer = getBoolean(R.styleable.CMHomeWidget_showShimmer, false)
                    } finally {
                        recycle()
                    }
                }
        }
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
                (this@CMHomeWidget).context, RecyclerView.HORIZONTAL,
                false
            )
            addItemDecoration(HorizontalSpaceItemDecorator(10, 0))
            adapter = (this@CMHomeWidget).adapter.get()
        }
    }

    private fun handleShimmer() {
        if (isShowShimmer) {
            adapter.get().loadData(getShimmerDataList())
            binding.tvCmHomeWidgetHeadingShimmer.visibility = View.VISIBLE
        }
    }

    private fun getShimmerDataList() =
        listOf(CMHomeWidgetProductCardShimmerData(), CMHomeWidgetViewAllCardShimmerData())

    override fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData) {
        this.cmHomeWidgetData = cmHomeWidgetData
        setUpUi()
    }

    private fun setUpUi() {
        setHeading()
        setItemsInRecyclerView()
    }

    private fun setHeading() {
        binding.tvCmHomeWidgetHeading.text = cmHomeWidgetData.widgetTitle
        binding.tvCmHomeWidgetHeadingShimmer.visibility = View.GONE
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

    override fun hide() {
        this.visibility = View.GONE
    }

    override fun show() {
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