package com.tokopedia.profile.view.util

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.profile.R
import kotlinx.android.synthetic.main.item_share.view.*

/**
 * @author by yfsx on 17/05/19.
 */
class ShareAdapter(@NonNull var mActivities : List<ResolveInfo>, @NonNull var mPackageManager: PackageManager)
    : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(packageName: String)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_share,
                parent, false)
        return ShareViewHolder(itemView)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun isPositionOther(position: Int): Boolean {
        return position == mActivities.size + 1
    }

    private fun isPositionCopy(position: Int): Boolean {
        return position == mActivities.size
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {

        val title: CharSequence
        val type: String
        val resources: Drawable?

        if (isPositionCopy(position)) {
            resources = AppCompatResources.getDrawable(holder.itemView.iconView.context, R.drawable.ic_copy_clipboard)
            title = holder.itemView.labelView.context.getString(R.string.copy)
            type = ShareBottomSheets.KEY_COPY
        } else if (isPositionOther(position)) {
            resources = AppCompatResources.getDrawable(holder.itemView.iconView.context, R.drawable.ic_btn_more)
            title = holder.itemView.labelView.context.getString(R.string.other)
            type = ShareBottomSheets.KEY_OTHER
        } else {
            val activity = mActivities[position]
            resources = activity.loadIcon(mPackageManager)
            title = activity.loadLabel(mPackageManager)
            type = activity.activityInfo.packageName
        }

        holder.bindItem(resources, title, type, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mActivities.size + 2 // for salin link and lainnya
    }

    class ShareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(resource: Drawable?, title: CharSequence?, type: String?, clickListener: OnItemClickListener?) {
            resource.let {
                itemView.iconView.setImageDrawable(it)
            }
            title.let {
                itemView.labelView.text = it
            }
            type?.let {typeString ->
                clickListener?.let {listener ->
                    itemView.setOnClickListener{
                        listener.onItemClick(typeString)
                    }
                }
            }
        }

    }
}