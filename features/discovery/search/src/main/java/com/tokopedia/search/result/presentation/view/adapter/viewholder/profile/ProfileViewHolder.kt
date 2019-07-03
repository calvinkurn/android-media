package com.tokopedia.search.result.presentation.view.adapter.viewholder.profile

import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintSet
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.view.listener.ProfileListener
import kotlinx.android.synthetic.main.search_search_result_profile.view.*

class ProfileViewHolder(itemView: View, val profileListListener: ProfileListener) : AbstractViewHolder<ProfileViewModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_search_result_profile
    }
    override fun bind(profileData: ProfileViewModel) {
        when(!TextUtils.isEmpty(profileData?.imgUrl?:"")){
            true -> ImageHandler.loadImageCircle2(itemView.context, itemView.img_profile, profileData!!.imgUrl)
        }
        itemView.tv_username.text = profileData.username
        itemView.tv_name.text = profileData.name

        when(profileData.isKol) {
            true -> {
                val kolDrawable : Drawable = itemView.context.resources.getDrawable(R.drawable.search_search_kol_badge)
                itemView.tv_name.setCompoundDrawablesWithIntrinsicBounds(
                    kolDrawable,
                    null,
                    null,
                    null
                )
            }
        }

        when(TextUtils.isEmpty(profileData.username)) {
            true -> itemView.tv_username.visibility = View.GONE
            false -> itemView.tv_username.visibility = View.VISIBLE
        }

        when(profileData.post_count != 0) {
            true -> {
                itemView.tv_post_count.text = String.format(
                    itemView.context.getString(R.string.post_count_value),
                    profileData.post_count.toString()
                )
                itemView.tv_post_count.visibility = View.VISIBLE
            }
            false -> {
                itemView.tv_post_count.visibility = View.GONE
            }
        }

        when(profileData.followed){
            true -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_following_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.SECONDARY
                itemView.btn_follow.isClickable = true

                val constraintSet = ConstraintSet()
                constraintSet.clone(itemView.constraintLayout)
                constraintSet.connect(
                    itemView.tv_name.id, ConstraintSet.END,
                    itemView.label_following.id, ConstraintSet.START)
                constraintSet.applyTo(itemView.constraintLayout)
                itemView.label_following.visibility = View.VISIBLE
            }
            false -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_follow_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.PRIMARY
                itemView.btn_follow.isClickable = true

                val constraintSet = ConstraintSet()
                constraintSet.clone(itemView.constraintLayout)
                constraintSet.connect(
                    itemView.tv_name.id, ConstraintSet.END,
                    itemView.constraintLayout.id, ConstraintSet.END)
                constraintSet.applyTo(itemView.constraintLayout)
                itemView.label_following.visibility = View.GONE
            }
        }

        itemView.setOnClickListener {
            profileListListener.onHandleProfileClick(profileData)
        }

        itemView.btn_follow.setOnClickListener {
            profileListListener.onFollowButtonClicked(adapterPosition, profileData)
        }
    }
}