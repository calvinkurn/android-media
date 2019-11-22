package com.tokopedia.createpost.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.type.ShareType
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_af_share.view.*

class ShareBottomSheetAdapter(val onClick: (ShareType, Boolean) -> Unit) : RecyclerView.Adapter<ShareBottomSheetAdapter.ShareBottomSheetViewHolder>() {

    private val shareOptionList: MutableList<ShareType> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareBottomSheetViewHolder {
        return ShareBottomSheetViewHolder(parent.inflateLayout(R.layout.item_af_share))
    }

    override fun getItemCount(): Int {
        return shareOptionList.size
    }

    override fun onBindViewHolder(holder: ShareBottomSheetViewHolder, pos: Int) {
        holder.bind(shareOptionList[pos])
    }

    fun setItems(optionList: List<ShareType>) {
        shareOptionList.run {
            clear()
            addAll(optionList)
        }
        notifyDataSetChanged()
    }

    inner class ShareBottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: ShareType) {
            itemView.apply {
                imageShare.setImageResource(item.imageRes)
                shareTitle.text = context.getString(item.titleRes)

                val subtitle =
                        if (item.subtitleRes != null) item.subtitleFormula?.invoke(context, item.subtitleRes)
                        else null

                if (subtitle != null) {
                    shareSubtitle.visible()
                    shareSubtitle.text = subtitle
                } else shareSubtitle.gone()

                toggleShare.apply {
                    setOnCheckedChangeListener(null)

                    isChecked = item.isActivated
                    isEnabled = !item.isMandatory

                    setOnCheckedChangeListener { _, isChecked -> onClick(item, isChecked) }
                }
            }
        }
    }
}