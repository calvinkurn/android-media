package com.tokopedia.feedcomponent.util.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
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
class ShareAdapter(private val mShareTypes : List<ShareBottomSheets.ShareType>)
    : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(type: ShareBottomSheets.ShareType)
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

//    private fun getYoutubePosition(): Int {
//        return mResolveActivities.size
//    }
//
//    private fun getCopyPosition(): Int {
//        return mResolveActivities.size
//    }
//
//    private fun getOtherPosition(): Int {
//        return mResolveActivities.size + 1
//    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        holder.bind(mShareTypes[position], onItemClickListener)

//        when(position) {
////            getYoutubePosition() -> { //for next development
////                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_copy_clipboard)
////                title = holder.itemView.context.getString(R.string.title_youtube)
////                type = ShareBottomSheets.KEY_YOUTUBE
////            }
//            getCopyPosition() -> {
//                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_copy_clipboard)
//                title = holder.itemView.context.getString(R.string.copy)
//                type = ShareBottomSheets.KEY_COPY
//            }
//            getOtherPosition() -> {
//                resources = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_btn_more)
//                title = holder.itemView.context.getString(R.string.other)
//                type = ShareBottomSheets.KEY_OTHER
//            }
//            else -> {
//                val resolveActivity = mResolveActivities[position]
//                resources = resolveActivity.loadIcon(mPackageManager)
//                title = resolveActivity.loadLabel(mPackageManager)
//                type = resolveActivity.activityInfo.packageName
//                if (title == ShareBottomSheets.KEY_INSTAGRAM_DIRECT) {
//                    title = ShareBottomSheets.NAME_INSTAGRAM
//                }
//            }
//        }
    }

    override fun getItemCount(): Int {
        return mShareTypes.size
    }

    class ShareViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val iconView: ImageView = view.findViewById(R.id.icon_view)
        private val labelView: TextView = view.findViewById(R.id.label_view)

        private val context: Context
            get() = view.context

        fun bind(type: ShareBottomSheets.ShareType, clickListener: OnItemClickListener?) {
            iconView.setImageDrawable(
                    when (type) {
                        is ShareBottomSheets.ShareType.ActivityShare -> type.getActivityIcon(context)
                        is ShareBottomSheets.ShareType.ActionShare -> AppCompatResources.getDrawable(context, type.imageResource)
                    }
            )

            labelView.text = type.displayName

            itemView.setOnClickListener { clickListener?.onItemClick(type) }
        }

//        fun bindItem(resource: Drawable?, title: CharSequence?, type: String?, clickListener: OnItemClickListener?) {
//            resource.let {
//                iconView.setImageDrawable(it)
//            }
//            title.let {
//                labelView.text = it
//            }
//            type?.let {typeString ->
//                clickListener?.let {listener ->
//                    itemView.setOnClickListener{
//                        listener.onItemClick(typeString)
//                    }
//                }
//            }
//        }

    }
}