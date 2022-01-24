package com.tokopedia.product.addedit.common

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.activateHighlight
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography

abstract class AddEditProductFragment : BaseDaggerFragment() {

    enum class PageIndicator {
        INDICATOR_MAIN_PAGE,
        INDICATOR_DETAIL_PAGE,
        INDICATOR_DESCRIPTION_PAGE,
        INDICATOR_SHIPMENT_PAGE
    }

    enum class PageState {
        ADD_MODE,
        EDIT_MODE
    }

    companion object {
        const val GUIDELINE_PERCENT = 0.2f
    }

    var toolbar: HeaderUnify? = null
    var doneButton: TextView? = null

    private var pageState: PageState = PageState.ADD_MODE
    private var btnIndicatorMain: Typography? = null
    private var btnIndicatorDetail: Typography? = null
    private var btnIndicatorDescription: Typography? = null
    private var btnIndicatorShipment: Typography? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = activity?.findViewById(R.id.toolbar)

        btnIndicatorMain = activity?.findViewById(R.id.btn_indicator_main)
        btnIndicatorDetail = activity?.findViewById(R.id.btn_indicator_detail)
        btnIndicatorDescription = activity?.findViewById(R.id.btn_indicator_description)
        btnIndicatorShipment = activity?.findViewById(R.id.btn_indicator_shipment)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val guideline: Guideline? = activity?.findViewById(R.id.guideline)
        val dividerNavigation: DividerUnify? = activity?.findViewById(R.id.divider_navigation)
        val isLandscape = resources.configuration.orientation == ORIENTATION_LANDSCAPE
        val guidelinePercent = if (isLandscape) GUIDELINE_PERCENT else Int.ZERO.toFloat()

        guideline?.setGuidelinePercent(guidelinePercent)
        dividerNavigation?.isVisible = isLandscape
    }

    fun initializeToolbar() {
        toolbar?.apply {
            headerTitle = getString(R.string.label_title_add_product)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            actionTextView?.text = getString(R.string.action_save)
            doneButton?.contentDescription = getString(R.string.content_desc_tv_done)
            doneButton = actionTextView
        }
    }

    fun setPageState(newPageState: PageState) {
        pageState = newPageState
        enableSidebar(pageState)
    }

    // Only appear at tablet mode
    fun setNavigationButtonsOnClickListener(listener: (pageIndicator: PageIndicator) -> Unit) {
        btnIndicatorMain?.setOnClickListener {
            listener.invoke(PageIndicator.INDICATOR_MAIN_PAGE)
        }
        btnIndicatorDetail?.setOnClickListener {
            listener.invoke(PageIndicator.INDICATOR_DETAIL_PAGE)
        }
        btnIndicatorDescription?.setOnClickListener {
            listener.invoke(PageIndicator.INDICATOR_DESCRIPTION_PAGE)
        }
        btnIndicatorShipment?.setOnClickListener {
            listener.invoke(PageIndicator.INDICATOR_SHIPMENT_PAGE)
        }
    }

    fun highlightNavigationButton(pageIndicator: PageIndicator) {
        btnIndicatorMain?.activateHighlight(false)
        btnIndicatorDetail?.activateHighlight(false)
        btnIndicatorDescription?.activateHighlight(false)
        btnIndicatorShipment?.activateHighlight(false)

        when (pageIndicator) {
            PageIndicator.INDICATOR_MAIN_PAGE -> {
                btnIndicatorMain?.activateHighlight(true)
            }
            PageIndicator.INDICATOR_DETAIL_PAGE -> {
                btnIndicatorDetail?.activateHighlight(true)
            }
            PageIndicator.INDICATOR_DESCRIPTION_PAGE -> {
                btnIndicatorDescription?.activateHighlight(true)
            }
            PageIndicator.INDICATOR_SHIPMENT_PAGE -> {
                btnIndicatorShipment?.activateHighlight(true)
            }
        }
    }

    private fun enableSidebar(pageState: PageState) {
        enableSidebar(pageState == PageState.EDIT_MODE)
    }

    private fun enableSidebar(enabled: Boolean) {
        btnIndicatorMain?.isEnabled = enabled
        btnIndicatorDetail?.isEnabled = enabled
        btnIndicatorDescription?.isEnabled = enabled
        btnIndicatorShipment?.isEnabled = enabled
    }
}