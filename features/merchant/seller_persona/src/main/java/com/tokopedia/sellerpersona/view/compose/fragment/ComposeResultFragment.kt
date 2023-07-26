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
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.analytics.SellerPersonaTracking
import com.tokopedia.sellerpersona.data.local.PersonaSharedPref
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.component.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.screen.personaresult.PersonaResultScreen
import com.tokopedia.sellerpersona.view.compose.screen.personaresult.ResultLoadingState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposePersonaResultViewModel
import com.tokopedia.sellerpersona.view.model.isActive
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
    private var isAnyChanges = false

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
                    viewModel.fetchPersonaData(getResultArguments())

                    viewModel.uiEvent.collectLatest {
                        when (it) {
                            is ResultUiEvent.RetakeQuiz -> retakeQuiz()
                            is ResultUiEvent.ApplyChanges -> sendClickApplyTracker(it)
                            is ResultUiEvent.OnPersonaStatusChanged -> onPersonaStatusChanged(it)
                            is ResultUiEvent.SelectPersona -> selectPersonaType(it)
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

                            else -> {
                                markForCheckedChanged(state.value.data.isSwitchChecked)
                                updatePersonaStatusFlag(state.value.data.personaStatus.isActive())
                                PersonaResultScreen(state.value, viewModel::onEvent)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressed()
    }

    private fun getResultArguments(): PersonaArgsUiModel {
        val paramPersona = args.paramPersona
        return PersonaArgsUiModel(paramPersona = paramPersona)
    }

    private fun onPersonaStatusChanged(data: ResultUiEvent.OnPersonaStatusChanged) {
        updatePersonaStatusFlag(data.personaStatus.isActive())
        if (data.isSuccess()) {
            goToSellerHome()
        } else {
            showToggleErrorMessage()
        }
    }

    private fun markForCheckedChanged(isSwitchChecked: Boolean) {
        isAnyChanges = isSwitchChecked != isPersonaActive
    }

    private fun updatePersonaStatusFlag(isActive: Boolean) {
        isPersonaActive = isActive
    }

    private fun sendClickApplyTracker(data: ResultUiEvent.ApplyChanges) {
        SellerPersonaTracking.sendClickSellerPersonaResultSavePersonaEvent(
            persona = data.persona, isActive = data.isActive
        )
    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, backPressedCallback
        )
    }

    private fun selectPersonaType(data: ResultUiEvent.SelectPersona) {
        view?.let {
            val action = ComposeResultFragmentDirections
                .actionToSelectTypeScreen(data.currentPersona)
            Navigation.findNavController(it).navigate(action)
            SellerPersonaTracking.sendClickSellerPersonaResultSelectPersonaEvent()
        }
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

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}