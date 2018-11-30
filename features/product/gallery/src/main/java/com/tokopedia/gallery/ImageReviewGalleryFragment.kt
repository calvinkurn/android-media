package com.tokopedia.gallery

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.gallery.adapter.GalleryAdapter
import com.tokopedia.gallery.adapter.TypeFactory
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider
import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.domain.GraphqlUseCase

import java.util.ArrayList

class ImageReviewGalleryFragment : BaseListFragment<ImageReviewItem, TypeFactory>(), BottomSheetImageReviewSlider.Callback, GalleryView {

    private var presenter: ReviewGalleryPresenter? = null
    private var activity: ImageReviewGalleryActivity? = null

    override val isAllowLoadMore: Boolean
        get() = !adapter.isLoading

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = getActivity() as ImageReviewGalleryActivity?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        activity!!.bottomSheetImageReviewSlider!!.setup(this)
        if (activity!!.isImageListPreloaded) {
            activity!!.bottomSheetImageReviewSlider!!.displayImage(activity!!.defaultPosition)
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun loadData(page: Int) {

        if (activity!!.isImageListPreloaded) {
            val imageUrlList = activity!!.imageUrlList
            handleItemResult(convertToImageReviewItemList(imageUrlList!!), false)
            return
        }

        activity!!.bottomSheetImageReviewSlider!!.onLoadingData()
        presenter!!.loadData(activity!!.productId, page)
    }

    private fun convertToImageReviewItemList(imageUrlList: ArrayList<String>): List<ImageReviewItem> {
        val imageReviewItemList = ArrayList<ImageReviewItem>()
        for (imageUrl in imageUrlList) {
            val imageReviewItem = ImageReviewItem()
            imageReviewItem.imageUrlThumbnail = imageUrl
            imageReviewItem.imageUrlLarge = imageUrl
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
        presenter = ReviewGalleryPresenterImpl(
                GetImageReviewUseCase(context, GraphqlUseCase()),
                this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onGalleryItemClicked(position: Int) {
        activity!!.bottomSheetImageReviewSlider!!.displayImage(position)
        ImageReviewGalleryTracking.eventClickReviewGalleryItem(getActivity()!!,
                Integer.toString(activity!!.productId))
    }

    override fun handleItemResult(imageReviewItemList: List<ImageReviewItem>, isHasNextPage: Boolean) {
        renderList(imageReviewItemList, isHasNextPage)
        activity!!.bottomSheetImageReviewSlider!!.onLoadDataSuccess(imageReviewItemList, isHasNextPage)
    }

    override fun handleErrorResult(e: Throwable) {
        showGetListError(e)
        activity!!.bottomSheetImageReviewSlider!!.onLoadDataFailed()
    }

    override fun onButtonBackPressed() {
        activity!!.onBackPressed()
    }

    override fun onRequestLoadMore(page: Int) {
        loadData(page)
    }

    override fun loadInitialData() {
        activity!!.bottomSheetImageReviewSlider!!.resetState()
        super.loadInitialData()
    }

    companion object {

        fun createInstance(): Fragment {
            return ImageReviewGalleryFragment()
        }
    }
}
