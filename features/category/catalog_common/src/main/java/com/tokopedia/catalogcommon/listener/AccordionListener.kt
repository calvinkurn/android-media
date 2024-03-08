package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel

interface AccordionListener {
    fun onImpressionAccordionInformation(widgetName: String)

    fun onClickItemAccordionInformation(position: Int, item: AccordionInformationUiModel.ItemAccordionInformationUiModel)

}
