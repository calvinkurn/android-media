package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.tokopedia.sellerpersona.data.local.PersonaSharedPrefInterface
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.component.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.PersonaResultState
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.screen.personaresult.ResultLoadingState
import com.tokopedia.sellerpersona.view.compose.screen.personaresult.ResultSuccessState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposePersonaResultViewModel
import com.tokopedia.sellerpersona.view.model.isActive
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ComposeResultFragment : BaseComposeFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sharedPref: PersonaSharedPrefInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ComposePersonaResultViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ComposePersonaResultViewModel::class.java]
    }

    private val args: ComposeResultFragmentArgs by navArgs()
    private var isPersonaActive = false
    private var isAnyChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return setComposeViewContent(inflater.context) {
            LaunchedEffect(key1 = Unit, block = {
                viewModel.onEvent(ResultUiEvent.FetchPersonaData(getResultArguments()))
                viewModel.uiEffect.collectLatest {
                    when (it) {
                        is ResultUiEffect.NavigateToQuestionnaire -> navigateToQuestionnaire()
                        is ResultUiEffect.NavigateToSelectPersona -> navigateToSelectPersona(it)
                        is ResultUiEffect.OnPersonaStatusChanged -> onPersonaStatusChanged(it)
                        is ResultUiEffect.SendClickApplyTracking -> sendClickApplyTracking(it)
                        is ResultUiEffect.SendSwitchCheckedChangedTracking -> {
                            SellerPersonaTracking.sendClickSellerPersonaResultToggleActiveEvent()
                        }

                        is ResultUiEffect.SendImpressionResultTracking -> {
                            SellerPersonaTracking.sendImpressionSellerPersonaResultEvent()
                        }
                    }
                }
            })

            NestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val state = viewModel.personaState.collectAsStateWithLifecycle()

                    when (state.value.state) {
                        is PersonaResultState.State.Loading -> ResultLoadingState()
                        is PersonaResultState.State.Error -> {
                            ErrorStateComponent(
                                actionText = stringResource(id = R.string.sp_reload),
                                title = stringResource(id = R.string.sp_common_global_error_title),
                                onActionClicked = {
                                    viewModel.onEvent(ResultUiEvent.Reload)
                                }
                            )
                        }

                        is PersonaResultState.State.Success -> {
                            updateActiveStatusFlag(state.value.data.personaStatus.isActive())
                            updateAnyChangesFlag(state.value.data.isSwitchChecked)
                            ResultSuccessState(
                                data = state.value.data,
                                hasImpressed = state.value.hasImpressed,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    override fun setOnBackPressed() {
        val context = context ?: return
        val paramPersona = args.paramPersona
        if (paramPersona.isNotBlank() || (paramPersona.isBlank() && isAnyChanges)) {
            val dialog = DialogUnify(
                context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE
            )
            with(dialog) {
                setTitle(context.getString(R.string.sp_poup_exit_title))
                setDescription(context.getString(R.string.sp_popup_exit_result_description))
                setPrimaryCTAText(context.getString(R.string.sp_popup_exit_result_primary_cta))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAText(context.getString(R.string.sp_popup_exit_secondary_cta))
                setSecondaryCTAClickListener {
                    dismiss()
                    if (sharedPref.isFirstVisit()) {
                        val appLink = ApplinkConstInternalSellerapp.SELLER_HOME
                        RouteManager.route(context, appLink)
                    }
                    activity?.finish()
                }
                show()
            }
        } else {
            activity?.finish()
        }
    }

    private fun getResultArguments(): PersonaArgsUiModel {
        val paramPersona = args.paramPersona
        return PersonaArgsUiModel(paramPersona = paramPersona)
    }

    private fun onPersonaStatusChanged(data: ResultUiEffect.OnPersonaStatusChanged) {
        updateActiveStatusFlag(data.personaStatus.isActive())
        if (data.isSuccess()) {
            goToSellerHome()
        } else {
            showGeneralErrorToaster()
        }
    }

    private fun updateAnyChangesFlag(isSwitchChecked: Boolean) {
        isAnyChanges = isSwitchChecked != isPersonaActive
    }

    private fun updateActiveStatusFlag(isActive: Boolean) {
        isPersonaActive = isActive
    }

    private fun sendClickApplyTracking(data: ResultUiEffect.SendClickApplyTracking) {
        SellerPersonaTracking.sendClickSellerPersonaResultSavePersonaEvent(
            persona = data.persona, isActive = data.isActive
        )
    }

    private fun navigateToSelectPersona(data: ResultUiEffect.NavigateToSelectPersona) {
        val view = view ?: return
        val action = ComposeResultFragmentDirections.actionToSelectTypeScreen(data.currentPersona)
        Navigation.findNavController(view).navigate(action)
        SellerPersonaTracking.sendClickSellerPersonaResultSelectPersonaEvent()
    }

    private fun navigateToQuestionnaire() {
        val view = view ?: return
        val action = ComposeResultFragmentDirections.actionResultFragmentToQuestionnaireFragment()
        Navigation.findNavController(view).navigate(action)
        SellerPersonaTracking.sendClickSellerPersonaResultRetakeQuizEvent()
    }

    private fun goToSellerHome() {
        val activity = activity ?: return
        val toasterMessage = if (isPersonaActive) {
            activity.getString(R.string.sp_persona_toggle_to_active_toaster_message)
        } else {
            activity.getString(R.string.sp_persona_toggle_to_inactive_toaster_message)
        }
        val param = mapOf(
            SellerHomeApplinkConst.TOASTER_MESSAGE to toasterMessage,
            SellerHomeApplinkConst.TOASTER_CTA to activity.getString(R.string.sp_oke),
            SellerHomeApplinkConst.IS_PERSONA to true.toString()
        )
        val appLink =
            UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME, param)
        RouteManager.route(activity, appLink)
        activity.finish()
    }

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}