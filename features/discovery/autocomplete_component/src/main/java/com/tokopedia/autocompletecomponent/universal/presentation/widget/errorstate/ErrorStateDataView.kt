package com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory

class ErrorStateDataView: Visitable<UniversalSearchTypeFactory> {
    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}