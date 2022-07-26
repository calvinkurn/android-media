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
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareOptionRequest
import com.tokopedia.tokopedianow.common.view.ToolbarHeaderView
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeDetailBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.listener.RecipeProductListener
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

class TokoNowRecipeDetailFragment : Fragment(), RecipeDetailView, MiniCartWidgetListener {

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
                productListener = RecipeProductListener(viewModel)
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
        val recipeId = activity?.intent?.data
            ?.getQueryParameter(KEY_PARAM_RECIPE_ID).orEmpty()

        setupToolbarHeader()
        setupRecyclerView()
        updateAddressData()
        observeLiveData()

        getRecipe(recipeId)
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

    override fun getFragmentActivity() = activity

    private fun setupToolbarHeader() {
        initToolbarHeader()
        setToolbarClickListener()
        setToolbarScrollListener()
    }

    private fun initToolbarHeader() {
        toolbarHeader = ToolbarHeaderView(
            header = binding?.toolbarHeader,
            statusBar = binding?.statusBar
        ) { rv, _, _ -> findStartSwitchThemePosition(rv) }.apply {
            icons = listOf(
                com.tokopedia.iconunify.R.drawable.iconunify_bookmark,
                com.tokopedia.iconunify.R.drawable.iconunify_share_mobile
            )
            onSwitchToNormal = {
                binding?.headerDivider?.show()
            }
            onSwitchToTransparent = {
                binding?.headerDivider?.hide()
            }
        }
    }

    private fun setToolbarClickListener() {
        toolbarHeader?.run {
            getActionItem(BOOKMARK_BTN_POSITION)?.setOnClickListener {
                // Implement bookmark recipe here
            }

            getActionItem(SHARE_BTN_POSITION)?.setOnClickListener {
                showShareBottomSheet()
            }

            setNavButtonClickListener {
                activity?.finish()
            }
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
        val imageUrl = recipeInfo.thumbnail
        val shareUrl = "https://tokopedia.link/aBc123DeF" // To-Do
        val shareTitle = getString(R.string.tokopedianow_share_recipe_title, title, portion, duration)
        val shareText = getString(R.string.tokopedianow_share_recipe_text, title, shareUrl)

        val shareData = ShareTokonow(
            sharingText = shareText,
            thumbNailImage = imageUrl,
            ogImageUrl = imageUrl,
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
                tnImage = imageUrl,
            )

            setOgImageUrl(imgUrl = imageUrl)
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
            if (it is Success) {
                adapter.submitList(it.data)
            }
        }

        observe(viewModel.recipeInfo) {
            if(it is Success) {
                setHeaderTitle(it.data.title)
                setupShareBottomSheet(it.data)
            }
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
            binding?.root?.setPadding(paddingZero, paddingZero, paddingZero, paddingBottom)
        }
    }

    private fun resetPadding() {
        val paddingZero = context?.resources?.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
        ).orZero()
        binding?.root?.setPadding(paddingZero, paddingZero, paddingZero, paddingZero)
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

    private fun getRecipe(recipeId: String) {
        viewModel.getRecipe(recipeId)
    }

    private fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun setHeaderTitle(title: String) {
        toolbarHeader?.title = title
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
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
        type: Int = Toaster.TYPE_NORMAL
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type
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
        context?.let { context ->
            viewModel.localAddressData?.let { currentData ->
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(context, currentData)) {
                    val addressData = ChooseAddressUtils.getLocalizingAddressData(context)
                    viewModel.localAddressData = addressData
                }
            }
        }
    }
}