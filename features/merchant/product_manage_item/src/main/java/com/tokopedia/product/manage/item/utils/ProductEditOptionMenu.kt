package com.tokopedia.product.manage.item.utils

import androidx.annotation.DrawableRes

class ProductEditOptionMenu {
    @DrawableRes
    var icon: Int = 0
    var title: String? = null
    var id: Int = 0

    constructor(title: String, id: Int) {
        this.title = title
        this.id = id
    }

    constructor(icon: Int, title: String, id: Int) {
        this.icon = icon
        this.title = title
        this.id = id
    }
}
