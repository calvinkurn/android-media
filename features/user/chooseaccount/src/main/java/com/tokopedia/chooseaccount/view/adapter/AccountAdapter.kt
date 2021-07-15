package com.tokopedia.chooseaccount.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.data.UserDetailDataModel
import com.tokopedia.chooseaccount.view.listener.ChooseAccountListener
import com.tokopedia.unifyprinciples.Typography

class AccountAdapter private constructor(
        private val viewListener: ChooseAccountListener,
        private val list: MutableList<UserDetailDataModel>,
        private var phone: String
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    private var context: Context? = null

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var avatar: ImageView = itemView.findViewById(R.id.avatar)
        internal var name: Typography = itemView.findViewById(R.id.name)
        internal var email: Typography = itemView.findViewById(R.id.email)
        internal var shopName: Typography = itemView.findViewById(R.id.shop_name)
        internal var shopView: View = itemView.findViewById(R.id.shop_view)
        internal var mainView: View = itemView.findViewById(R.id.main_view)

        init {
            mainView.setOnClickListener { v -> viewListener.onSelectedAccount(list[adapterPosition], phone) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.choose_login_phone_account_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userDetail = list[position]
        ImageHandler.loadImageCircle2(context, holder.avatar, userDetail.image)
        holder.name.text = MethodChecker.fromHtml(userDetail.fullname)
        holder.email.text = userDetail.email
        val shopDetail = userDetail.shopDetailDataModel
        if (shopDetail != null && shopDetail.name.isNotEmpty()) {
            holder.shopView.visibility = View.VISIBLE
            holder.shopName.text = shopDetail.name
        } else {
            holder.shopView.visibility = View.GONE
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