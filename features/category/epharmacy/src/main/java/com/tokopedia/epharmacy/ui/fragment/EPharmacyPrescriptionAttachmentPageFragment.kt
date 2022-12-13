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
import com.tokopedia.common_epharmacy.*
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
import com.tokopedia.epharmacy.network.response.EPharmacyInitiateConsultationResponse
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyReminderScreenBottomSheet
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
    private var trackingSentBoolean = false

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
        setUpObservers()
        initViews(view)
        initData()
        getData()
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observerEPharmacyButtonData()
        observerPrescriptionError()
        observerInitiateConsultation()
        observerConsultationDetails()
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
        addShimmer()
        ePharmacyPrescriptionAttachmentViewModel.getPrepareProductGroup()
    }

    private fun addShimmer() {
        ePharmacyRecyclerView?.show()
        ePharmacyDoneButton?.hide()
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        ePharmacyAttachmentUiUpdater.updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
        ePharmacyAttachmentUiUpdater.updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_2, SHIMMER_COMPONENT))
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

    private fun observerPrescriptionError() {
        ePharmacyPrescriptionAttachmentViewModel.uploadError.observe(viewLifecycleOwner) { error ->
            when (error) {
                is EPharmacyMiniConsultationToaster -> showToast(
                    if (error.showErrorToast) {
                        TYPE_ERROR
                    } else {
                        Toaster.TYPE_NORMAL
                    },
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
                    onFailInitiateConsultation(data.throwable)
                }
            }
        }
    }

    private fun observerConsultationDetails() {
        ePharmacyPrescriptionAttachmentViewModel.consultationDetails.observe(viewLifecycleOwner) { consultationDetails ->
            when (consultationDetails) {
                is Success -> {
                    consultationDetails.data.epharmacyConsultationDetailsData?.consultationData?.prescription?.firstOrNull()?.documentUrl?.let { url ->
                        RouteManager.route(activity, "${WEB_LINK_PREFIX}$url")
                    }
                }
                is Fail -> {
                    onFailGetConsultationDetails(consultationDetails.throwable)
                }
            }
        }
    }

    private fun onFailGetConsultationDetails(throwable: Throwable) {
        showToasterError(throwable)
    }

    private fun observerEPharmacyButtonData() {
        ePharmacyPrescriptionAttachmentViewModel.buttonLiveData.observe(viewLifecycleOwner) { papCTA ->
            ePharmacyDoneButton?.show()
            papCTA?.let { cta ->
                ePharmacyDoneButton?.text = cta.title
                when (cta.state) {
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

    private fun onSuccessInitiateConsultation(consultationResponse: EPharmacyInitiateConsultationResponse) {
        if (consultationResponse.getInitiateConsultation?.header?.code == ERROR_CODE_OUTSIDE_WORKING_HOUR) {
            ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData?.detailData?.groupsData?.epharmacyGroups?.find {
                it?.epharmacyGroupId == consultationResponse.epharmacyGroupId
            }?.let { group ->
                presentReminderScreen(
                    true,
                    group.consultationSource?.operatingSchedule?.daily?.openTime,
                    group.consultationSource?.operatingSchedule?.daily?.closeTime,
                    group.consultationSource?.id ?: 0L,
                    group.epharmacyGroupId,
                    group.consultationSource?.enablerName
                )
            }
        } else if (consultationResponse.getInitiateConsultation?.initiateConsultationData != null &&
            !consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.pwaLink.isNullOrBlank()
        ) {
            activity?.let { safeContext ->
                startActivityForResult(
                    RouteManager.getIntent(safeContext, "${WEB_LINK_PREFIX}${consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.pwaLink}"),
                    EPHARMACY_MINI_CONSULTATION_REQUEST_CODE
                )
            }
        }
    }

    private fun onFailInitiateConsultation(throwable: Throwable) {
        showToasterError(throwable)
    }

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error) ?: "")
            else -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail) ?: "")
        }
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

    private fun showToast(type: Int = Toaster.TYPE_NORMAL, message: String) {
        if (message.isNotBlank()) {
            view?.let { safeView ->
                Toaster.build(safeView, message, LENGTH_LONG, type).show()
            }
        }
    }

    private fun onSuccessGroupData(it: Success<EPharmacyDataModel>) {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        var showReminderScreen = false
        it.data.listOfComponents.forEach { component ->
            ePharmacyAttachmentUiUpdater.updateModel(component)

            if (component is EPharmacyAttachmentDataModel &&
                !showReminderScreen
            ) {
                if ((component).consultationData?.consultationStatus == EPharmacyConsultationStatus.DOCTOR_NOT_AVAILABLE.status) {
                    showReminderScreen = true
                    presentReminderScreen(
                        false,
                        component.consultationSource?.operatingSchedule?.daily?.openTime,
                        component.consultationSource?.operatingSchedule?.daily?.closeTime,
                        component.consultationSource?.id ?: 0L,
                        component.epharmacyGroupId,
                        component.enablerName
                    )
                }
            }
        }
        if (!trackingSentBoolean) {
            EPharmacyMiniConsultationAnalytics.userViewAttachPrescriptionPage(
                userSession.isLoggedIn,
                userSession.userId,
                ePharmacyPrescriptionAttachmentViewModel.getShopIds().size.toString(),
                ePharmacyPrescriptionAttachmentViewModel.getGroupIds().toString()
            )
            trackingSentBoolean = true
        }
        updateUi()
    }

    private fun presentReminderScreen(
        isOutSideWordingHours: Boolean,
        openTime: String?,
        closeTime: String?,
        consultationId: Long,
        groupId: String?,
        enablerName: String?
    ) {
        if (!openTime.isNullOrBlank() && !closeTime.isNullOrBlank()) {
            EPharmacyReminderScreenBottomSheet.newInstance(
                isOutSideWordingHours,
                openTime,
                closeTime,
                TYPE_DOCTOR_NOT_AVAILABLE_REMINDER,
                consultationId,
                groupId,
                enablerName
            ).show(childFragmentManager, EPharmacyReminderScreenBottomSheet::class.simpleName)
        }
    }

    private fun updateUi() {
        val newData = ePharmacyAttachmentUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun onDoneButtonClick(appLink: String?) {
        if (hasAnyError()) {
            showToast(TYPE_ERROR, context?.resources?.getString(com.tokopedia.epharmacy.R.string.epharmacy_local_prescription_not_uploaded_error) ?: "")
            updateUi()
        } else {
            EPharmacyMiniConsultationAnalytics.clickCTAButton("${ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData?.detailData?.groupsData?.toaster?.message}", ePharmacyPrescriptionAttachmentViewModel.getGroupIds().toString())
            if (!appLink.isNullOrBlank() && appLink.contains(EPHARMACY_CHECKOUT_APPLINK)) {
                activity?.setResult(
                    EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE,
                    Intent().apply {
                        putParcelableArrayListExtra(
                            EPHARMACY_CONSULTATION_RESULT_EXTRA,
                            ePharmacyPrescriptionAttachmentViewModel.getResultForCheckout()
                        )
                    }
                )
                activity?.finish()
            } else if (!appLink.isNullOrBlank() && appLink.contains(EPHARMACY_CART_APPLINK)) {
                activity?.setResult(
                    EPHARMACY_REDIRECT_CART_RESULT_CODE,
                    Intent()
                )
                activity?.finish()
            } else {
                RouteManager.route(activity, appLink)
            }
        }
    }

    private fun hasAnyError(): Boolean {
        var isError = false
        ePharmacyAttachmentUiUpdater.mapOfData.forEach { entry ->
            (entry.value as? EPharmacyAttachmentDataModel)?.let { ePharmacyAttachmentDataModel ->
                if (EPharmacyUtils.checkIsError(ePharmacyAttachmentDataModel)) {
                    val updated = (entry.value as EPharmacyAttachmentDataModel).copy()
                    updated.isError = true
                    if (!isError) {
                        updated.isFirstError = true
                    }
                    ePharmacyAttachmentUiUpdater.updateModel(updated)
                    isError = true
                }
            }
        }
        return isError
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
        redirectAttachmentCTA(
            model.enablerName,
            model.enablerLogo,
            model.epharmacyGroupId,
            model.prescriptionCTA,
            model.tokoConsultationId,
            model.consultationSource?.id
        )
        EPharmacyMiniConsultationAnalytics.clickAttachPrescriptionButton(
            model.prescriptionCTA?.title ?: "",
            ePharmacyPrescriptionAttachmentViewModel.getEnablers().toString(),
            adapterPosition.toString(),
            ePharmacyPrescriptionAttachmentViewModel.getShopIds(model.epharmacyGroupId).size.toString(),
            ePharmacyPrescriptionAttachmentViewModel.getShopIds(model.epharmacyGroupId).toString(),
            model.epharmacyGroupId
        )
    }

    override fun onError(adapterPosition: Int, modelKey: String?) {
        super.onError(adapterPosition, modelKey)
        val updated = (ePharmacyAttachmentUiUpdater.mapOfData[modelKey] as EPharmacyAttachmentDataModel).copy()
        updated.isError = false
        if (updated.isFirstError) {
            ePharmacyRecyclerView?.smoothScrollToPosition(adapterPosition)
        }
        ePharmacyAttachmentUiUpdater.updateModel(updated)
    }

    override fun onToast(toasterType: Int, message: String) {
        super.onToast(toasterType, message)
        showToast(toasterType, message)
    }

    private fun redirectAttachmentCTA(
        enablerName: String?,
        enablerImage: String?,
        groupId: String?,
        prescriptionCTA: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionCTA?,
        tokoConsultationId: Long?,
        consultationId: Long?
    ) {
        when (prescriptionCTA?.actionType) {
            PrescriptionActionType.REDIRECT_PWA.type -> {
                startMiniConsultation(enablerName, groupId, consultationId)
            }
            PrescriptionActionType.REDIRECT_UPLOAD.type -> {
                startPhotoUpload(groupId)
            }
            PrescriptionActionType.REDIRECT_OPTION.type -> {
                startAttachmentChooser(enablerImage, groupId, enablerName, consultationId)
            }
            PrescriptionActionType.REDIRECT_PRESCRIPTION.type -> {
                tokoConsultationId?.let {
                    ePharmacyPrescriptionAttachmentViewModel.getConsultationDetails(it)
                }
            }
            PrescriptionActionType.REDIRECT_CHECK_PRESCRIPTION.type -> {
                groupId?.let { ePharmacyGroupId ->
                    startPhotoUpload(ePharmacyGroupId, EPHARMACY_CEK_RESEP_REQUEST_CODE)
                }
            }
        }
    }

    private fun startAttachmentChooser(
        enablerImage: String?,
        ePharmacyGroupId: String?,
        enablerName: String?,
        consultationSourceId: Long?
    ) {
        RouteManager.getIntent(activity, EPHARMACY_CHOOSER_APPLINK).apply {
            putExtra(ENABLER_IMAGE_URL, enablerImage)
            putExtra(EPHARMACY_GROUP_ID, ePharmacyGroupId)
            putExtra(EPHARMACY_ENABLER_NAME, enablerName)
            putExtra(EPHARMACY_CONSULTATION_SOURCE_ID, consultationSourceId)
        }.also {
            startActivityForResult(it, EPHARMACY_CHOOSER_REQUEST_CODE)
        }
    }

    private fun startPhotoUpload(groupId: String?, requestCode: Int = EPHARMACY_UPLOAD_REQUEST_CODE) {
        RouteManager.getIntent(activity, EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING, groupId)
        }.also {
            startActivityForResult(it, requestCode)
        }
    }

    private fun startMiniConsultation(enablerName: String?, groupId: String?, consultationSourceId: Long?) {
        if (!groupId.isNullOrBlank()) {
            EPharmacyMiniConsultationAnalytics.viewMiniConsultationPage(
                userSession.isLoggedIn,
                userSession.userId,
                enablerName ?: "",
                groupId,
                consultationSourceId.toString()
            )
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
                        data?.getStringExtra(EPHARMACY_GROUP_ID)?.let { groupId ->
                            startMiniConsultation(
                                data.getStringExtra(EPHARMACY_ENABLER_NAME),
                                groupId,
                                data.getLongExtra(EPHARMACY_CONSULTATION_SOURCE_ID, 0L)
                            )
                            EPharmacyMiniConsultationAnalytics.clickChatDokter(data.getStringExtra(EPHARMACY_ENABLER_NAME), groupId)
                        }
                    }
                    EPHARMACY_UPLOAD_REQUEST_CODE -> {
                        data?.getStringExtra(EPHARMACY_GROUP_ID)?.let { groupId ->
                            startPhotoUpload(groupId)
                            EPharmacyMiniConsultationAnalytics.clickUploadResepDokter(data.getStringExtra(EPHARMACY_ENABLER_NAME), groupId)
                        }
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
