package com.tokopedia.notifcenter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.TransactionSectionAdapter.TransactionSectionViewHolder

class TransactionSectionAdapter(
        private val elements: DrawerNotification,
        val listener: TransactionMenuListener
): RecyclerView.Adapter<TransactionSectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionSectionViewHolder {
        return TransactionSectionViewHolder.create(parent, listener)
    }
    override fun onBindViewHolder(holder: TransactionSectionViewHolder, position: Int) {
        holder.bind(elements.title, elements.childs[position])
    }

    override fun getItemCount(): Int = elements.childs.size

    class TransactionSectionViewHolder(
            view: View,
            val listener: TransactionMenuListener
    ): RecyclerView.ViewHolder(view) {

        private val container = view.findViewById<ConstraintLayout>(R.id.container)
        private val imgMenuIcon = view.findViewById<ImageView>(R.id.imgMenuIcon)
        private val txtCounter = view.findViewById<TextView>(R.id.txtCounter)
        private val txtSubItemTitle = view.findViewById<TextView>(R.id.txtSubItemTitle)

        private val context by lazy { itemView.context }

        fun bind(
                parentTitle: String,
                element: DrawerNotification.ChildDrawerNotification
        ) {
            txtSubItemTitle.text = element.newLineTitle
            imgMenuIcon.setImageResource(element.icon)
            setBadge(element.badge)

            container.setOnClickListener {
                RouteManager.route(context, element.applink)
                listener.sendTrackingData(element.title, parentTitle)
            }
        }

        private fun setBadge(badge: Int?) {
            if (badge == null || badge == 0) return
            txtCounter.text = badge.toString()
            txtCounter.show()
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.item_notification_transaction_item

            fun create(
                    viewGroup: ViewGroup,
                    listener: TransactionMenuListener
            ): TransactionSectionViewHolder {
                val layout = LayoutInflater
                        .from(viewGroup.context)
                        .inflate(LAYOUT, viewGroup, false)
                return TransactionSectionViewHolder(layout, listener)
            }
        }

    }

}