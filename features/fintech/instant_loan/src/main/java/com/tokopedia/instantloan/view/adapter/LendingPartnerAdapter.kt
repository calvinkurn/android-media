package com.tokopedia.instantloan.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.data.model.response.GqlLendingPartnerData
import com.tokopedia.instantloan.network.InstantLoanUrl


class LendingPartnerAdapter(private val partnerList: ArrayList<GqlLendingPartnerData>?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(com.tokopedia.instantloan.R.layout.il_lending_partner_item, null)
        return LePartnerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return partnerList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LePartnerViewHolder).bindData(partnerList!![position])
    }

    class LePartnerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageView: ImageView = view.findViewById(com.tokopedia.instantloan.R.id.partner_image_view)
        var context: Context = view.context

        fun bindData(partnerItem: GqlLendingPartnerData) {
            itemView.tag = partnerItem.partnerNameSlug
            ImageHandler.LoadImage(imageView, partnerItem.partnerIconUrl)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val rUrl = String.format(InstantLoanUrl.COMMON_URL.IL_PARTNER_URL, view?.tag as String)
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    rUrl))
        }
    }
}

