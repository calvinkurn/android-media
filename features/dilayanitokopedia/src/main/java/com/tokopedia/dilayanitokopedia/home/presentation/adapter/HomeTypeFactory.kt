package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeLoadingStateUiModel


interface HomeTypeFactory {

    fun type(uiModel: HomeLoadingStateUiModel): Int

}
