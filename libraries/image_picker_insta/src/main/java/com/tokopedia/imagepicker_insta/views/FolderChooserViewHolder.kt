package com.tokopedia.imagepicker_insta.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.FolderData

class FolderChooserViewHolder(listItemView: View):RecyclerView.ViewHolder(listItemView) {

    private val tvFolderName :AppCompatTextView = listItemView.findViewById(R.id.tv_folder_name)
    private val tvSubtitle :AppCompatTextView = listItemView.findViewById(R.id.tv_subtitle)
    private val image :AssetImageView = listItemView.findViewById(R.id.image)

    companion object {
        fun getInstance(parent: ViewGroup): FolderChooserViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_folder_chooser_vh, parent, false)
            return FolderChooserViewHolder(v)
        }
    }

    fun setData(data:FolderData){
        tvFolderName.text = data.folderTitle
        tvSubtitle.text = data.folderSubtitle
        image.loadUriThumbnail(data.thumbnailUri,0)
    }
}