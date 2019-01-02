package com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_recommendation_card.view.*

/**
 * @author by milhamj on 20/12/18.
 */
class RecommendationCardAdapter(private val list: MutableList<RecommendationCardViewModel>)
    : RecyclerView.Adapter<RecommendationCardAdapter.RecommendationCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation_card, parent, false)
        return RecommendationCardViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecommendationCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class RecommendationCardViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(element: RecommendationCardViewModel) {
            initView(element)
            initViewListener(element)
        }

        private fun initView(element: RecommendationCardViewModel) {
            itemView.ivImage1.loadImage(element.image1Url)
            itemView.ivImage2.loadImage(element.image2Url)
            itemView.ivImage3.loadImage(element.image3Url)
            itemView.ivProfile.loadImageCircle(element.profileImageUrl)
            setButtonUi(element.isFollowing, element.btnText)
            itemView.tvDescription.text = element.description
            itemView.ivBadge.loadImage(element.badgeUrl)
            itemView.tvName.text = element.profileName
        }

        private fun initViewListener(element: RecommendationCardViewModel) {
            itemView.btnFollow.setOnClickListener {

            }
        }

        private fun setButtonUi(isActive: Boolean, text: String) {
            itemView.btnFollow.text = text
            if (isActive)
                itemView.btnFollow.buttonCompatType = ButtonCompat.PRIMARY
            else
                itemView.btnFollow.buttonCompatType = ButtonCompat.SECONDARY

        }
    }
}