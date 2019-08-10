package com.tokopedia.shop.product.view.adapter

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipQuests
import com.tokopedia.shop.common.widget.MembershipStampView

class MembershipStampAdapter(private val context: Context, private val listener: MembershipStampAdapterListener) : RecyclerView.Adapter<MembershipStampAdapter.MembershipViewHolder>() {


    interface MembershipStampAdapterListener {
        fun onButtonClaimClicked()
    }

    private lateinit var membershipQuests: List<MembershipQuests>

    fun setMembershipData(data: List<MembershipQuests>) {
        membershipQuests = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_custom_membership, parent, false)

        val layoutParams = itemView.layoutParams

        if (membershipQuests.size > 1) {
            // Measure width 85% to screen
            if (parent.width != 0) {
                layoutParams.width = (parent.width * 0.85).toInt()
            } else {
                val mWinMgr = itemView.context
                        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = mWinMgr.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = size.x
                layoutParams.width = (width * 0.85).toInt()
            }
        }

        itemView.layoutParams = layoutParams
        return MembershipViewHolder(itemView)
    }

    override fun getItemCount(): Int = membershipQuests.size

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        holder.bind(membershipQuests[position], membershipQuests.size)
    }

    inner class MembershipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var membershipView: MembershipStampView = view.findViewById(R.id.membership_stamp_view)
        fun bind(data: MembershipQuests, dataSize: Int) {
            membershipView.setMembershipModel(data, dataSize)
            membershipView.btnClaim.setOnClickListener {
                listener.onButtonClaimClicked()
            }
        }
    }
}
