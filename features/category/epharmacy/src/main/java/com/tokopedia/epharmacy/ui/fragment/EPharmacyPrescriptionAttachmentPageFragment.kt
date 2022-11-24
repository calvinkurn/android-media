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
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.params.InitiateConsultationParam
import com.tokopedia.epharmacy.network.response.InitiateConsultation
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.ATTACH_PRESCRIPTION_PAGE
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
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

class EPharmacyPrescriptionAttachmentPageFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyRecyclerView: RecyclerView? = null
    private var ePharmacyDoneButton: UnifyButton? = null
    private var ePharmacyGlobalError: GlobalError? = null

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
        orderId = arguments?.getLong(EXTRA_ORDER_ID_LONG) ?: 0L
        checkoutId = arguments?.getString(EXTRA_CHECKOUT_ID_STRING, "") ?: ""
        entryPoint = arguments?.getString(EXTRA_ENTRY_POINT_STRING, "") ?: ""
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observerEPharmacyButtonData()
        observerUploadPrescriptionError()
        observerInitiateConsultation()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyDoneButton = findViewById(R.id.done_button)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
    }

    private fun initData() {
        setupRecyclerView()
    }

    private fun getData() {
        // ePharmacyLoader?.show()
        addShimmer()
        ePharmacyPrescriptionAttachmentViewModel.getPrepareProductGroup()
    }

    private fun addShimmer() {
        ePharmacyRecyclerView?.show()
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        ePharmacyAttachmentUiUpdater.updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
        ePharmacyAttachmentUiUpdater.updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
        updateUi()
    }

    private fun removeShimmer() {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        ePharmacyRecyclerView?.hide()
        updateUi()
    }

    private fun setupRecyclerView() {
        ePharmacyRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ePharmacyAdapter
        }
    }

    private fun observerEPharmacyDetail() {
        ePharmacyPrescriptionAttachmentViewModel.productGroupLiveDataResponse.observe(viewLifecycleOwner) {
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
                is EPharmacyMiniConsultationToaster -> showToast(
                    if(error.showErrorToast) TYPE_ERROR
                    else  Toaster.TYPE_NORMAL,
                    error.message
                )
            }
        }
    }

    private fun observerInitiateConsultation() {
        ePharmacyPrescriptionAttachmentViewModel.initiateConsultation.observe(viewLifecycleOwner) { data ->
            when (data) {
                is Success -> {
                    onSuccessInitiateConsultation(data.data)
                }
                is Fail -> {
                    onFailInitiateConsultation(data)
                }
            }
        }
    }

    private fun observerEPharmacyButtonData() {
        ePharmacyPrescriptionAttachmentViewModel.buttonLiveData.observe(viewLifecycleOwner) { papCTA ->
            ePharmacyDoneButton?.show()
            papCTA?.let { cta ->
                ePharmacyDoneButton?.text = cta.title
                when(cta.state) {
                    EPharmacyButtonState.ACTIVE.state -> {
                        ePharmacyDoneButton?.isEnabled = true
                        ePharmacyDoneButton?.setOnClickListener {
                            onDoneButtonClick(cta.redirectLinkApps)
                        }
                    }
                    EPharmacyButtonState.DISABLED.state -> {
                        ePharmacyDoneButton?.isEnabled = false
                        ePharmacyDoneButton?.setOnClickListener(null)
                    }
                }
            }
        }
    }

    private fun onSuccessInitiateConsultation(initiateConsultationData : InitiateConsultation.InitiateConsultationData) {
        if(initiateConsultationData.tokoConsultationId.isNullOrBlank()){
            activity?.let { safeContext ->
                startActivityForResult(RouteManager.getIntent(safeContext, "tokopedia://webview?url=${initiateConsultationData.consultationSource?.pwaLink}"),
                    EPHARMACY_MINI_CONSULTATION_REQUEST_CODE)
            }
        }
    }

    private fun onFailInitiateConsultation(it: Fail) {

    }

    private fun onFailGroupData(it: Fail) {
        ePharmacyDoneButton?.hide()
        removeShimmer()
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

    private fun showToast(type : Int = Toaster.TYPE_NORMAL, message: String) {
        if(message.isNotBlank()){
            view?.let { safeView ->
                Toaster.build(safeView, message, LENGTH_LONG, type).show()
            }
        }
    }

    private fun onSuccessGroupData(it: Success<EPharmacyDataModel>) {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        it.data.listOfComponents.forEach { component ->
            ePharmacyAttachmentUiUpdater.updateModel(component)
        }
        updateUi()
    }

    private fun updateUi() {
        val newData = ePharmacyAttachmentUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun onDoneButtonClick(appLink: String?) {
        if (appLink.isNullOrBlank() || appLink.contains(EPHARMACY_CHECKOUT_APPLINK)) {
            activity?.setResult(
                EPHARMACY_MINI_CONSULTATION_REQUEST_CODE,
                Intent().apply {
                     putParcelableArrayListExtra(EPHARMACY_MINI_CONSULTATION_RESULT_EXTRA, getResultForCheckout())
                }
            )
        } else {
            RouteManager.route(activity, appLink)
        }
    }

    private fun getResultForCheckout(): ArrayList<EPharmacyMiniConsultationResult> {
        val result = arrayListOf<EPharmacyMiniConsultationResult>()
        ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData?.let { response ->
            response.detailData?.groupsData?.epharmacyGroups?.forEach { group ->
                result.add(
                    EPharmacyMiniConsultationResult(
                        group?.epharmacyGroupId,
                        group?.shopInfo,
                        group?.consultationData?.consultationStatus,
                        group?.consultationData?.consultationString,
                        group?.consultationData?.prescription,
                        group?.consultationData?.partnerConsultationId,
                        group?.consultationData?.tokoConsultationId,
                        group?.prescriptionImages
                    )
                )
            }
        }
        return result
    }

    private fun disableButton(buttonText: String) {
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
        redirectAttachmentCTA(model.enablerLogo, model.epharmacyGroupId, model.prescriptionCTA,
            model.consultationSource?.pwaLink, model.tokoConsultationId,
            model.consultationData?.prescription?.firstOrNull()?.documentUrl,
            model.enablerName)
    }

    private fun redirectAttachmentCTA(enablerImage: String?, groupId: String?,
                                      prescriptionCTA: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionCTA?,
                                      miniConsultationWebLink: String?, tokoConsultationId: String?, documentUrl: String?,
                                      enablerName : String?) {
        when (prescriptionCTA?.actionType) {
            PrescriptionActionType.REDIRECT_PWA.type -> {
                startMiniConsultation(miniConsultationWebLink,groupId)
            }
            PrescriptionActionType.REDIRECT_UPLOAD.type -> {
                startPhotoUpload(groupId)
            }
            PrescriptionActionType.REDIRECT_OPTION.type -> {
                startAttachmentChooser(enablerImage, groupId, miniConsultationWebLink)
            }
            PrescriptionActionType.REDIRECT_PRESCRIPTION.type -> {
                if(documentUrl?.isNotBlank() == true){
                    activity?.let { safeContext ->
                        startActivity(RouteManager.getIntent(safeContext, "tokopedia://webview?url=${documentUrl}"))
                    }
                }else {
                    tokoConsultationId?.let { id ->
                        ePharmacyPrescriptionAttachmentViewModel.getConsultationDetails(id)
                    }
                }
            }
        }
    }

    private fun startAttachmentChooser(
        enablerImage: String?,
        tokoConsultationId: String?,
        miniConsultationLink: String?
    ) {
        RouteManager.getIntent(activity, EPHARMACY_CHOOSER_APPLINK).apply {
            putExtra(ENABLER_IMAGE_URL, enablerImage)
            putExtra(EXTRA_CHECKOUT_ID_STRING, tokoConsultationId)
            putExtra(EXTRA_CONSULTATION_WEB_LINK_STRING, miniConsultationLink)
        }.also {
            startActivityForResult(it, EPHARMACY_CHOOSER_REQUEST_CODE)
        }
    }

    private fun startPhotoUpload(tokoConsultationId: String?) {
        RouteManager.getIntent(activity, EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING, tokoConsultationId)
        }.also {
            startActivityForResult(it, EPHARMACY_UPLOAD_REQUEST_CODE)
        }
    }

    private fun startMiniConsultation(miniConsultationLink: String?, groupId: String?) {
//        miniConsultationLink?.let { webLink ->
//            activity?.let { safeContext ->
//                startActivityForResult(RouteManager.getIntent(safeContext, "tokopedia://webview?url=${webLink}"), EPHARMACY_MINI_CONSULTATION_REQUEST_CODE)
//            }
//        }
        if(!groupId.isNullOrBlank()){
            ePharmacyPrescriptionAttachmentViewModel.initiateConsultation(
                InitiateConsultationParam(
                    InitiateConsultationParam.InitiateConsultationParamInput(groupId)
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EPHARMACY_CHOOSER_REQUEST_CODE -> {
                when (resultCode) {
                    EPHARMACY_MINI_CONSULTATION_REQUEST_CODE -> {
                        getData()
                    }
                    EPHARMACY_UPLOAD_REQUEST_CODE -> {
                        getData()
                    }
                }
            }
            EPHARMACY_MINI_CONSULTATION_REQUEST_CODE -> {
                getData()
            }
            EPHARMACY_UPLOAD_REQUEST_CODE -> {
                getData()
            }
        }
    }

    override fun getScreenName() = ATTACH_PRESCRIPTION_PAGE

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(): EPharmacyPrescriptionAttachmentPageFragment {
            return EPharmacyPrescriptionAttachmentPageFragment()
        }
    }
}
