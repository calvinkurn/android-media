package com.tokopedia.sellerappwidget.view.remoteview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 16/11/20
 */

class OrderWidgetRemoteViewService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return OrderAppWidgetFactory(applicationContext, intent)
    }

    class OrderAppWidgetFactory(
            private val context: Context,
            intent: Intent
    ) : RemoteViewsFactory {

        private var items: ArrayList<OrderItemUiModel> = arrayListOf()

        init {
            val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
            items = bundle?.getParcelableArrayList(Const.Extra.ORDER_ITEMS) ?: arrayListOf()
        }

        override fun onCreate() {

        }

        override fun getViewAt(position: Int): RemoteViews {
            val item = items[position]
            return RemoteViews(context.packageName, R.layout.saw_app_widget_order_item).apply {
                val deadLineText = context.getString(R.string.saw_response_limit, item.deadLineText)
                setTextViewText(R.id.tvSawOrderItemProductName, item.product?.productName.orEmpty())
                setTextViewText(R.id.tvSawOrderItemDeadlineText, deadLineText)

                //handle on item click
                val fillIntent = Intent().apply {
                    putExtra(Const.Extra.BUNDLE, Bundle().apply {
                        putParcelable(Const.Extra.ORDER_ITEM, item)
                    })
                }
                setOnClickFillInIntent(R.id.containerSawOrderItem, fillIntent)

                val productCount = item.productCount.orZero()
                if (productCount > 1) {
                    setInt(R.id.tvSawOrderItemOtherOrder, Const.Method.SET_VISIBILITY, View.VISIBLE)
                    val otherProducts = "+${productCount.minus(1)} ${context.getString(R.string.saw_product)}"
                    setTextViewText(R.id.tvSawOrderItemOtherOrder, otherProducts)
                } else {
                    setInt(R.id.tvSawOrderItemOtherOrder, Const.Method.SET_VISIBILITY, View.GONE)
                }

                val gridLineVisibility = if (position == items.size.minus(1)) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
                setInt(R.id.horLineSawOrderItem, Const.Method.SET_VISIBILITY, gridLineVisibility)

                val px46 = context.pxToDp(46).toInt()
                val radius = context.pxToDp(6).toInt()
                val builder = Glide.with(context)
                        .asBitmap()
                        .load(item.product?.picture)
                        .transform(CenterCrop(), RoundedCorners(radius))
                val futureTarget = builder.submit(px46, px46)

                try {
                    setImageViewBitmap(R.id.imgSawOrderItemProduct, futureTarget.get())
                } catch (e: Exception) {
                    Timber.i(e)
                }
            }
        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.saw_app_widget_order_item_shimmer).apply {
                setInt(R.id.shimmerSawOrder1, Const.Method.SET_VISIBILITY, View.INVISIBLE)
                setInt(R.id.shimmerSawOrder2, Const.Method.SET_VISIBILITY, View.INVISIBLE)
                setInt(R.id.shimmerSawOrder3, Const.Method.SET_VISIBILITY, View.INVISIBLE)
                setInt(R.id.sawOrderItemHorLine, Const.Method.SET_VISIBILITY, View.INVISIBLE)
            }
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = true

        override fun getCount(): Int = items.size

        override fun getViewTypeCount(): Int = 1

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {

        }
    }
}