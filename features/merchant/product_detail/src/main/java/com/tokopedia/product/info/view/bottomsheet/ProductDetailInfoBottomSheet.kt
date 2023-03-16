package com.tokopedia.product.info.view.bottomsheet

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductEducationalHelper
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.databinding.BottomSheetProductDetailInfoBinding
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.tracking.ProductDetailBottomSheetTracking
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.util.getIntentImagePreviewWithoutDownloadButton
import com.tokopedia.product.info.data.response.ShopNotesData
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoVisitable
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoBottomSheet : BottomSheetUnify(), ProductDetailInfoListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: BsProductDetailInfoViewModel? = null

    private var productDetailComponent: ProductDetailComponent? = null
    private var currentList: List<ProductDetailInfoVisitable>? = null
    private var listener: ProductDetailBottomSheetListener? = null

    private var _binding by autoClearedNullable<BottomSheetProductDetailInfoBinding>()
    private val binding get() = _binding!!

    companion object {
        const val PRODUCT_DETAIL_INFO_PARCEL_KEY = "parcelId"
        const val PRODUCT_DETAIL_BOTTOM_SHEET_KEY = "product detail info bs"
    }

    private val productDetailInfoAdapter by lazy {
        BsProductDetailInfoAdapter(
            differ = AsyncDifferConfig.Builder(ProductDetailInfoDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build(),
            adapterTypeFactory = adapterTypeFactory
        )
    }

    private val adapterTypeFactory by lazy {
        ProductDetailInfoAdapterFactoryImpl(this)
    }

    fun setup(
        daggerProductDetailComponent: ProductDetailComponent?,
        listener: ProductDetailBottomSheetListener
    ) {
        this.productDetailComponent = daggerProductDetailComponent
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailComponent?.inject(this)
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        /**
         * fix crash when open bottom sheet in case of don't keep activity from developer setting
         * caused by lateinit when don't keep activity which is initialized in bottom-sheet not created
         */
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(BsProductDetailInfoViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetProductDetailInfoBinding.inflate(layoutInflater)
        initView()
        getDataParcel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * if [viewModelFactory] still un-initialized until onViewCreated
         * so dismiss this view
         */
        view.post {
            if (!::viewModelFactory.isInitialized && isVisible) {
                dismiss()
            }
        }
    }

    private fun getDataParcel() {
        arguments?.let {
            context?.let { ctx ->
                val cacheId = it.getString(PRODUCT_DETAIL_INFO_PARCEL_KEY)
                val cacheManager = SaveInstanceCacheManager(ctx, cacheId)
                val parcelData: ProductInfoParcelData = cacheManager.get(
                    this::class.java.simpleName,
                    ProductInfoParcelData::class.java
                ) ?: ProductInfoParcelData()

                viewModel?.setParams(parcelData)
            }
        }
    }

    private fun observeData() = viewModel?.apply {
        viewLifecycleOwner.observe(bottomSheetDetailData) { data ->
            data.doSuccessOrFail({
                currentList = ArrayList(it.data)
                val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.bsProductInfoRv.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                        setupFullScreen(it.data)
                    }
                }
                binding.bsProductInfoRv.viewTreeObserver?.addOnGlobalLayoutListener(layoutListener)
                productDetailInfoAdapter.submitList(it.data)
            }) {
            }
        }

        viewLifecycleOwner.observe(bottomSheetTitle) {
            val title = it.ifBlank {
                getString(R.string.merchant_product_detail_label_product_detail)
            }
            setTitle(newTitle = title)
        }
    }

    private fun initView() {
        setChild(binding.root)
        setupRecyclerView()

        clearContentPadding = true

        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            observeData()
        }
    }

    private fun setupRecyclerView() = with(binding.bsProductInfoRv) {
        layoutManager = object : LinearLayoutManager(context) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return false
            }
        }

        itemAnimator = null
        adapter = productDetailInfoAdapter
        isNestedScrollingEnabled = false
    }

    override fun goToDiscussion(discussionCount: Int) {
        if (discussionCount > 0) {
            listener?.goToTalkReadingBottomSheet()
        } else {
            listener?.onDiscussionSendQuestionBottomSheetClicked()
        }
    }

    private fun setupFullScreen(data: List<ProductDetailInfoVisitable>) = with(binding) {
        var isFullScreen = false
        data.forEach {
            if (it is ProductDetailInfoExpandableImageDataModel || it is ProductDetailInfoExpandableDataModel || it is ProductDetailInfoExpandableListDataModel) {
                isFullScreen = true
                return@forEach
            }
        }

        try {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels

            if (isFullScreen) {
                bsProductInfoContainer.setPadding(0, 0, 0, 20.dpToPx(displayMetrics))
                bsProductInfoContainer.layoutParams?.height =
                    height - bottomSheetHeader.height - (bottomSheetHeader.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin - bottomSheetWrapper.paddingTop
            } else {
                bsProductInfoContainer.setPadding(0, 0, 0, 6.dpToPx(displayMetrics))
                bsProductInfoContainer.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            bsProductInfoContainer.requestLayout()
        } catch (_: Throwable) {
        }
    }

    override fun goToCatalog(url: String, catalogName: String) {
        DynamicProductDetailTracking.ProductDetailSheet.onCatalogBottomSheetClicked(
            productInfo = listener?.getPdpDataSource(),
            userId = userSession.userId.orEmpty(),
            catalogName = catalogName
        )
        goToApplink(url)
    }

    override fun goToCategory(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            DynamicProductDetailTracking.ProductDetailSheet.onCategoryBottomSheetClicked(
                productInfo = listener?.getPdpDataSource(),
                userId = userSession.userId.orEmpty()
            )
            goToApplink(url)
        }
    }

    override fun goToEtalase(url: String) {
        DynamicProductDetailTracking.ProductDetailSheet.onEtalaseBottomSheetClicked(
            productInfo = listener?.getPdpDataSource(),
            userId = userSession.userId.orEmpty()
        )
        goToApplink(url)
    }

    override fun goToApplink(url: String) {
        RouteManager.route(context, url)
    }

    override fun goToEducational(url: String, infoTitle: String, infoValue: String, position: Int) {
        val context = context ?: return
        val data = listener?.getPdpDataSource() ?: return

        ProductEducationalHelper.goToEducationalBottomSheet(
            context = context,
            url = url,
            productId = data.basic.productID,
            shopId = data.basic.shopID
        )

        ProductDetailBottomSheetTracking.clickInfoItem(
            productInfo = data,
            userId = userSession.userId,
            infoTitle = infoTitle,
            infoValue = infoValue,
            position = position
        )
    }

    override fun onBranchLinkClicked(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getSplashScreenIntent(activity)
            intent.putExtra(RouteManager.BRANCH, url)
            intent.putExtra(RouteManager.BRANCH_FORCE_NEW_SESSION, true)
            startActivity(intent)
        }
    }

    override fun goToVideoPlayer(url: List<String>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                == YouTubeInitializationResult.SUCCESS
            ) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, url, index))
            } else {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + url[index])
                        )
                    )
                } catch (e: Throwable) {
                    Timber.d(e)
                }
            }
        }
    }

    override fun goToShopNotes(shopNotesData: ShopNotesData) {
        context?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onShopNotesClicked(
                productInfo = listener?.getPdpDataSource(),
                userId = userSession.userId.orEmpty(),
                shopNotesTitle = shopNotesData.title
            )
            val bsShopNotes = ProductDetailBottomSheetBuilder.getShopNotesBottomSheet(
                context = it,
                shopNotesData = shopNotesData
            )
            bsShopNotes.show(childFragmentManager, "shopNotes")
        }
    }

    override fun goToSpecification(annotation: List<ProductDetailInfoContent>) {
        DynamicProductDetailTracking.ProductDetailSheet.onSpecificationClick(
            productInfo = listener?.getPdpDataSource(),
            userId = userSession.userId.orEmpty()
        )
        showImmediately(childFragmentManager, "specBs") {
            ProductAnnotationBottomSheet.create(
                title = bottomSheetTitle.text.toString(),
                annotation = annotation
            )
        }
    }

    override fun goToImagePreview(url: String) {
        onVariantGuideLineBottomSheetClicked(url)
    }

    override fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean) {
        productDetailInfoAdapter.closeAllExpanded(
            uniqueIdentifier = uniqueIdentifier,
            toggle = toggle,
            currentData = currentList.orEmpty()
        )
    }

    override fun onCustomInfoClicked(url: String) {
        DynamicProductDetailTracking.ProductDetailSheet.onCustomInfoPalugadaBottomSheetClicked()
        goToApplink(url)
    }

    private fun onVariantGuideLineBottomSheetClicked(url: String) {
        activity?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onVariantGuideLineBottomSheetClicked(
                listener?.getPdpDataSource(), userSession.userId.orEmpty()
            )
            startActivity(getIntentImagePreviewWithoutDownloadButton(it, arrayListOf(url)))
        }
    }

    override fun onImpressInfo(infoTitle: String, infoValue: String, position: Int) {
        val data = listener?.getPdpDataSource() ?: return

        ProductDetailBottomSheetTracking.impressInfoItem(
            p1Data = data,
            userId = userSession.userId,
            infoTitle = infoTitle,
            infoValue = infoValue,
            position = position,
            trackingQueue = trackingQueue
        )
    }

    override fun onImpressCatalog(key: String, value: String, position: Int) {
        val data = listener?.getPdpDataSource() ?: return

        ProductDetailBottomSheetTracking.impressSpecification(
            p1Data = data,
            userId = userSession.userId,
            key = key,
            value = value,
            position = position,
            trackingQueue = trackingQueue
        )
    }

    override fun onPause() {
        if (this::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
        super.onPause()
    }
}

interface ProductDetailBottomSheetListener {
    fun goToTalkReadingBottomSheet()
    fun onDiscussionSendQuestionBottomSheetClicked()
    fun getPdpDataSource(): DynamicProductInfoP1?
}
