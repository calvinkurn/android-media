package com.tokopedia.buyerorder.list.view.adapter.viewholder

import androidx.lifecycle.Observer
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import android.text.Html
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.Color
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.common.OrderListContants
import com.tokopedia.buyerorder.list.data.MetaData
import com.tokopedia.buyerorder.list.data.Order
import com.tokopedia.buyerorder.list.data.OrderCategory
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListUiModel
import com.tokopedia.buyerorder.list.view.viewstate.*
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.DoubleTextView

class OrderListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics,
                          var menuListener: OnMenuItemListener?) : AbstractViewHolder<OrderListUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list

        private const val KEY_FROM_PAYMENT = "from_payment=false"
        private const val KEY_META_DATA = "a/n"
    }

    var status = itemView?.findViewById<TextView>(R.id.list_element_status)
    var date = itemView?.findViewById<TextView>(R.id.date)
    var invoice = itemView?.findViewById<TextView>(R.id.invoice)
    var orderListBtnOverflow = itemView?.findViewById<LinearLayout>(R.id.order_list_but_overflow)
    var conditionalInfoLayout = itemView?.findViewById<LinearLayout>(R.id.conditional_info_layout)
    var conditionalInfoText = itemView?.findViewById<TextView>(R.id.conditional_info)
    var conditionalInfoLayoutBottom = itemView?.findViewById<LinearLayout>(R.id.conditional_info_layout_bottom)
    var conditionalInfoTextBottom = itemView?.findViewById<TextView>(R.id.conditional_info_bottom)
    var imgShopAvatar = itemView?.findViewById<ImageView>(R.id.shop_avatar)
    var categoryName = itemView?.findViewById<TextView>(R.id.category_name)
    var title = itemView?.findViewById<TextView>(R.id.title)
    var itemCount = itemView?.findViewById<TextView>(R.id.itemCount)
    var paymentAvatar = itemView?.findViewById<ImageView>(R.id.status_shop_avatar)
    var totalLabel = itemView?.findViewById<TextView>(R.id.total_price_label)
    var total = itemView?.findViewById<TextView>(R.id.total)
    var parentMetadataLayout = itemView?.findViewById<LinearLayout>(R.id.metadata)

    var cornerRadiusValue: Float = 9f
    var topTextSize: Float = 11f
    var textSize: Float = 10f
    var padding16: Int = 16
    var orderCategory: String = ""
    var appLink: String = ""

    override fun bind(element: OrderListUiModel) {
        setObservers(element)
        orderCategory = element.order.category()
        appLink = element.order.appLink
        if (!(orderCategory == OrderCategory.DIGITAL || orderCategory == OrderCategory.FLIGHTS)) {
            if (appLink.contains("?")) {
                appLink += "&"
            } else {
                appLink += "?"
            }
            appLink += KEY_FROM_PAYMENT
        }
        parentMetadataLayout?.removeAllViews()
        element.setViewData()
        element.setDotMenuVisibility()
        if (element.order.items().size > 0) {
            ImageHandler.loadImageThumbs(itemView.context, imgShopAvatar, element.order.items()[0].imageUrl())
        }
        setClickListeners(element.order)

    }

    private fun setObservers(element: OrderListUiModel) {
        element.orderListLiveData.removeObservers(itemView.context as FragmentActivity)
        element.orderListLiveData.observe(itemView.context as FragmentActivity, Observer {
            when (it) {
                is DotMenuVisibility -> {
                    orderListBtnOverflow?.visibility = it.visibility
                }
                is SetCategoryAndTitle -> {
                    setCategoryAndTitle(it.title, it.categoryName)
                }
                is SetItemCount -> {
                    setItemCount(it.itemCount)
                }
                is SetTotal -> {
                    setTotal(it.textColor, it.totalLabel, it.totalValue)
                }
                is SetDate -> {
                    date?.text = it.date
                }
                is SetInvoice -> {
                    setConditionalInvoice(it.invoice)
                }
                is SetConditionalInfo -> {
                    setConditionalInfo(it.successConditionalText, it.successCondInfoVisibility, it.color)
                }
                is SetConditionalInfoBottom -> {
                    setConditionalInfoBottom(it.successConditionalText, it.successCondInfoVisibility, it.color)
                }
                is SetFailStatusBgColor -> {
                    if(it.statusColor.isNotEmpty())
                        status?.setBackgroundColor(android.graphics.Color.parseColor(it.statusColor))
                }
                is SetStatus -> {
                    status?.text = it.statusText
                }
                is SetMetaDataToCustomView -> {
                    setMetadata(it.metaData)
                }
                is SetPaymentAvatar -> {
                    setPaymentAvatar(it.imgUrl)
                }
            }
        })
    }

    private fun setCategoryAndTitle(titleText: String, category: String) {
        if (TextUtils.isEmpty(category))
            categoryName?.hide()
        else
            categoryName?.text = Html.fromHtml(Html.fromHtml(category).toString())
        title?.text = titleText
    }

    private fun setItemCount(count: Int) {
        if ((orderCategory.equals(OrderListContants.BELANJA, ignoreCase = true) || orderCategory.equals(OrderListContants.MARKETPLACE, ignoreCase = true)) && count > 0) {
            itemCount?.show()
            title?.hide()
            itemCount?.text = String.format(itemView.context.resources.getString(R.string.item_count), count)
        } else {
            itemCount?.hide()
        }
    }

    private fun setTotal(textColor: String, totalLabelText: String, totalValue: String) {
        totalLabel?.text = totalLabelText
        total?.text = totalValue
        if (textColor.isNotEmpty()) {
            total?.setTextColor(android.graphics.Color.parseColor(textColor))
        }
    }

    private fun setPaymentAvatar(imgUrl: String) {
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageHandler.loadImageThumbs(itemView.context, paymentAvatar, imgUrl)
            paymentAvatar?.show()
        } else {
            paymentAvatar?.invisible()
        }
    }

    private fun setConditionalInvoice(invoiceText: String) {
        if (invoiceText.isNotEmpty()) {
            invoice?.text = invoiceText
        } else {
            invoice?.hide()
        }
    }

    private fun setConditionalInfo(successConditionalText: String?, successCondInfoVisibility: Int, color: Color?) {
        if (successConditionalText != null) {
            conditionalInfoLayout?.visibility = successCondInfoVisibility
            val shape = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = cornerRadiusValue
                if(color?.background()?.isNotEmpty() == true)
                    setColor(android.graphics.Color.parseColor(color.background()))
                if(color?.border()?.isNotEmpty() == true)
                    setStroke(1, android.graphics.Color.parseColor(color?.border()))
            }
            conditionalInfoText?.background = shape
            conditionalInfoText?.setPadding(padding16, padding16, padding16, padding16)
            conditionalInfoText?.text = successConditionalText
        } else {
            conditionalInfoLayout?.hide()
        }
    }

    private fun setConditionalInfoBottom(successConditionalText: String?, successCondInfoVisibility: Int, color: Color?) {
        if (successConditionalText != null) {
            conditionalInfoLayoutBottom?.visibility = successCondInfoVisibility
            val shape = GradientDrawable().apply {
                this.shape = GradientDrawable.RECTANGLE
                cornerRadius = cornerRadiusValue
                color?.let {
                    if (!it.background().isNullOrEmpty()) {
                        setColor(android.graphics.Color.parseColor(it.background()))
                    }
                    if (!it.border().isNullOrEmpty()) {
                        setStroke(1, android.graphics.Color.parseColor(it.border()))
                    }
                }
            }
            conditionalInfoTextBottom?.background = shape
            conditionalInfoTextBottom?.setPadding(padding16, padding16, padding16, padding16)
            conditionalInfoTextBottom?.text = successConditionalText
        } else {
            conditionalInfoLayoutBottom?.hide()
        }
    }

    private fun setMetadata(metaData: MetaData) {
        val childLayout = DoubleTextView(itemView.context, LinearLayout.VERTICAL)
        childLayout.setTopText(metaData.label())
        childLayout.setTopTextSize(topTextSize)
        val value: String? = metaData.value()
        val tv = TextView(itemView.context)
        if (value?.contains(KEY_META_DATA) == true) {
            val values = value.split(KEY_META_DATA.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            tv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            tv.textSize = textSize
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.text = KEY_META_DATA.plus(values[1])
            childLayout.setBottomText(values[0])
            parentMetadataLayout?.addView(childLayout)
            parentMetadataLayout?.addView(tv)
        } else {
            childLayout.setBottomText(value)
            parentMetadataLayout?.addView(childLayout)
        }
    }

    private fun setClickListeners(order: Order) {
        orderListBtnOverflow?.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.menu.add(Menu.NONE, R.id.action_bantuan,
                    Menu.NONE, itemView.context.getResources().getString(R.string.tokopedia_care))
            popup.menu.add(Menu.NONE, R.id.action_order_detail,
                    Menu.NONE, itemView.context.getResources().getString(R.string.lihat_order_detail))
            popup.setOnMenuItemClickListener(OnMenuPopupClicked(it.context, order))
            popup.show()
        }
        itemView.setOnClickListener {
            if (!TextUtils.isEmpty(appLink)) {
                orderListAnalytics.sendProductClickEvent(status?.text.toString())
                orderListAnalytics.sendPageClickEvent("order-detail")
                orderListAnalytics.sendProductViewEvent(order, categoryName?.text.toString(), this.position, total?.text.toString())

                var separator = "?"
                if(appLink.contains(separator)){
                    separator = "&"
                }
                RouteManager.route(itemView.context, "${appLink}${separator}upstream=${order.upstream}")
            }
        }
    }

    private inner class OnMenuPopupClicked(private val context: Context, private val order: Order) : PopupMenu.OnMenuItemClickListener {

        private val list = order.dotMenu()
        private val URL_POSITION = 0

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_bantuan -> {
                    if (list.isNotEmpty() && list.get(URL_POSITION).uri().isNotEmpty()) {
                        menuListener?.startUri(list.get(URL_POSITION).uri())
                    }
                    true
                }
                R.id.action_order_detail -> {
                    if (order.appLink.isNotEmpty()) {
                        var separator = "?"
                        if(order.appLink.contains(separator)){
                            separator = "&"
                        }
                        RouteManager.route(context, "${order.appLink}${separator}upstream=${order.upstream}")
                    }
                    true
                }
                else -> false
            }
        }
    }

    interface OnMenuItemListener {
        fun startUri(uri: String)
    }
}