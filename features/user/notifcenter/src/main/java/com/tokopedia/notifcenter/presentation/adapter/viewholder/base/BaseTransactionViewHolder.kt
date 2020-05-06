package com.tokopedia.notifcenter.presentation.adapter.viewholder.base

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.TransactionMenuListener

abstract class BaseTransactionViewHolder<T: Visitable<*>>(
        view: View,
        val listener: TransactionMenuListener
) : AbstractViewHolder<T>(view) {

    private val txtHeaderTitle: TextView? = view.findViewById(R.id.txtHeaderTitle)
    private val btnLoadMore: TextView? = view.findViewById(R.id.btnLoadMore)
    private val btnWaitingPayment: RelativeLayout? = view.findViewById(R.id.btnWaitingPayment)
    private val txtCounterWaitingPayment: TextView? = view.findViewById(R.id.txtCounterWaitingPayment)
    protected val lstMenuItem: RecyclerView? = view.findViewById(R.id.lstMenuItem)

    protected val context: Context? by lazy { view.context }

    abstract fun initChildItem(element: T)

    override fun bind(element: T) {
        lstMenuItem?.layoutManager = GridLayoutManager(context, 4)
    }

    protected fun setHeader(title: String, applink: String) {
        txtHeaderTitle?.text = title
        btnLoadMore?.setOnClickListener {
            listener.sendTrackingData(title, btnLoadMore.text.toString())
            RouteManager.route(context, applink)
        }
    }

    protected fun addWaitingPaymentMenu(badgeCount: Int? = 0, onClick: () -> Unit = {}) {
        // action button
        btnWaitingPayment?.setOnClickListener { onClick() }
        btnWaitingPayment?.show()

        // badge counter
        if (badgeCount != null && badgeCount != 0) {
            txtCounterWaitingPayment?.text = badgeCount.toString()
            txtCounterWaitingPayment?.show()
        }
    }

}