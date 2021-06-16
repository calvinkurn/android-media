package com.tokopedia.product.addedit.common

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.unifyprinciples.Typography

abstract class AddEditProductFragment : BaseDaggerFragment() {

    enum class PageIndicator {
        INDICATOR_MAIN_PAGE,
        INDICATOR_DETAIL_PAGE,
        INDICATOR_DESCRIPTION_PAGE,
        INDICATOR_SHIPMENT_PAGE
    }

    var toolbar: HeaderUnify? = null
    var doneButton: TextView? = null

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

    fun initializeToolbar() {
        toolbar?.apply {
            headerTitle = getString(R.string.label_title_add_product)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            actionTextView?.text = getString(R.string.action_save)
            doneButton = actionTextView
        }
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
        activateHighlight(btnIndicatorMain, false)
        activateHighlight(btnIndicatorDetail, false)
        activateHighlight(btnIndicatorDescription, false)
        activateHighlight(btnIndicatorShipment, false)

        when (pageIndicator) {
            PageIndicator.INDICATOR_MAIN_PAGE -> {
                activateHighlight(btnIndicatorMain, true)
            }
            PageIndicator.INDICATOR_DETAIL_PAGE -> {
                activateHighlight(btnIndicatorDetail, true)
            }
            PageIndicator.INDICATOR_DESCRIPTION_PAGE -> {
                activateHighlight(btnIndicatorDescription, true)
            }
            PageIndicator.INDICATOR_SHIPMENT_PAGE -> {
                activateHighlight(btnIndicatorShipment, true)
            }
        }
    }

    private fun activateHighlight(typography: Typography?, isActive: Boolean) {
        val backgroundColor = if (isActive) {
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G200)
        } else {
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
        typography?.setBackgroundColor(backgroundColor)
    }
}