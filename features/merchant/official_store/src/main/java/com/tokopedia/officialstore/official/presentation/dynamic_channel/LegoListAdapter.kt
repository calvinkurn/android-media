package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.officialstore.R

class LegoListAdapter(
        private val ctx: Context?
) : RecyclerView.Adapter<LegoListAdapter.LegoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LegoItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_lego_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
        ImageHandler.loadImageFitCenter(
                ctx,
                holder.imageView,
                "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/10/16/20723472/20723472_fe5ceae7-b9b4-4f0f-9abc-7846e3a21db2.png"
        )
    }

    override fun getItemCount() = 6

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView by lazy {
            view.findViewById<SquareImageView>(R.id.dc_lego_image_item)
        }
    }
}
