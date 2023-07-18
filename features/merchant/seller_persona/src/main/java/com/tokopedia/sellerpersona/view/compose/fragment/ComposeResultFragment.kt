package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.analytics.SellerPersonaTracking
import com.tokopedia.sellerpersona.data.local.PersonaSharedPref
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.common.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.ResultArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.screen.PersonaResultScreen
import com.tokopedia.sellerpersona.view.compose.screen.ResultLoadingState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposePersonaResultViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ComposeResultFragment : Fragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sharedPref: PersonaSharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ComposePersonaResultViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ComposePersonaResultViewModel::class.java]
    }

    private val args: ComposeResultFragmentArgs by navArgs()
    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@ComposeResultFragment.handleOnBackPressed()
            }
        }
    }
    private var isPersonaActive = false

    private val impressHolder by lazy { ImpressHolder() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {

                LaunchedEffect(key1 = Unit, block = {
                    viewModel.setArguments(getResultArguments())
                    viewModel.fetchPersonaData()

                    viewModel.uiEvent.collectLatest {
                        when (it) {
                            is ResultUiEvent.RetakeQuiz -> retakeQuiz()
                            is ResultUiEvent.ApplyChanges -> sendClickApplyTracker(it)
                            is ResultUiEvent.OnPersonaStatusChanged -> onPersonaStatusChanged(it)
                            else -> {/* no-op */
                            }
                        }
                    }
                })

                NestTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        val state = viewModel.personaState.collectAsState()

                        val isLoading = state.value.isLoading
                        val isError = state.value.error != null

                        when {
                            isLoading -> ResultLoadingState()
                            isError -> {
                                ErrorStateComponent(actionText = stringResource(id = R.string.sp_reload),
                                    title = stringResource(id = R.string.sp_common_global_error_title),
                                    onActionClicked = {
                                        viewModel.onEvent(ResultUiEvent.Reload)
                                    })
                            }

                            else -> PersonaResultScreen(state.value.data, viewModel::onEvent)
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupOnBackPressed()
    }

    private fun getResultArguments(): ResultArgsUiModel {
        return ResultArgsUiModel(paramPersona = args.paramPersona)
    }

    private fun onPersonaStatusChanged(data: ResultUiEvent.OnPersonaStatusChanged) {
        if (data.isSuccess) {
            goToSellerHome()
        } else {
            showToggleErrorMessage()
        }
    }

    private fun sendClickApplyTracker(data: ResultUiEvent.ApplyChanges) {
        SellerPersonaTracking.sendClickSellerPersonaResultSavePersonaEvent(
            persona = data.persona,
            isActive = data.isActive
        )
    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, backPressedCallback
        )
    }

    private fun retakeQuiz() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.actionResultFragmentToQuestionnaireFragment)
            SellerPersonaTracking.sendClickSellerPersonaResultRetakeQuizEvent()
        }
    }

    private fun handleOnBackPressed() {
        context?.let {
            val paramPersona = args.paramPersona
            val isAnyChanges = false//binding?.switchSpActivatePersona?.isChecked != isPersonaActive
            if (paramPersona.isNotBlank() || (paramPersona.isBlank() && isAnyChanges)) {
                val dialog = DialogUnify(
                    it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE
                )
                with(dialog) {
                    setTitle(it.getString(R.string.sp_poup_exit_title))
                    setDescription(it.getString(R.string.sp_popup_exit_result_description))
                    setPrimaryCTAText(it.getString(R.string.sp_popup_exit_result_primary_cta))
                    setPrimaryCTAClickListener {
                        dismiss()
                    }
                    setSecondaryCTAText(it.getString(R.string.sp_popup_exit_secondary_cta))
                    setSecondaryCTAClickListener {
                        dismiss()
                        if (sharedPref.isFirstVisit) {
                            val appLink = ApplinkConstInternalSellerapp.SELLER_HOME
                            RouteManager.route(it, appLink)
                        }
                        activity?.finish()
                    }
                    show()
                }
            } else {
                activity?.finish()
            }
        }
    }

    private fun goToSellerHome() {
        activity?.let {
            val toasterMessage = if (isPersonaActive) {
                it.getString(R.string.sp_persona_toggle_to_active_toaster_message)
            } else {
                it.getString(R.string.sp_persona_toggle_to_inactive_toaster_message)
            }
            val param = mapOf(
                SellerHomeApplinkConst.TOASTER_MESSAGE to toasterMessage,
                SellerHomeApplinkConst.TOASTER_CTA to it.getString(R.string.sp_oke),
                SellerHomeApplinkConst.IS_PERSONA to true.toString()
            )
            val appLink =
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME, param)
            RouteManager.route(it, appLink)
            it.finish()
        }
    }

    private fun showToggleErrorMessage() {
        view?.run {
            dismissLoadingButton()
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

//    private fun observePersonaData() {
//        viewLifecycleOwner.observe(viewModel.state) {
//            dismissLoadingState()
//            when (it) {
//                is Success -> {
//                    this.personaData = it.data
//                    showPersonaData()
//                }
//
//                is Fail -> handleError()
//            }
//        }
//    }

    private fun handleError() {
//        binding?.run {
//            dismissLoadingState()
//            tvSpLblActivatePersonaStatus.gone()
//            btnSpApplyPersona.gone()
//            groupSpResultComponents.gone()
//            errorViewPersonaResult.visible()
//            errorViewPersonaResult.setOnActionClicked {
//                viewModel.fetchPersonaData()
//            }
//        }
    }

    private fun showPersonaData() {
//        val data = personaData ?: return
//        binding?.run {
//            errorViewPersonaResult.gone()
//            groupSpResultComponents.visible()
//            isPersonaActive = data.personaStatus == PersonaStatus.ACTIVE
//
//            setTextPersonaActiveStatus(true)
//            setTipsVisibility(data.persona)
//            setupSwitcher()
//            setupApplyButton()
//
//            val persona = data.personaData
//            imgSpResultAvatar.loadImage(persona.avatarImage)
//            imgSpResultBackdrop.loadImage(persona.backgroundImage)
//            rvSpResultInfoList.adapter = PersonaSimpleListAdapter().apply {
//                setItems(persona.itemList)
//                isSelectedMode = !root.context.isDarkMode()
//            }
//            tvSpSellerType.text = String.format(PERSONA_TITLE, persona.headerTitle)
//            tvSpSellerTypeNote.text = root.context.getString(
//                R.string.sp_result_account_type, persona.headerSubTitle
//            )
//            tvSpResultInfoTitle.text = root.context.getString(
//                R.string.sp_result_list_section_gedongan, persona.headerTitle
//            )
//
//            btnSpApplyPersona.setOnClickListener {
//                toggleSellerPersona()
//                SellerPersonaTracking.sendClickSellerPersonaResultSavePersonaEvent(
//                    persona.value,
//                    switchSpActivatePersona.isChecked
//                )
//            }
//        }
    }

    private fun toggleSellerPersona() {
//        binding?.run {
//            val status = if (switchSpActivatePersona.isChecked) {
//                PersonaStatus.ACTIVE
//            } else {
//                PersonaStatus.INACTIVE
//            }
//            viewModel.toggleUserPersona(status)
//            btnSpApplyPersona.isLoading = true
//        }
    }

    private fun setupApplyButton() {
//        binding?.run {
//            if (args.paramPersona.isNotBlank()) {
//                btnSpApplyPersona.visible()
//                setApplyButtonText()
//            } else {
//                btnSpApplyPersona.gone()
//            }
//        }
    }

    private fun setApplyButtonText() {
//        binding?.run {
//            btnSpApplyPersona.text = if (sharedPref.isFirstVisit) {
//                root.context.getString(R.string.sp_apply)
//            } else {
//                root.context.getString(R.string.sp_apply_changes)
//            }
//        }
    }

    private fun setupSwitcher() {
//        binding?.run {
//            if (args.paramPersona.isBlank()) {
//                switchSpActivatePersona.isChecked = isPersonaActive
//                setTextPersonaActiveStatus(isPersonaActive)
//            }
//            switchSpActivatePersona.setOnCheckedChangeListener { _, isChecked ->
//                if (args.paramPersona.isBlank()) {
//                    if (isPersonaActive != isChecked) {
//                        btnSpApplyPersona.visible()
//                        setApplyButtonText()
//                    } else {
//                        btnSpApplyPersona.gone()
//                    }
//                }
//                setTextPersonaActiveStatus(isChecked)
//                SellerPersonaTracking.sendClickSellerPersonaResultToggleActiveEvent()
//            }
//        }
    }

    private fun setTextPersonaActiveStatus(isActive: Boolean) {
//        val activeStatus = if (isActive) {
//            R.string.sp_active
//        } else {
//            R.string.sp_inactive
//        }
//        binding?.run {
//            tvSpLblActivatePersonaStatus.visible()
//            tvSpLblActivatePersonaStatus.text = root.context.getString(activeStatus)
//        }
    }

    private fun setTipsVisibility(persona: String) {
//        binding?.run {
//            if (persona == CORPORATE_EMPLOYEE && userSession.isShopOwner) {
//                dividerSpResultBottom.visible()
//                icSpOwnerInfo.visible()
//                tvSpLblOwnerInfo.visible()
//            } else {
//                dividerSpResultBottom.gone()
//                icSpOwnerInfo.gone()
//                tvSpLblOwnerInfo.gone()
//            }
//        }
    }

    private fun showLoadingState() {
//        binding?.run {
//            loaderSpResult.visible()
//            groupSpResultComponents.gone()
//        }
    }

    private fun dismissLoadingState() {
//        binding?.loaderSpResult?.gone()
    }

    private fun setupView() {
//        binding?.run {
//            val hexColor = Utils.getHexColor(
//                root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500
//            )
//            tvSpSelectManualType.text = root.context.getString(
//                R.string.sp_persona_result_select_manual, hexColor
//            ).parseAsHtml()
//            btnSpRetryQuestionnaire.setOnClickListener {
//                it.findNavController()
//                    .navigate(R.id.actionResultFragmentToQuestionnaireFragment)
//                SellerPersonaTracking.sendClickSellerPersonaResultRetakeQuizEvent()
//            }
//            tvSpSelectManualType.setOnClickListener {
//                val action = PersonaResultFragmentDirections
//                    .actionResultFragmentToSelectTypeFragment(personaData?.persona)
//                it.findNavController().navigate(action)
//                SellerPersonaTracking.sendClickSellerPersonaResultSelectPersonaEvent()
//            }
//
//            root.addOnImpressionListener(impressHolder) {
//                SellerPersonaTracking.sendImpressionSellerPersonaResultEvent()
//            }
//        }
    }

    private fun dismissLoadingButton() {
//        binding?.btnSpApplyPersona?.isLoading = false
    }

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}