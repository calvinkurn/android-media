package com.tokopedia.tokofood.feature.search.common.presentation.adapter

import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel

interface SearchTypeFactory {

    fun type(uiModel: TokofoodSearchErrorStateUiModel): Int

}