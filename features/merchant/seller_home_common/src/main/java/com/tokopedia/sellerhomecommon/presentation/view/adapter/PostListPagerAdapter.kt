package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhomecommon.databinding.ShcItemPostListPagerBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.PostListAdapterTypeFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel

/**
 * Created By @ilhamsuaib on 14/06/21
 */

class PostListPagerAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<PostListPagerAdapter.PostListPagerViewHolder>() {

    private val pagers = mutableListOf<PostListPagerUiModel>()
    private var isCheckingMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListPagerViewHolder {
        val binding = ShcItemPostListPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostListPagerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostListPagerViewHolder, position: Int) {
        val pager = pagers[position]
        holder.bind(pager, isCheckingMode)
    }

    override fun getItemCount(): Int = pagers.size

    fun setCheckingMode(isCheckingMode: Boolean) {
        this.isCheckingMode = isCheckingMode
    }

    fun setPagers(pagers: List<PostListPagerUiModel>) {
        this.pagers.clear()
        this.pagers.addAll(pagers)
    }

    inner class PostListPagerViewHolder(
        private val binding: ShcItemPostListPagerBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pager: PostListPagerUiModel, isCheckingMode: Boolean) {
            with(binding) {
                val postAdapter = BaseListAdapter<PostItemUiModel, PostListAdapterTypeFactoryImpl>(
                    PostListAdapterTypeFactoryImpl(listener, isCheckingMode)
                )
                postAdapter.data.addAll(pager.postList)

                rvShcPostList.layoutManager = object : LinearLayoutManager(itemView.context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvShcPostList.adapter = postAdapter
            }
        }
    }

    interface Listener {
        fun onItemClicked(model: PostItemUiModel)
        fun onCheckedListener(isChecked: Boolean)
        fun onTimerFinished()
        fun onCancelDismissalClicked()
    }
}
