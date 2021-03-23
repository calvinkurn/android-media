package com.tokopedia.product.addedit.optionpicker.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.optionpicker.adapter.OptionTypeFactory

/**
 * Created by faisalramd on 2020-03-12.
 */

data class OptionModel(
        var text: String = "",
        var isSelected: Boolean = false
): Visitable<OptionTypeFactory> {
    override fun type(typeFactory: OptionTypeFactory): Int = typeFactory.type(this)
}