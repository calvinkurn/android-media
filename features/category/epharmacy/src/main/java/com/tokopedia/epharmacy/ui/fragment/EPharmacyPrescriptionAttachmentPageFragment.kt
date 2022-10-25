package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EPharmacyPrescriptionAttachmentPageFragment : BaseDaggerFragment() , EPharmacyListener {

    private var ePharmacyRecyclerView : RecyclerView? = null
    private var ePharmacyDoneButton : UnifyButton? = null
    private var ePharmacyLoader : LoaderUnify? = null
    private var ePharmacyGlobalError : GlobalError? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val ePharmacyPrescriptionAttachmentViewModel: EPharmacyPrescriptionAttachmentViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(EPharmacyPrescriptionAttachmentViewModel::class.java)
    }

    private var ePharmacyUiUpdater: EPharmacyUiUpdater = EPharmacyUiUpdater(mutableMapOf())

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(this) }

    private var orderId = DEFAULT_ZERO_VALUE
    private var checkoutId = ""
    private var entryPoint = ""

    private val ePharmacyAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel> = AsyncDifferConfig.Builder(
            EPharmacyDetailDiffUtil()
        )
            .build()
        EPharmacyAdapter(asyncDifferConfig, ePharmacyAdapterFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.epharmacy_upload_prescription_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        setUpObservers()
        initViews(view)
        initData()
        getData()
    }

    private fun initArguments() {
        orderId = arguments?.getLong(EXTRA_ORDER_ID_LONG)  ?: 0L
        checkoutId = arguments?.getString(EXTRA_CHECKOUT_ID_STRING,"") ?: ""
        entryPoint = arguments?.getString(EXTRA_ENTRY_POINT_STRING,"") ?: ""
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyDoneButton = findViewById(R.id.done_button)
            ePharmacyLoader = findViewById(R.id.epharmacy_loader)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
    }

    private fun initData(){
        setupRecyclerView()
        renderButtons()
    }

    private fun getData() {
        ePharmacyLoader?.show()
        ePharmacyPrescriptionAttachmentViewModel.getPrepareProductGroup()
    }

    private fun setupRecyclerView() {
        ePharmacyRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ePharmacyAdapter
        }
    }

    private fun renderButtons() {
        ePharmacyDoneButton?.setOnClickListener {
            onDoneButtonClick()
        }
    }

    private fun onDoneButtonClick(){
        ePharmacyLoader?.show()

    }


    private fun observerEPharmacyDetail(){
        ePharmacyPrescriptionAttachmentViewModel.productGroupLiveDataResponse.observe(viewLifecycleOwner){
            when (it) {
                is Success -> {
                    onSuccessGroupData(it)
                }
                is Fail -> {
                    onFailGroupData(it)
                }
            }
        }
    }

    private fun onFailGroupData(it: Fail) {
        ePharmacyLoader?.hide()
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

    private fun showToast(message : String) {
        view?.let { it ->
            Toaster.build(it,message,LENGTH_LONG,TYPE_ERROR).show()
        }
    }


    private fun onSuccessGroupData(it: Success<EPharmacyDataModel>) {
        ePharmacyLoader?.hide()
        it.data.listOfComponents.forEach { component ->
            if(component.name() == PRESCRIPTION_COMPONENT){
                (component as? EPharmacyPrescriptionDataModel)?.let { presComponent ->
                    presComponent.prescriptions?.let {
                        //uploadPrescriptionViewModel.onSuccessGetPrescriptionImages(it)
                    }
                }
            }else {
                ePharmacyUiUpdater.updateModel(component)
            }
        }
        updateUi()
    }

    private fun updateUi() {
        val newData = ePharmacyUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun showDoneButtonState(){
        ePharmacyDoneButton?.show()
    }

    private fun showUploadPhotoButtonState(){
        ePharmacyDoneButton?.hide()
    }

    private fun hideAllButtons(){
        ePharmacyDoneButton?.hide()
    }

    private fun submitList(visitableList: List<BaseEPharmacyDataModel>) {
        ePharmacyAdapter.submitList(visitableList)
    }

    override fun getScreenName() = EPHARMACY_SCREEN_NAME

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        const val DELAY_IN_MILLS_FOR_SNACKBAR_VIEW = 2000L
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyPrescriptionAttachmentPageFragment {
            val fragment = EPharmacyPrescriptionAttachmentPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
