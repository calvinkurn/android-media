package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import kotlinx.android.synthetic.main.item_umrah_gallery_three_image.view.*

class UmrahTravelAgentGalleryThreeImageViewHolder(view: View, val listener : SetOnClickListener) :
        AbstractViewHolder<UmrahGallery>(view){
    override fun bind(element: UmrahGallery) {
        with(itemView){
            if (element.title.isNotEmpty()) tg_umrah_gallery_three_image_title.text = element.title
            else tg_umrah_gallery_three_image_title.gone()

            if(element.subTitle.isNotEmpty()) tg_umrah_gallery_three_image_sub_title.text = element.subTitle
            else tg_umrah_gallery_three_image_sub_title.gone()

            if(element.description.isNotEmpty()) tg_umrah_gallery_three_image_desc.text = element.description
            else tg_umrah_gallery_three_image_desc.gone()

            if(element.medias.size >= 1 && element.medias[0].source.isNotEmpty()){
                iw_umrah_gallery_three_image_first.loadImage(element.medias[0].source)
                iw_umrah_gallery_three_image_first.setOnClickListener {
                    listener.onClickThreeImage(element,0)
                }
            }
            else iw_umrah_gallery_three_image_first.invisible()

            if(element.medias.size >= 2 && element.medias[1].source.isNotEmpty()){
                iw_umrah_gallery_three_image_second.loadImage(element.medias[1].source)
                iw_umrah_gallery_three_image_second.setOnClickListener {
                    listener.onClickThreeImage(element,1)
                }
            }
            else iw_umrah_gallery_three_image_second.invisible()

            if(element.medias.size >= 3 && element.medias[2].source.isNotEmpty()){
                iw_umrah_gallery_three_image_third.loadImage(element.medias[2].source)
                iw_umrah_gallery_three_image_third.setOnClickListener {
                    listener.onClickThreeImage(element,2)
                }
            }
            else iw_umrah_gallery_three_image_third.invisible()

            if(element.medias.size>3) {
                tg_umrah_gallery_three_next_label.show()
                tg_umrah_gallery_three_next_label.text =
                        getString(R.string.umrah_travel_gallery_next_images,(element.medias.size - 3).toString())
            }

            container_umrah_gallery_three_images.setOnClickListener {

            }
        }
    }
    companion object {
        val LAYOUT = R.layout.item_umrah_gallery_three_image
    }

    interface SetOnClickListener{
        fun onClickThreeImage(gallery: UmrahGallery, positionImage:Int)
    }
}
