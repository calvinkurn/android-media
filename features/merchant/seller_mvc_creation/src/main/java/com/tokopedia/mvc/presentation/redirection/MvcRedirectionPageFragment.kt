package com.tokopedia.mvc.presentation.redirection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvc.databinding.SmvcFragmentMvcRedirectionPageBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.redirection.uimodel.MvcRedirectionPageAction
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcRedirectionPageFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<SmvcFragmentMvcRedirectionPageBinding>()
    private val loader: LoaderUnify? by lazy {
        binding?.loader
    }
    private val globalError: GlobalError? by lazy {
        binding?.globalError
    }

    @Inject
    lateinit var viewModel: MvcRedirectionPageViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcRedirectionPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        observeUiAction()
        getRedirectionAppLink()
    }

    private fun getRedirectionAppLink() {
        showLoading()
        viewModel.getRedirectionAppLink()
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated{
            viewModel.uiAction.collect { action ->
                when (action) {
                    is MvcRedirectionPageAction.RedirectTo -> {
                        redirectToGivenAppLink(action.redirectionAppLink)
                    }
                    is MvcRedirectionPageAction.ShowError -> {
                        showGlobalError(action.error)
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        loader?.visibility = View.GONE
    }

    private fun showLoading() {
        loader?.visibility  = View.VISIBLE
    }

    private fun showGlobalError(error: Throwable) {
        hideLoading()
        globalError?.apply {
            if (error is MessageErrorException) {
                setType(GlobalError.SERVER_ERROR)
            } else {
                setType(GlobalError.NO_CONNECTION)
            }
            setActionClickListener {
                hide()
                getRedirectionAppLink()
            }
            show()
        }
    }

    private fun redirectToGivenAppLink(redirectionAppLink: String) {
        hideLoading()
        RouteManager.route(context, redirectionAppLink)
        activity?.finish()
    }

}
