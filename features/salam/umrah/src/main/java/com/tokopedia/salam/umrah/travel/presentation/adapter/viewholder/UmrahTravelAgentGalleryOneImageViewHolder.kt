package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import kotlinx.android.synthetic.main.item_umrah_gallery_one_image.view.*

class UmrahTravelAgentGalleryOneImageViewHolder(view: View, val listener: SetOnClickListener) : AbstractViewHolder<UmrahGallery>(view){
    override fun bind(element: UmrahGallery) {
        with(itemView){
            if (element.title.isNotEmpty()) tg_umrah_gallery_one_image_title.text = element.title
            else tg_umrah_gallery_one_image_title.gone()

            if(element.subTitle.isNotEmpty()) tg_umrah_gallery_one_image_sub_title.text = element.subTitle
            else tg_umrah_gallery_one_image_sub_title.gone()

            if(element.description.isNotEmpty()) tg_umrah_gallery_one_image_desc.text = element.description
            else tg_umrah_gallery_one_image_desc.gone()

            if(element.medias[0].source.isNotEmpty()){
                iw_umrah_gallery_one_image.loadImage(element.medias[0].source)
                iw_umrah_gallery_one_image.setOnClickListener {
                    listener.onClickOneImage(element, 0)
                }
            }
            else iw_umrah_gallery_one_image.gone()

            container_umrah_gallery_one_image.setOnClickListener {

            }
        }
    }
    companion object {
        val LAYOUT = R.layout.item_umrah_gallery_one_image
    }

    interface SetOnClickListener{
        fun onClickOneImage(gallery: UmrahGallery,positionImage:Int)
    }
}