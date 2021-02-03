package com.tokopedia.gallery.customview

import android.content.Context
import android.content.res.Resources
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.gallery.R
import com.tokopedia.gallery.viewmodel.ImageReviewItem

import java.util.ArrayList

/**
 * Created by henrypriyono on 12/03/18.
 */

class BottomSheetImageReviewSlider : FrameLayout, ImageReviewSliderView {

    private var bottomSheetBehavior: UserLockBottomSheetBehavior<*>? = null
    private var containerView: View? = null
    private var backButton: View? = null
    private var bottomSheetLayout: View? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SliderAdapter? = null

    private var callback: Callback? = null

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    val isBottomSheetShown: Boolean
        get() = bottomSheetBehavior != null && bottomSheetBehavior!!.state != BottomSheetBehavior.STATE_HIDDEN

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
    }

    private fun init() {
        bindView()
        initRecyclerView()
    }

    private fun bindView() {
        containerView = View.inflate(context, R.layout.review_image_slider, this)
        recyclerView = containerView!!.findViewById(R.id.review_image_slider_recycler_view)
        backButton = containerView!!.findViewById(R.id.backButton)
        bottomSheetLayout = this
    }

    private fun initRecyclerView() {
        adapter = SliderAdapter()
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        loadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (callback!!.isAllowLoadMore) {
                    callback!!.onRequestLoadMore(page)
                } else {
                    updateStateAfterGetData()
                }
            }
        }
        loadMoreTriggerListener?.let { recyclerView!!.addOnScrollListener(it) }
    }

    fun setup(callback: Callback) {
        this.callback = callback
        initListener()
    }

    fun closeView() {
        if (bottomSheetBehavior != null && bottomSheetBehavior!!.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initListener() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout!!) as UserLockBottomSheetBehavior<*>
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN

        backButton!!.setOnClickListener { callback!!.onButtonBackPressed() }
    }

    override fun onBackPressed(): Boolean {
        if (isBottomSheetShown) {
            closeView()
            return true
        } else {
            return false
        }
    }

    override fun resetState() {
        adapter!!.resetState()
        loadMoreTriggerListener!!.resetState()
    }

    override fun onLoadingData() {
        adapter!!.addLoading()
    }

    override fun displayImage(position: Int) {
        recyclerView!!.scrollToPosition(position)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onLoadDataSuccess(imageReviewItems: List<ImageReviewItem>, isHasNextPage: Boolean) {
        adapter!!.removeLoading()
        adapter!!.appendItems(imageReviewItems)
        loadMoreTriggerListener!!.updateStateAfterGetData()
        loadMoreTriggerListener!!.setHasNextPage(isHasNextPage)
    }

    override fun onLoadDataFailed() {
        adapter!!.removeLoading()
        loadMoreTriggerListener!!.updateStateAfterGetData()
        recyclerView!!.scrollToPosition(adapter!!.galleryItemCount - 1)
    }

    interface Callback {
        val isAllowLoadMore: Boolean
        fun onRequestLoadMore(page: Int)
        fun onButtonBackPressed()
    }

    private class SliderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val imageReviewItemList = ArrayList<ImageReviewItem>()
        var isLoadingItemEnabled = true
            private set

        val galleryItemCount: Int
            get() = imageReviewItemList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ImageSliderViewHolder) {
                holder.bind(imageReviewItemList[position])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(viewType, parent, false)
            return if (viewType == ImageSliderViewHolder.LAYOUT) {
                ImageSliderViewHolder(view)
            } else {
                LoadingSliderViewHolder(view)
            }
        }

        override fun getItemCount(): Int {
            return if (isLoadingItemEnabled) imageReviewItemList.size + 1 else imageReviewItemList.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (isGalleryItem(position)) {
                ImageSliderViewHolder.LAYOUT
            } else {
                LoadingSliderViewHolder.LAYOUT
            }
        }

        fun appendItems(imageReviewItems: List<ImageReviewItem>) {
            imageReviewItemList.addAll(imageReviewItems)
            notifyDataSetChanged()
        }

        fun resetState() {
            imageReviewItemList.clear()
            isLoadingItemEnabled = true
            notifyDataSetChanged()
        }

        fun isGalleryItem(position: Int): Boolean {
            return position < imageReviewItemList.size
        }

        fun removeLoading() {
            isLoadingItemEnabled = false
            notifyDataSetChanged()
        }

        fun addLoading() {
            isLoadingItemEnabled = true
            notifyDataSetChanged()
        }
    }

    private class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView
        private val date: TextView
        private val name: TextView
        private val reviewContainer: View
        private val rating: ImageView

        init {
            imageView = itemView.findViewById(R.id.review_image_slider_item_image_view)
            date = itemView.findViewById(R.id.review_image_slider_date)
            name = itemView.findViewById(R.id.review_image_slider_name)
            reviewContainer = itemView.findViewById(R.id.review_image_slider_container)
            rating = itemView.findViewById(R.id.review_image_slider_rating)
        }

        fun bind(item: ImageReviewItem) {
            ImageHandler.LoadImage(imageView, item.imageUrlLarge)

            if (!TextUtils.isEmpty(item.reviewerName)) {
                name.text = item.reviewerName
                reviewContainer.visibility = View.VISIBLE
            } else {
                reviewContainer.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(item.formattedDate)) {
                date.text = item.formattedDate
                date.visibility = View.VISIBLE
            } else {
                date.visibility = View.GONE
            }

            if (item.rating != ImageReviewItem.NO_RATING_DATA) {
                ImageHandler.loadImageRounded2(itemView.context, rating, RatingView.getRatingDrawable(item.rating), 0f)
                rating.visibility = View.VISIBLE
            } else {
                rating.visibility = View.GONE
            }
        }

        companion object {

            @LayoutRes
            val LAYOUT = R.layout.review_image_slider_item
        }
    }

    private class LoadingSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {

            @LayoutRes
            val LAYOUT = R.layout.review_image_slider_loading
        }
    }
}
