package com.tokopedia.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gallery.adapter.GalleryAdapter
import com.tokopedia.gallery.adapter.TypeFactory
import com.tokopedia.gallery.customview.BottomSheetImageReviewSliderCallback
import com.tokopedia.gallery.di.DaggerGalleryComponent
import com.tokopedia.gallery.di.GalleryComponent
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking
import com.tokopedia.gallery.viewmodel.GalleryViewModel
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class ImageReviewGalleryFragment : BaseListFragment<ImageReviewItem, TypeFactory>(),
    BottomSheetImageReviewSliderCallback, GalleryView, HasComponent<GalleryComponent> {

    companion object {
        fun createInstance(): Fragment {
            return ImageReviewGalleryFragment()
        }
    }

    @Inject
    lateinit var viewModel: GalleryViewModel

    private var activity: ImageReviewGalleryActivity? = null

    override val isAllowLoadMore: Boolean
        get() = !adapter.isLoading

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as ImageReviewGalleryActivity?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        observeReviewImages()
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
            viewModel.setPage(it.productId, page)
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

    override fun getComponent(): GalleryComponent? {
        return activity?.run {
            DaggerGalleryComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
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

    override fun handleItemResult(
        imageReviewItemList: List<ImageReviewItem>,
        isHasNextPage: Boolean
    ) {
        renderList(imageReviewItemList, isHasNextPage)
        activity?.let {
            it.bottomSheetImageReviewSlider?.onLoadDataSuccess(
                imageReviewItemList,
                isHasNextPage
            )
        }
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
            RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY,
                it.productId
            )
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

    private fun observeReviewImages() {
        viewModel.reviewImages.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> handleItemResult(it.data.reviewItems, it.data.hasNext)
                is Fail -> handleErrorResult(it.throwable)
            }
        })
    }
}
