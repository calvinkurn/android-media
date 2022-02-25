package com.tokopedia.createpost.uprofile.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.model.ProfileFollowerV2
import com.tokopedia.createpost.uprofile.model.ProfileFollowingListBase
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.createpost.uprofile.viewmodels.UserProfileViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession

open class ProfileFollowingAdapter(
    val viewModel: FollowerFollowingViewModel,
    val callback: AdapterCallback
) : BaseAdapter<ProfileFollowerV2>(callback) {

    protected var cList: MutableList<BaseItem>? = null
    private var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgProfile: ImageUnify = view.findViewById(R.id.img_profile_image)
        internal var btnAction: UnifyButton = view.findViewById(R.id.btn_action_follow)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)
        var isVisited = false

        override fun bindView(item: ProfileFollowerV2, position: Int) {
            setData(this, item, position)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_followers, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int, vararg args: String?) {
        super.loadData(pageNumber, *args)
        args[0]?.let { viewModel.getFollowings(it, cursor, 10) }
    }

    fun onSuccess(data: ProfileFollowingListBase) {
        loadCompleted(data.profileFollowings.profileFollower, data)
        cursor = data.profileFollowings.newCursor
        isLastPage = data.profileFollowings.newCursor.isEmpty();
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ProfileFollowerV2, position: Int) {
        val itemContext = holder.itemView.context
        holder.imgProfile.setImageUrl(item.profile.imageCover)
        holder.textName.text = item.profile.name
        holder.textUsername.text = item.profile.username

        if (item.profile.userID == UserSession(itemContext).userId) {
            holder.btnAction.hide()
        } else {
            holder.btnAction.show()

            if (item.isFollow) {
                updateToFollowUi(holder.btnAction)

                holder.btnAction.setOnClickListener { v ->
                    viewModel.doUnFollow(item.profile.encryptedUserID)
                    item.isFollow = false
                    notifyItemChanged(position)
                }
            } else {
                updateToUnFollowUi(holder.btnAction)

                holder.btnAction.setOnClickListener { v ->
                    viewModel.doFollow(item.profile.encryptedUserID)
                    item.isFollow = true
                    notifyItemChanged(position)
                }
            }
        }

        holder.itemView.setOnClickListener { v ->
            RouteManager.route(itemContext, item.profile.sharelink.applink)
        }
    }

    private fun updateToFollowUi(btnAction: UnifyButton) {
        btnAction?.text = "Following"
        btnAction?.buttonVariant = UnifyButton.Variant.GHOST
        btnAction?.buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun updateToUnFollowUi(btnAction: UnifyButton) {
        btnAction?.text = "Follow"
        btnAction?.buttonVariant = UnifyButton.Variant.FILLED
        btnAction?.buttonType = UnifyButton.Type.MAIN
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
            //listener.shopProductImpressed(holder.adapterPosition, data)
        }
    }
}

