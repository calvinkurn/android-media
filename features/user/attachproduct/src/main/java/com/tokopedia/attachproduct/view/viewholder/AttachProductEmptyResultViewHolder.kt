package com.tokopedia.attachproduct.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder

class AttachProductEmptyResultViewHolder : EmptyResultViewHolder {
    constructor(itemView: View?) : super(itemView)
    constructor(itemView: View?, callback: Callback?) : super(itemView, callback)
}
