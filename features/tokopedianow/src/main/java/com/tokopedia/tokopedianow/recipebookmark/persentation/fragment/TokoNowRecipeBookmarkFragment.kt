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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeBookmarkBinding
import com.tokopedia.tokopedianow.recipebookmark.di.component.DaggerRecipeBookmarkComponent
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkAdapterTypeFactory
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel.TokoNowRecipeBookmarkViewModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import com.tokopedia.tokopedianow.recipebookmark.util.VerticalSpaceItemDecoration
import com.tokopedia.tokopedianow.recipebookmark.util.repeatOnLifecycle
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowRecipeBookmarkFragment: Fragment() {

    companion object {
        const val DEFAULT_LIMIT_RECIPES = 10
        const val DEFAULT_PAGE_RECIPES = 1

        fun newInstance(): TokoNowRecipeBookmarkFragment {
            return TokoNowRecipeBookmarkFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeBookmarkViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeBookmarkBinding>()

    private var adapter: RecipeBookmarkAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        setupRecyclerView()

        viewModel.getRecipeBookmarks(
            userId = userSession.userId,
            warehouseId = "1231",
            page = DEFAULT_PAGE_RECIPES,
            limit = DEFAULT_LIMIT_RECIPES
        )

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

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeBookmarks.collect { state ->
                    when(state) {
                        is UiState.Fail -> {}
                        is UiState.Success -> showSuccessState(state.data)
                        is UiState.Loading -> showLoadingState()
                        else -> showEmptyState()
                    }
                }

                viewModel.toasterMessage.collect { uiModel ->
                    uiModel?.apply {
                        Toaster.build(
                            binding?.root!!,
                            uiModel.message
                        ).show()
                    }
                }
            }
        }
    }

    private fun showSuccessState(data: List<RecipeUiModel>?) {
        if (!data.isNullOrEmpty()) {
            showRecipesList()
            adapter?.submitList(data)
        } else {
            showEmptyState()
        }
    }

    private fun showRecipesList() {
        binding?.apply {
            loader.hide()
            rvRecipeBookmark.show()
            emptyState.root.hide()
        }
    }

    private fun showEmptyState() {
        binding?.apply {
            loader.hide()
            rvRecipeBookmark.hide()
            emptyState.root.show()
            emptyState.ubSeeRecipes.setOnClickListener {

            }
        }
    }

    private fun showLoadingState() {
        binding?.apply {
            loader.show()
            rvRecipeBookmark.hide()
            emptyState.root.hide()
        }
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
        adapter = RecipeBookmarkAdapter(RecipeBookmarkAdapterTypeFactory())
        binding?.rvRecipeBookmark?.apply {
            adapter = this@TokoNowRecipeBookmarkFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecoration(context))
        }
    }

    private fun injectDependencies() {
        DaggerRecipeBookmarkComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }
}