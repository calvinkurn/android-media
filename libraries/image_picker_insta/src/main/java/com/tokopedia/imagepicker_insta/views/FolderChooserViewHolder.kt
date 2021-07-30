package com.tokopedia.imagepicker_insta.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.PhotosViewHolder
import com.tokopedia.imagepicker_insta.R

class FolderChooserViewHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
    val tvFolderName :AppCompatTextView = itemView.findViewById(R.id.tv_folder_name)
    companion object {
        fun getInstance(parent: ViewGroup): FolderChooserViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_folder_chooser_vh, parent, false)
            return FolderChooserViewHolder(v)
        }
    }

    fun setData(data:String){
        tvFolderName.text = data
    }
}