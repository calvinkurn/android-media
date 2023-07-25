package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.screen.SelectPersonaTypeScreen
import com.tokopedia.sellerpersona.view.compose.viewmodel.ComposePersonaSelectTypeViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {
                LaunchedEffect(key1 = Unit, block = {
                    viewModel.fetchPersonaList(getSelectTypeArguments())
                })

                NestTheme {
                    val state = viewModel.state.collectAsState()
                    SelectPersonaTypeScreen(data = state.value, onEvent = viewModel::onEvent)
                }
            }
        }
    }

    private fun getSelectTypeArguments(): PersonaArgsUiModel {
        return PersonaArgsUiModel(
            paramPersona = args.paramPersona.orEmpty()
        )
    }

    private fun inject() {
        (activity as? SellerPersonaActivity)?.component?.inject(this)
    }
}