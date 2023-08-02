package com.tokopedia.chooseaccount.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.databinding.ItemOclAccountBinding
import com.tokopedia.chooseaccount.view.listener.OclChooseAccountListener
import com.tokopedia.media.loader.loadImageCircle

class OclAccountAdapter() : RecyclerView.Adapter<OclAccountAdapter.OclViewHolder>() {

    val list: MutableList<OclAccount> = mutableListOf()
    var mListener: OclChooseAccountListener? = null

    inner class OclViewHolder internal constructor(val binding: ItemOclAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OclAccount) {
            binding.avatar.loadImageCircle(item.profilePicture)
            binding.mainView.setOnClickListener { mListener?.onAccountSelected(item) }
            binding.name.text = item.fullName
            binding.email.text = item.loginTypeWording
            binding.deleteIcon.setOnClickListener { mListener?.onDeleteButtonClicked(item) }
        }
    }

    fun setListener(listener: OclChooseAccountListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OclAccountAdapter.OclViewHolder {
        val binding = ItemOclAccountBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OclViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OclViewHolder, position: Int) {
        val userDetail = list[position]
        holder.bind(userDetail)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<OclAccount>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
