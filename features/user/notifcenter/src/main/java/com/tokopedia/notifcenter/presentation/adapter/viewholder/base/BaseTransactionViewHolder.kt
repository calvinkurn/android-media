package com.tokopedia.notifcenter.presentation.adapter.viewholder.base

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R

abstract class BaseTransactionViewHolder<T: Visitable<*>>(
        view: View
): AbstractViewHolder<T>(view) {

    private val txtHeaderTitle = view.findViewById<TextView>(R.id.txtHeaderTitle)
    protected val btnLoadMore = view.findViewById<TextView>(R.id.btnLoadMore)
    private val btnWaitingPayment = view.findViewById<RelativeLayout>(R.id.btnWaitingPayment)
    protected val lstMenuItem = view.findViewById<RecyclerView>(R.id.lstMenuItem)

    protected val context: Context? by lazy { view.context }

    abstract fun onLoadMoreClicked(element: T)

    override fun bind(element: T) {
        lstMenuItem.layoutManager = GridLayoutManager(context, 4)
    }

    protected fun setHeader(title: String, invoke: () -> Unit) {
        txtHeaderTitle.text = title
        btnLoadMore.setOnClickListener { invoke() }
    }

    protected fun isHasWaitingPayment(hasWaitingPayment: Boolean) {
        if (hasWaitingPayment) {
            btnWaitingPayment?.show()
        } else {
            btnWaitingPayment?.hide()
        }
    }

}