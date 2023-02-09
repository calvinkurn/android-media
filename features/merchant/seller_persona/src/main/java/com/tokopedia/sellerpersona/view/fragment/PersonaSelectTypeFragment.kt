package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.sellerpersona.databinding.FragmentPersonaSelectTypeBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaTypeAdapter
import com.tokopedia.sellerpersona.view.adapter.viewholder.PersonaTypeViewHolder
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.sellerpersona.view.viewhelper.PersonaTypeItemDecoration
import com.tokopedia.sellerpersona.view.viewmodel.SelectPersonaTypeViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSelectTypeFragment : BaseFragment<FragmentPersonaSelectTypeBinding>(),
    PersonaTypeViewHolder.Listener {

    companion object {
        const val KEY_SELECTED_PERSONA = "key_selected_persona"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SelectPersonaTypeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SelectPersonaTypeViewModel::class.java)
    }
    private val personaTypeAdapter by lazy { PersonaTypeAdapter(this) }
    private val itemDecoration by lazy { PersonaTypeItemDecoration() }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaSelectTypeBinding {
        return FragmentPersonaSelectTypeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPersonaList()

        setupRecyclerView()
        observePersonaList()
        onApplyButtonClicked()
    }

    private fun onApplyButtonClicked() {
        binding?.btnSpSelectType?.setOnClickListener { _ ->
            val selectedPersona = personaTypeAdapter.getItems().firstOrNull { it.isSelected }
            selectedPersona?.let {
                findNavController().previousBackStackEntry?.savedStateHandle?.set<PersonaUiModel>(
                    KEY_SELECTED_PERSONA, it
                )
                findNavController().navigateUp()
            }
        }
    }

    override fun onItemClickListener(item: PersonaUiModel) {
        if (item.isSelected) {
            personaTypeAdapter.getItems().forEachIndexed { i, persona ->
                if (item.value != persona.value) {
                    persona.isSelected = false
                    try {
                        personaTypeAdapter.notifyItemChanged(i)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun observePersonaList() {
        viewLifecycleOwner.observe(viewModel.personaList) {
            when (it) {
                is Success -> showPersonaList(it.data)
                is Fail -> showErrorState(it.throwable)
            }
        }
    }

    private fun showPersonaList(data: List<PersonaUiModel>) {
        binding?.rvSpSelectType?.post {
            personaTypeAdapter.clearAllElements()
            personaTypeAdapter.addElement(data)
        }
    }

    private fun showErrorState(throwable: Throwable) {

    }

    private fun fetchPersonaList() {
        viewModel.fetchPersonaList()
    }

    private fun setupRecyclerView() {
        binding?.rvSpSelectType?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = personaTypeAdapter
            removeItemDecoration(itemDecoration)
            addItemDecoration(itemDecoration)

            try {
                PagerSnapHelper().attachToRecyclerView(this)
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }
        }
    }
}