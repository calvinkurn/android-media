package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.util.componentTextColor
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener

class SellerSectionViewHolder(
        private val listener: SectionItemListener,
        itemView: View?
): AbstractViewHolder<SellerSection>(itemView) {

    private val container = itemView?.findViewById<LinearLayout>(R.id.container_seller)
    private val txtTitle = itemView?.findViewById<TextView>(R.id.txt_title)
    private val imgIcon = itemView?.findViewById<ImageView>(R.id.img_icon)

    private val context by lazy {
        itemView?.context
    }

    override fun bind(element: SellerSection?) {
        if (element == null) return
        txtTitle?.text = context?.getString(element.title)
        setIconState(element)
        setIconColorState(element)
        setContainerClicked(element.isEnabled)
    }

    private fun setContainerClicked(state: Boolean) {
        container?.isEnabled = state
        container?.setOnClickListener {
            listener.onItemClicked()
        }
    }

    private fun setIconColorState(element: SellerSection) {
        context?.let {
            val colorId = componentTextColor(element.isEnabled)
            txtTitle?.setTextColor(ContextCompat.getColor(it, colorId))
        }
    }

    private fun setIconState(element: SellerSection) {
        if (element.isEnabled) {
            imgIcon?.setImageResource(element.resourceIcon)
        } else {
            val deactivatedIcon = R.drawable.ic_notifsetting_deactivated_seller
            imgIcon?.setImageResource(deactivatedIcon)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_seller
    }

}