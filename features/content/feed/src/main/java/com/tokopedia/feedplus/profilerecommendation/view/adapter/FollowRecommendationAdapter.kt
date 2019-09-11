package com.tokopedia.feedplus.profilerecommendation.view.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.feedplus.R
import com.tokopedia.kotlin.extensions.view.*

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationAdapter(list: List<RecommendationCardViewModel>)
    : RecyclerView.Adapter<FollowRecommendationAdapter.FollowRecommendationViewHolder>() {

    private val itemList: MutableList<RecommendationCardViewModel> = list.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowRecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_recommendation_card, parent, false)
        return FollowRecommendationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FollowRecommendationViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun onViewRecycled(holder: FollowRecommendationViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun addItems(list: List<RecommendationCardViewModel>) {
        itemList.addAll(list)
    }

    fun clearItems() {
        itemList.clear()
    }

    inner class FollowRecommendationViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val ivImage1 = itemView.findViewById<ImageView>(R.id.ivImage1)
        private val ivImage2 = itemView.findViewById<ImageView>(R.id.ivImage2)
        private val ivImage3 = itemView.findViewById<ImageView>(R.id.ivImage3)
        private val ivProfile = itemView.findViewById<ImageView>(R.id.ivProfile)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val btnFollow = itemView.findViewById<UnifyButton>(R.id.btnFollow)
        private val ivBadge = itemView.findViewById<ImageView>(R.id.ivBadge)

        fun bind(element: RecommendationCardViewModel) {
            initView(element)
            initViewListener(element)
        }

        fun onViewRecycled() {
            ivImage1.clearImage()
            ivImage2.clearImage()
            ivImage3.clearImage()
            ivProfile.clearImage()
        }

        private fun initView(element: RecommendationCardViewModel) {
            loadImageOrDefault(ivImage1, element.image1Url)
            loadImageOrDefault(ivImage2, element.image2Url)
            loadImageOrDefault(ivImage3, element.image3Url)
            tvDescription.text = element.description
            tvName.text = element.profileName

            if (!TextUtils.isEmpty(element.profileImageUrl)) {
                ivProfile.loadImageCircle(element.profileImageUrl)
            } else {
                ivProfile.setImageDrawable(
                        MethodChecker.getDrawable(itemView.context, R.drawable.error_drawable)
                )
            }

            setBadge(element.badgeUrl)

            if (element.template.ctaLink) {
                btnFollow.show()
                setButtonUi(element.cta)
            } else {
                btnFollow.invisible()
            }
        }

        private fun initViewListener(element: RecommendationCardViewModel) {
            itemView.setOnClickListener {
//                listener.onRecommendationAvatarClick(
//                        positionInFeed,
//                        adapterPosition,
//                        element.redirectUrl
//                )
            }

            btnFollow.setOnClickListener {
//                listener.onRecommendationActionClick(
//                        positionInFeed,
//                        adapterPosition,
//                        element.cta.authorID,
//                        element.cta.authorType,
//                        element.cta.isFollow
//                )
            }
        }

        private fun setBadge(badgeUrl: String) {
            val layoutParams = tvName.layoutParams as ViewGroup.MarginLayoutParams
            if (!TextUtils.isEmpty(badgeUrl)) {
                ivBadge.show()
                ivBadge.loadImage(badgeUrl)
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            } else {
                ivBadge.hide()
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_0).toInt()
            }
        }

        private fun setButtonUi(cta: FollowCta) {
            if (cta.isFollow) {
//                btnFollow.buttonCompatType = ButtonCompat.SECONDARY
                btnFollow.text = cta.textTrue
            } else {
//                btnFollow.buttonCompatType = ButtonCompat.PRIMARY
                btnFollow.text = cta.textFalse
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
}