package com.tokopedia.product.detail.view.util

import android.content.Context

/**
 * Created by Yehezkiel on 26/01/21
 */
class ProductSeparatorItemDecoration(context: Context) : com.tokopedia.abstraction.base.view.widget.DividerItemDecoration(context) {
    override fun getDimenPaddingLeft(): Int = com.tokopedia.unifyprinciples.R.dimen.unify_space_0
}