package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.notifcenter.R
import com.tokopedia.unifycomponents.CardUnify

class CardProductNotificationCardUnify(
        context: Context,
        attrs: AttributeSet?
) : CardUnify(context, attrs) {

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.partial_single_product_notification, this)?.apply {
            initViewBinding(this)
        }
    }

    private fun initViewBinding(view: View) {
        // TODO: findview here
    }
}