package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profile.R
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import kotlinx.android.synthetic.main.item_profile_header.view.*

/**
 * @author by milhamj on 9/20/18.
 */
class ProfileHeaderViewHolder(val v: View, val viewListener: ProfileEmptyContract.View)
    : AbstractViewHolder<ProfileHeaderViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_profile_header

        const val PAYLOAD_FOLLOW = 13
        const val TEXT_LENGTH_MIN = 3
    }

    override fun bind(element: ProfileHeaderViewModel) {
        ImageHandler.loadImageCircle2(itemView.context, itemView.avatar, element.avatar)
        itemView.name.text = element.name

        if (element.isKol || element.isAffiliate) {
            itemView.kolBadge.visibility = if (element.isKol) View.VISIBLE else View.GONE

            itemView.followers.visibility =
                    if (getFollowersText(element).length > TEXT_LENGTH_MIN) View.VISIBLE
                    else View.GONE
            itemView.followers.text = getFollowersText(element)
            itemView.followers.movementMethod = LinkMovementMethod.getInstance()

            if (!element.isOwner && GlobalConfig.isCustomerApp()) {
                itemView.followBtn.visibility = View.VISIBLE
                itemView.followBtn.setOnClickListener {
                    viewListener.followUnfollowUser(element.userId, !element.isFollowed)
                }
                updateButtonState(element.isFollowed)
            } else {
                itemView.followBtn.visibility = View.GONE
            }
        } else {
            itemView.kolBadge.visibility = View.GONE
            if (element.isOwner) {
                itemView.followers.visibility =
                        if (getFollowersText(element).length > TEXT_LENGTH_MIN) View.VISIBLE
                        else View.GONE
                itemView.followers.text = getFollowersText(element)
                itemView.followers.movementMethod = LinkMovementMethod.getInstance()
            } else {
                itemView.followers.visibility = View.GONE
            }
        }

        if (element.isOwner) {
            itemView.changeAvatar.visibility = View.VISIBLE
            itemView.changeAvatar.setOnClickListener {
                viewListener.onChangeAvatarClicked()
            }
            itemView.avatar.setOnClickListener {
                viewListener.onChangeAvatarClicked()
            }
        } else {
            itemView.changeAvatar.visibility = View.GONE
        }
    }

    override fun bind(element: ProfileHeaderViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element?.let {
            when (payloads[0]) {
                PAYLOAD_FOLLOW -> {
                    itemView.followers.visibility =
                            if (getFollowersText(element).length > TEXT_LENGTH_MIN) View.VISIBLE
                            else View.GONE
                    itemView.followers.text = getFollowersText(element)
                    updateButtonState(it.isFollowed)
                }
                else -> bind(element)
            }
        }
    }

    private fun getFollowersText(element: ProfileHeaderViewModel): SpannableString {
        val spannableString: SpannableString
        val following =
                if (element.following != ProfileHeaderViewModel.ZERO)
                    String.format(getString(R.string.profile_following_number), element.following)
                else ""
        spannableString = if ((element.isKol || element.isAffiliate)
                && element.followers != ProfileHeaderViewModel.ZERO) {

            val followers = String.format(
                    getString(R.string.profile_followers_number),
                    element.followers
            )
            val followersAndFollowing =
                    if (!TextUtils.isEmpty(following)) String.format(
                            getString(R.string.profile_followers_and_following),
                            followers,
                            following
                    )
                    else followers
            SpannableString(followersAndFollowing)

        } else {
            SpannableString(following)
        }

        val goToFollowing = object : ClickableSpan() {
            override fun onClick(p0: View?) {
                viewListener.goToFollowing()
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.setUnderlineText(false)
                ds?.color = MethodChecker.getColor(itemView.context, R.color.black_54)
            }
        }

        if (spannableString.indexOf(following) != -1) {
            spannableString.setSpan(
                    goToFollowing,
                    spannableString.indexOf(following),
                    spannableString.indexOf(following) + following.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

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