package com.tokopedia.epharmacy.ui.fragment

import android.content.Intent
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
import com.tokopedia.common_epharmacy.network.response.EPharmacyItemButtonData
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
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL
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
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun initArguments() {
        orderId = arguments?.getLong(EXTRA_ORDER_ID_LONG)  ?: 0L
        checkoutId = arguments?.getString(EXTRA_CHECKOUT_ID_STRING,"") ?: ""
        entryPoint = arguments?.getString(EXTRA_ENTRY_POINT_STRING,"") ?: ""
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observerEPharmacyButtonData()
        observerUploadPrescriptionError()
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

    private fun observerUploadPrescriptionError() {
        ePharmacyPrescriptionAttachmentViewModel.uploadError.observe(viewLifecycleOwner) { error ->
            when (error) {
                is EPharmacyNoDigitalPrescriptionError -> showToast(context?.resources?.getString(R.string.epharmacy_no_digital_prescription)
                    ?: "")
            }
        }
    }

    private fun observerEPharmacyButtonData() {
        ePharmacyPrescriptionAttachmentViewModel.buttonLiveData.observe(viewLifecycleOwner){
            ePharmacyDoneButton?.show()
            if(it.second.isNotBlank()){
                enableButton(it.first,it.second)
            }else {
                disableButton(it.first)
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

    private fun enableButton(buttonText : String, appLink : String ){
        ePharmacyDoneButton?.apply {
            text = buttonText
            isEnabled = true
            setOnClickListener {
                onDoneButtonClick(appLink)
            }
        }
    }

    private fun onDoneButtonClick(appLink: String){
        RouteManager.route(activity,appLink)
    }

    private fun disableButton(buttonText: String){
        ePharmacyDoneButton?.apply {
            text = buttonText
            isEnabled = false
        }
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
        redirectAttachmentCTA(model.enablerLogo, model.epharmacyGroupId,model.epharmacyButton, model.consultationSource?.pwaLink)
    }

    private fun redirectAttachmentCTA(enablerImage : String?, groupId : String?, epharmacyButton: EPharmacyItemButtonData?, miniConsultationWebLink : String?) {
        when(epharmacyButton?.sourceType){
            PrescriptionSourceType.MINI_CONSULT.type -> {
                startMiniConsultation(miniConsultationWebLink)
            }
            PrescriptionSourceType.UPLOAD.type -> {
                startPhotoUpload(groupId)
            }
            PrescriptionSourceType.MULTI.type -> {
                startAttachmentChooser(enablerImage,groupId)
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
            startActivityForResult(it,EPHARMACY_UPLOAD_REQUEST_CODE)
        }
    }

    private fun startMiniConsultation(miniConsultationLink : String?) {
        miniConsultationLink?.let { webLink ->
            activity?.let { safeContext ->
                RouteManager.route(context,"tokopedia://webview?url=https://accounts-staging.tokopedia.com/oauth/sandbox/in")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ePharmacyPrescriptionAttachmentViewModel.validateButtonData(ePharmacyAttachmentUiUpdater)
        when(requestCode){
            EPHARMACY_CHOOSER_REQUEST_CODE -> {
                showToast("EPHARMACY_CHOOSER_REQUEST_CODE")
            }
            EPHARMACY_MINI_CONSULTATION_REQUEST_CODE -> {
                showToast("EPHARMACY_MINI_CONSULTATION_REQUEST_CODE")
            }
            EPHARMACY_UPLOAD_REQUEST_CODE -> {
                showToast("EPHARMACY_UPLOAD_REQUEST_CODE")
            }
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
