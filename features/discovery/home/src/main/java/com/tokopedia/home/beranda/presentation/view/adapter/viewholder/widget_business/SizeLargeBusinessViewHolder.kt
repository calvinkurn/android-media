package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.support.v7.widget.AppCompatImageView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import kotlinx.android.synthetic.main.layout_template_icon_business_widget.view.*
import kotlinx.android.synthetic.main.layout_template_large_business.view.*

class SizeLargeBusinessViewHolder (
        itemView: View?,
        listener: BusinessUnitItemView
) : SizeSmallBusinessViewHolder(itemView, listener) {

    companion object {
        val LAYOUT: Int = R.layout.layout_template_large_business
    }

    override fun getProductName(): TextView {
        return itemView.productName
    }

    override fun getIcon(): AppCompatImageView {
        return itemView.icon
    }

    override fun renderTitle(element: HomeWidget.ContentItemTab?) {
        if (element?.title1st.isNullOrEmpty()) {
            itemView.title1st.visibility = View.GONE
        } else {
            itemView.title1st.visibility = View.VISIBLE
            itemView.title1st.text = MethodChecker.fromHtml(element?.title1st)
        }

        if (element?.title2nd.isNullOrEmpty()) {
            itemView.title2nd.visibility = View.GONE
        } else {
            itemView.title2nd.visibility = View.VISIBLE
            itemView.title2nd.text = MethodChecker.fromHtml(element?.title2nd)
        }

        if (element?.desc1st.isNullOrEmpty()
                && element?.title2nd.isNullOrEmpty()
                && element?.desc2nd.isNullOrEmpty()
        ) {
            if (hasPrice(element) || hasTagLabel(element)) {
                itemView.title1st.maxLines = 2
            } else {
                itemView.title1st.maxLines = 3
            }
        } else {
            itemView.title1st.maxLines = 1
        }
    }

    override fun renderSubtitle(element: HomeWidget.ContentItemTab?) {
        if (element?.desc1st.isNullOrEmpty()) {
            itemView.desc1st.visibility = View.GONE
        } else {
            itemView.desc1st.visibility = View.VISIBLE
            itemView.desc1st.text = MethodChecker.fromHtml(element?.desc1st)
        }

        if (element?.desc2nd.isNullOrEmpty()) {
            itemView.desc2nd.visibility = View.GONE
        } else {
            itemView.desc2nd.visibility = View.VISIBLE
            itemView.desc2nd.text = MethodChecker.fromHtml(element?.desc2nd)
        }

        if (element?.title1st.isNullOrEmpty()
                && element?.title2nd.isNullOrEmpty()
                && element?.desc2nd.isNullOrEmpty()
        ) {
            if (hasPrice(element) || hasTagLabel(element)) {
                itemView.desc1st.maxLines = 2
                itemView.desc1st.gravity = Gravity.START
            } else {
                itemView.desc1st.maxLines = 3
                itemView.desc1st.gravity = Gravity.START
            }
        } else {
            itemView.desc1st.maxLines = 1
            itemView.desc1st.gravity = Gravity.END
        }
    }

}
