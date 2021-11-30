package com.tokopedia.data_explorer.domain.shared.models.parameters

internal fun pragma(initializer: Pragma.() -> Unit): String {
    return Pragma().apply(initializer).build()
}

/*internal fun select(initializer: Select.() -> Unit): String {
    return Select().apply(initializer).build()
}*/
