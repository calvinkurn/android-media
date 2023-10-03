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
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUiEvent
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireErrorState
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireLoadingState
import com.tokopedia.sellerpersona.view.compose.screen.questionnaire.QuestionnaireSuccessState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposeQuestionnaireViewModel
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                LaunchedEffect(key1 = Unit, block = {
                    viewModel.onEvent(QuestionnaireUiEvent.FetchQuestionnaire)
                })

                NestTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val state = viewModel.state.collectAsStateWithLifecycle()

                        when (val current = state.value) {
                            is QuestionnaireState.Loading -> QuestionnaireLoadingState()
                            is QuestionnaireState.Error -> QuestionnaireErrorState(onEvent = viewModel::onEvent)
                            is QuestionnaireState.Success -> QuestionnaireSuccessState(
                                data = current.data.questionnaireList,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}