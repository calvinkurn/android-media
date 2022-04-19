package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel

/**
 * Created By @ilhamsuaib on 06/03/21
 */

interface QuestionnaireAdapterFactory {

    fun type(model: QuestionnaireUiModel.QuestionnaireRatingUiModel): Int

    fun type(model: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel): Int
}