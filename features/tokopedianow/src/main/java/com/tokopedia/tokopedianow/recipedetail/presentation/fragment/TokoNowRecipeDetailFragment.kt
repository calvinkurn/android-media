package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareOptionRequest
import com.tokopedia.tokopedianow.common.view.ToolbarHeaderView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeDetailBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.listener.RecipeChooseAddressListener
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BookmarkUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeDetailFragment : Fragment(), RecipeDetailView, MiniCartWidgetListener,
    ServerErrorListener {

    companion object {
        private const val KEY_PARAM_RECIPE_ID = "recipe_id"

        private const val PAGE_NAME = "Tokonow"
        private const val PAGE_TYPE = "Recipe Detail"
        private const val SHARE_FEATURE_NAME = "Share"

        private const val RECIPE_INFO_POSITION = 1
        private const val BOOKMARK_BTN_POSITION = 0
        private const val SHARE_BTN_POSITION = 1

        fun newInstance(): TokoNowRecipeDetailFragment {
            return TokoNowRecipeDetailFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private val adapter by lazy {
        RecipeDetailAdapter(
            RecipeDetailAdapterTypeFactory(
                view = this,
                serverErrorListener = this
            )
        )
    }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeDetailBinding>()

    private var toolbarHeader: ToolbarHeaderView? = null
    private var shareBottomSheet: UniversalShareBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        setRecipeData()
        setupToolbarHeader()
        setupRecyclerView()
        updateAddressData()
        observeLiveData()
        checkAddressData()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        updateAddressData()
        getMiniCart()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.setProductAddToCartQuantity(miniCartSimplifiedData)
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        viewModel.onQuantityChanged(productId, shopId, quantity)
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        viewModel.addItemToCart(productId, shopId, quantity)
    }

    override fun deleteCartItem(productId: String) {
        val miniCartItem = viewModel.getMiniCartItem(productId)
        val cartId = miniCartItem?.cartId.orEmpty()
        viewModel.deleteCartItem(productId, cartId)
    }

    override fun showChooseAddressBottomSheet() {
        val bottomSheet = ChooseAddressBottomSheet().apply {
            val recyclerView = binding?.rvRecipeDetail
            setListener(RecipeChooseAddressListener(
                toolbarHeader,
                recyclerView,
                viewModel
            ))
        }
        bottomSheet.show(childFragmentManager)
    }

    override fun onClickRetryButton() {
        viewModel.refreshPage()
    }

    override fun getFragmentActivity() = activity

    private fun setRecipeData() {
        val recipeId = activity?.intent?.data
            ?.getQueryParameter(KEY_PARAM_RECIPE_ID).orEmpty()
        viewModel.setRecipeId(recipeId)
    }

    private fun setupToolbarHeader() {
        setToolbarHeaderView()
        setNavBtnClickListener()
    }

    private fun setToolbarHeaderView() {
        toolbarHeader = ToolbarHeaderView(
            header = binding?.toolbarHeader,
            statusBar = binding?.statusBar
        ) { rv, _, _ -> findStartSwitchThemePosition(rv) }.apply {
            rightIcons = listOf(
                R.drawable.tokopedianow_ic_recipe_bookmark_dark,
                R.drawable.tokopedianow_ic_recipe_share_dark
            )
            setBackButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            onSwitchToNormal = {
                binding?.headerDivider?.show()
            }
            onSwitchToTransparent = {
                binding?.headerDivider?.hide()
            }
            enableSwitchTheme = true
        }
        binding?.headerDivider?.hide()
    }

    private fun setToolbarClickListener() {
        toolbarHeader?.run {
            val bookmarkBtn = getActionItem(BOOKMARK_BTN_POSITION)
            val shareBtn = getActionItem(SHARE_BTN_POSITION)

            bookmarkBtn?.setOnClickListener {
                onClickBookmarkBtn()
            }

            shareBtn?.setOnClickListener {
                showShareBottomSheet()
            }

            setNavBtnClickListener()
        }
    }

    private fun onClickBookmarkBtn() {
        val bookmarkedId = com.tokopedia.iconunify.R.drawable.iconunify_bookmark_filled
        val bookmarked = toolbarHeader?.getRightIcon(BOOKMARK_BTN_POSITION) == bookmarkedId
        setupToolbarHeaderIcons(!bookmarked)

        val bookmarkBtn = toolbarHeader?.getRightIcon(BOOKMARK_BTN_POSITION)
        val addBookmark = bookmarkBtn == bookmarkedId

        viewModel.onClickBookmarkBtn(addBookmark)
    }

    private fun setNavBtnClickListener() {
        toolbarHeader?.setNavButtonClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        binding?.rvRecipeDetail?.apply {
            adapter = this@TokoNowRecipeDetailFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupShareBottomSheet(recipeInfo: RecipeInfoUiModel) {
        val title = recipeInfo.title
        val portion = recipeInfo.portion
        val duration = recipeInfo.duration
        val thumbnailImageUrl = recipeInfo.thumbnail
        val imageUrls = recipeInfo.imageUrls
        val shareUrl = "https://tokopedia.link/aBc123DeF" // To-Do
        val shareTitle = getString(R.string.tokopedianow_share_recipe_title, title, portion, duration)
        val shareText = getString(R.string.tokopedianow_share_recipe_text, title, shareUrl)

        val shareData = ShareTokonow(
            sharingText = shareText,
            thumbNailImage = thumbnailImageUrl,
            ogImageUrl = thumbnailImageUrl,
        )

        shareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(createShareListener(shareData))

            setUtmCampaignData(
                pageName = PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = listOf(PAGE_TYPE),
                feature = SHARE_FEATURE_NAME
            )

            setMetaData(
                tnTitle = shareTitle,
                tnImage = thumbnailImageUrl,
                imageList = ArrayList(imageUrls),
            )

            setOgImageUrl(imgUrl = thumbnailImageUrl)
        }
    }

    private fun createShareListener(data: ShareTokonow): ShareBottomsheetListener {
        return object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                shareOptionRequest(
                    shareModel = shareModel,
                    shareTokoNowData = data,
                    activity = activity,
                    view = view,
                    onSuccess = {
                        shareBottomSheet?.dismiss()
                    }
                )
            }

            override fun onCloseOptionClicked() {
            }
        }
    }

    private fun showShareBottomSheet() {
        shareBottomSheet?.show(childFragmentManager, this@TokoNowRecipeDetailFragment)
    }

    private fun observeLiveData() {
        observe(viewModel.layoutList) {
            adapter.submitList(it)
        }

        observe(viewModel.recipeInfo) {
            onSuccessGetRecipeInfo(it)
        }

        observe(viewModel.addItemToCart) {
            when (it) {
                is Success -> onSuccessAddItemToCart(it.data)
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.removeCartItem) {
            when (it) {
                is Success -> onSuccessRemoveCartItem(it.data)
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.updateCartItem) {
            when (it) {
                is Success -> onSuccessUpdateCartItem()
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.miniCart) {
            when(it) {
                is Success -> {
                    val data = it.data
                    showMiniCart(data)
                    setupPadding(data)
                }
                is Fail -> {
                    hideMiniCart()
                    resetPadding()
                }
            }
        }

        observe(viewModel.isBookmarked) {
            setupToolbarHeaderIcons(it)
        }

        observe(viewModel.addBookmark) {
            if(it.isSuccess) {
                onSuccessAddBookmark(it)
            } else {
                onFailedAddBookmark()
            }
        }

        observe(viewModel.removeBookmark) {
            if(it.isSuccess) {
                onSuccessRemoveBookmark(it)
            } else {
                onFailedRemoveBookmark()
            }
        }
    }

    private fun showMiniCart(data: MiniCartSimplifiedData) {
        val miniCartWidget = binding?.miniCart
        val showMiniCartWidget = data.isShowMiniCartWidget

        if(showMiniCartWidget) {
            val shopId = viewModel.getShopId()
            val pageName = MiniCartAnalytics.Page.HOME_PAGE
            val shopIds = listOf(shopId)
            val source = MiniCartSource.TokonowHome
            miniCartWidget?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                pageName = pageName,
                source = source
            )
            miniCartWidget?.show()
        } else {
            hideMiniCart()
        }
    }

    private fun hideMiniCart() {
        binding?.miniCart?.apply {
            hideCoachMark()
            hide()
        }
    }

    private fun setupPadding(data: MiniCartSimplifiedData) {
        binding?.miniCart?.post {
            val paddingZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
            ).orZero()

            val paddingBottom = if (data.isShowMiniCartWidget) {
                getMiniCartHeight()
            } else {
                paddingZero
            }
            binding?.rvRecipeDetail?.setPadding(paddingZero, paddingZero, paddingZero, paddingBottom)
        }
    }

    private fun resetPadding() {
        val paddingZero = context?.resources?.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
        ).orZero()
        binding?.rvRecipeDetail?.setPadding(paddingZero, paddingZero, paddingZero, paddingZero)
    }

    private fun onSuccessGetRecipeInfo(it: RecipeInfoUiModel) {
        setHeaderTitle(it.title)
        setToolbarScrollListener()
        setToolbarIconsColor()
        setupShareBottomSheet(it)
    }

    private fun onSuccessAddItemToCart(data: AddToCartDataModel) {
        val message = data.errorMessage.joinToString(separator = ", ")
        showToaster(message = message)
        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
        getMiniCart()
    }

    private fun onSuccessUpdateCartItem() {
        val shopId = viewModel.getShopId()
        binding?.miniCart?.updateData(listOf(shopId))
    }

    private fun onSuccessAddBookmark(data: BookmarkUiModel) {
        showToaster(getString(
            R.string.tokopedianow_recipe_success_add_bookmark,
            data.recipeTitle
        ))
    }

    private fun onFailedAddBookmark() {
        val message = getString(R.string.tokopedianow_recipe_failed_add_bookmark)
        val actionText = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again)
        showToaster(message = message, type = Toaster.TYPE_ERROR, actionText = actionText) {
            viewModel.addRecipeBookmark()
        }
    }

    private fun onSuccessRemoveBookmark(data: BookmarkUiModel) {
        showToaster(getString(
            R.string.tokopedianow_recipe_bookmark_toaster_description_success_removing_recipe,
            data.recipeTitle
        ))
    }

    private fun onFailedRemoveBookmark() {
        val message = getString(R.string.tokopedianow_recipe_failed_remove_bookmark)
        val actionText = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again)
        showToaster(message = message, type = Toaster.TYPE_ERROR, actionText = actionText) {
            viewModel.removeRecipeBookmark()
        }
    }

    private fun checkAddressData() {
        viewModel.checkAddressData()
    }

    private fun showLoading() {
        viewModel.showLoading()
    }

    private fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun setHeaderTitle(title: String) {
        toolbarHeader?.title = title
    }

    private fun setupToolbarHeaderIcons(bookmarked: Boolean) {
        toolbarHeader?.run {
            val bookmarkIcon = if(bookmarked) {
                com.tokopedia.iconunify.R.drawable.iconunify_bookmark_filled
            } else {
                com.tokopedia.iconunify.R.drawable.iconunify_bookmark
            }
            rightIcons = listOf(
                bookmarkIcon,
                com.tokopedia.iconunify.R.drawable.iconunify_share_mobile
            )
            setToolbarClickListener()
        }
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setToolbarIconsColor() {
        toolbarHeader?.apply {
            setRightIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            setBackButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
    }

    private fun setToolbarScrollListener() {
        toolbarHeader?.scrollListener?.let {
            binding?.rvRecipeDetail?.addOnScrollListener(it)
        }
    }

    private fun findStartSwitchThemePosition(rv: RecyclerView): Float {
        val vh = rv.findViewHolderForAdapterPosition(RECIPE_INFO_POSITION)
        val statusBarHeight = binding?.statusBar?.height.orZero()
        return vh?.itemView?.y.orZero() - statusBarHeight
    }

    private fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickAction
                )
                toaster.show()
            }
        }
    }

    private fun showErrorToaster(error: Fail) {
        showToaster(
            message = error.throwable.message.orEmpty(),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun getMiniCartHeight(): Int {
        val space16 = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_16
        )?.toInt().orZero()
        return binding?.miniCart?.height.orZero() - space16
    }

    private fun updateAddressData() {
        viewModel.updateAddressData()
    }
}