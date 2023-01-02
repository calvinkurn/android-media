package com.tokopedia.mvc.presentation.intro.adapter.factory

import com.tokopedia.mvc.presentation.intro.uimodel.ChoiceOfVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.IntroVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherTypeUiModel

interface MvcIntroAdapterFactory {
    fun type(introVoucherUiModel: IntroVoucherUiModel): Int
    fun type(voucherTypeUiModel: VoucherTypeUiModel): Int
    fun type(choiceOfVoucherUiModel: ChoiceOfVoucherUiModel): Int
    fun type(voucherIntroCarouselUiModel: VoucherIntroCarouselUiModel): Int
}
