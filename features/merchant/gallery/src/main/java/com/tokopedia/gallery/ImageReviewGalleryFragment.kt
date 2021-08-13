package com.tokopedia.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.gallery.adapter.GalleryAdapter
import com.tokopedia.gallery.adapter.TypeFactory
import com.tokopedia.gallery.customview.BottomSheetImageReviewSliderCallback
import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterContract
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.domain.GraphqlUseCase
import java.util.*

class ImageReviewGalleryFragment : BaseListFragment<ImageReviewItem, TypeFactory>(), BottomSheetImageReviewSliderCallback, GalleryView {

    companion object {
        fun createInstance(): Fragment {
            return ImageReviewGalleryFragment()
        }
    }

    private var presenter: ReviewGalleryPresenterContract? = null
    private var activity: ImageReviewGalleryActivity? = null

    override val isAllowLoadMore: Boolean
        get() = !adapter.isLoading

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as ImageReviewGalleryActivity?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_gallery_pdp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            it.bottomSheetImageReviewSlider?.setup(this, it.shouldShowSeeAllButton)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        activity?.let {
            if (it.isImageListPreloaded) {
                it.bottomSheetImageReviewSlider?.displayImage(it.defaultPosition)
            }
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun loadData(page: Int) {
        activity?.let {
            if (it.isImageListPreloaded) {
                val imageUrlList = it.imageUrlList
                handleItemResult(convertToImageReviewItemList(imageUrlList ?: arrayListOf()), false)
                return
            }
            it.bottomSheetImageReviewSlider?.onLoadingData()
            presenter?.loadData(it.productId.toLongOrNull() ?: 0L, page)
        }
    }

    private fun convertToImageReviewItemList(imageUrlList: ArrayList<String>): List<ImageReviewItem> {
        val imageReviewItemList = ArrayList<ImageReviewItem>()
        for (imageUrl in imageUrlList) {
            val imageReviewItem = ImageReviewItem()
            imageReviewItem.imageUrlThumbnail = imageUrl
            imageReviewItem.imageUrlLarge = imageUrl
            imageReviewItem.imageCount = activity?.imageCount ?: ""
            imageReviewItemList.add(imageReviewItem)
        }
        return imageReviewItemList
    }

    override fun getAdapterTypeFactory(): TypeFactory {
        return GalleryAdapter(this)
    }

    override fun onItemClicked(imageReviewItem: ImageReviewItem) {

    }

    override fun initInjector() {
        presenter = ReviewGalleryPresenter(
                GetImageReviewUseCase(context, GraphqlUseCase()),
                this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onGalleryItemClicked(position: Int) {
        activity?.let {
            it.bottomSheetImageReviewSlider?.displayImage(position)
            ImageReviewGalleryTracking.eventClickReviewGalleryItem(it, activity?.productId ?: "")
        }
    }

    override fun handleItemResult(imageReviewItemList: List<ImageReviewItem>, isHasNextPage: Boolean) {
        renderList(imageReviewItemList, isHasNextPage)
        activity?.let { it.bottomSheetImageReviewSlider?.onLoadDataSuccess(imageReviewItemList, isHasNextPage) }
    }

    override fun handleErrorResult(e: Throwable) {
        showGetListError(e)
        activity?.let { it.bottomSheetImageReviewSlider?.onLoadDataFailed() }
    }

    override fun onButtonBackPressed() {
        activity?.let { it.onBackPressed() }
    }

    override fun onRequestLoadMore(page: Int) {
        loadData(page)
    }

    override fun onSeeAllButtonClicked() {
        activity?.let {
            it.finish()
            ImageReviewGalleryActivity.moveTo(it, it.productId)
        }
    }

    override fun loadInitialData() {
        activity?.let { it.bottomSheetImageReviewSlider?.resetState() }
        super.loadInitialData()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.image_review_gallery_recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.image_review_gallery_swipe_refresh_layout
    }
}
