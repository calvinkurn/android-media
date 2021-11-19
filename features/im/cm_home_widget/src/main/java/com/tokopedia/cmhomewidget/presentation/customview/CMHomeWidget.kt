package com.tokopedia.cmhomewidget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetData
import com.tokopedia.cmhomewidget.interactor.CMHomeWidgetInteractor
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener

import com.tokopedia.unifycomponents.BaseCustomView

class CMHomeWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), CMHomeWidgetInteractor {

    private lateinit var cmHomeWidgetData: CMHomeWidgetData
    private lateinit var binding: LayoutCmHomeWidgetBinding

    init {
        initLayout()
    }

    private fun initLayout() {
        binding = LayoutCmHomeWidgetBinding.inflate(
            LayoutInflater.from(context),
            this, true
        )
    }

    override fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData) {
        this.cmHomeWidgetData = cmHomeWidgetData
        setUpUi()
    }

    private fun setUpUi() {
        showCMHomeWidget()
        setHeading()
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
}