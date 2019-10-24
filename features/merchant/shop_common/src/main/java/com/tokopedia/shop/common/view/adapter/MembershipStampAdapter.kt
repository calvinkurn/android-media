package com.tokopedia.shop.common.view.adapter

import android.content.Context
import android.graphics.Point
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemRegisteredViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemUnregisteredViewModel
import com.tokopedia.shop.common.view.BaseMembershipViewHolder
import com.tokopedia.shop.common.view.viewholder.MembershipItemRegisteredViewHolder
import com.tokopedia.shop.common.view.viewholder.MembershipItemUnregisteredViewHolder

class MembershipStampAdapter(private val context: Context, private val listener: MembershipStampAdapterListener) : RecyclerView.Adapter<BaseMembershipViewHolder<*>>() {

    companion object {
        private const val TYPE_UNREGISTERED = 1
        private const val TYPE_REGISTERED = 2
    }

    interface MembershipStampAdapterListener {
        fun onButtonClaimClicked(questId: Int)
        fun goToVoucherOrRegister(url: String? = null, clickOrigin: String? = null)
    }

    private var membershipData: List<BaseMembershipViewModel> = listOf()

    fun setMembershipData(data: List<BaseMembershipViewModel>?) {
        membershipData = data ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMembershipViewHolder<*> {
        when (viewType) {
            TYPE_REGISTERED -> {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.item_custom_membership, parent, false)
                val layoutParams = itemView.layoutParams

                setWidthPercentage(parent, layoutParams, itemView)
                return MembershipItemRegisteredViewHolder(itemView, membershipData.size, listener, (membershipData.first() as ItemRegisteredViewModel).url)
            }
            TYPE_UNREGISTERED -> {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.item_membership_register, parent, false)
                return MembershipItemUnregisteredViewHolder(itemView, listener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    private fun setWidthPercentage(parent: ViewGroup, layoutParams: ViewGroup.LayoutParams, view: View) {
        if (membershipData.size > 1) {
            // Measure width 85% to screen
            if (parent.width != 0) {
                layoutParams.width = (parent.width * 0.85).toInt()
            } else {
                val mWinMgr = view.context
                        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = mWinMgr.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = size.x
                layoutParams.width = (width * 0.85).toInt()
            }
        }
        view.layoutParams = layoutParams
    }

    override fun getItemViewType(position: Int): Int {
        return when (membershipData.first()) {
            is ItemRegisteredViewModel -> TYPE_REGISTERED
            is ItemUnregisteredViewModel -> TYPE_UNREGISTERED
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = membershipData.size

    override fun onBindViewHolder(holder: BaseMembershipViewHolder<*>, position: Int) {
        when (holder) {
            is MembershipItemUnregisteredViewHolder -> holder.bind((membershipData[position] as ItemUnregisteredViewModel))
            is MembershipItemRegisteredViewHolder -> holder.bind((membershipData[position] as ItemRegisteredViewModel).membershipProgram)
        }
    }

}
