package com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_recommendation_card.view.*

/**
 * @author by yfsx on 04/12/18.
 */
class RecommendationCardViewHolder(v: View) : AbstractViewHolder<RecommendationCardViewModel>(v) {

   companion object {
       @LayoutRes
       val LAYOUT = R.layout.item_recommendation_card
   }

    override fun bind(element: RecommendationCardViewModel) {
        initView(element)
        initViewListener(element)
    }

    fun initView(element: RecommendationCardViewModel) {
        itemView.ivImage1.loadImage(element.image1Url)
        itemView.ivImage2.loadImage(element.image2Url)
        itemView.ivImage3.loadImage(element.image3Url)
        itemView.ivProfile.loadImageCircle(element.profileImageUrl)
        setButtonUi(element.isFollowing, element.btnText)
        itemView.tvDescription.text = element.description
        itemView.ivBadge.loadImage(element.badgeUrl)
        itemView.tvName.text = element.profileName
    }

    fun initViewListener(element: RecommendationCardViewModel) {
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