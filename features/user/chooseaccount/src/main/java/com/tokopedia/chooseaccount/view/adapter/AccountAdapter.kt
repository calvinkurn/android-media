package com.tokopedia.chooseaccount.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chooseaccount.data.UserDetailDataModel
import com.tokopedia.chooseaccount.databinding.ChooseLoginPhoneAccountItemBinding
import com.tokopedia.chooseaccount.view.listener.ChooseAccountListener

class AccountAdapter private constructor(
        private val viewListener: ChooseAccountListener,
        private val list: MutableList<UserDetailDataModel>,
        private var phone: String
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(val binding: ChooseLoginPhoneAccountItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mainView.setOnClickListener { viewListener.onSelectedAccount(list[adapterPosition], phone) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChooseLoginPhoneAccountItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userDetail = list[position]
        ImageHandler.loadImageCircle2(holder.binding.avatar.context, holder.binding.avatar, userDetail.image)
        holder.binding.name.text = MethodChecker.fromHtml(userDetail.fullname)
        holder.binding.email.text = userDetail.email
        val shopDetail = userDetail.shopDetailDataModel
        if (shopDetail != null && shopDetail.name.isNotEmpty()) {
            holder.binding.shopView.visibility = View.VISIBLE
            holder.binding.shopName.text = shopDetail.name
        } else {
            holder.binding.shopView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<UserDetailDataModel>, phone: String) {
        this.phone = phone
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    companion object {

        fun createInstance(
                viewListener: ChooseAccountListener,
                listAccount: MutableList<UserDetailDataModel>,
                phone: String
        ): AccountAdapter {
            return AccountAdapter(viewListener, listAccount, phone)
        }
    }
}