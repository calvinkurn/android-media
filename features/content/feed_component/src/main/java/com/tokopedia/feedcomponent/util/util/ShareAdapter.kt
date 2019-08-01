package com.tokopedia.feedcomponent.util.util

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.feedcomponent.R

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet_share,
                parent, false)
        return ShareViewHolder(itemView)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getYoutubePosition(): Int {
        return mActivities.size
    }

    private fun getCopyPosition(): Int {
        return mActivities.size
    }

    private fun getOtherPosition(): Int {
        return mActivities.size + 1
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {

        var title: CharSequence
        var type: String
        var resources: Drawable?

        when(position) {
//            getYoutubePosition() -> { //for next development
//                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_copy_clipboard)
//                title = holder.itemView.context.getString(R.string.title_youtube)
//                type = ShareBottomSheets.KEY_YOUTUBE
//            }
            getCopyPosition() -> {
                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_copy_clipboard)
                title = holder.itemView.context.getString(R.string.copy)
                type = ShareBottomSheets.KEY_COPY
            }
            getOtherPosition() -> {
                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_btn_more)
                title = holder.itemView.context.getString(R.string.other)
                type = ShareBottomSheets.KEY_OTHER
            }
            else -> {
                val activity = mActivities[position]
                resources = activity.loadIcon(mPackageManager)
                title = activity.loadLabel(mPackageManager)
                type = activity.activityInfo.packageName
                if (title.equals(ShareBottomSheets.KEY_INSTAGRAM_DIRECT)) {
                    title = ShareBottomSheets.NAME_INSTAGRAM
                }
            }
        }


        holder.bindItem(resources, title, type, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mActivities.size + 2 // for youtube, salin link and lainnya
    }

    class ShareViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var iconView: ImageView = view.findViewById(R.id.icon_view)
        private var labelView: TextView = view.findViewById(R.id.label_view)

        fun bindItem(resource: Drawable?, title: CharSequence?, type: String?, clickListener: OnItemClickListener?) {
            resource.let {
                iconView.setImageDrawable(it)
            }
            title.let {
                labelView.text = it
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