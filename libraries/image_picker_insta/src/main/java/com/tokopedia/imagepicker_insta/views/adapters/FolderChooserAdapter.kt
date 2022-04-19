package com.tokopedia.imagepicker_insta.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.views.FolderChooserViewHolder

class FolderChooserAdapter(val dataList:List<FolderData>): RecyclerView.Adapter<FolderChooserViewHolder>() {

    var onClick:Function1<FolderData?,Unit>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderChooserViewHolder {
        return FolderChooserViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: FolderChooserViewHolder, position: Int) {
        holder.setData(dataList[position])
        holder.itemView.setOnClickListener {
            if(position == PhotoImporter.INDEX_OF_RECENT_MEDIA_IN_FOLDER_LIST){
                onClick?.invoke(null)
            }else{
                onClick?.invoke(dataList[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}