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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_product_detail_info.*
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoBottomSheet : BottomSheetUnify(), ProductDetailInfoListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: BsProductDetailInfoViewModel? = null

    private var productDetailComponent: ProductDetailComponent? = null
    private var rvBsProductDetail: RecyclerView? = null
    private var currentList: List<ProductDetailInfoVisitable>? = null
    private var listener: ProductDetailBottomSheetListener? = null

    companion object {
        const val PRODUCT_DETAIL_INFO_PARCEL_KEY = "parcelId"
        const val PRODUCT_DETAIL_BOTTOM_SHEET_KEY = "product detail info bs"
    }

    private val productDetailInfoAdapter by lazy {
        BsProductDetailInfoAdapter(AsyncDifferConfig.Builder(ProductDetailInfoDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build(), adapterTypeFactory)
    }

    private val adapterTypeFactory by lazy {
        ProductDetailInfoAdapterFactoryImpl(this)
    }

    fun show(childFragmentManager: FragmentManager, daggerProductDetailComponent: ProductDetailComponent?, listener: ProductDetailBottomSheetListener) {
        this.productDetailComponent = daggerProductDetailComponent
        this.listener = listener

        show(childFragmentManager, PRODUCT_DETAIL_BOTTOM_SHEET_KEY)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    override fun goToDiscussion(discussionCount: Int) {
        if (discussionCount > 0) {
            listener?.goToTalkReadingBottomSheet()
        } else {
            listener?.onDiscussionSendQuestionBottomSheetClicked()
        }
    }

    private fun setupFullScreen(data: List<ProductDetailInfoVisitable>) {
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
                bs_product_info_container?.setPadding(0, 0, 0, 20.dpToPx(displayMetrics))
                bs_product_info_container?.layoutParams?.height = height - bottomSheetHeader.height - (bottomSheetHeader.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin - bottomSheetWrapper.paddingTop
            } else {
                bs_product_info_container?.setPadding(0, 0, 0, 6.dpToPx(displayMetrics))
                bs_product_info_container?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        } catch (e: Throwable) {
        }
    }

    override fun goToApplink(url: String) {
        val categoryApplink: String = context?.getString(R.string.pdp_category_applink) ?: ""

        when {
            url.startsWith(categoryApplink) -> {
                onCategoryClicked(url)
            }
            else -> {
                val uriLink = Uri.parse(url).pathSegments

                if (uriLink.size >= 2 && uriLink[1] == "etalase") {
                    onEtalaseClicked()
                } else {
                    RouteManager.route(context, url)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailComponent?.inject(this)
        super.onCreate(savedInstanceState)
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(BsProductDetailInfoViewModel::class.java)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        getDataParcel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productDetailInfoAdapter.submitList(listOf(ProductDetailInfoLoadingDataModel()))
        view.post {
            if (!::viewModelFactory.isInitialized && isVisible) {
                dismiss()
            }
        }
    }

    override fun onBranchLinkClicked(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra(RouteManager.BRANCH, url)
            intent.putExtra(RouteManager.BRANCH_FORCE_NEW_SESSION, true)
            startActivity(intent)
        }
    }

    override fun goToVideoPlayer(url: List<String>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, url, index))
            } else {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + url[index])));
                } catch (e: Throwable) {
                    Timber.d(e)
                }
            }
        }
    }

    override fun goToShopNotes(title: String, date: String, desc: String) {
        context?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onShopNotesClicked(listener?.getPdpDataSource(), userSession.userId
                    ?: "", title)
            val bsShopNotes = ProductDetailBottomSheetBuilder.getShopNotesBottomSheet(it, date, desc, title)
            bsShopNotes.show(childFragmentManager, "shopNotes")
        }
    }

    override fun goToSpecification(annotation: List<ProductDetailInfoContent>) {
        DynamicProductDetailTracking.ProductDetailSheet.onSpecificationClick(listener?.getPdpDataSource(), userSession.userId
                ?: "")
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

    private fun observeData() {
        if (viewModel != null) {
            viewLifecycleOwner.observe(viewModel!!.bottomSheetDetailData) { data ->
                data.doSuccessOrFail({
                    currentList = ArrayList(it.data)
                    rvBsProductDetail?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            rvBsProductDetail?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                            setupFullScreen(it.data)
                        }
                    })
                    productDetailInfoAdapter.submitList(it.data)
                }) {
                }
            }
        }
    }

    private fun initView() {
        setTitle(getString(R.string.merchant_product_detail_label_product_detail))
        val childView = View.inflate(requireContext(), R.layout.bottom_sheet_product_detail_info, null)
        setupRecyclerView(childView)
        setChild(childView)
        clearContentPadding = true

        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    private fun setupRecyclerView(childView: View) {
        rvBsProductDetail = childView.findViewById(R.id.bs_product_info_rv)

        rvBsProductDetail?.layoutManager = object : LinearLayoutManager(context) {
            override fun requestChildRectangleOnScreen(parent: RecyclerView, child: View, rect: Rect, immediate: Boolean, focusedChildVisible: Boolean): Boolean {
                return false
            }
        }

        rvBsProductDetail?.itemAnimator = null
        rvBsProductDetail?.adapter = productDetailInfoAdapter
        rvBsProductDetail?.isNestedScrollingEnabled = false
    }

    private fun onVariantGuideLineBottomSheetClicked(url: String) {
        activity?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onVariantGuideLineBottomSheetClicked(listener?.getPdpDataSource(), userSession.userId
                    ?: "")
            startActivity(ImagePreviewActivity.getCallingIntent(it, arrayListOf(url)))
        }
    }

    private fun onCategoryClicked(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            DynamicProductDetailTracking.ProductDetailSheet.onCategoryBottomSheetClicked(listener?.getPdpDataSource(), userSession.userId
                    ?: "")
            RouteManager.route(context, url)
        }
    }

    private fun onEtalaseClicked() {
        val etalaseId = listener?.getPdpDataSource()?.basic?.menu?.id ?: ""
        val shopId = listener?.getPdpDataSource()?.basic?.shopID ?: ""

        val intent = RouteManager.getIntent(context, if (etalaseId.isNotEmpty()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopId, etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopId)
        })

        DynamicProductDetailTracking.ProductDetailSheet.onEtalaseBottomSheetClicked(listener?.getPdpDataSource(), userSession.userId
                ?: "")
        startActivity(intent)
    }

}

interface ProductDetailBottomSheetListener {
    fun goToTalkReadingBottomSheet()
    fun onDiscussionSendQuestionBottomSheetClicked()
    fun getPdpDataSource(): DynamicProductInfoP1?
}