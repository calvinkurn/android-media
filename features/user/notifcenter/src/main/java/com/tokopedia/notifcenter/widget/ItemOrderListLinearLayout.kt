package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.Card
import com.tokopedia.unifyprinciples.Typography

class ItemOrderListLinearLayout : LinearLayout {

    private var icon: ImageView? = null
    private var title: Typography? = null
    private var counter: Typography? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.partial_notification_card_order_list, this)?.apply {
            initViewBinding(this)
        }
    }

    private fun initViewBinding(view: View) {
        icon = view.findViewById(R.id.iv_order_icon)
        title = view.findViewById(R.id.tp_order_title)
        counter = view.findViewById(R.id.tp_counter)
    }

    fun hasVisibleIcon(): Boolean {
        return icon?.isVisible == true
    }

    fun setTitleMarginStart(leftMargin: Float?) {
        leftMargin ?: return
        val lpTitle = title?.layoutParams as? MarginLayoutParams ?: return
        lpTitle.apply {
            setMargins(leftMargin.toInt(), topMargin, rightMargin, bottomMargin)
        }
        title?.layoutParams = lpTitle
    }

    fun bindItem(
            order: Card,
            onClick: () -> Unit
    ) {
        bindIcon(order)
        bindTitle(order)
        bindCounter(order)
        bindClick(order, onClick)
    }

    private fun bindIcon(order: Card) {
        val iconImage = order.icon
        if (iconImage.isNotEmpty()) {
            icon?.show()
            ImageHandler.LoadImage(icon, order.icon)
        } else {
            icon?.hide()
        }
    }

    private fun bindTitle(order: Card) {
        title?.text = order.text
    }

    private fun bindCounter(order: Card) {
        if (order.hasCounter()) {
            counter?.show()
            counter?.text = order.counter
        } else {
            counter?.hide()
        }
    }

    private fun bindClick(
            order: Card,
            onClick: () -> Unit
    ) {
        setOnClickListener {
            RouteManager.route(context, order.link.androidLink)
            onClick()
        }
    }
}