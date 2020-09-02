package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener

class SellerSectionViewHolder(
        private val listener: SectionItemListener,
        itemView: View?
): AbstractViewHolder<SellerSection>(itemView) {

    private val container = itemView?.findViewById<LinearLayout>(R.id.container_seller)
    private val txtTitle = itemView?.findViewById<TextView>(R.id.txt_title)
    private val imgIcon = itemView?.findViewById<ImageView>(R.id.img_icon)

    private val context by lazy { itemView?.context }

    override fun bind(element: SellerSection?) {
        if (element == null) return
        txtTitle?.text = context?.getString(element.title)
        imgIcon?.setImageResource(element.resourceIcon)
        setContainerClicked()
    }

    private fun setContainerClicked() {
        container?.setOnClickListener {
            listener.onItemClicked()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_seller
    }

}