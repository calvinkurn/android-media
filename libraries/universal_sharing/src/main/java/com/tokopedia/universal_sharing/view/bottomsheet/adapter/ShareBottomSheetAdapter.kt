package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ShareBottomsheetViewHolder
import com.tokopedia.universal_sharing.view.model.ShareModel

class ShareBottomSheetAdapter (
    private val context: Context?,
    private val bottomsheetListener: (shareModel: ShareModel) -> Unit,
    data: List<ShareModel>
): RecyclerView.Adapter<ShareBottomsheetViewHolder>() {

    private var socialMediaList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareBottomsheetViewHolder {
        return ShareBottomsheetViewHolder(
                View.inflate(context, ShareBottomsheetViewHolder.LAYOUT, null),
                bottomsheetListener
        )
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }

    override fun onBindViewHolder(holder: ShareBottomsheetViewHolder, position: Int) {
        holder.bind(socialMediaList[position])
    }

}