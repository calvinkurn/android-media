package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import androidx.appcompat.widget.AppCompatImageView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget

class SizeLargeBusinessViewHolder (
        itemView: View,
        listener: BusinessUnitItemViewListener
) : SizeSmallBusinessViewHolder(itemView, listener) {


    private var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
    private var title1st: TextView? = itemView.findViewById(R.id.title1st)
    private var title2nd: TextView? = itemView.findViewById(R.id.title2nd)
    private var desc1st: TextView? = itemView.findViewById(R.id.desc1st)
    private var desc2nd: TextView? = itemView.findViewById(R.id.desc2nd)
    companion object {
        val LAYOUT: Int = R.layout.layout_template_large_business
    }

    override fun getProductName(): TextView {
        return itemView.findViewById(R.id.productName)
    }

    override fun getIcon(): AppCompatImageView {
        return icon
    }

    override fun renderTitle(element: HomeWidget.ContentItemTab?) {
        if (element?.title1st.isNullOrEmpty()) {
            title1st?.visibility = View.GONE
        } else {
            title1st?.visibility = View.VISIBLE
            title1st?.text = MethodChecker.fromHtml(element?.title1st)
        }

        if (element?.title2nd.isNullOrEmpty()) {
            title2nd?.visibility = View.GONE
        } else {
            title2nd?.visibility = View.VISIBLE
            title2nd?.text = MethodChecker.fromHtml(element?.title2nd)
        }

        if (element?.desc1st.isNullOrEmpty()
                && element?.title2nd.isNullOrEmpty()
                && element?.desc2nd.isNullOrEmpty()
        ) {
            if (hasPrice(element) || hasTagLabel(element)) {
                title1st?.maxLines = 2
            } else {
                title1st?.maxLines = 3
            }
        } else {
            title1st?.maxLines = 1
        }
    }

    override fun renderSubtitle(element: HomeWidget.ContentItemTab?) {
        if (element?.desc1st.isNullOrEmpty()) {
            desc1st?.visibility = View.GONE
        } else {
            desc1st?.visibility = View.VISIBLE
            desc1st?.text = MethodChecker.fromHtml(element?.desc1st)
        }

        if (element?.desc2nd.isNullOrEmpty()) {
            desc2nd?.visibility = View.GONE
        } else {
            desc2nd?.visibility = View.VISIBLE
            desc2nd?.text = MethodChecker.fromHtml(element?.desc2nd)
        }

        if (element?.title1st.isNullOrEmpty()
                && element?.title2nd.isNullOrEmpty()
                && element?.desc2nd.isNullOrEmpty()
        ) {
            if (hasPrice(element) || hasTagLabel(element)) {
                desc1st?.maxLines = 2
                desc1st?.gravity = Gravity.START
            } else {
                desc1st?.maxLines = 3
                desc1st?.gravity = Gravity.START
            }
        } else {
            desc1st?.maxLines = 1
            desc1st?.gravity = Gravity.END
        }
    }

}
