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
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.databinding.BottomSheetProductDetailInfoBinding
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.tracking.ProductDetailBottomSheetTracking
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.util.getIntentImagePreviewWithoutDownloadButton
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.concurrent.Executors
import javax.inject.Inject
import timber.log.Timber

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
    private val viewModel: BsProductDetailInfoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BsProductDetailInfoViewModel::class.java)
    }

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetProductDetailInfoBinding.inflate(layoutInflater)
        getDataParcel()
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
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

                viewModel.setParams(parcelData)
            }
        }
    }

    private fun observeData() = with(viewModel) {
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
        } catch (_: Throwable) {
        }
    }

    override fun goToCatalog(url: String, catalogName: String) {
        DynamicProductDetailTracking.ProductDetailSheet.onCatalogBottomSheetClicked(
            listener?.getPdpDataSource(), userSession.userId.orEmpty(), catalogName
        )
        goToApplink(url)
    }

    override fun goToCategory(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            DynamicProductDetailTracking.ProductDetailSheet.onCategoryBottomSheetClicked(
                listener?.getPdpDataSource(), userSession.userId.orEmpty()
            )
            goToApplink(url)
        }
    }

    override fun goToEtalase(url: String) {
        DynamicProductDetailTracking.ProductDetailSheet.onEtalaseBottomSheetClicked(
            listener?.getPdpDataSource(), userSession.userId.orEmpty()
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
            context,
            url,
            data.basic.productID,
            data.basic.shopID
        )

        ProductDetailBottomSheetTracking.clickInfoItem(
            data,
            userSession.userId,
            infoTitle,
            infoValue,
            position
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

    override fun goToShopNotes(title: String, date: String, desc: String) {
        context?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onShopNotesClicked(
                listener?.getPdpDataSource(),
                userSession.userId.orEmpty(),
                title
            )
            val bsShopNotes =
                ProductDetailBottomSheetBuilder.getShopNotesBottomSheet(it, date, desc, title)
            bsShopNotes.show(childFragmentManager, "shopNotes")
        }
    }

    override fun goToSpecification(annotation: List<ProductDetailInfoContent>) {
        DynamicProductDetailTracking.ProductDetailSheet.onSpecificationClick(
            listener?.getPdpDataSource(),
            userSession.userId.orEmpty()
        )
        val bs = ProductAnnotationBottomSheet()
        bs.getData(annotation)
        bs.show(childFragmentManager, "specBs")
    }

    override fun goToImagePreview(url: String) {
        onVariantGuideLineBottomSheetClicked(url)
    }

    override fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean) {
        productDetailInfoAdapter.closeAllExpanded(uniqueIdentifier, toggle, currentList ?: listOf())
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
            data,
            userSession.userId,
            infoTitle,
            infoValue,
            position,
            trackingQueue
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