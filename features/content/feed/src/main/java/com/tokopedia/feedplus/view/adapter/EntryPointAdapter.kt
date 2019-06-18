package com.tokopedia.feedplus.view.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.kolcommon.data.pojo.Author

/**
 * @author by nisie on 27/03/19.
 */
class EntryPointAdapter(val activity: Activity,
                        val entryPointList: List<Author>,
                        val listener : ActionListener) : RecyclerView
.Adapter<EntryPointAdapter
.ViewHolder>() {

    interface ActionListener {
        fun onEntryPointClicked(applink : String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val avatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val badge: ImageView = itemView.findViewById(R.id.ivBadge)
        val layout: View = itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_create_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return entryPointList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author = entryPointList[position]
        holder.name.text = MethodChecker.fromHtml(author.name)
        holder.title.text = author.title

        ImageHandler.loadImageCircle2(
                holder.avatar.context,
                holder.avatar,
                author.thumbnail
        )

        if (author.badge.isBlank()) {
            holder.badge.visibility = View.GONE
        } else {
            holder.badge.visibility = View.VISIBLE
            ImageHandler.LoadImage(
                    holder.badge,
                    author.badge
            )
        }

        holder.layout.setOnClickListener {
            listener.onEntryPointClicked(author.link)
        }

    }

}
