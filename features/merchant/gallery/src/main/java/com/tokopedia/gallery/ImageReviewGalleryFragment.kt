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
import com.tokopedia.gallery.networkmodel.ProductrevGetReviewImage
import com.tokopedia.gallery.networkmodel.ReviewDetail
import com.tokopedia.gallery.networkmodel.ReviewGalleryImage
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
            if (shouldGoToNewGallery())  {
                RouteManager.route(context, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, it.productId)
            } else {
                ImageReviewGalleryActivity.moveTo(it, it.productId)
            }
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

    private fun shouldGoToNewGallery(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.EXPERIMENT_NAME_REVIEW_PRODUCT_READING, RollenceKey.VARIANT_OLD_REVIEW_PRODUCT_READING
            ) == RollenceKey.VARIANT_NEW_REVIEW_PRODUCT_READING
        } catch (e: Exception) {
            false
        }
    }

    private fun observeReviewImages() {
        viewModel.reviewImages.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> handleItemResult(convertNetworkResponseToImageReviewItemList(it.data), it.data.hasNext)
                is Fail -> handleErrorResult(it.throwable)
            }
        })
    }

    private fun convertNetworkResponseToImageReviewItemList(gqlResponse: ProductrevGetReviewImage): List<ImageReviewItem> {
        val reviewMap = HashMap<String, ReviewDetail>()
        val imageMap = HashMap<String, ReviewGalleryImage>()

        gqlResponse.detail.reviewGalleryImages.map {
            imageMap[it.attachmentId] = it
        }

        gqlResponse.detail.reviewDetail.map {
            reviewMap[it.feedbackId] = it
        }

        return gqlResponse.reviewImages.map {
            val image = imageMap[it.imageId]
            val review = reviewMap[it.feedbackId]
            ImageReviewItem(
                it.feedbackId,
                review?.createTimestamp ?: "",
                review?.user?.fullName ?: "",
                image?.thumbnailURL ?: "",
                image?.fullsizeURL ?: "",
                review?.rating ?: 0
            )
        }
    }
}
