package com.tokopedia.feedcomponent.util.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifyprinciples.Typography

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

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        holder.bind(mShareTypes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mShareTypes.size
    }

    class ShareViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val iconView: ImageView = view.findViewById(R.id.icon_view)
        private val labelView: Typography = view.findViewById(R.id.label_view)

        private val context: Context
            get() = view.context

        fun bind(type: ShareBottomSheets.ShareType, clickListener: OnItemClickListener?) {
            iconView.setImageDrawable(
                    when (type) {
                        is ShareBottomSheets.ShareType.ActivityShare -> type.getActivityIcon(context)
                        is ShareBottomSheets.ShareType.ActionShare -> MethodChecker.getDrawable(context, type.imageResource)
                    }
            )

            labelView.text = type.displayName

            itemView.setOnClickListener { clickListener?.onItemClick(type) }
        }
    }
}