package com.tokopedia.tokopedianow.recipelist.base.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NOTEBOOK
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowNavToolbar
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorAnalytics
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeListBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListAdapterTypeFactory
import com.tokopedia.tokopedianow.recipelist.presentation.constant.ImageUrl
import com.tokopedia.tokopedianow.recipelist.presentation.listener.RecipeFilterListener
import com.tokopedia.tokopedianow.recipelist.presentation.listener.RecipeListListener
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeEmptyStateViewHolder.RecipeEmptyStateListener
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

abstract class BaseTokoNowRecipeListFragment : Fragment(),
    RecipeListView,
    ServerErrorListener,
    ServerErrorAnalytics,
    RecipeEmptyStateListener
{
    companion object {
        const val HOME_PAGE_NAME = "TokoNow Recipe Home"
        const val REQUEST_CODE_FILTER = 101
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val analytics by lazy {
        RecipeListAnalytics(
            userSession = userSession,
            pageName = pageName,
            warehouseId = viewModel.warehouseId
        )
    }

    private val adapter by lazy {
        RecipeListAdapter(
            RecipeListAdapterTypeFactory(
                recipeItemListener = RecipeListListener(
                    view = this,
                    analytics = analytics,
                    viewModel = viewModel
                ),
                recipeFilterListener = RecipeFilterListener(
                    view = this,
                    analytics = analytics,
                    pageName = pageName
                ),
                serverErrorListener = this,
                serverErrorAnalytics = this,
                recipeEmptyStateListener = this
            )
        )
    }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeListBinding>()

    private val loadMoreListener by lazy { createLoadMoreListener() }

    private var navToolbar: TokoNowNavToolbar? = null

    abstract val viewModel: BaseTokoNowRecipeListViewModel

    abstract val searchHintData: List<HintData>

    abstract val enableHeaderBackground: Boolean

    abstract val pageName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavToolbar()
        setupStickyView()
        setupHeaderBackground()
        setupSwipeRefreshLayout()
        setupRecyclerView()
        observeLiveData()
        onViewCreated()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            REQUEST_CODE_FILTER -> {
                val filters = data
                    ?.getParcelableArrayListExtra<SelectedFilter>(EXTRA_SELECTED_FILTER).orEmpty()
                viewModel.applyFilter(filters)
                addLoadMoreListener()
            }
        }
    }

    override fun onClickRetryButton() {
        onRefreshPage()
    }

    override fun trackImpressErrorPage() {
        analytics.impressFailedLoadPage()
    }

    override fun trackClickRetryPage() {
        analytics.clickRetryFailedLoadPage()
    }

    override fun onClickResetFilter() {
        viewModel.resetFilter()
        analytics.clickResetFilter()
    }

    override fun onImpressEmptyStatePage() {
        analytics.impressNoSearchResult()
    }

    override fun viewModel() = viewModel

    override fun context() = context

    override fun fragment() = this

    private fun setupNavToolbar() {
        val fragment = this
        val headerBg = binding?.ivHeaderBackground

        val icons = IconBuilder().addNotebookIcon()

        navToolbar = TokoNowNavToolbar(
            activity = activity,
            navToolbar = binding?.navToolbar,
            pageName = pageName,
            hintData = searchHintData
        ) {
            analytics.clickSearchBar()
            RouteManager.route(context, ApplinkConst.TokopediaNow.RECIPE_AUTO_COMPLETE)
        }
        navToolbar?.headerBackground = headerBg
        navToolbar?.setIcon(icons)
        navToolbar?.init(fragment)

        navToolbar?.setBackButtonOnClickListener {
            analytics.clickBackButton()
            activity?.finish()
        }

        setNavToolbarScrollListener()
        setToolbarShadowScrollListener()
    }

    private fun setupStickyView() {
        val context = context ?: return
        val top = NavToolbarExt.getToolbarHeight(context)
        binding?.stickyView?.setMargin(0.toDp(), top, 0.toDp(), 0.toDp())
        binding?.stickyView?.show()
    }

    private fun setNavToolbarScrollListener() {
        navToolbar?.scrollListener?.let {
            binding?.recyclerView?.addOnScrollListener(it)
        }
    }

    private fun setToolbarShadowScrollListener() {
        navToolbar?.navShadowScrollListener?.let {
            binding?.recyclerView?.addOnScrollListener(it)
        }
    }

    private fun setupHeaderBackground() {
        if (enableHeaderBackground) {
            binding?.ivHeaderBackground?.setImageResource(
                R.drawable.tokopedianow_ic_header_background_shimmering
            )
            binding?.ivHeaderBackground?.show()
        } else {
            binding?.ivHeaderBackground?.hide()
        }
        viewModel.enableHeaderBackground = enableHeaderBackground
    }

    private fun setupSwipeRefreshLayout() {
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            onRefreshPage()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@BaseTokoNowRecipeListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = object : DefaultItemAnimator() {
                override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
                    dispatchAddFinished(holder)
                    return true
                }
            }
        }
        addLoadMoreListener()
    }

    private fun observeLiveData() {
        observe(viewModel.visitableList) {
            submitList(it)
            resetSwipeRefresh()
            updateLoadStatus()
        }

        observe(viewModel.showProgressBar) {
            binding?.loader?.showWithCondition(it)
        }

        observe(viewModel.showHeaderBackground) {
            setHeaderBackgroundVisibility(it)
        }

        observe(viewModel.searchKeyword) {
            setSearchbarText(it)
        }

        observe(viewModel.removeScrollListener) { shouldRemove ->
            if(shouldRemove) {
                removeLoadMoreListener()
            }
        }

        observe(viewModel.showBookmarkToaster) {
            showBookmarkToaster(it)
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated()
    }

    private fun updateLoadStatus() {
        analytics.pageStatus = viewModel.getLoadPageStatus()
    }

    private fun showBookmarkToaster(toasterUiModel: ToasterUiModel) {
        val isFailed = toasterUiModel.model?.title.isNullOrEmpty()
        if (isFailed) {
            showFailBookmarkToaster(data = toasterUiModel)
        } else {
            showSuccessBookmarkToaster(data = toasterUiModel)
        }
    }

    private fun showSuccessBookmarkToaster(data: ToasterUiModel?) {
        data?.model?.apply {
            if (data.isRemoving) {
                showToaster(
                    message = getString(R.string.tokopedianow_recipe_toaster_description_success_removing_bookmark, title),
                    isSuccess = isSuccess,
                    cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_cancel),
                    clickListener = {
                        viewModel.addRecipeBookmark(recipeId, data.position.orZero(), title)
                        analytics.clickCancelUnBookmarkToaster()
                    }
                )
                analytics.impressUnBookmarkToaster()
            } else {
                showToaster(
                    message = getString(R.string.tokopedianow_recipe_toaster_description_success_adding_bookmark, title),
                    isSuccess = isSuccess,
                    cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_see),
                    clickListener = {
                        RouteManager.route(context, ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK)
                        analytics.clickSeeBookmarkToaster()
                    }
                )
                analytics.impressBookmarkToasterAdded()
            }
        }
    }

    private fun showFailBookmarkToaster(data: ToasterUiModel?) {
        data?.model?.apply {
            if (data.isRemoving) {
                showToaster(
                    message = message.ifEmpty { getString(R.string.tokopedianow_recipe_toaster_description_failed_removing_bookmark) },
                    isSuccess = isSuccess,
                    cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again),
                    clickListener = {
                        viewModel.removeRecipeBookmark(
                            recipeId = recipeId,
                            position = data.position.orZero(),
                            title = data.model.title
                        )
                        analytics.clickRetryFailedUnBookmarkToaster()
                    }
                )
                analytics.impressFailedUnBookmarkToaster()
            } else {
                showToaster(
                    message = message.ifEmpty { getString(R.string.tokopedianow_recipe_toaster_description_failed_adding_bookmark) },
                    isSuccess = isSuccess,
                    cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again),
                    clickListener = {
                        viewModel.addRecipeBookmark(
                            recipeId = recipeId,
                            position = data.position.orZero(),
                            title = data.model.title
                        )
                        analytics.clickRetryFailedBookmarkToaster()
                    }
                )
                analytics.impressFailedBookmarkToaster()
            }
        }
    }

    private fun showToaster(message: String, isSuccess: Boolean, cta: String, clickListener: () -> Unit) {
        binding?.apply {
            val toaster = Toaster.build(
                view = root,
                text = message,
                type = if (isSuccess) Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR,
                actionText = cta,
                clickListener = {
                    clickListener.invoke()
                }
            )
            toaster.show()
        }
    }

    private fun submitList(items: List<Visitable<*>>) {
        adapter.submitList(items)
    }

    private fun setHeaderBackgroundVisibility(show: Boolean) {
        binding?.apply {
            if (show) {
                ivHeaderBackground.loadImage(ImageUrl.RECIPE_BACKGROUND_IMAGE_URL)
                ivHeaderBackground.show()
            } else {
                ivHeaderBackground.hide()
            }
        }
    }

    private fun setSearchbarText(keyword: String) {
        navToolbar?.setSearchbarText(keyword)
    }

    private fun IconBuilder.addNotebookIcon(): IconBuilder {
        return addIcon(
            iconId = ID_NOTEBOOK,
            disableRouteManager = false,
            disableDefaultGtmTracker = true,
            onClick = ::goToBookmarkPage,
        )
    }

    private fun goToBookmarkPage() {
        analytics.clickBookmarkList()
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK)
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
                viewModel.onScroll(lastVisibleItemIndex)
            }
        }
    }

    private fun setupLoadMoreListener() {
        removeLoadMoreListener()
        addLoadMoreListener()
    }

    private fun addLoadMoreListener() {
        binding?.recyclerView?.post {
            binding?.recyclerView?.addOnScrollListener(loadMoreListener)
        }
    }

    private fun removeLoadMoreListener() {
        binding?.recyclerView?.post {
            binding?.recyclerView?.removeOnScrollListener(loadMoreListener)
        }
    }

    private fun resetSwipeRefresh() {
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    private fun onRefreshPage() {
        setupLoadMoreListener()
        viewModel.refreshPage()
    }
}
