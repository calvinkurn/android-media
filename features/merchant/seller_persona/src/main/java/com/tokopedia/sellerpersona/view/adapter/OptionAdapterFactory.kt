package com.tokopedia.sellerpersona.view.adapter

import com.tokopedia.sellerpersona.view.model.QuestionOptionMultipleUiModel
import com.tokopedia.sellerpersona.view.model.QuestionOptionSingleUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

interface OptionAdapterFactory {
    fun type(model: QuestionOptionSingleUiModel): Int
    fun type(model: QuestionOptionMultipleUiModel): Int
}