package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.Utils
import com.tokopedia.sellerpersona.data.remote.model.TogglePersonaModel
import com.tokopedia.sellerpersona.databinding.FragmentPersonaResultBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaSimpleListAdapter
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
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
        private const val CORPORATE_OWNER = "corporate-supervisor-owner"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PersonaResultViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PersonaResultViewModel::class.java)
    }
    private var isPersonaActive = false

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
        observePersonaToggleStatus()
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    private fun observePersonaToggleStatus() {
        viewLifecycleOwner.observe(viewModel.togglePersonaStatus) {
            dismissLoadingButton()
            when (it) {
                is Success -> setOnToggleSuccess(it.data)
                is Fail -> handleError(it.throwable)
            }
        }
    }

    private fun setOnToggleSuccess(toggleStatus: TogglePersonaModel) {
        if (toggleStatus.isError) {
            showToggleErrorMessage()
        } else {
            goToSellerHome()
        }
    }

    private fun goToSellerHome() {
        activity?.let {
            val toasterMessage = if (isPersonaActive) {
                it.getString(R.string.sp_persona_toggle_to_inactive_toaster_message)
            } else {
                it.getString(R.string.sp_persona_toggle_to_active_toaster_message)
            }
            val param = mapOf(
                SellerHomeApplinkConst.TOASTER_MESSAGE to toasterMessage,
                SellerHomeApplinkConst.TOASTER_CTA to it.getString(R.string.sp_oke)
            )
            val appLink = UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME, param)
            RouteManager.route(it, appLink)
            it.finish()
        }
    }

    private fun showToggleErrorMessage() {

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
            isPersonaActive = data.personaStatus == PersonaStatus.ACTIVE

            setTextPersonaActiveStatus(isPersonaActive)
            dismissLoadingState()
            setTipsVisibility(data.persona)
            setupSwitcher()

            val persona = data.personaData
            btnSpApplyPersona.gone()
            imgSpResultAvatar.loadImage(persona.avatarImage)
            imgSpResultBackdrop.loadImage(persona.backgroundImage)
            rvSpResultInfoList.adapter = PersonaSimpleListAdapter(persona.itemList)
            tvSpSellerType.text = String.format(PERSONA_TITLE, persona.headerTitle)
            tvSpSellerTypeNote.text = root.context.getString(
                R.string.sp_result_account_type, persona.headerSubTitle
            )
            tvSpResultInfoTitle.text = root.context.getString(
                R.string.sp_result_list_section_gedongan, persona.headerTitle
            )

            btnSpApplyPersona.setOnClickListener {
                val status = if (switchSpActivatePersona.isChecked) {
                    PersonaStatus.ACTIVE
                } else {
                    PersonaStatus.INACTIVE
                }
                btnSpApplyPersona.isLoading = true
                viewModel.toggleUserPersona(status)
            }
        }
    }

    private fun setupSwitcher() {
        binding?.run {
            switchSpActivatePersona.isChecked = isPersonaActive
            switchSpActivatePersona.setOnCheckedChangeListener { _, isChecked ->
                val shouldBtnVisible = isPersonaActive != isChecked
                if (shouldBtnVisible) {
                    btnSpApplyPersona.visible()
                    btnSpApplyPersona.text = root.context.getString(R.string.sp_apply_changes)
                } else {
                    btnSpApplyPersona.gone()
                }
                setTextPersonaActiveStatus(isChecked)
            }
        }
    }

    private fun setTextPersonaActiveStatus(isActive: Boolean) {
        val activeStatus = if (isActive) {
            R.string.sp_active
        } else {
            R.string.sp_inactive
        }
        binding?.run {
            tvSpLblActivatePersonaStatus.text = root.context.getString(activeStatus)
        }
    }

    private fun setTipsVisibility(persona: String) {
        binding?.run {
            if (persona == CORPORATE_OWNER) {
                dividerSpResultBottom.visible()
                tvSpOwnerInfo.visible()
                tvSpLblOwnerInfo.visible()
            } else {
                dividerSpResultBottom.gone()
                tvSpOwnerInfo.gone()
                tvSpLblOwnerInfo.gone()
            }
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

    private fun dismissLoadingButton() {
        binding?.btnSpApplyPersona?.isLoading = false
    }
}