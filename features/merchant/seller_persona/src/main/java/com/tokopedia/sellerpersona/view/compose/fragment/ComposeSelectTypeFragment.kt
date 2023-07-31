package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.compose.screen.selecttype.PersonSuccessState
import com.tokopedia.sellerpersona.view.compose.screen.selecttype.SelectTypeErrorState
import com.tokopedia.sellerpersona.view.compose.screen.selecttype.SelectTypeLoadingState
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposePersonaSelectTypeViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class ComposeSelectTypeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ComposePersonaSelectTypeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ComposePersonaSelectTypeViewModel::class.java]
    }
    private val args: ComposeSelectTypeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {
                LaunchedEffect(key1 = Unit, block = {
                    viewModel.fetchPersonaList(args = getSelectTypeArguments())

                    viewModel.uiEvent.collectLatest {
                        when (it) {
                            is SelectTypeUiEvent.CloseThePage -> closeThePage()
                            is SelectTypeUiEvent.OnPersonaChanged -> setOnPersonaChanged(it)
                            else -> {/* no-op */
                            }
                        }
                    }
                })

                NestTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                            val state = viewModel.state.collectAsState()
                            val data = state.value

                            when (data.state) {
                                is SelectTypeState.State.Loading -> SelectTypeLoadingState()
                                is SelectTypeState.State.Error -> SelectTypeErrorState(viewModel::onEvent)
                                is SelectTypeState.State.Success -> PersonSuccessState(
                                    data = data.data,
                                    onEvent = viewModel::onEvent
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setOnPersonaChanged(model: SelectTypeUiEvent.OnPersonaChanged) {
        val isSuccess = model.exception == null
        if (isSuccess) {
            val action = ComposeSelectTypeFragmentDirections.actionSelectTypeToResult(
                paramPersona = model.persona
            )
            findNavController().navigate(action)
        } else {
            onChangePersonaFailed()
        }
    }

    private fun closeThePage() {
        findNavController().navigateUp()
    }

    private fun onChangePersonaFailed() {
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

    private fun getSelectTypeArguments(): PersonaArgsUiModel {
        return PersonaArgsUiModel(paramPersona = args.paramPersona.orEmpty())
    }

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}