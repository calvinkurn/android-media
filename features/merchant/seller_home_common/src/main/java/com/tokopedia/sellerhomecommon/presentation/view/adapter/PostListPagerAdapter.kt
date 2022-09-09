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

    var pagers = listOf<PostListPagerUiModel>()
    private var isCheckingMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListPagerViewHolder {
        val binding = ShcItemPostListPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostListPagerViewHolder(binding, listener, isCheckingMode)
    }

    override fun onBindViewHolder(holder: PostListPagerViewHolder, position: Int) {
        val pager = pagers[position]
        holder.bind(pager)
    }

    override fun getItemCount(): Int = pagers.size

    fun setCheckingMode(isCheckingMode: Boolean) {
        this.isCheckingMode = isCheckingMode
    }

    class PostListPagerViewHolder(
        private val binding: ShcItemPostListPagerBinding,
        listener: Listener,
        isCheckingMode: Boolean = false
    ) : RecyclerView.ViewHolder(binding.root) {

        private val postAdapter = BaseListAdapter<PostItemUiModel, PostListAdapterTypeFactoryImpl>(
            PostListAdapterTypeFactoryImpl(listener, isCheckingMode)
        )

        fun bind(pager: PostListPagerUiModel) {
            with(binding) {
                rvShcPostList.layoutManager = object : LinearLayoutManager(itemView.context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvShcPostList.adapter = postAdapter
            }

            postAdapter.data.addAll(pager.postList)
        }
    }

    interface Listener {
        fun onItemClicked(model: PostItemUiModel)
        fun onCheckedListener(isChecked: Boolean)
        fun onTimerFinished()
        fun onCancelDismissalClicked()
    }
}