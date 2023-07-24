package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSearchComposeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InitialSearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialSearchViewModel::class.java)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let { ComposeView(it) }?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        viewModel.getSellerSearch.removeObservers(this)
        viewModel.deleteHistorySearch.removeObservers(this)
        viewModel.insertSuccessSearch.removeObservers(this)
        super.onDestroy()
    }
}
