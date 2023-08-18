package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.databinding.EpharmacyReminderScreenBottomSheetBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.viewmodel.EPharmacyLoadingViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EPharmacyLoadingFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyGlobalError: GlobalError? = null
    private var tConsultationId = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val ePharmacyLoadingViewModel: EPharmacyLoadingViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[EPharmacyLoadingViewModel::class.java]
    }

    private var binding by autoClearedNullable<EpharmacyReminderScreenBottomSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyReminderScreenBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        setUpObservers()
        initViews(view)
        getData()
    }

    private fun extractArguments() {
        tConsultationId = arguments?.getString(EPHARMACY_TOKO_CONSULTATION_ID, "") ?: ""
    }

    private fun setUpObservers() {
        observerVerifyConsulatationOrder()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
    }

    private fun getData() {
        ePharmacyLoadingViewModel.getVerifyConsultationOrder(tConsultationId)
    }

    private fun observerVerifyConsulatationOrder() {
        ePharmacyLoadingViewModel.ePharmacyVerifyConsultationData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessData(it)
                }
                is Fail -> {
                    onFailData(it)
                }
            }
        }
    }

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(Toaster.TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error) ?: "")
            else -> showToast(Toaster.TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail) ?: "")
        }
    }

    private fun onSuccessData(it: Success<EPharmacyVerifyConsultationResponse>) {
    }

    private fun onFailData(it: Fail) {
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        ePharmacyGlobalError?.setType(errorType)
        ePharmacyGlobalError?.visible()
        ePharmacyGlobalError?.setActionClickListener {
            ePharmacyGlobalError?.gone()
            getData()
        }
    }

    private fun showToast(type: Int = Toaster.TYPE_NORMAL, message: String) {
        if (message.isNotBlank()) {
            binding?.root?.rootView?.let { safeView ->
                Toaster.build(safeView, message, Toaster.LENGTH_LONG, type).show()
            }
        }
    }

    override fun getScreenName(): String = CategoryKeys.EPHARMACY_LOADING_PAGE

    override fun initInjector() {
        getComponent(EPharmacyComponent::class.java).inject(this)
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyLoadingFragment {
            val fragment = EPharmacyLoadingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
