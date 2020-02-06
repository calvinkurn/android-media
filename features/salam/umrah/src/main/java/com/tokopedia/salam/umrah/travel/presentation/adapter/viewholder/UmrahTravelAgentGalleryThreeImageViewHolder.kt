package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesEntity
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import kotlinx.android.synthetic.main.item_umrah_gallery_three_image.view.*

class UmrahTravelAgentGalleryThreeImageViewHolder(view: View) : AbstractViewHolder<UmrahGallery>(view){
    override fun bind(element: UmrahGallery) {
        with(itemView){
            if (element.title.isNotEmpty()) tg_umrah_gallery_three_image_title.text = element.title
            else tg_umrah_gallery_three_image_title.gone()

            if(element.subTitle.isNotEmpty()) tg_umrah_gallery_three_image_sub_title.text = element.subTitle
            else tg_umrah_gallery_three_image_sub_title.gone()

            if(element.description.isNotEmpty()) tg_umrah_gallery_three_image_desc.text = element.description
            else tg_umrah_gallery_three_image_desc.gone()

            if(element.medias[0].source.isNotEmpty()) iw_umrah_gallery_three_image_first.loadImageRounded(element.medias[0].source,20.0f)
            else iw_umrah_gallery_three_image_first.invisible()

            if(element.medias[1].source.isNotEmpty()) iw_umrah_gallery_three_image_second.loadImageRounded(element.medias[1].source,20.0f)
            else iw_umrah_gallery_three_image_second.invisible()

            if(element.medias[2].source.isNotEmpty()) iw_umrah_gallery_three_image_third.loadImageRounded(element.medias[0].source,20.0f)
            else iw_umrah_gallery_three_image_third.invisible()
        }
    }
    companion object {
        val LAYOUT = R.layout.item_umrah_gallery_three_image
    }
}
