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
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeInstructionBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionAdapterTypeFactory
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowRecipeInstructionFragment: Fragment() {

    companion object {
        fun newInstance(): TokoNowRecipeInstructionFragment {
            return TokoNowRecipeInstructionFragment()
        }
    }

    private val adapter by lazy { RecipeInstructionAdapter(RecipeInstructionAdapterTypeFactory()) }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeInstructionBinding>()

    private var items: List<Visitable<*>> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeInstructionBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvInstruction?.apply {
            adapter = this@TokoNowRecipeInstructionFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        submitList(items)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    fun setItemList(items: List<Visitable<*>>) {
        this.items = items
        submitList(items)
    }

    private fun submitList(items: List<Visitable<*>>) {
        adapter.submitList(items)
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}