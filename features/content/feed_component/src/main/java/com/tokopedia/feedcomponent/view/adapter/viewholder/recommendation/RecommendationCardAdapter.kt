package com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_recommendation_card.view.*

/**
 * @author by milhamj on 20/12/18.
 */
class RecommendationCardAdapter(val list: MutableList<RecommendationCardViewModel>,
                                private val positionInFeed: Int,
                                private val listener: RecommendationCardListener)
    : RecyclerView.Adapter<RecommendationCardAdapter.RecommendationCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation_card, parent, false)
        return RecommendationCardViewHolder(view, positionInFeed, listener)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecommendationCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onViewRecycled(holder: RecommendationCardViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    class RecommendationCardViewHolder(v: View,
                                       private val positionInFeed: Int,
                                       private val listener: RecommendationCardListener)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: RecommendationCardViewModel) {
            initView(element)
            initViewListener(element)
        }

        fun onViewRecycled() {
            itemView.ivImage1.clearImage()
            itemView.ivImage2.clearImage()
            itemView.ivImage3.clearImage()
            itemView.ivProfile.clearImage()
        }

        private fun initView(element: RecommendationCardViewModel) {
            loadImageOrDefault(itemView.ivImage1, element.image1Url)
            loadImageOrDefault(itemView.ivImage2, element.image2Url)
            loadImageOrDefault(itemView.ivImage3, element.image3Url)
            itemView.tvDescription.text = element.description
            itemView.tvName.text = element.profileName

            if (!TextUtils.isEmpty(element.profileImageUrl)) {
                itemView.ivProfile.loadImageCircle(element.profileImageUrl)
            } else {
                itemView.ivProfile.setImageDrawable(
                        MethodChecker.getDrawable(itemView.context, R.drawable.error_drawable)
                )
            }

            setBadge(element.badgeUrl)

            if (element.template.ctaLink) {
                itemView.btnFollow.show()
                setButtonUi(element.cta)
            } else {
                itemView.btnFollow.invisible()
            }
        }

        private fun initViewListener(element: RecommendationCardViewModel) {
            itemView.setOnClickListener {
                listener.onRecommendationAvatarClick(
                        positionInFeed,
                        adapterPosition,
                        element.redirectUrl
                )
            }

            itemView.btnFollow.setOnClickListener {
                listener.onRecommendationActionClick(
                        positionInFeed,
                        adapterPosition,
                        element.cta.authorID,
                        element.cta.authorType,
                        element.cta.isFollow
                )
            }
        }

        private fun setBadge(badgeUrl: String) {
            val layoutParams = itemView.tvName.layoutParams as ViewGroup.MarginLayoutParams
            if (!TextUtils.isEmpty(badgeUrl)) {
                itemView.ivBadge.show()
                itemView.ivBadge.loadImage(badgeUrl)
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            } else {
                itemView.ivBadge.hide()
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_0).toInt()
            }
        }

        private fun setButtonUi(cta: FollowCta) {
            if (cta.isFollow) {
                itemView.btnFollow.buttonCompatType = ButtonCompat.SECONDARY
                itemView.btnFollow.text = cta.textTrue
            } else {
                itemView.btnFollow.buttonCompatType = ButtonCompat.PRIMARY
                itemView.btnFollow.text = cta.textFalse
            }
        }

        private fun loadImageOrDefault(imageView: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) {
                imageView.loadImage(imageUrl)
            } else {
                imageView.setBackgroundColor(
                        MethodChecker.getColor(imageView.context, R.color.feed_image_default)
                )
            }
        }
    }

    interface RecommendationCardListener {
        fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String)

        fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean)
    }
}