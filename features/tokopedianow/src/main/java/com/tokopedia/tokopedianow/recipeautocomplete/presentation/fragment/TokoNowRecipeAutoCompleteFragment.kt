package com.tokopedia.tokopedianow.recipeautocomplete.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRecipeAutoCompleteBinding
import com.tokopedia.tokopedianow.recipeautocomplete.di.component.DaggerRecipeAutoCompleteComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowRecipeAutoCompleteFragment: Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return TokoNowRecipeAutoCompleteFragment()
        }
    }

    private var binding by autoClearedNullable<FragmentTokopedianowRecipeAutoCompleteBinding>()

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRecipeAutoCompleteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun injectDependencies() {
        DaggerRecipeAutoCompleteComponent
            .builder()
            .baseAppComponent((requireContext().applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }
}