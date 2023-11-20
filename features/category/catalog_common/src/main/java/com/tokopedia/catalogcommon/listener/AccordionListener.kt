package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel

interface AccordionListener {
    fun onImpressionAccordionInformation()

    fun onClickItemAccordionInformation(position: Int, item: AccordionInformationUiModel.ItemAccordionInformationUiModel)

}
