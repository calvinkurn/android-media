package com.tokopedia.product.edit.adapter.viewholder

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
import com.tokopedia.product.edit.adapter.ProductAddVideoRecommendationFeaturedAdapter
import com.tokopedia.product.edit.listener.GetVideoRecommendationListener
import com.tokopedia.product.edit.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.viewmodel.SectionVideoRecommendationViewModel
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class SectionVideoRecommendationViewHolder(itemView: View,
                                           var sectionVideoRecommendationListener: SectionVideoRecommendationListener) : AbstractViewHolder<SectionVideoRecommendationViewModel>(itemView), GetVideoRecommendationListener {

    private lateinit var productAddVideoRecommendationFeaturedAdapter: ProductAddVideoRecommendationFeaturedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textShowMore: TextView
    private lateinit var itemDecoration:RecyclerView.ItemDecoration

    init {
        setViews(itemView)
    }

    private fun setViews(view: View) {
        sectionVideoRecommendationListener.setGetVideoRecommendationListener(this)
        textShowMore = view.findViewById(R.id.text_video_recommendation_show_more)
        textShowMore.setOnClickListener({
            sectionVideoRecommendationListener.onShowMoreClicked()
        })

        recyclerView = view.findViewById(R.id.recycler_view)

        val videoFeaturedClickListener: VideoFeaturedClickListener = object : VideoFeaturedClickListener{
            override fun onVideoFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
                sectionVideoRecommendationListener.onVideoRecommendationFeaturedClicked(videoRecommendationViewModel)
            }
        }

        productAddVideoRecommendationFeaturedAdapter = ProductAddVideoRecommendationFeaturedAdapter(ArrayList(), videoFeaturedClickListener, sectionVideoRecommendationListener.getVideoIDs)
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
        val videoRecommendationFeaturedList: ArrayList<VideoRecommendationViewModel> = ArrayList()
        if(videoRecommendationViewModelList.size <= 3){
            textShowMore.visibility = View.GONE
            videoRecommendationFeaturedList.addAll(videoRecommendationViewModelList)
        } else {
            for (i in 0..2) {
                videoRecommendationFeaturedList.add(videoRecommendationViewModelList[i])
            }
        }

        for(videoID in sectionVideoRecommendationListener.getVideoIDs){
            for(videoRecommendationFeatured in videoRecommendationFeaturedList){
                if(videoRecommendationFeatured.videoID == videoID){
                    videoRecommendationFeatured.choosen = true
                }
            }
        }
        productAddVideoRecommendationFeaturedAdapter.replaceData(videoRecommendationFeaturedList)
    }

    interface VideoFeaturedClickListener {
        fun onVideoFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_section_video_recommendation
    }
}