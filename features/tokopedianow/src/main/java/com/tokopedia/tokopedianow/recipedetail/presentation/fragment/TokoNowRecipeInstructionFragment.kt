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
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeInstructionBinding
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionAdapterTypeFactory
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeInstructionViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRecipeInstructionFragment: Fragment() {

    companion object {
        private const val EXTRA_INSTRUCTION_DATA = "extra_instruction_data"

        fun newInstance(data: InstructionTabUiModel): TokoNowRecipeInstructionFragment {
            return TokoNowRecipeInstructionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_INSTRUCTION_DATA, data)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecipeInstructionViewModel

    private val adapter by lazy { RecipeInstructionAdapter(RecipeInstructionAdapterTypeFactory()) }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeInstructionBinding>()

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
        val data = arguments?.getParcelable<InstructionTabUiModel>(EXTRA_INSTRUCTION_DATA)

        binding?.rvInstruction?.apply {
            adapter = this@TokoNowRecipeInstructionFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        observe(viewModel.itemList) {
            adapter.submitList(it)
        }

        viewModel.getLayout(data)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}