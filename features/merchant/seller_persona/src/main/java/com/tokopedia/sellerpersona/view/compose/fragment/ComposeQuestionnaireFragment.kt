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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uieffect.QuestionnaireUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUserEvent
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireErrorState
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireLoadingState
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireSuccessState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposeQuestionnaireViewModel
import com.tokopedia.sellerpersona.view.fragment.PersonaQuestionnaireFragmentDirections
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

class ComposeQuestionnaireFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ComposeQuestionnaireViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                LaunchedEffect(key1 = Unit, block = {
                    viewModel.uiEffect.collectLatest {
                        when (it) {
                            is QuestionnaireUiEffect.NavigateToResultPage -> navigateToResultPage(it.personaName)
                            is QuestionnaireUiEffect.ShowGeneralErrorToast -> showGeneralErrorToaster()
                        }
                    }
                })

                LaunchedEffect(key1 = Unit, block = {
                    viewModel.onEvent(QuestionnaireUserEvent.FetchQuestionnaire)
                })

                NestTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                    ) {
                        val state = viewModel.state.collectAsStateWithLifecycle()

                        when (val current = state.value) {
                            is QuestionnaireState.Loading -> QuestionnaireLoadingState()
                            is QuestionnaireState.Error -> QuestionnaireErrorState(onEvent = viewModel::onEvent)
                            is QuestionnaireState.Success -> QuestionnaireSuccessState(
                                data = current.data,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateToResultPage(personaName: String) {
        view?.let {
            val action = ComposeQuestionnaireFragmentDirections.actionQuestionnaireToResult(
                paramPersona = personaName
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun showGeneralErrorToaster() {
        view?.run {
            val dp48 = context.resources.getDimensionPixelSize(
                unifyprinciplesR.dimen.layout_lvl6
            )
            Toaster.toasterCustomBottomHeight = dp48
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