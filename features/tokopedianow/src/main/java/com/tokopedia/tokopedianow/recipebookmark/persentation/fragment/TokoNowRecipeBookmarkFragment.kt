package com.tokopedia.tokopedianow.recipebookmark.persentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeBookmarkBinding
import com.tokopedia.tokopedianow.recipebookmark.di.component.DaggerRecipeBookmarkComponent
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkAdapterTypeFactory
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel.TokoNowRecipeBookmarkViewModel
import com.tokopedia.tokopedianow.recipebookmark.util.RecyclerViewSpaceItemDecoration
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.RecipeViewHolder
import com.tokopedia.tokopedianow.recipebookmark.util.repeatOnLifecycle
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class TokoNowRecipeBookmarkFragment: Fragment(), RecipeViewHolder.RecipeListener {

    companion object {
        private const val NO_DATA_IMAGE = "https://images.tokopedia.net/img/android/tokonow/no_data_recipe_bookmarks.png"

        const val DEFAULT_PAGE = 1
        const val DEFAULT_LIMIT = 10
        const val DEFAULT_WIDGET_COUNTER = 0
        const val SCROLL_DOWN_DIRECTION = 1
        const val ERROR_PAGE_NOT_FOUND = "404"
        const val ERROR_SERVER = "500"
        const val ERROR_PAGE_FULL = "501"
        const val ERROR_MAINTENANCE = "502"

        fun newInstance(): TokoNowRecipeBookmarkFragment {
            return TokoNowRecipeBookmarkFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeBookmarkViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeBookmarkBinding>()
    private var adapter by autoClearedNullable<RecipeBookmarkAdapter>()

    private val isLoadMoreLoading: Boolean
        get() = adapter?.isLoadingLoadMore.orFalse()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        setupRecyclerView()

        viewModel.loadFirstPage()

        collectStateFlow()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowRecipeBookmarkBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onRemoveBookmark(title: String, position: Int, recipeId: String) {
        viewModel.removeRecipeBookmark(
            title = title,
            position = position,
            recipeId = recipeId
        )
    }

    private fun injectDependencies() {
        DaggerRecipeBookmarkComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    /**
     * Create a new coroutine in the [lifecycleScope]. [repeatOnLifecycle] launches the block in a new coroutine
     * every time the lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
     */
    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                /**
                 * Because [collect] is a suspend function, need different coroutines to collect multiple flows in parallel.
                 * The suspending function suspends until the Flow terminates.
                 */
                launch { collectRecipeBookmarks() }
                launch { collectMoreRecipeBookmarks() }
                launch { collectToaster() }
                launch { collectIsScrollNotNeeded() }
            }
        }
    }

    private suspend fun collectRecipeBookmarks() {
        viewModel.loadRecipeBookmarks.collect { state ->
            when(state) {
                is UiState.Fail -> showGlobalError(state.throwable, state.errorCode)
                is UiState.Success -> showPage(state.data)
                is UiState.Loading -> showLoadingState()
            }
        }
    }

    private suspend fun collectToaster() {
        viewModel.toaster.collect { state ->
            when(state) {
                is UiState.Fail -> showFailToaster(state.throwable, state.data)
                is UiState.Success -> showSuccessToaster(state.data)
                is UiState.Loading -> showRecipeItemLoading(state.data)
            }
        }
    }

    private suspend fun collectMoreRecipeBookmarks() {
        viewModel.moreRecipeBookmarks.collect { state ->
            when(state) {
                is UiState.Fail -> hideLoadMoreLoading()
                is UiState.Success -> showMoreWidgets(state.data)
                is UiState.Loading -> showLoadMoreLoading()
            }
        }
    }

    private suspend fun collectIsScrollNotNeeded() {
        viewModel.isOnScrollNotNeeded.collect { isNotNeeded ->
            if (isNotNeeded) {
                binding?.rvRecipeBookmark?.clearOnScrollListeners()
            }
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isAtTheBottomOfThePage = !recyclerView.canScrollVertically(SCROLL_DOWN_DIRECTION)
                loadMore(isAtTheBottomOfThePage)
            }
        }
    }

    private fun showGlobalError(throwable: Throwable?, errorCode: String?){
        binding?.apply {
            showErrorState()
            val typeError = when{
                throwable is UnknownHostException -> GlobalError.NO_CONNECTION
                errorCode == ERROR_PAGE_FULL -> GlobalError.PAGE_FULL
                errorCode == ERROR_SERVER -> GlobalError.SERVER_ERROR
                errorCode == ERROR_MAINTENANCE -> GlobalError.MAINTENANCE
                errorCode == ERROR_PAGE_NOT_FOUND -> GlobalError.PAGE_NOT_FOUND
                else -> GlobalError.SERVER_ERROR
            }
            errorState.setType(typeError)
            errorState.setActionClickListener {
                if (errorCode == ERROR_PAGE_NOT_FOUND || errorCode == ERROR_MAINTENANCE) {
                    activity?.onBackPressed()
                } else {
                    viewModel.loadFirstPage()
                }
            }
        }
    }

    private fun loadMore(isAtTheBottomOfThePage: Boolean) {
        viewModel.loadMore(isAtTheBottomOfThePage, isLoadMoreLoading)
    }

    private fun showRecipeItemLoading(data: ToasterUiModel?) {
        data?.apply {
            if (adapter?.data?.size == 1) {
                showRecipesList()
                adapter?.showItemLoading(
                    position = position.orZero(),
                    isRemoving = true
                )
            } else {
                adapter?.showItemLoading(
                    position = position.orZero(),
                    isRemoving = isRemoving.orFalse()
                )
            }
        }
    }

    private fun showSuccessToaster(data: ToasterUiModel?) {
        data?.model?.apply {
            setupToaster(
                message = getString(R.string.tokopedianow_recipe_bookmark_toaster_description_success_removing_recipe, title),
                isSuccess = isSuccess,
                cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_cancel),
                clickListener = {
                    viewModel.addRecipeBookmark(recipeId)
                }
            )
        }
    }

    private fun showFailToaster(throwable: Throwable?, data: ToasterUiModel?) {
        data?.model?.apply {
            setupToaster(
                message = if (throwable == null) message else ErrorHandler.getErrorMessage(context, throwable),
                isSuccess = isSuccess,
                cta = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again),
                clickListener = {
                    if (data.isRemoving) {
                        viewModel.removeRecipeBookmark(title, data.position.orZero(), recipeId)
                    } else {
                        viewModel.addRecipeBookmark(recipeId)
                    }
                }
            )
        }
    }

    private fun setupToaster(message: String, isSuccess: Boolean, cta: String, clickListener: () -> Unit) {
        binding?.apply {
            val toaster = Toaster.build(
                view = root,
                text = message,
                type = if (isSuccess) TYPE_NORMAL else TYPE_ERROR,
                actionText = cta,
                clickListener = {
                    clickListener.invoke()
                }
            )
            toaster.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    viewModel.removeToaster()
                }
            })
            toaster.show()
        }
    }

    private fun showPage(data: List<Visitable<*>>?) {
        if (!data.isNullOrEmpty()) {
            showRecipesList()
            adapter?.submitList(data)
        } else {
            showEmptyState()
        }
    }

    private fun showRecipesList() {
        binding?.apply {
            loadingState.root.hide()
            rvRecipeBookmark.show()
            emptyState.root.hide()
        }
    }

    private fun showEmptyState() {
        binding?.apply {
            loadingState.root.hide()
            errorState.hide()
            rvRecipeBookmark.hide()
            emptyState.root.show()
            emptyState.iuRecipePicture.loadImage(NO_DATA_IMAGE)
            emptyState.ubSeeRecipes.setOnClickListener { /* don't know the direction page */ }
        }
    }

    private fun showErrorState() {
        binding?.apply {
            loadingState.root.hide()
            rvRecipeBookmark.hide()
            emptyState.root.hide()
            errorState.show()
        }
    }

    private fun showLoadingState() {
        binding?.apply {
            rvRecipeBookmark.hide()
            emptyState.root.hide()
            errorState.hide()
            loadingState.root.show()
        }
    }

    private fun hideLoadMoreLoading() {
        adapter?.hideLoading()
    }

    private fun showMoreWidgets(data: List<Visitable<*>>?) {
        hideLoadMoreLoading()
        showPage(data)
    }

    private fun showLoadMoreLoading() {
        adapter?.showLoading()
    }

    private fun setupHeader() {
        binding?.huRecipeBookmark?.apply {
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeBookmarkAdapter(RecipeBookmarkAdapterTypeFactory(this))
        binding?.rvRecipeBookmark?.apply {
            adapter = this@TokoNowRecipeBookmarkFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerViewSpaceItemDecoration(bottom = context.resources.getDimension(R.dimen.tokopedianow_space_item_recipe_bookmark).toIntSafely()))
            addOnScrollListener(createLoadMoreListener())
        }
    }
}