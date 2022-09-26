package com.tokopedia.dilayanitokopedia.common.adapter.typefactory

import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel

/**
 * Created by irpan on 12/09/22.
 */
interface DtChooseAddressWidgetTypeFactory {
    fun type(uiModel: DtChooseAddressWidgetUiModel): Int
}