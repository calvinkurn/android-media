package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.sellerpersona.databinding.FragmentPersonaSelectTypeBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaTypeAdapter
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.sellerpersona.view.viewmodel.SelectPersonaTypeViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSelectTypeFragment : BaseFragment<FragmentPersonaSelectTypeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var personaTypeAdapter: PersonaTypeAdapter

    private val viewModel: SelectPersonaTypeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SelectPersonaTypeViewModel::class.java)
    }

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
        personaTypeAdapter.setItems(data)
        personaTypeAdapter.notifyItemRangeChanged(Int.ZERO, data.size.minus(Int.ONE))
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

            try {
                PagerSnapHelper().attachToRecyclerView(this)
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }
        }
    }
}