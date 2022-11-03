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
import com.tokopedia.applink.RouteManager
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
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

    private var ePharmacyAttachmentUiUpdater: EPharmacyAttachmentUiUpdater = EPharmacyAttachmentUiUpdater(
        linkedMapOf()
    )

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(this) }

    private var orderId = DEFAULT_ZERO_VALUE
    private var checkoutId = ""
    private var entryPoint = ""

    private val ePharmacyAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel> = AsyncDifferConfig.Builder(
            EPharmacyAttachmentDetailDiffUtil()
        )
            .build()
        EPharmacyAdapter(asyncDifferConfig, ePharmacyAdapterFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.epharmacy_prescription_attachment_fragment, container, false)
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
            ePharmacyAttachmentUiUpdater.updateModel(component)
        }
        updateUi()
    }

    private fun updateUi() {
        val newData = ePharmacyAttachmentUiUpdater.mapOfData.values.toList()
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

    override fun onInteractAccordion(adapterPosition: Int, isExpanded: Boolean, modelKey: String?) {
        super.onInteractAccordion(adapterPosition, isExpanded, modelKey)
        val updated = (ePharmacyAttachmentUiUpdater.mapOfData[modelKey] as EPharmacyAttachmentDataModel).copy()
        updated.productsIsExpanded = !isExpanded
        ePharmacyAttachmentUiUpdater.updateModel(updated)
        updateUi()
    }

    override fun onCTACClick(adapterPosition: Int, modelKey: String?) {
        super.onCTACClick(adapterPosition, modelKey)
        val model = (ePharmacyAttachmentUiUpdater.mapOfData[modelKey] as EPharmacyAttachmentDataModel)
        redirectAttachmentCTA(model.partnerLogo, model.tokoConsultationId,model.prescriptionSource, model.consultationSource?.pwaLink)
    }

    private fun redirectAttachmentCTA(enablerImage : String?, tokoConsultationId : String?,prescriptionSource: List<String?>?, miniConsultationWebLink : String?) {
        prescriptionSource?.let {
            if(it.size > 1){
                startAttachmentChooser(enablerImage,tokoConsultationId)
            }else {
                it.firstOrNull()?.let { source ->
                    when(source){
                        PRESCRIPTION_SOURCE_TYPE.MINI_CONSULT.type ->{
                            startMiniConsultation(miniConsultationWebLink)
                        }

                        PRESCRIPTION_SOURCE_TYPE.UPLOAD.type ->{
                            startPhotoUpload(tokoConsultationId)
                        }
                    }
                }
            }
        }
    }

    private fun startAttachmentChooser(enablerImage : String?, tokoConsultationId : String?) {
        RouteManager.getIntent(activity,EPHARMACY_CHOOSER_APPLINK).apply {
            putExtra(ENABLER_IMAGE_URL,enablerImage)
            putExtra(EXTRA_CHECKOUT_ID_STRING,tokoConsultationId)
        }.also {
            startActivityForResult(it,EPHARMACY_CHOOSER_REQUEST_CODE)
        }
    }

    private fun startPhotoUpload(tokoConsultationId : String?) {
        RouteManager.getIntent(activity,EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING,tokoConsultationId)
        }.also {
            startActivityForResult(it,EPHARMACY_REQUEST_CODE)
        }
    }

    private fun startMiniConsultation(miniConsultationLink : String?) {
        miniConsultationLink?.let { webLink ->
            RouteManager.route(activity,webLink)
        }
    }

    override fun getScreenName() = EPHARMACY_SCREEN_NAME

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyPrescriptionAttachmentPageFragment {
            val fragment = EPharmacyPrescriptionAttachmentPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
