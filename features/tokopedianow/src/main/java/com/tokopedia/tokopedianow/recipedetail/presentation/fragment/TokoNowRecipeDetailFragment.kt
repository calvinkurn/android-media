package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeDetailBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeDetailFragment: Fragment() {

    companion object {
        fun newInstance(): TokoNowRecipeDetailFragment {
            return TokoNowRecipeDetailFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeDetailViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeDetailBinding>()

    private var adapter: RecipeDetailAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun setupRecyclerView() {
        adapter = RecipeDetailAdapter(RecipeDetailAdapterTypeFactory())
        binding?.rvRecipeDetail?.apply {
            adapter = this@TokoNowRecipeDetailFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}