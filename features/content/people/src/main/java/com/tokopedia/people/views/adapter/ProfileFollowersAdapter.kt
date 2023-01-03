package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.people.R
import com.tokopedia.people.listener.FollowerFollowingListener
import com.tokopedia.people.listener.FollowingFollowerListener
import com.tokopedia.people.model.ProfileFollowerListBase
import com.tokopedia.people.model.ProfileFollowerV2
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment
import com.tokopedia.people.views.fragment.UserProfileFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession

open class ProfileFollowersAdapter(
    val viewModel: FollowerFollowingViewModel,
    val callback: AdapterCallback,
    val userSession: UserSession,
    val listener: FollowerFollowingListener,
    private val followerListener: FollowingFollowerListener,
) : BaseAdapter<ProfileFollowerV2>(callback) {

    protected var cList: MutableList<BaseItem>? = null
    public var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgProfile: ImageUnify = view.findViewById(R.id.img_profile_image)
        internal var imgBadge: ImageUnify = view.findViewById(R.id.img_badge)
        internal var btnAction: UnifyButton = view.findViewById(R.id.btn_action_follow)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)
        var isVisited = false

        override fun bindView(item: ProfileFollowerV2, position: Int) {
            setData(this, item, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerView.layoutManager!!.childCount
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition().orZero()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        startDataLoading("")
                    }
                }
            }
        })
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_followers, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int, vararg args: String?) {
        super.loadData(pageNumber, *args)
        if (args == null || args.isEmpty()) {
            return
        }

        args[0]?.let { viewModel.getFollowers(cursor, PAGE_COUNT) }
    }

    fun onSuccess(data: ProfileFollowerListBase) {
        if (data == null ||
            data.profileFollowers == null ||
            data.profileFollowers.profileFollower == null
        ) {
            loadCompleted(mutableListOf(), data)
            isLastPage = true
            cursor = ""
        }

        loadCompleted(data.profileFollowers.profileFollower, data)
        cursor = data.profileFollowers.newCursor
        isLastPage = data.profileFollowers.newCursor.isEmpty()
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ProfileFollowerV2, position: Int) {
        val itemContext = holder.itemView.context
        holder.imgProfile.setImageUrl(item.profile.imageCover)
        holder.textName.text = item.profile.name

        val badgeUrl = item.profile.badges.getOrNull(BADGE_URL_IDX).orEmpty()
        if(badgeUrl.isNotEmpty()) {
            holder.imgBadge.show()
            holder.imgBadge.setImageUrl(badgeUrl)
        }
        else {
            holder.imgBadge.hide()
        }

        if (item.profile.username.isNotBlank()) {
            holder.textUsername.show()
            holder.textUsername.text = "@" + item.profile.username
        } else {
            holder.textUsername.hide()
        }

        holder.itemView.setOnClickListener {
            followerListener.clickUser(userSession.userId, item.profile.userID == userSession.userId)
            val intent = RouteManager.getIntent(
                itemContext,
                item.profile.sharelink.applink,
            )
            intent.putExtra(UserProfileFragment.EXTRA_POSITION_OF_PROFILE, position)
            listener.callstartActivityFromFragment(
                intent,
                UserProfileFragment.REQUEST_CODE_USER_PROFILE,
            )
        }

        if (item.profile.userID == this@ProfileFollowersAdapter.userSession.userId) {
            holder.btnAction.hide()
        } else {
            holder.btnAction.show()

            if (item.isFollow) {
                updateToFollowUi(holder.btnAction)

                holder.btnAction.setOnClickListener { v ->
                    if (!DeviceConnectionInfo.isInternetAvailable(itemContext.applicationContext)) {
                        val snackBar = Toaster.build(
                            holder.btnAction as View,
                            itemContext.getString(com.tokopedia.people.R.string.up_error_unfollow),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                        )

                        snackBar.show()

                        return@setOnClickListener
                    }

                    if (!userSession?.isLoggedIn) {
                        listener.callstartActivityFromFragment(
                            ApplinkConst.LOGIN,
                            FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW,
                        )
                    } else {
                        followerListener.clickUnfollow(userSession.userId, item.profile.userID == userSession.userId)
                        viewModel.doUnFollow(item.profile.encryptedUserID)
                        item.isFollow = false
                        notifyItemChanged(position)
                    }
                }
            } else {
                updateToUnFollowUi(holder.btnAction)

                holder.btnAction.setOnClickListener { v ->
                    if (!DeviceConnectionInfo.isInternetAvailable(itemContext.applicationContext)) {
                        val snackBar = Toaster.build(
                            holder.btnAction as View,
                            itemContext.getString(com.tokopedia.people.R.string.up_error_follow),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                        )

                        snackBar.show()

                        return@setOnClickListener
                    }

                    if (!userSession?.isLoggedIn) {
                        listener.callstartActivityFromFragment(
                            ApplinkConst.LOGIN,
                            FollowerFollowingListingFragment.REQUEST_CODE_LOGIN_TO_FOLLOW,
                        )
                    } else {
                        followerListener.clickFollow(userSession.userId, item.profile.userID == userSession.userId)
                        viewModel.doFollow(item.profile.encryptedUserID)
                        item.isFollow = true
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }
    fun updateFollowUnfollow(position: Int, isFollowed: Boolean) {
        if (position >= 0 && position < items.size) {
            items[position].isFollow = isFollowed
            notifyItemChanged(position)
        }
    }

    private fun updateToFollowUi(btnAction: UnifyButton) {
        btnAction.text = btnAction.context.getString(R.string.up_lb_following)
        btnAction.buttonVariant = UnifyButton.Variant.GHOST
        btnAction.buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun updateToUnFollowUi(btnAction: UnifyButton) {
        btnAction.text = btnAction.context.getString(R.string.up_btn_text_follow)
        btnAction.buttonVariant = UnifyButton.Variant.FILLED
        btnAction.buttonType = UnifyButton.Type.MAIN
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
            // listener.shopProductImpressed(holder.adapterPosition, data)
        }
    }

    companion object {
        const val PAGE_COUNT = 10

        private const val BADGE_URL_IDX = 1
    }
}
