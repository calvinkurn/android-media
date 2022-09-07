package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeIngredientBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.OutOfCoverageViewHolder.OutOfCoverageListener
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeIngredientViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeIngredientFragment : Fragment(), RecipeProductListener, OutOfCoverageListener {

    companion object {

        fun newInstance(): TokoNowRecipeIngredientFragment {
            return TokoNowRecipeIngredientFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeIngredientViewModel

    private var items: List<Visitable<*>> = emptyList()
    private var adapter: RecipeIngredientAdapter? = null
    private var recipeDetailView: RecipeDetailView? = null

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
        setupRecyclerView()
        observeLiveData()
        onViewCreated()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun deleteCartItem(productId: String) {
        recipeDetailView?.deleteCartItem(productId)
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        recipeDetailView?.onQuantityChanged(productId, shopId, quantity)
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        recipeDetailView?.addItemToCart(productId, shopId, quantity)
    }

    override fun onCLickChangeAddress() {
        recipeDetailView?.showChooseAddressBottomSheet()
    }

    fun setRecipeDetailView(recipeDetailView: RecipeDetailView) {
        this.recipeDetailView = recipeDetailView
    }

    fun setItemList(itemList: List<Visitable<*>>) {
        this.items = itemList
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(
            RecipeIngredientAdapterTypeFactory(
                productListener = this,
                outOfCoverageListener = this
            )
        )

        binding?.rvIngredient?.apply {
            adapter = this@TokoNowRecipeIngredientFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observe(viewModel.itemList) {
            submitList(it)
        }
    }

    private fun submitList(items: List<Visitable<*>>) {
        adapter?.submitList(items)
    }

    private fun onViewCreated() {
        viewModel.onViewCreated(items)
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}