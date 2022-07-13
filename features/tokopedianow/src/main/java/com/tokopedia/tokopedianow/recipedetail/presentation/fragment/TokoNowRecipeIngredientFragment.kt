package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeIngredientBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeIngredientViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeIngredientFragment : Fragment() {

    companion object {
        private const val EXTRA_INGREDIENT_DATA = "extra_ingredient_data"

        fun newInstance(ingredient: IngredientTabUiModel): TokoNowRecipeIngredientFragment {
            return TokoNowRecipeIngredientFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_INGREDIENT_DATA, ingredient)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeIngredientViewModel

    private var adapter: RecipeIngredientAdapter? = null
    private var productListener: RecipeProductListener? = null

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeIngredientBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeIngredientBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments
            ?.getParcelable<IngredientTabUiModel>(EXTRA_INGREDIENT_DATA)

        setupRecyclerView()
        observeLiveData()

        viewModel.getLayout(data)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(RecipeIngredientAdapterTypeFactory(
            productListener = productListener
        ))

        binding?.rvIngredient?.apply {
            adapter = this@TokoNowRecipeIngredientFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observe(viewModel.itemList) {
            adapter?.submitList(it)
        }
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    fun setProductListener(productListener: RecipeProductListener) {
        this.productListener = productListener
    }
}