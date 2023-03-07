package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.people.R
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import com.tokopedia.people.views.adapter.listener.UserFollowListener
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel.PeopleUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

open class ProfileFollowersAdapter(
    private val viewModel: FollowerFollowingViewModel,
    callback: AdapterCallback,
    private val listener: UserFollowListener,
) : BaseAdapter<PeopleUiModel>(callback) {

    protected var cList: MutableList<BaseItem>? = null
    var lastCursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgProfile: ImageUnify = view.findViewById(R.id.img_profile_image)
        internal var imgBadge: ImageUnify = view.findViewById(R.id.img_badge)
        internal var btnAction: UnifyButton = view.findViewById(R.id.btn_action_follow)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)

        override fun bindView(item: PeopleUiModel, position: Int) {
            setData(this, item, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerView.layoutManager?.childCount.orZero()
                val totalItemCount = recyclerView.layoutManager?.itemCount.orZero()
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
        viewModel.getFollowers(lastCursor, PAGE_COUNT)
    }

    fun onSuccess(data: List<PeopleUiModel>, cursor: String) {
        if (data.isEmpty()) {
            loadCompleted(mutableListOf(), data)
            isLastPage = true
            lastCursor = ""
        }

        loadCompleted(data, data)
        lastCursor = cursor
        isLastPage = cursor.isEmpty()
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun bindUser(holder: ViewHolder, item: ProfileUiModel.UserUiModel, position: Int) {
        holder.imgProfile.setImageUrl(item.photoUrl)
        holder.textName.text = item.name
        holder.imgBadge.hide()

        if (item.username.isNotBlank()) {
            holder.textUsername.show()
            holder.textUsername.text = "@${item.username}"
        } else {
            holder.textUsername.hide()
        }

        holder.itemView.setOnClickListener {
            listener.onItemUserClicked(item, position)
        }

        if (item.isMySelf) {
            holder.btnAction.hide()
        } else {
            updateFollowButton(holder.btnAction, item.isFollowed)
            holder.btnAction.show()
            holder.btnAction.setOnClickListener { view ->
                listener.onFollowUserClicked(item, position)
            }
        }
    }

    private fun bindShop(holder: ViewHolder, item: ProfileUiModel.ShopUiModel, position: Int) {
        holder.imgProfile.setImageUrl(item.logoUrl)
        holder.textName.text = item.name

        if (item.badgeUrl.isNotBlank()) {
            holder.imgBadge.show()
            holder.imgBadge.setImageUrl(item.badgeUrl)
        } else {
            holder.imgBadge.hide()
        }

        holder.textUsername.hide()

        holder.itemView.setOnClickListener {
            listener.onItemShopClicked(item, position)
        }
        updateFollowButton(holder.btnAction, item.isFollowed)
        holder.btnAction.show()
        holder.btnAction.setOnClickListener { view ->
            listener.onFollowShopClicked(item, position)
        }
    }

    private fun setData(holder: ViewHolder, item: PeopleUiModel, position: Int) {
        when (item) {
            is ProfileUiModel.UserUiModel -> bindUser(holder, item, position)
            is ProfileUiModel.ShopUiModel -> bindShop(holder, item, position)
        }
    }

    fun updateFollowUnfollow(position: Int, isFollowed: Boolean) {
        if (position >= 0 && position < items.size) {
            when(val item = items[position]) {
                is ProfileUiModel.UserUiModel -> {
                    items[position] = item.copy(isFollowed = isFollowed)
                }
                is ProfileUiModel.ShopUiModel -> {
                    items[position] = item.copy(isFollowed = isFollowed)
                }
            }
            notifyItemChanged(position)
        }
    }

    private fun updateFollowButton(button: UnifyButton, isFollowed: Boolean) {
        if (isFollowed) {
            button.text = button.context.getString(R.string.up_lb_following)
            button.buttonVariant = UnifyButton.Variant.GHOST
            button.buttonType = UnifyButton.Type.ALTERNATE
        } else {
            button.text = button.context.getString(R.string.up_btn_text_follow)
            button.buttonVariant = UnifyButton.Variant.FILLED
            button.buttonType = UnifyButton.Type.MAIN
        }
    }

    companion object {
        const val PAGE_COUNT = 10
    }
}
