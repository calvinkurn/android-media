package com.tokopedia.tokopedianow.recipelist.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NOTEBOOK
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.common.view.TokoNowNavToolbar
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeListBinding
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListAdapterTypeFactory
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel
import com.tokopedia.tokopedianow.recipelist.presentation.constant.ImageUrl
import com.tokopedia.tokopedianow.recipelist.presentation.listener.RecipeListener
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class BaseTokoNowRecipeListFragment : Fragment(), ServerErrorListener {

    private val adapter by lazy {
        RecipeListAdapter(
            RecipeListAdapterTypeFactory(
                recipeItemListener = RecipeListener(context),
                serverErrorListener = this
            )
        )
    }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeListBinding>()

    private var navToolbar: TokoNowNavToolbar? = null

    abstract val viewModel: BaseTokoNowRecipeListViewModel

    abstract val searchHintData: List<HintData>

    abstract val showHeaderBackground: Boolean

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
    }

    override fun onClickRetryButton() {
        viewModel.refreshPage()
    }

    private fun setupNavToolbar() {
        val fragment = this
        val headerBg = binding?.ivHeaderBackground

        val icons = IconBuilder().addNotebookIcon()

        navToolbar = TokoNowNavToolbar(
            context = context,
            navToolbar = binding?.navToolbar,
            pageName = pageName,
            hintData = searchHintData
        ) {
            // go to search page
        }
        navToolbar?.headerBackground = headerBg
        navToolbar?.setIcon(icons)
        navToolbar?.init(fragment)

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
        if (showHeaderBackground) {
            binding?.ivHeaderBackground?.loadImage(ImageUrl.RECIPE_BACKGROUND_IMAGE_URL)
            binding?.ivHeaderBackground?.show()
        } else {
            binding?.ivHeaderBackground?.hide()
        }
        viewModel.showHeaderBackground = showHeaderBackground
    }

    private fun setupSwipeRefreshLayout() {
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@BaseTokoNowRecipeListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observe(viewModel.visitableList) {
            adapter.submitList(it)
        }

        observe(viewModel.showProgressBar) {
            binding?.loader?.showWithCondition(it)
        }
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
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK)
    }
}