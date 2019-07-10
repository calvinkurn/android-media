package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.kotlin.extensions.view.inflateLayout
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
                shareTitle.text = item.showTitleFormula(context, item.titleRes)
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