package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.PostListAdapterTypeFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import kotlinx.android.synthetic.main.shc_item_post_list_pager.view.*

/**
 * Created By @ilhamsuaib on 14/06/21
 */

class PostListPagerAdapter(
        private val onPostItemClicked: (PostItemUiModel) -> Unit
) : RecyclerView.Adapter<PostListPagerAdapter.PostListPagerViewHolder>() {

    private var pagers = listOf<PostListPagerUiModel>()

    fun setItems(pagers: List<PostListPagerUiModel>) {
        this.pagers = pagers
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListPagerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shc_item_post_list_pager, parent, false)
        return PostListPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListPagerViewHolder, position: Int) {
        val pager = pagers[position]
        holder.bind(pager, onPostItemClicked)
    }

    override fun getItemCount(): Int = pagers.size

    inner class PostListPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val postAdapter = BaseListAdapter<PostItemUiModel, PostListAdapterTypeFactoryImpl>(PostListAdapterTypeFactoryImpl())

        fun bind(pager: PostListPagerUiModel, onPostItemClicked: (PostItemUiModel) -> Unit) {
            with(itemView) {
                rvShcPostList.layoutManager = object : LinearLayoutManager(itemView.context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvShcPostList.adapter = postAdapter
            }

            if (postAdapter.itemCount <= 0) {
                postAdapter.data.addAll(pager.postList)
                postAdapter.notifyDataSetChanged()
            }

            postAdapter.setOnAdapterInteractionListener {
                onPostItemClicked(it)
            }
        }
    }
}