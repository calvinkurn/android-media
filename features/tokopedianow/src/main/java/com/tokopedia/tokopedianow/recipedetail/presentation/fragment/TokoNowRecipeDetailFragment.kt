package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeDetailBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeDetailFragment: Fragment(), RecipeDetailView {

    companion object {
        private const val KEY_PARAM_RECIPE_ID = "recipe_id"

        fun newInstance(): TokoNowRecipeDetailFragment {
            return TokoNowRecipeDetailFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeDetailViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeDetailBinding>()

    private val adapter by lazy { RecipeDetailAdapter(RecipeDetailAdapterTypeFactory(this)) }

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
        setupStatusBar()
        setupRecyclerView()
        observeLiveData()
        getRecipe()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun getFragmentActivity() = activity

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
    }

    private fun getRecipe() {
        context?.let {
            val recipeId = activity?.intent?.data?.getQueryParameter(KEY_PARAM_RECIPE_ID).orEmpty()
            val addressData = ChooseAddressUtils.getLocalizingAddressData(it)
            val warehouseId = addressData.warehouse_id
            viewModel.getRecipe(recipeId, warehouseId)
        }
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupStatusBar() {
        activity?.let {
            binding?.statusBar?.apply {
                val show = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                val drawable = binding?.statusBar?.background
                drawable?.alpha = 0

                binding?.statusBar?.background = drawable
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (show) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}