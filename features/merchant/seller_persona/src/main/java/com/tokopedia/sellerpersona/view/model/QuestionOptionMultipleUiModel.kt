package com.tokopedia.sellerpersona.view.model

import com.tokopedia.sellerpersona.view.adapter.OptionAdapterFactory

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class QuestionOptionMultipleUiModel : BaseOptionUiModel {

    override fun type(typeFactory: OptionAdapterFactory): Int {
        return typeFactory.type(this)
    }
}