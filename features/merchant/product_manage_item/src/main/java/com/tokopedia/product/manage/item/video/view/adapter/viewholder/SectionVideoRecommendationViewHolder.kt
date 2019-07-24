package com.tokopedia.product.manage.item.video.view.adapter.viewholder

import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoRecommendationFeaturedAdapter
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment.Companion.MAX_VIDEO
import com.tokopedia.product.manage.item.video.view.listener.SectionVideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.model.SectionVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel
import kotlinx.android.synthetic.main.item_product_add_section_video_recommendation.view.*

class SectionVideoRecommendationViewHolder(itemView: View,
                                           var sectionVideoRecommendationListener: SectionVideoRecommendationListener) : AbstractViewHolder<SectionVideoRecommendationViewModel>(itemView),
        ProductAddVideoFragment.Listener{

    private lateinit var productAddVideoRecommendationFeaturedAdapter: ProductAddVideoRecommendationFeaturedAdapter
    private lateinit var itemDecoration:RecyclerView.ItemDecoration

    private var videoRecommendationFeaturedList: ArrayList<VideoRecommendationViewModel?> = ArrayList()

    init {
        setViews()
        sectionVideoRecommendationListener.setProductAddVideoFragmentListener(this)
    }

    private fun setViews() {
        itemView.textShowMore.visibility = View.GONE
        itemView.textShowMore.setOnClickListener({
            sectionVideoRecommendationListener.onShowMoreClicked()
        })

        val videoFeaturedClickListener: VideoFeaturedClickListener = object : VideoFeaturedClickListener{
            override fun onVideoFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
                sectionVideoRecommendationListener.onVideoRecommendationFeaturedClicked(videoRecommendationViewModel)
            }
            override fun onVideoPlusClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
                sectionVideoRecommendationListener.onVideoRecommendationPlusClicked(videoRecommendationViewModel)
            }
        }

        videoRecommendationFeaturedList.add(null)
        productAddVideoRecommendationFeaturedAdapter = ProductAddVideoRecommendationFeaturedAdapter(videoRecommendationFeaturedList, videoFeaturedClickListener)
        itemView.recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        itemView.recyclerView.adapter = productAddVideoRecommendationFeaturedAdapter
        itemView.recyclerView.setHasFixedSize(true)

        itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position == 0) {
                    outRect.left = dpToPx(itemView.context, 8).toInt()
                }
                if(position == productAddVideoRecommendationFeaturedAdapter.videoRecommendationFeatured.size - 1){
                    outRect.right = dpToPx(itemView.context, 8).toInt()
                }
            }
        }

        itemView.recyclerView.addItemDecoration(itemDecoration)
    }

    fun dpToPx(context: Context, valueInDp: Int): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp.toFloat(), metrics)
    }

    override fun bind(videoRecommendationViewModel: SectionVideoRecommendationViewModel) {

    }

    override fun onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList: List<VideoRecommendationViewModel>) {
        videoRecommendationFeaturedList.remove(null)
        videoRecommendationFeaturedList = ArrayList()
        if(videoRecommendationViewModelList.size <= MAX_VIDEO){
            itemView.textShowMore.visibility = View.GONE
            videoRecommendationFeaturedList.addAll(videoRecommendationViewModelList)
        } else {
            itemView.textShowMore.visibility = View.VISIBLE
            for (i in 0 until MAX_VIDEO) {
                videoRecommendationFeaturedList.add(videoRecommendationViewModelList[i])
            }
        }
        productAddVideoRecommendationFeaturedAdapter.replaceData(videoRecommendationFeaturedList)
    }

    override fun notifyVideoChanged() {
        productAddVideoRecommendationFeaturedAdapter.notifyDataSetChanged()
    }

    interface VideoFeaturedClickListener {
        fun onVideoFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)
        fun onVideoPlusClicked(videoRecommendationViewModel : VideoRecommendationViewModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_section_video_recommendation
    }
}