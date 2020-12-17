package com.tokopedia.oneclickcheckout.preference.edit.view.address

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.unifycomponents.LoaderUnify

class AddressLoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LOADING_LAYOUT = R.layout.item_loading_address_list
    }

    private val loader = itemView.findViewById<LoaderUnify>(R.id.progress_bar_address)

    fun bind() {
        loader.visible()
    }
}