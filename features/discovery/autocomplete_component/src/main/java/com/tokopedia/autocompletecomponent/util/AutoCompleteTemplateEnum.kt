package com.tokopedia.autocompletecomponent.util

enum class AutoCompleteTemplateEnum(val dataName: String) {
    Master("master"),
    Title("title"),
    Education("education");

    override fun toString(): String {
        return dataName
    }
}
