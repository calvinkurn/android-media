package com.tokopedia.profile.view.adapter.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profile.R
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import kotlinx.android.synthetic.main.item_profile_header.view.*

/**
 * @author by milhamj on 9/20/18.
 */
class ProfileHeaderViewHolder(val v: View, val viewListener: ProfileContract.View)
    : AbstractViewHolder<ProfileHeaderViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.item_profile_header
    }

    override fun bind(element: ProfileHeaderViewModel) {
        ImageHandler.loadImageCircle2(itemView.context, itemView.avatar, element.avatar)
        itemView.kolBadge.visibility = if (element.isKol) View.VISIBLE else View.GONE
        itemView.name.text = element.name

        if (element.isKol) {
            setFollowersText(element)

            if (!isOwnUser(element.userId)) {
                itemView.followBtn.visibility = View.VISIBLE
                itemView.followBtn.setOnClickListener {
                    viewListener.followUnfollowUser(element.userId, !element.isFollowed)
                }
                updateButtonState(element.isFollowed)
            } else {
                itemView.followBtn.visibility = View.GONE
            }
        }
    }

    private fun setFollowersText(element: ProfileHeaderViewModel) {
        val followers = String.format(
                getString(R.string.profile_followers_number),
                element.followers
        )
        val following = String.format(
                getString(R.string.profile_following_number),
                element.following
        )
        val followersAndFollowing = String.format(
                getString(R.string.profile_followers_and_following),
                followers,
                following
        )
        val spannableString = SpannableString(followersAndFollowing)
        val goToFollowing = object: ClickableSpan() {
            override fun onClick(p0: View?) {
                viewListener.goToFollowing(element.userId)
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.setUnderlineText(false)
            }
        }
        spannableString.setSpan(
                goToFollowing,
                spannableString.indexOf(following),
                spannableString.indexOf(following) + following.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun isOwnUser(userId: Int) = !TextUtils.isEmpty(viewListener.userSession.userId)
            && userId == Integer.valueOf(viewListener.userSession.userId)

    private fun updateButtonState(isFollowed: Boolean) {
        if (isFollowed) {
            itemView.followBtn.buttonCompatType = ButtonCompat.SECONDARY
            itemView.followBtn.text = getString(R.string.profile_following)
        } else {
            itemView.followBtn.buttonCompatType = ButtonCompat.PRIMARY
            itemView.followBtn.text = getString(R.string.profile_follow)
        }
    }
}