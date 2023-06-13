package com.tokopedia.home_account.privacy_account.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.consentWithdrawal.data.ConsentGroupDataModel
import com.tokopedia.home_account.privacy_account.listener.PrivacyAccountListener
import com.tokopedia.home_account.privacy_account.view.adapter.viewholder.PrivacyAccountViewHolder

class PrivacyAccountAdapter(
    private val listener: PrivacyAccountListener
) : RecyclerView.Adapter<PrivacyAccountViewHolder>() {

    private var internalList = arrayListOf<ConsentGroupDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivacyAccountViewHolder {
        return PrivacyAccountViewHolder(
            listener,
            LayoutInflater.from(parent.context).inflate(
                PrivacyAccountViewHolder.LAYOUT,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PrivacyAccountViewHolder, position: Int) {
        holder.bind(internalList[position])
    }

    override fun getItemCount(): Int = internalList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<ConsentGroupDataModel>) {
        internalList.clear()
        internalList.addAll(list)
        notifyDataSetChanged()
    }
}
