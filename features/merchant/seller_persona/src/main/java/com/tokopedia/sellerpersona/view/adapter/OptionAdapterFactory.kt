package com.tokopedia.sellerpersona.view.adapter

import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

interface OptionAdapterFactory {
    fun type(model: BaseOptionUiModel.QuestionOptionSingleUiModel): Int
    fun type(model: BaseOptionUiModel.QuestionOptionMultipleUiModel): Int
}