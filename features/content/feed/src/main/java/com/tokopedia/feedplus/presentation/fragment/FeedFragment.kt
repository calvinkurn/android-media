package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedShareDataModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
@Suppress("DEPRECATION")
class FeedFragment :
    BaseDaggerFragment(),
    FeedListener,
    ContentThreeDotsMenuBottomSheet.Listener,
    ProductItemInfoBottomSheet.Listener,
    ShareBottomsheetListener {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dissmisByGreyArea = true
    private var shareData: LinkerData? = null

    private var isInClearViewMode: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    private val feedMainViewModel: FeedMainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val feedPostViewModel: FeedPostViewModel by viewModels { viewModelFactory }

    private val reportPostLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val feedMenuSheet =
                childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet
            if (feedMenuSheet != null && userSession.isLoggedIn) {
                feedMenuSheet.showReportLayoutWhenLaporkanClicked()
                feedMenuSheet.showToasterOnLoginSuccessFollow(
                    getString(
                        feedR.string.feed_report_login_success_toaster_text,
                        userSession.name
                    ),
                    Toaster.TYPE_NORMAL
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        } ?: savedInstanceState?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(ARGUMENT_DATA, data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedImmersiveBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedPostViewModel.fetchFeedPosts(data?.type ?: "")

        initView()
        observeClearViewData()
        observePostData()
        observeAddToCart()
        observeReport()
        observeFollow()
    }

    override fun onDestroyView() {
        binding = null
        (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOMSHEET) as? ProductItemInfoBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.dismiss()
        super.onDestroyView()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onMenuClicked(model: FeedCardImageContentModel) {
        activity?.let {
            val feedMenuSheet =
                ContentThreeDotsMenuBottomSheet.getFragment(
                    childFragmentManager,
                    it.classLoader,
                    TAG_FEED_MENU_BOTTOMSHEET
                )
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(getMenuItemData(), model.id)
            feedMenuSheet.show(childFragmentManager, TAG_FEED_MENU_BOTTOMSHEET)
        }
    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.LAPORKAN -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.showReportLayoutWhenLaporkanClicked()
                }
            }

            FeedMenuIdentifier.MODE_NONTON -> {
                feedMainViewModel.toggleClearView(true)
            }
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel) {
        feedMainViewModel.reportContent(feedReportRequestParamModel)
    }

    override fun disableClearView() {
        feedMainViewModel.toggleClearView(false)
    }

    override fun inClearViewMode(): Boolean = isInClearViewMode

    override fun onSharePostClicked(model: FeedCardImageContentModel) {
        activity?.let {
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(model.id)
                .setName(
                    String.format(
                        getString(feedR.string.feed_share_title),
                        model.author.name
                    )
                )
                .setDescription(
                    String.format(
                        getString(feedR.string.feed_share_desc_text),
                        model.author.name
                    )
                )
                .setDesktopUrl(model.weblink)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(model.media.firstOrNull()?.mediaUrl ?: "")
                .setDeepLink(model.applink)
                .setUri(model.weblink)

            shareData = shareDataBuilder.build()
            showUniversalShareBottomSheet(getFeedShareDataModel(model))
        }
    }

    override fun onFollowClicked(id: String, isShop: Boolean) {
        feedPostViewModel.doFollow(id, isShop)
    }

    override fun changeTab(type: String) {
        feedMainViewModel.changeCurrentTabByType(type)
    }

    override fun onProductTagItemClicked(model: FeedCardImageContentModel) {
        openProductTagBottomSheet(model)
    }

    override fun onProductTagViewClicked(model: FeedCardImageContentModel) {
        val numberOfTaggedProducts = model.totalProducts
        val productData =
            if (model.isTypeProductHighlight) model.products else model.tags

        if (numberOfTaggedProducts == 1) {
            val appLink = productData.firstOrNull()?.applink
            if (appLink?.isNotEmpty() == true) {
                activity?.let {
                    RouteManager.route(it, appLink)
                }
            }
        } else {
            openProductTagBottomSheet(model)
        }
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagModelNew,
        context: Context,
        shopId: String
    ) {
//        TODO("Not yet implemented")
    }

    override fun onTaggedProductCardImpressed(
        activityId: String,
        postTagItemList: List<FeedXProduct>,
        type: String,
        shopId: String,
        isFollowed: Boolean,
        mediaType: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        // To be used for analytics
    }

    override fun onTaggedProductCardClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int,
        mediaType: String
    ) {
        RouteManager.route(requireContext(), redirectUrl)
    }

    override fun onAddToCartButtonClicked(item: ProductPostTagModelNew) {
        feedPostViewModel.addToCart(
            productId = item.product.id,
            productName = item.product.name,
            price = item.price,
            shopId = item.shopId
        )
    }

    override fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int) {
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        dissmisByGreyArea = false
        val universalShareBottomSheet =
            (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)
        universalShareBottomSheet?.dismiss()

        val linkerShareData = DataMapper().getLinkerShareData(shareData)
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        context?.let {
                            val shareString =
                                if (shareData?.description?.contains("%s") == true) {
                                    shareData?.description?.let { it1 ->
                                        String.format(
                                            it1,
                                            linkerShareData?.shareUri ?: ""
                                        )
                                    }
                                } else {
                                    shareData?.description + "\n" + linkerShareData?.shareUri
                                }

                            if (shareString != null) {
                                SharingUtil.executeShareIntent(
                                    shareModel,
                                    linkerShareData,
                                    activity,
                                    view,
                                    shareString
                                )
                            }

                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        // Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        dissmisByGreyArea = false
    }

    private fun observeAddToCart() {
        feedPostViewModel.atcRespData.observe(
            viewLifecycleOwner
        ) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOMSHEET) as? ProductItemInfoBottomSheet)
            when (it) {
                is FeedResult.Success -> {
                    productBottomSheet?.showToastWithAction(
                        getString(feedR.string.feeds_add_to_cart_success_text),
                        Toaster.TYPE_NORMAL,
                        getString(feedR.string.feeds_add_to_cart_toaster_action_text)
                    ) {
                        moveToAddToCartPage()
                    }
                }
                is FeedResult.Failure -> {
                    val errorMessage =
                        it.error.message
                    if (errorMessage != null) {
                        productBottomSheet?.showToasterOnBottomSheetOnSuccessFollow(
                            errorMessage,
                            Toaster.TYPE_ERROR
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private fun observeReport() {
        feedMainViewModel.reportResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.apply {
                        setFinalView()
                    }
                }
                is Fail -> {}
            }
        }
    }

    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory(this))
            if (adapter!!.itemCount == 0) {
                adapter?.showLoading()
            }

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        layoutManager!!.findLastVisibleItemPosition() >= (adapter!!.itemCount - MINIMUM_ENDLESS_CALL)
                    ) {
                        feedPostViewModel.fetchFeedPosts(data?.type ?: "")
                    }
                }
            })
        }
    }

    private fun observeClearViewData() {
        feedMainViewModel.isInClearView.observe(viewLifecycleOwner) {
            isInClearViewMode = it
            adapter?.onToggleClearView()
        }
    }

    private fun observePostData() {
        feedPostViewModel.feedHome.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    adapter?.hideLoading()
                    if (it.data.items.isEmpty()) {
                        adapter?.addElement(FeedNoContentModel())
                    } else {
                        adapter?.addElement(it.data.items)
                    }
                }
                is Fail -> {}
            }
        }
    }

    private fun observeFollow() {
        feedPostViewModel.followResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    adapter?.updateList(
                        feedPostViewModel.updateFollowStatus(
                            adapter?.list ?: emptyList(),
                            it.data.id,
                            it.data.isFollowing
                        )
                    )
                }
                is Fail -> {}
            }
        }
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            reportPostLoginResult.launch(intent)
        }
    }

    private fun getFeedShareDataModel(model: FeedCardImageContentModel): FeedShareDataModel {
        val mediaUrl =
            if (model.isTypeProductHighlight) {
                model.products.firstOrNull()?.coverUrl ?: ""
            } else {
                model.media.firstOrNull()?.mediaUrl ?: ""
            }

        return FeedShareDataModel(
            id = model.id,
            name = model.author.name,
            tnTitle = (
                String.format(
                    context?.getString(feedR.string.feed_share_title) ?: "",
                    model.author.name
                )
                ),
            tnImage = mediaUrl,
            ogUrl = mediaUrl
        )
    }

    private fun showUniversalShareBottomSheet(shareModel: FeedShareDataModel) {
        val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@FeedFragment)
            setUtmCampaignData(
                pageName = FeedShareDataModel.PAGE,
                userId = userSession.userId,
                pageId = shareModel.id,
                feature = FeedShareDataModel.FEATURE
            )

            setMetaData(
                tnTitle = shareModel.tnTitle,
                tnImage = shareModel.tnImage
            )
        }
        universalShareBottomSheet.setOnDismissListener {
            if (dissmisByGreyArea) {
                // TODO to be used for analytics
            } else {
                dissmisByGreyArea = true
            }
        }
        universalShareBottomSheet.let {
            if (!it.isAdded) {
                it.show(childFragmentManager, this, null)
            }
        }
    }

    private fun getMenuItemData(): List<FeedMenuItem> {
        val items = arrayListOf<FeedMenuItem>()

        items.add(
            FeedMenuItem(
                drawable = getIconUnifyDrawable(requireContext(), IconUnify.VISIBILITY),
                name = getString(feedR.string.feed_clear_mode),
                type = FeedMenuIdentifier.MODE_NONTON
            )
        )
        items.add(
            FeedMenuItem(
                drawable = getIconUnifyDrawable(
                    requireContext(),
                    IconUnify.WARNING,
                    MethodChecker.getColor(
                        context,
                        unifyR.color.Unify_RN500
                    )
                ),
                name = getString(feedR.string.feed_report),
                type = FeedMenuIdentifier.LAPORKAN
            )
        )
        return items
    }

    private fun openProductTagBottomSheet(feedXCard: FeedCardImageContentModel) {
        val productBottomSheet = ProductItemInfoBottomSheet()
        productBottomSheet.show(
            childFragmentManager,
            this,
            ProductBottomSheetData(
                postId = feedXCard.id,
                shopId = feedXCard.author.id,
                postType = feedXCard.typename,
                isFollowed = feedXCard.followers.isFollowed,
                shopName = feedXCard.author.name,
                saleStatus = feedXCard.campaign.status,
                saleType = feedXCard.campaign.name,
                hasVoucher = feedXCard.hasVoucher,
                authorType = feedXCard.author.type.toString()
            ),
            viewModelFactory = viewModelFactory,
            tag = TAG_FEED_PRODUCT_BOTTOMSHEET
        )
    }

    private fun moveToAddToCartPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"

        private const val MINIMUM_ENDLESS_CALL = 1

        private const val TAG_FEED_MENU_BOTTOMSHEET = "TAG_FEED_MENU_BOTTOMSHEET"
        private const val TAG_FEED_PRODUCT_BOTTOMSHEET = "TAG_FEED_PRODUCT_BOTTOMSHEET"

        fun createFeedFragment(data: FeedDataModel): FeedFragment = FeedFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA, data)
            }
        }
    }
}
