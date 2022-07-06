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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.view.ToolbarHeaderView
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeDetailBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeDetailFragment : Fragment(), RecipeDetailView {

    companion object {
        private const val KEY_PARAM_RECIPE_ID = "recipe_id"

        private const val RECIPE_INFO_POSITION = 1
        private const val BOOKMARK_BTN_POSITION = 0
        private const val SHARE_BTN_POSITION = 1

        fun newInstance(): TokoNowRecipeDetailFragment {
            return TokoNowRecipeDetailFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeDetailViewModel

    private val adapter by lazy { RecipeDetailAdapter(RecipeDetailAdapterTypeFactory(this)) }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeDetailBinding>()

    private var toolbarHeader: ToolbarHeaderView? = null

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
        setupHeader()
        setupRecyclerView()
        observeLiveData()
        getRecipe()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun getFragmentActivity() = activity

    private fun setupHeader() {
        setupToolbarHeader()
        setToolbarClickListener()
    }

    private fun setupToolbarHeader() {
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
                // Implement share recipe here
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

    private fun observeLiveData() {
        observe(viewModel.layoutList) {
            if (it is Success) {
                adapter.submitList(it.data)
            }
        }

        observe(viewModel.recipeInfo) {
            setHeaderTitle(it.title)
            setToolbarScrollListener()
        }
    }

    private fun getRecipe() {
        context?.let {
            val recipeId = activity?.intent?.data?.getQueryParameter(KEY_PARAM_RECIPE_ID).orEmpty()
            val addressData = ChooseAddressUtils.getLocalizingAddressData(it)
            val warehouseId = addressData.warehouse_id
            viewModel.getRecipe(recipeId, warehouseId)
        }
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
}