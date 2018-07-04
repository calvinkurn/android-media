package com.tokopedia.product.edit.view.adapter.viewholder

import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.adapter.ProductAddVideoRecommendationFeaturedAdapter
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment.Companion.MAX_VIDEO
import com.tokopedia.product.edit.view.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.view.viewmodel.SectionVideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoViewModel

class SectionVideoRecommendationViewHolder(itemView: View,
                                           var sectionVideoRecommendationListener: SectionVideoRecommendationListener) : AbstractViewHolder<SectionVideoRecommendationViewModel>(itemView),
        ProductAddVideoFragment.Listener{

    private lateinit var productAddVideoRecommendationFeaturedAdapter: ProductAddVideoRecommendationFeaturedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textShowMore: TextView
    private lateinit var itemDecoration:RecyclerView.ItemDecoration

    private var videoRecommendationFeaturedList: ArrayList<VideoRecommendationViewModel?> = ArrayList()

    init {
        setViews(itemView)
    }

    private fun setViews(view: View) {
        sectionVideoRecommendationListener.setProductAddVideoFragmentListener(this)
        textShowMore = view.findViewById(R.id.text_video_recommendation_show_more)
        textShowMore.visibility = View.GONE
        textShowMore.setOnClickListener({
            sectionVideoRecommendationListener.onShowMoreClicked()
        })

        recyclerView = view.findViewById(R.id.recycler_view)

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
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = productAddVideoRecommendationFeaturedAdapter
        recyclerView.setHasFixedSize(true)

        itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
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

        recyclerView.addItemDecoration(itemDecoration)
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
            textShowMore.visibility = View.GONE
            videoRecommendationFeaturedList.addAll(videoRecommendationViewModelList)
        } else {
            textShowMore.visibility = View.VISIBLE
            for (i in 0 until MAX_VIDEO) {
                videoRecommendationFeaturedList.add(videoRecommendationViewModelList[i])
            }
        }
        productAddVideoRecommendationFeaturedAdapter.replaceData(videoRecommendationFeaturedList)
    }

    override fun onVideoChosenAdded(videoViewModel: VideoViewModel) {
        for(videoRecommendationFeatured in videoRecommendationFeaturedList){
            if(videoRecommendationFeatured?.videoID == videoViewModel.videoID){
                videoRecommendationFeatured?.chosen = true
            }
        }
        productAddVideoRecommendationFeaturedAdapter.notifyDataSetChanged()
    }

    override fun onVideoChosenDeleted(videoViewModel : VideoViewModel) {
        for(videoRecommendationFeatured in videoRecommendationFeaturedList){
            if(videoRecommendationFeatured?.videoID == videoViewModel.videoID){
                videoRecommendationFeatured?.chosen = false
            }
        }
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