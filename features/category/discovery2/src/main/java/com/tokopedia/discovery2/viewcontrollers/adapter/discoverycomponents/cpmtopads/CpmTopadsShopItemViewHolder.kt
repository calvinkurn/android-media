package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class CpmTopadsShopItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var cpmTopadsItemViewModel: CpmTopadsShopItemViewModel

    private val brandImage: ImageView = itemView.findViewById(R.id.shop_image)
    private val brandName: TextView = itemView.findViewById(R.id.shop_name)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopadsItemViewModel = discoveryBaseViewModel as CpmTopadsShopItemViewModel
        cpmTopadsItemViewModel.getComponent().observe(fragment.viewLifecycleOwner, Observer { item ->
            val data = item.data?.get(0)
            ImageHandler.loadImageCircle2(itemView.context,
                    brandImage,
                    data?.imageUrlMobile)
            data?.let { setName(it) }
            setClick(data?.applinks)
        })

    }


    private fun setClick(applinks: String?) {
        if (!applinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, applinks)
            }
        }
    }

    private fun setName(item: DataItem) {
        val name = item.name + " \n" + item.buttonText
        val button = item.buttonText ?: ""
        val startIndexOfLink = name.indexOf(button)
        val spannableString = SpannableString(name)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = itemView.context.resources.getColor(com.tokopedia.design.R.color.green_250) // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + button.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        brandName.highlightColor = Color.TRANSPARENT
        brandName.setText(spannableString, TextView.BufferType.SPANNABLE)
    }


}