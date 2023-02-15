package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.FragmentPersonaSelectTypeBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaTypeAdapter
import com.tokopedia.sellerpersona.view.adapter.viewholder.PersonaTypeViewHolder
import com.tokopedia.sellerpersona.view.model.PersonaTypeLoadingUiModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.sellerpersona.view.viewhelper.PersonaTypeItemDecoration
import com.tokopedia.sellerpersona.view.viewmodel.SelectPersonaTypeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSelectTypeFragment : BaseFragment<FragmentPersonaSelectTypeBinding>(),
    PersonaTypeViewHolder.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SelectPersonaTypeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SelectPersonaTypeViewModel::class.java)
    }
    private val args: PersonaSelectTypeFragmentArgs by navArgs()
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

        setupView()
        setupRecyclerView()
        fetchPersonaList()

        observePersonaList()
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

    private fun setupView() {
        binding?.btnSpSelectType?.setOnClickListener { _ ->
            setOnApplyButtonClicked()
        }
    }

    private fun setOnApplyButtonClicked() {
        val selectedPersona = personaTypeAdapter.getItems().firstOrNull { it.isSelected }
        selectedPersona?.let { persona ->
            binding?.btnSpSelectType?.isLoading = true
            viewModel.setPersona(persona.value)
            viewModel.setPersonaResult.observeOnce(viewLifecycleOwner) {
                when (it) {
                    is Success -> onSuccessSetPersona(it.data)
                    is Fail -> onFailingSetPersona()
                }
            }
        }
    }

    private fun onFailingSetPersona() {
        binding?.btnSpSelectType?.isLoading = false
        view?.run {
            val dp64 = context.resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl7
            )
            Toaster.toasterCustomBottomHeight = dp64
            Toaster.build(
                rootView,
                context.getString(R.string.sp_toaster_error_message),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                context.getString(R.string.sp_oke)
            ).show()
        }
    }

    private fun onSuccessSetPersona(persona: String) {
        val selectedPersona = personaTypeAdapter.getItems().firstOrNull { it.value == persona }
        selectedPersona?.let {
            val action = PersonaSelectTypeFragmentDirections.actionSelectTypeToResult(
                paramPersona = persona
            )
            findNavController().navigate(action)
        }
    }

    private fun observePersonaList() {
        viewLifecycleOwner.observe(viewModel.personaList) {
            dismissLoadingState()
            when (it) {
                is Success -> showPersonaList(it.data)
                is Fail -> showErrorState()
            }
        }
    }

    private fun showPersonaList(data: List<PersonaUiModel>) {
        binding?.run {
            val persona = args.paramPersona
            data.forEach {
                it.isSelected = it.value == persona
            }
            personaTypeAdapter.clearAllElements()
            personaTypeAdapter.addElement(data)
            dividerSpSelectType.visible()
            btnSpSelectType.visible()
        }
    }

    private fun showErrorState() {
        binding?.run {
            dismissLoadingState()
            dividerSpSelectType.visible()
            btnSpSelectType.visible()
            errorViewPersonaSelectType.visible()
            errorViewPersonaSelectType.setOnActionClicked {
                fetchPersonaList()
            }
        }
    }

    private fun fetchPersonaList() {
        showLoadingState()
        viewModel.fetchPersonaList()
    }

    private fun showLoadingState() {
        binding?.run {
            btnSpSelectType.gone()
            dividerSpSelectType.gone()
            errorViewPersonaSelectType.gone()

            val loadingItems = listOf(
                PersonaTypeLoadingUiModel,
                PersonaTypeLoadingUiModel,
                PersonaTypeLoadingUiModel
            )

            personaTypeAdapter.clearAllElements()
            personaTypeAdapter.addElement(loadingItems)
        }
    }

    private fun dismissLoadingState() {
        personaTypeAdapter.clearAllElements()
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