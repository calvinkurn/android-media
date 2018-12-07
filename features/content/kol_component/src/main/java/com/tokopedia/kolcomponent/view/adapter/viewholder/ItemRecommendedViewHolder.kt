package com.tokopedia.kolcomponent.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.viewmodel.ItemRecommendedViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_feed_recommended.view.*

/**
 * @author by yfsx on 04/12/18.
 */
class ItemRecommendedViewHolder(v: View) : AbstractViewHolder<ItemRecommendedViewModel>(v) {

   companion object {
       @LayoutRes
       val LAYOUT = R.layout.item_feed_recommended
   }

    override fun bind(element: ItemRecommendedViewModel) {
        initView(element)
        initViewListener(element)
    }

    fun initView(element: ItemRecommendedViewModel) {
        itemView.tvTitle.text = element.title
        itemView.ivImage1.loadImage(element.image1Url)
        itemView.ivImage2.loadImage(element.image2Url)
        itemView.ivImage3.loadImage(element.image3Url)
        itemView.ivProfile.loadImageCircle(element.profileImageUrl)
        setButtonUi(element.isFollowing, element.btnText)
        itemView.tvDescription.text = element.description
        itemView.ivBadge.loadImage(element.badgeUrl)
        itemView.tvName.text = element.profileName
    }

    fun initViewListener(element: ItemRecommendedViewModel) {
        itemView.btnFollow.setOnClickListener {

        }
    }

    fun setButtonUi(isActive: Boolean, text: String) {
        itemView.btnFollow.text = text
        if (isActive)
            itemView.btnFollow.buttonCompatType = ButtonCompat.PRIMARY
        else
            itemView.btnFollow.buttonCompatType = ButtonCompat.SECONDARY

    }
}