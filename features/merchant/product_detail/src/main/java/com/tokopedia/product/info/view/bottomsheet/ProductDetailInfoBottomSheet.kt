package com.tokopedia.product.info.view.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.model.specification.Specification
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoBottomSheet : BottomSheetUnify(), ProductDetailInfoListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: BsProductDetailInfoViewModel? = null

    private var productDetailComponent: ProductDetailComponent? = null
    private var rvBsProductDetail: RecyclerView? = null
    private var currentList: List<ProductDetailInfoVisitable>? = null
    private var listener: ProductDetailBottomSheetListener? = null

    private val productDetailInfoAdapter by lazy {
        BsProductDetailInfoAdapter(AsyncDifferConfig.Builder(ProductDetailInfoDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build(), adapterTypeFactory)
    }

    private val adapterTypeFactory by lazy {
        ProductDetailInfoAdapterFactoryImpl(this)
    }

    fun setDaggerComponent(daggerProductDetailComponent: ProductDetailComponent?, listener: ProductDetailBottomSheetListener) {
        this.productDetailComponent = daggerProductDetailComponent
        this.listener = listener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel?.let { vm ->
            viewLifecycleOwner.observe(vm.bottomSheetDetailData) { data ->
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

    override fun goToDiscussion(discussionCount: Int) {
        if (discussionCount > 0) {
            listener?.goToReadingActivity()
        } else {
            listener?.onDiscussionSendQuestionClicked(null)
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

        frameDialogView.layoutParams.height = if (isFullScreen) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        frameDialogView.requestLayout()
    }

    override fun goToApplink(url: String) {
        listener?.goToApplink(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailComponent?.inject(this)
        super.onCreate(savedInstanceState)
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(BsProductDetailInfoViewModel::class.java)
        }
        getDataParcel()
        initView()
    }

    private fun getDataParcel() {
        arguments?.let {
            context?.let { ctx ->
                val cacheId = it.getString("ParcelId")
                val cacheManager = SaveInstanceCacheManager(ctx, cacheId)
                val parcelData: ProductInfoParcelData = cacheManager.get(
                        this::class.java.simpleName,
                        ProductInfoParcelData::class.java
                ) ?: ProductInfoParcelData()

                viewModel?.setParams(parcelData)
            }
        }
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
                }
            }
        }
    }

    override fun goToShopNotes(title: String, date: String, desc: String) {
        context?.let {
            val bsShopNotes = ProductDetailBottomSheetBuilder.getShopNotesBottomSheet(it, date, desc, title)
            bsShopNotes.show(childFragmentManager, "shopNotes")
        }
    }

    override fun goToSpecification(specification: List<Specification>) {
        val bs = ProductSpecificationBottomSheet()
        bs.getData(specification)
        bs.show(childFragmentManager, "specBs")
    }

    override fun goToImagePreview(url: String) {
        listener?.onVariantGuideLineClicked(url)
    }

    override fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean) {
        productDetailInfoAdapter.closeAllExpanded(uniqueIdentifier, toggle, currentList ?: listOf())
    }

    private fun initView() {
        setTitle(getString(R.string.merchant_product_detail_label_product_detail))
        setShowListener {
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        val childView = View.inflate(requireContext(), R.layout.bottom_sheet_product_detail_info, null)

        setupRecyclerView(childView)
        setChild(childView)
    }

    private fun setupRecyclerView(childView: View) {
        rvBsProductDetail = childView.findViewById(R.id.bs_product_info_rv)
        rvBsProductDetail?.itemAnimator = null
        rvBsProductDetail?.adapter = productDetailInfoAdapter
        rvBsProductDetail?.isNestedScrollingEnabled = false
    }

}

interface ProductDetailBottomSheetListener {
    fun goToReadingActivity()
    fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel?)
    fun onVariantGuideLineClicked(url: String)
    fun goToApplink(url: String)
}