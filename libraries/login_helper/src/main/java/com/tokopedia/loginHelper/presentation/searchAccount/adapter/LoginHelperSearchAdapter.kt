package com.tokopedia.loginHelper.presentation.searchAccount.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.loginHelper.databinding.ItemLoginSearchBinding
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener.LoginHelperSearchListener
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.viewholder.LoginHelperSearchViewHolder

class LoginHelperSearchAdapter(
    private val listener: LoginHelperSearchListener
) : RecyclerView.Adapter<LoginHelperSearchViewHolder>() {

    private var data: MutableList<UserDataUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginHelperSearchViewHolder {
        val binding = ItemLoginSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoginHelperSearchViewHolder(binding, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LoginHelperSearchViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun addDataList(newData: List<UserDataUiModel>) {
        data = mutableListOf()
        data.addAll(newData)
        notifyItemRangeInserted(Int.ZERO, newData.size)
    }
}
