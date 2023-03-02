package com.tokopedia.feedplus.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
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
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment(), FeedListener, ContentThreeDotsMenuBottomSheet.Listener, ProductItemInfoBottomSheet.Listener {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var isInClearViewMode: Boolean = false
    private var productBottomSheet: ProductItemInfoBottomSheet? = null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface


    private val feedMainViewModel: FeedMainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val feedPostViewModel: FeedPostViewModel by viewModels { viewModelFactory }

    private lateinit var feedMenuSheet: ContentThreeDotsMenuBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity?.run {
//            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
//            feedMainViewModel = viewModelProvider[FeedMainViewModel::class.java]
//        }

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

        feedPostViewModel.fetchFeedPosts()

        initView()
        observeClearViewData()
        observePostData()
        observeAddToCart()
    }

    private fun observeAddToCart() {
        feedPostViewModel.atcRespData.observe(
                viewLifecycleOwner
        ) {
            when (it) {
                is FeedResult.Loading -> {

                }
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
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        feedMainViewModel.run {
            reportResponse.observe(lifecycleOwner) {
                when (it) {
                    is Success -> {
                        if (::feedMenuSheet.isInitialized) {
                            feedMenuSheet.setFinalView()
                        }
                    }
                    is Fail -> Toast.makeText(context, "Laporkan fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        productBottomSheet?.onDestroy()
        super.onDestroyView()
        if (::feedMenuSheet.isInitialized) {
            feedMenuSheet.dismiss()
        }
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {
            REQUEST_REPORT_POST_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                if (::feedMenuSheet.isInitialized) {
                    feedMenuSheet.showReportLayoutWhenLaporkanClicked()
                    feedMenuSheet.showToasterOnLoginSuccessFollow(
                        getString(feedR.string.feed_report_login_success_toaster_text),
                        Toaster.TYPE_NORMAL
                    )
                }
            }
            else -> {
            }
        }
    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.LAPORKAN -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    feedMenuSheet.showReportLayoutWhenLaporkanClicked()
                    Toast.makeText(context, "Laporkan - onMenuItemClick", Toast.LENGTH_SHORT).show()
                }
            }

            FeedMenuIdentifier.MODE_NONTON -> {
                feedMainViewModel.toggleClearView(true)
                Toast.makeText(context, "Mode Nonton", Toast.LENGTH_SHORT).show()
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

    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory(this))

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    layoutManager?.let { lm ->
                        adapter?.let { mAdapter ->
                            if (newState == RecyclerView.SCROLL_STATE_IDLE && lm.findLastVisibleItemPosition() >= (mAdapter.itemCount - MINIMUM_ENDLESS_CALL)) {
                                feedPostViewModel.fetchFeedPosts()
                            }
                        }
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
                    adapter?.addElement(it.data.items)
                }
                is Fail -> {}
            }
        }
    }

    private fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            requireActivity().startActivityForResult(intent, REQUEST_REPORT_POST_LOGIN)
        }
    }

    override fun onMenuClicked(model: FeedCardModel) {
        activity?.let {
            feedMenuSheet = ContentThreeDotsMenuBottomSheet
                .getFragment(it.supportFragmentManager, it.classLoader)
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(getMenuItemData(), model.id)
            feedMenuSheet.show(it.supportFragmentManager)
        }
    }

    private fun getMenuItemData() : List<FeedMenuItem> {
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

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"
        const val REQUEST_REPORT_POST_LOGIN = 1201

        private const val MINIMUM_ENDLESS_CALL = 1

        fun createFeedFragment(data: FeedDataModel): FeedFragment = FeedFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA, data)
            }
        }
    }

    override fun onProductTagItemClicked(model: FeedCardModel) {
        openProductTagBottomSheet(model)
    }

    override fun onProductTagViewClicked(model: FeedCardModel) {
        val numberOfTaggedProducts = model.totalProducts
        val productData =
            if (model.isTypeProductHighlight) model.products else model.tags

        if (numberOfTaggedProducts == 1) {
            val appLink = productData.firstOrNull()?.applink
            if (appLink?.isNotEmpty() == true)
                activity?.let {
                    RouteManager.route(it, appLink)
                }
        } else {
            openProductTagBottomSheet(model)
        }
    }

    private fun openProductTagBottomSheet(feedXCard: FeedCardModel) {
        productBottomSheet = ProductItemInfoBottomSheet()
        productBottomSheet?.disMissed = {
            productBottomSheet?.onDestroy()
            productBottomSheet = null
        }
        productBottomSheet?.show(
            childFragmentManager,
            this,
            ProductBottomSheetData(
                postId = feedXCard.id,
                shopId = feedXCard.author.id,
                postType = feedXCard.typename,
                isFollowed = feedXCard.followers.isFollowed,
                playChannelId = feedXCard.playChannelId,
                shopName = feedXCard.author.name,
                saleStatus = feedXCard.campaign.status,
                saleType = feedXCard.campaign.name,
                hasVoucher = feedXCard.hasVoucher,
                authorType = feedXCard.author.type.toString()
            ),
            viewModelFactory = viewModelFactory
        )
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagModelNew,
        context: Context,
        shopId: String
    ) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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

    override fun onAddToCartButtonClicked(product: ProductPostTagModelNew) {
        feedPostViewModel.addToCart(
            productId = product.product.id,
            productName = product.product.name,
            price = product.price,
            shopId = product.shopId
        )
    }

    override fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int) {
    }

    private fun moveToAddToCartPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }
}
