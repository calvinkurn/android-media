package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_RELEVANT_POST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedShareDataModel
import com.tokopedia.feedplus.presentation.model.LikeFeedDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.VideoPlayerManager
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
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

    private var _binding: FragmentFeedImmersiveBinding? = null
    private val binding: FragmentFeedImmersiveBinding
        get() = _binding!!

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var dissmisByGreyArea = true
    private var shareData: LinkerData? = null

    private val videoPlayerManager by lazy { VideoPlayerManager(requireContext()) }

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
            }
        }

    private val followLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            feedPostViewModel.processSuspendedFollow()
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
    ): View {
        _binding = FragmentFeedImmersiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedPostViewModel.fetchFeedPosts(
            data?.type ?: "",
            isNewData = true,
            postId = arguments?.getString(UF_EXTRA_FEED_RELEVANT_POST)
        )

        initView()
        observePostData()
        observeAddToCart()
        observeReport()
        observeFollow()
        observeLikeContent()
    }

    override fun onDestroyView() {
        _binding = null
        (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOMSHEET) as? ProductItemInfoBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.dismiss()
        super.onDestroyView()

        videoPlayerManager.releaseAll()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onMenuClicked(id: String) {
        activity?.let {
            val feedMenuSheet =
                ContentThreeDotsMenuBottomSheet.getFragment(
                    childFragmentManager,
                    it.classLoader,
                    TAG_FEED_MENU_BOTTOMSHEET
                )
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(getMenuItemData(), id)
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
                adapter?.showClearView(binding.rvFeedPost.currentItem)
            }

            else -> {}
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel) {
        feedMainViewModel.reportContent(feedReportRequestParamModel)
    }

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        // implement bottomsheet close tracker
    }

    override fun onSharePostClicked(
        id: String,
        authorName: String,
        applink: String,
        weblink: String,
        imageUrl: String
    ) {
        activity?.let {
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(id)
                .setName(
                    String.format(
                        getString(feedR.string.feed_share_title),
                        authorName
                    )
                )
                .setDescription(
                    String.format(
                        getString(feedR.string.feed_share_desc_text),
                        authorName
                    )
                )
                .setDesktopUrl(weblink)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(imageUrl)
                .setDeepLink(applink)
                .setUri(weblink)

            shareData = shareDataBuilder.build()
            showUniversalShareBottomSheet(
                getFeedShareDataModel(
                    id,
                    authorName,
                    imageUrl
                )
            )
        }
    }

    override fun onFollowClicked(id: String, encryptedId: String, isShop: Boolean) {
        if (userSession.isLoggedIn) {
            feedPostViewModel.doFollow(id, encryptedId, isShop)
        } else {
            feedPostViewModel.suspendFollow(id, encryptedId, isShop)
            followLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }
    }

    override fun changeTab() {
        feedMainViewModel.changeCurrentTabByType(FeedBaseFragment.TAB_TYPE_FOR_YOU)
    }

    override fun reload() {
        feedPostViewModel.fetchFeedPosts(data?.type ?: "")
        adapter?.removeErrorNetwork()
        showLoading()
        adapter?.showLoading()
    }

    override fun getVideoPlayer(): FeedExoPlayer {
        return videoPlayerManager.occupy()
    }

    override fun detachPlayer(player: FeedExoPlayer) {
        videoPlayerManager.detach(player)
    }

    override fun onProductTagButtonClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean
    ) {
        openProductTagBottomSheet(
            postId = postId,
            author = author,
            postType = postType,
            isFollowing = isFollowing,
            campaign = campaign,
            hasVoucher = hasVoucher
        )
    }

    override fun onProductTagViewClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int
    ) {
        if (totalProducts == FeedProductTagView.PRODUCT_COUNT_ONE) {
            val appLink = products.firstOrNull()?.applink
            if (appLink?.isNotEmpty() == true) {
                activity?.let {
                    RouteManager.route(it, appLink)
                }
            }
        } else {
            openProductTagBottomSheet(
                postId = postId,
                author = author,
                postType = postType,
                isFollowing = isFollowing,
                campaign = campaign,
                hasVoucher = hasVoucher
            )
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

    override fun onReminderClicked(campaignId: Long, setReminder: Boolean) {
        feedPostViewModel.setUnsetReminder(campaignId, setReminder)
    }

    override fun onTimerFinishUpcoming() {
//        TODO("Not yet implemented")
    }

    override fun onTimerFinishOnGoing() {
//        TODO("Not yet implemented")
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
                    val errorMessage = it.error.message
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
        binding.let {
            it.swipeRefreshFeedLayout.setOnRefreshListener {
                feedPostViewModel.fetchFeedPosts(data?.type ?: "", isNewData = true)
            }

            adapter = FeedPostAdapter(
                FeedAdapterTypeFactory(this, binding.rvFeedPost)
            )
            if (adapter!!.itemCount == 0) {
                showLoading()
            }

            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.orientation = ViewPager2.ORIENTATION_VERTICAL
            it.rvFeedPost.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == SCROLL_STATE_IDLE && it.rvFeedPost.currentItem >= (adapter!!.itemCount - MINIMUM_ENDLESS_CALL)) {
                        feedPostViewModel.fetchFeedPosts(data?.type ?: "")
                    }
                }

                override fun onPageSelected(position: Int) {
                    if (position > ZERO) {
                        adapter?.notifyItemChanged(position - ONE, FEED_POST_NOT_SELECTED)
                    }
                    if (position < (adapter?.itemCount ?: 0)) {
                        adapter?.notifyItemChanged(position + ONE, FEED_POST_NOT_SELECTED)
                    }

                    adapter?.notifyItemChanged(position, FEED_POST_SELECTED)
                }
            })
        }
    }

    private fun observePostData() {
        feedPostViewModel.feedHome.observe(viewLifecycleOwner) {
            binding.swipeRefreshFeedLayout.isRefreshing = false
            when (it) {
                is Success -> {
                    hideLoading()
                    if (it.data.items.isEmpty()) {
                        adapter?.setElements(listOf(FeedNoContentModel()))
                    } else {
                        adapter?.updateList(it.data.items)
                    }
                    feedMainViewModel.onPostDataLoaded(it.data.items.isNotEmpty())
                }
                is Fail -> {
                    hideLoading()
                    adapter?.showErrorNetwork()
                }
            }
        }
    }

    private fun observeFollow() {
        feedPostViewModel.followResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedR.string.feed_message_success_follow, it.data),
                        Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToast(
                        getString(feedR.string.feed_message_failed_follow),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observeLikeContent() {
        feedPostViewModel.getLikeKolResp.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                FeedResult.Loading -> {}
                is FeedResult.Success -> {
                    onSuccessLikeResponse(it.data)
                }
                is FeedResult.Failure -> {
                    val error = it.error
                    val errorMessage = if (error is CustomUiMessageThrowable) {
                        requireContext().getString(error.errorMessageId)
                    } else {
                        ErrorHandler.getErrorMessage(requireContext(), it.error)
                    }
                    showToast(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            reportPostLoginResult.launch(intent)
        }
    }

    override fun onLikePostCLicked(id: String, isLiked: Boolean, rowNumber: Int) {
        val feedLikeAction = FeedLikeAction.getLikeAction(isLiked)
        feedPostViewModel.likeContent(
            contentId = id,
            action = feedLikeAction,
            rowNumber = rowNumber
        )
    }

    private fun onSuccessLikeResponse(data: LikeFeedDataModel) {
        val newList = adapter?.list
        val rowNumber = data.rowNumber
        if ((newList?.size ?: 0) > data.rowNumber) {
            val item = newList?.get(rowNumber)
            if (item is FeedCardImageContentModel) {
                if (!item.like.isLiked) {
                    try {
                        val likeValue = Integer.valueOf(item.like.countFmt) + 1
                        adapter?.updateLikeUnlikeData(
                            rowNumber,
                            like = item.like.copy(
                                isLiked = item.like.isLiked.not(),
                                countFmt = likeValue.toString(),
                                count = item.like.count + 1
                            )
                        )
                    } catch (ignored: NumberFormatException) {
                        Timber.e(ignored)
                    }
                } else {
                    try {
                        val likeValue = Integer.valueOf(item.like.countFmt) - 1
                        adapter?.updateLikeUnlikeData(
                            rowNumber,
                            like = item.like.copy(
                                isLiked = item.like.isLiked.not(),
                                countFmt = likeValue.toString(),
                                count = item.like.count - 1
                            )
                        )
                    } catch (ignored: NumberFormatException) {
                        Timber.e(ignored)
                    }
                }
                adapter?.notifyItemChanged(
                    rowNumber,
                    FEED_POST_LIKED_UNLIKED
                )
            }
        }
    }

    private fun getFeedShareDataModel(
        id: String,
        authorName: String,
        imageUrl: String
    ): FeedShareDataModel = FeedShareDataModel(
        id = id,
        name = authorName,
        tnTitle = (
            String.format(
                context?.getString(feedR.string.feed_share_title) ?: "",
                authorName
            )
            ),
        tnImage = imageUrl,
        ogUrl = imageUrl
    )

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

    private fun openProductTagBottomSheet(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean
    ) {
        val productBottomSheet = ProductItemInfoBottomSheet()
        productBottomSheet.show(
            childFragmentManager,
            this,
            ProductBottomSheetData(
                postId = postId,
                shopId = author.id,
                postType = postType,
                isFollowed = isFollowing,
                shopName = author.name,
                saleStatus = campaign.status,
                saleType = campaign.name,
                hasVoucher = hasVoucher,
                authorType = author.type.toString()
            ),
            viewModelFactory = viewModelFactory,
            tag = TAG_FEED_PRODUCT_BOTTOMSHEET
        )
    }

    private fun moveToAddToCartPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }

    private fun showLoading() {
        binding.feedLoading.show()
        binding.swipeRefreshFeedLayout.hide()
    }

    private fun hideLoading() {
        binding.feedLoading.hide()
        binding.swipeRefreshFeedLayout.show()
    }

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"

        private const val MINIMUM_ENDLESS_CALL = 1

        private const val ZERO = 0
        private const val ONE = 1

        private const val TAG_FEED_MENU_BOTTOMSHEET = "TAG_FEED_MENU_BOTTOMSHEET"
        private const val TAG_FEED_PRODUCT_BOTTOMSHEET = "TAG_FEED_PRODUCT_BOTTOMSHEET"

        fun createFeedFragment(
            data: FeedDataModel,
            extras: Bundle
        ): FeedFragment = FeedFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA, data)
                putAll(extras)
            }
        }
    }
}
