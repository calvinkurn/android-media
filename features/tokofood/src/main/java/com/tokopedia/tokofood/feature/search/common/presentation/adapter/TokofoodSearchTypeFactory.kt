package com.tokopedia.tokofood.feature.search.common.presentation.adapter

import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel

interface TokofoodSearchTypeFactory {

    fun type(uiModel: TokofoodSearchErrorStateUiModel): Int

}