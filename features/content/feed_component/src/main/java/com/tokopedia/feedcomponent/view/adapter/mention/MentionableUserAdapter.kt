package com.tokopedia.feedcomponent.view.adapter.mention

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by jegul on 2019-08-05.
 */

class MentionableUserAdapter(
        val listener: MentionAdapterListener
) : BaseAdapter(), Filterable {

    interface MentionAdapterListener {
        fun shouldGetMentionableUser(keyword: String)
    }

    private var filteredList: List<MentionableUserModel> = emptyList()

    override fun isEmpty(): Boolean {
        return filteredList.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View? = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_mention, parent, false)

            holder = ViewHolder(
                    view.findViewById(R.id.tv_user_name),
                    view.findViewById(R.id.tv_full_name),
                    view.findViewById(R.id.iv_avatar)
            )

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.tvFullName.text = filteredList[position].fullName

        val userName = filteredList[position].userName
        if (userName.isNullOrEmpty()) {
            holder.tvUserName.gone()
        } else {
            holder.tvUserName.text = userName
            holder.tvUserName.visible()
        }

        ImageHandler.loadImageCircle2(holder.ivAvatar.context, holder.ivAvatar, filteredList[position].avatarUrl)

        return view!!
    }

    override fun getItem(p0: Int): Any {
        return filteredList[p0]
    }

    override fun isEnabled(p0: Int): Boolean {
        return true
    }

    override fun getItemId(p0: Int): Long {
        return filteredList[p0].id.toLong()
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (!constraint.isNullOrEmpty()) listener.shouldGetMentionableUser(constraint.toString())

                return FilterResults().apply {
                    count = 0
                    values = emptyList<MentionableUserModel>()
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            }
        }
    }

    fun setMentionableUser(userList: List<MentionableUserModel>) {
        filteredList = userList
        notifyDataSetChanged()
    }

    internal data class ViewHolder(
            val tvUserName: TextView,
            val tvFullName: TextView,
            val ivAvatar: ImageView
    )
}
