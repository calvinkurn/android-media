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
import com.tokopedia.cmhomewidget.analytics.CMHomeWidgetAnalytics
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.*
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.presentation.adapter.CMHomeWidgetAdapter
import com.tokopedia.cmhomewidget.presentation.adapter.decorator.CMHomeWidgetItemDecorator
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), CMHomeWidgetProductCardListener,
    CMHomeWidgetViewAllCardListener {

    @Inject
    lateinit var binding: LayoutCmHomeWidgetBinding

    @Inject
    lateinit var adapter: dagger.Lazy<CMHomeWidgetAdapter>

    @Inject
    lateinit var itemDecorator: dagger.Lazy<CMHomeWidgetItemDecorator>

    @Inject
    lateinit var cmHomeWidgetAnalytics: dagger.Lazy<CMHomeWidgetAnalytics>

    private var cmHomeWidgetData: CMHomeWidgetData? = null

    private var productCardHeight = 0

    private var isShowShimmer = false

    private var cmHomeWidgetCloseClickListener : CMHomeWidgetCloseClickListener? = null

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
            addItemDecoration(itemDecorator.get())
            adapter = (this@CMHomeWidget).adapter.get()
            setHasFixedSize(true)
        }
    }

    private fun handleShimmer() {
        if (isShowShimmer) {
            adapter.get().loadData(getShimmerDataList())
        }
    }

    private fun getShimmerDataList() =
        listOf(CMHomeWidgetProductCardShimmerData(), CMHomeWidgetViewAllCardShimmerData())

    fun loadCMHomeWidgetData(cmHomeWidgetData: CMHomeWidgetData) {
        if (this.cmHomeWidgetData == null || this.cmHomeWidgetData !== cmHomeWidgetData) {
            this.cmHomeWidgetData = cmHomeWidgetData
            setUpUi()
            sendCMWidgetReceivedEvent()
        }
    }

    private fun setUpUi() {
        setHeading()
        setItemsInRecyclerView()
    }

    private fun setHeading() {
        binding.tvCmHomeWidgetHeading.text = cmHomeWidgetData?.widgetTitle
    }

    private fun setItemsInRecyclerView() {
        val itemsList = ArrayList<CMHomeWidgetVisitable>()
        cmHomeWidgetData?.cmHomeWidgetProductCardData?.let {
            itemsList.addAll(it)
        }
        cmHomeWidgetData?.cmHomeWidgetViewAllCardData?.let {
            itemsList.add(it)
        }
        if (itemsList.isNotEmpty()) {
            binding.root.visibility = View.VISIBLE
            adapter.get().loadData(itemsList)
        } else {
            adapter.get().clearAllElements()
            binding.root.visibility = View.GONE
        }
    }

    fun setOnCMHomeWidgetCloseClickListener(cmHomeWidgetCloseClickListener: CMHomeWidgetCloseClickListener) {
        this.cmHomeWidgetCloseClickListener = cmHomeWidgetCloseClickListener
        binding.ivCmHomeWidgetClose.setOnClickListener {
            cmHomeWidgetData?.let {
                cmHomeWidgetCloseClickListener.onCMHomeWidgetDismissClick(
                    it.parentId,
                    it.campaignId
                )
            }
        }
    }

    fun removeCMHomeWidgetDismissListener() {
        binding.ivCmHomeWidgetClose.setOnClickListener(null)
    }

    private fun sendCMWidgetReceivedEvent() {
        cmHomeWidgetData?.let {
            if (!it.isTest) {
                cmHomeWidgetAnalytics.get().sendCMHomeWidgetReceivedEvent(
                    it.parentId,
                    it.campaignId,
                    it.notificationId,
                    it.messageId
                )
            }
        }
    }

    override fun onProductCardClick(dataItem: CMHomeWidgetProductCardData) {
        sendCMHomeWidgetClickEvent()
        startRequiredActivity(dataItem.appLink)
    }

    override fun onBuyDirectBtnClick(dataItem: CMHomeWidgetProductCardData) {
        cmHomeWidgetData?.let {
            cmHomeWidgetCloseClickListener?.onRemoveCmWidgetLocally()
        }
        sendCMHomeWidgetClickEvent()
        startRequiredActivity(dataItem.cmHomeWidgetActionButtons?.get(0)?.appLink)
    }

    override fun setProductCardHeight(height: Int) {
        productCardHeight = height
    }

    override fun onViewAllCardClick(dataItem: CMHomeWidgetViewAllCardData) {
        sendCMHomeWidgetClickEvent()
        startRequiredActivity(dataItem.appLink)
    }

    override fun getProductCardHeight(): Int {
        return productCardHeight
    }

    private fun startRequiredActivity(appLink: String?) {
        val intent = RouteManager.getIntent(context, appLink)
        context.startActivity(intent)
    }

    private fun sendCMHomeWidgetClickEvent() {
        cmHomeWidgetData?.let {
            if (!it.isTest) {
                cmHomeWidgetAnalytics.get()
                    .sendCMHomeWidgetClickEvent(
                        it.parentId,
                        it.campaignId,
                        it.notificationId,
                        it.messageId
                    )
            }
        }
    }
}