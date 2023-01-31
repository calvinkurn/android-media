package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.Utils
import com.tokopedia.sellerpersona.databinding.FragmentPersonaResultBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaSimpleListAdapter
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.viewmodel.PersonaResultViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaResultFragment : BaseFragment<FragmentPersonaResultBinding>() {

    companion object {
        private const val PERSONA_TITLE = "\uD83C\uDF1F %s \uD83C\uDF1F"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PersonaResultViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PersonaResultViewModel::class.java)
    }

    override fun bind(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentPersonaResultBinding {
        return FragmentPersonaResultBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPersonaList()
        setupView()
        observePersonaList()
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    private fun observePersonaList() {
        viewLifecycleOwner.observe(viewModel.personaList) {
            when (it) {
                is Success -> showPersonaData(it.data)
                is Fail -> handleError(it.throwable)
            }
        }
    }

    private fun handleError(throwable: Throwable) {

    }

    private fun showPersonaData(data: PersonaDataUiModel) {
        binding?.run {
            groupSpResultComponents.visible()
            dismissLoadingState()

            val persona = data.personaData
            imgSpResultAvatar.loadImage(persona.avatarImage)
            imgSpResultBackdrop.loadImage(persona.backgroundImage)
            rvSpResultInfoList.adapter = PersonaSimpleListAdapter(persona.itemList)
            tvSpSellerType.text = String.format(PERSONA_TITLE, persona.headerTitle)
            tvSpSellerTypeNote.text = persona.headerSubTitle
            tvSpResultInfoTitle.text = root.context.getString(
                R.string.sp_result_list_section, persona.headerTitle
            )
        }
    }

    private fun fetchPersonaList() {
        showLoadingState()
        viewModel.fetchPersonaList()
    }

    private fun showLoadingState() {
        binding?.run {
            loaderSpResult.visible()
            groupSpResultComponents.gone()
        }
    }

    private fun dismissLoadingState() {
        binding?.run {
            loaderSpResult.gone()
        }
    }

    private fun setupView() {
        binding?.run {
            val hexColor = Utils.getHexColor(
                root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            tvSpSelectManualType.text = root.context.getString(
                R.string.sp_persona_result_select_manual, hexColor
            ).parseAsHtml()
            btnSpRetryQuestionnaire.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.actionResultFragmentToQuestionnaireFragment)
            }
            tvSpSelectManualType.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.actionResultFragmentToSelectTyoeFragment)
            }
        }
    }
}