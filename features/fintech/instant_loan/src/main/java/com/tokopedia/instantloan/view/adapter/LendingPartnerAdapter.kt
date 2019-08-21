package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlLendingPartnerData
import com.tokopedia.instantloan.network.InstantLoanUrl


class LendingPartnerAdapter(lendingPartnerList: ArrayList<GqlLendingPartnerData>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val partnerList = lendingPartnerList
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.il_lending_partner_item, null)
        return LendingPartnerAdapter.LePartnerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return partnerList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LendingPartnerAdapter.LePartnerViewHolder).bindData(partnerList!!.get(position), position)
    }

    class LePartnerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageView: ImageView
        var context: Context

        init {
            context = view.context
            imageView = view.findViewById(R.id.partner_image_view)
        }

        fun bindData(partnerItem: GqlLendingPartnerData, position: Int) {
            ImageHandler.LoadImage(imageView, partnerItem.partnerIconUrl)
            itemView.tag = partnerItem.partnerNameSlug
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val rUrl = String.format(InstantLoanUrl.COMMON_URL.IL_PARTNER_URL, view?.tag as String)
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    rUrl))
        }
    }
}

