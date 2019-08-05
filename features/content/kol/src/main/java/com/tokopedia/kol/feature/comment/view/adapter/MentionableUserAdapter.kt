package com.tokopedia.kol.feature.comment.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.kol.R

/**
 * Created by jegul on 2019-08-05.
 */

class MentionableUserAdapter(
        val mentionList: List<MentionableUserViewModel>
) : BaseAdapter(), Filterable {

    private var filteredList: List<MentionableUserViewModel> = mentionList

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

        holder.tvUserName.text = filteredList[position].userName
        holder.tvFullName.text = filteredList[position].fullName

        ImageHandler.loadImageCircle2(holder.ivAvatar.context, holder.ivAvatar, filteredList[position].imageUrl)

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
                val constraintString = constraint.toString()
                val filteredList = mentionList.filter {
                    it.fullName.contains(constraintString, true)
                }

                return FilterResults().apply {
                    count = filteredList.size
                    values = filteredList
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredList = (p1?.values as (List<MentionableUserViewModel>?)).orEmpty()
                notifyDataSetChanged()
            }
        }
    }

    internal data class ViewHolder(
            val tvUserName: TextView,
            val tvFullName: TextView,
            val ivAvatar: ImageView
    )
}