package com.tokopedia.epharmacy.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common_epharmacy.EPHARMACY_CEK_RESEP_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_CHOOSER_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_MINI_CONSULTATION_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_PPG_SOURCE_CHECKOUT
import com.tokopedia.common_epharmacy.EPHARMACY_SEND_RESULT_KEY
import com.tokopedia.common_epharmacy.EPHARMACY_UPLOAD_REQUEST_CODE
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
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
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyCommonBottomSheet
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyReminderScreenBottomSheet
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.ATTACH_PRESCRIPTION_PAGE
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EPHARMACY_ANDROID_SOURCE
import com.tokopedia.epharmacy.utils.EPHARMACY_APPLINK
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_NAME
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_SOURCE
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_IDS
import com.tokopedia.epharmacy.utils.EPharmacyAttachmentUiUpdater
import com.tokopedia.epharmacy.utils.EPharmacyButtonState
import com.tokopedia.epharmacy.utils.EPharmacyConsultationStatus
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationAnalytics
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationToaster
import com.tokopedia.epharmacy.utils.EPharmacyNavigator
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.ERROR_CODE_OUTSIDE_WORKING_HOUR
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_ID_STRING
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_PAGE_SOURCE
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_PAGE_SOURCE_EPHARMACY
import com.tokopedia.epharmacy.utils.EXTRA_SOURCE_STRING
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.LabelKeys.Companion.FAILED
import com.tokopedia.epharmacy.utils.LabelKeys.Companion.SUCCESS
import com.tokopedia.epharmacy.utils.PapCtaRedirection
import com.tokopedia.epharmacy.utils.PrescriptionActionType
import com.tokopedia.epharmacy.utils.SHIMMER_COMPONENT
import com.tokopedia.epharmacy.utils.SHIMMER_COMPONENT_1
import com.tokopedia.epharmacy.utils.SHIMMER_COMPONENT_2
import com.tokopedia.epharmacy.utils.TYPE_DOCTOR_NOT_AVAILABLE_REMINDER
import com.tokopedia.epharmacy.utils.TrackerId
import com.tokopedia.epharmacy.utils.UPLOAD_PAGE_SOURCE_PAP
import com.tokopedia.epharmacy.utils.openDocument
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanEqualZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.track.builder.Tracker
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
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EG
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyPrescriptionAttachmentPageFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyRecyclerView: RecyclerView? = null
    private var ePharmacyDoneButton: UnifyButton? = null
    private var ePharmacyGlobalError: GlobalError? = null
    private var trackingSentBoolean = false

    private var tConsultationId = 0L
    private var source = EPHARMACY_PPG_SOURCE_CHECKOUT
    private var isSendResult = false

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
        initArguments()
        setUpObservers()
        initViews(view)
        initData()
        getData()
    }

    private fun initArguments() {
        tConsultationId = arguments?.getLong(EPHARMACY_TOKO_CONSULTATION_ID).orZero()
        isSendResult = arguments?.getBoolean(EPHARMACY_SEND_RESULT_KEY).orFalse()
        source = arguments?.getString(EPHARMACY_SOURCE).orEmpty()
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observerEPharmacyButtonData()
        observerPrescriptionError()
        observerInitiateConsultation()
        observerConsultationDetails()
        observerUpdateEPharmacyCart()
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
        ePharmacyPrescriptionAttachmentViewModel.getPrepareProductGroup(source, makeRequestParams())
    }

    private fun makeRequestParams(): MutableMap<String, Any?> {
        val params = mutableMapOf<String, Any?>(
            EPharmacyPrepareProductsGroupUseCase.PARAM_SOURCE to source
        )
        if (!tConsultationId.isLessThanEqualZero()) {
            params[EPharmacyPrepareProductsGroupUseCase.PARAM_TOKO_CONSULTATION_IDS] = arrayOf(tConsultationId)
        }
        return params
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
            if (error is EPharmacyMiniConsultationToaster) {
                val group = ePharmacyPrescriptionAttachmentViewModel.findGroup(error.ePharmacyGroupId)
                showToast(
                    if (error.showErrorToast) {
                        EPharmacyMiniConsultationAnalytics.viewAttachPrescriptionResult(
                            group?.consultationSource?.id.orZero(),
                            group?.consultationSource?.enablerName.orEmpty(),
                            FAILED,
                            group?.epharmacyGroupId.orEmpty()
                        )
                        TYPE_ERROR
                    } else {
                        EPharmacyMiniConsultationAnalytics.viewAttachPrescriptionResult(
                            group?.consultationSource?.id.orZero(),
                            group?.consultationSource?.enablerName.orEmpty(),
                            SUCCESS,
                            group?.epharmacyGroupId.orEmpty()
                        )
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
                        context?.openDocument(url)
                    }
                }
                is Fail -> {
                    onFailGetConsultationDetails(consultationDetails.throwable)
                }
            }
        }
    }

    private fun observerUpdateEPharmacyCart() {
        ePharmacyPrescriptionAttachmentViewModel.updateEPharmacyCart.observe(viewLifecycleOwner) { updateEPharmacyCart ->
            if (updateEPharmacyCart) {
                onSuccessUpdateEPharmacyCart()
            } else {
                showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail).orEmpty())
            }
        }
    }

    private fun onSuccessUpdateEPharmacyCart() {
        RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT).apply {
            putExtra(EXTRA_CHECKOUT_PAGE_SOURCE, EXTRA_CHECKOUT_PAGE_SOURCE_EPHARMACY)
        }.also {
            startActivity(it)
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
                    group.consultationSource?.id.orZero(),
                    group.epharmacyGroupId,
                    group.consultationSource?.enablerName
                )
            }
        } else if (consultationResponse.getInitiateConsultation?.initiateConsultationData != null &&
            !consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.pwaLink.isNullOrBlank()
        ) {
            EPharmacyMiniConsultationAnalytics.viewMiniConsultationPage(
                userSession.isLoggedIn,
                userSession.userId,
                consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.enablerName.orEmpty(),
                consultationResponse.epharmacyGroupId.orEmpty(),
                consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.id.toString()
            )
            openConsultationRedirection(consultationResponse)
        }
    }

    private fun openConsultationRedirection(consultationResponse: EPharmacyInitiateConsultationResponse) {
        consultationResponse.getInitiateConsultation?.initiateConsultationData?.consultationSource?.pwaLink?.let { link ->
            startActivityForResult(RouteManager.getIntent(context, link), EPHARMACY_MINI_CONSULTATION_REQUEST_CODE)
        }
    }

    private fun onFailInitiateConsultation(throwable: Throwable) {
        showToasterError(throwable)
    }

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error).orEmpty())
            else -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail).orEmpty())
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
                        component.consultationSource?.id.orZero(),
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

    // Redirection-Actions
    private fun onDoneButtonClick(appLink: String?) {
        if (hasAnyError()) {
            showToast(TYPE_ERROR, context?.resources?.getString(epharmacyR.string.epharmacy_local_prescription_not_uploaded_error).orEmpty())
            updateUi()
            return
        }

        EPharmacyMiniConsultationAnalytics.clickLanjutButton(
            EPharmacyUtils.getConsultationIds(ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData).toString(),
            ePharmacyPrescriptionAttachmentViewModel.getEnablers().toString(),
            "${ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData?.detailData?.groupsData?.toaster?.message}",
            ePharmacyPrescriptionAttachmentViewModel.getGroupIds().toString(),
            EPharmacyUtils.getPrescriptionIds(ePharmacyPrescriptionAttachmentViewModel.ePharmacyPrepareProductsGroupResponseData).toString()
        )

        EPharmacyNavigator.prescriptionAttachmentDoneRedirection(
            activity,
            appLink,
            source,
            ePharmacyPrescriptionAttachmentViewModel.getResultForCheckout()
        ).let { result ->
            when (result) {
                is PapCtaRedirection.RedirectionUpdateQuantity -> ePharmacyPrescriptionAttachmentViewModel.updateEPharmacyCart(ePharmacyAttachmentUiUpdater)
                is PapCtaRedirection.RedirectionQuantityEditor -> {
                    EPharmacyCommonBottomSheet.newInstance(
                        Bundle().apply {
                            putString(EPharmacyCommonBottomSheet.COMPONENT_NAME, EPharmacyCommonBottomSheet.TYPE_QUANTITY_EDITOR)
                            putStringArrayList(
                                EPHARMACY_TOKO_CONSULTATION_IDS,
                                ArrayList(result.tConsultationIds)
                            )
                        }
                    ).show(childFragmentManager, EPharmacyCommonBottomSheet::class.simpleName)
                }
                is PapCtaRedirection.None -> {
                    // No - op
                }
            }
        }
    }

    private fun hasAnyError(): Boolean {
        var isError = false
        ePharmacyAttachmentUiUpdater.mapOfData.forEach { entry ->
            (entry.value as? EPharmacyAttachmentDataModel)?.let { ePharmacyAttachmentDataModel ->
                if (ePharmacyAttachmentDataModel.showUploadWidget && EPharmacyUtils.checkIsError(ePharmacyAttachmentDataModel)) {
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
            model.chooserLogo,
            model.epharmacyGroupId,
            model.prescriptionCTA,
            model.tokoConsultationId,
            model.consultationData,
            model.price,
            model.operatingSchedule,
            model.note
        )
        EPharmacyMiniConsultationAnalytics.clickAttachPrescriptionButton(
            model.prescriptionCTA?.title.orEmpty(),
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
        if (updated.isFirstError) {
            ePharmacyRecyclerView?.smoothScrollToPosition(adapterPosition)
        }
    }

    override fun onEndAnimation(adapterPosition: Int, modelKey: String?) {
        super.onEndAnimation(adapterPosition, modelKey)
        val updated = (ePharmacyAttachmentUiUpdater.mapOfData[modelKey] as EPharmacyAttachmentDataModel).copy()
        updated.isFirstError = false
        ePharmacyAttachmentUiUpdater.updateModel(updated)
        updateUi()
    }

    override fun onToast(toasterType: Int, message: String) {
        super.onToast(toasterType, message)
        showToast(toasterType, message)
    }

    override fun redirect(link: String) {
        super.redirect(link)
        val viewIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        )
        try {
            startActivity(viewIntent)
        } catch (e: ActivityNotFoundException) {
            EPharmacyUtils.logException(e)
        }
    }

    private fun redirectAttachmentCTA(
        enablerName: String?,
        chooserLogo: String?,
        groupId: String?,
        prescriptionCTA: EG.PrescriptionCTA?,
        tokoConsultationId: String?,
        consultationData: EG.ConsultationData?,
        price: String?,
        operatingSchedule: EG.ConsultationSource.OperatingSchedule?,
        note: String?
    ) {
        when (prescriptionCTA?.actionType) {
            PrescriptionActionType.REDIRECT_PWA.type -> {
                redirectActionPWA(consultationData, chooserLogo, groupId, enablerName, price, operatingSchedule, note)
            }
            PrescriptionActionType.REDIRECT_UPLOAD.type -> {
                startPhotoUpload(enablerName, groupId)
            }
            PrescriptionActionType.REDIRECT_OPTION.type -> {
                startAttachmentChooser(chooserLogo, groupId, enablerName, price, operatingSchedule, note)
            }
            PrescriptionActionType.REDIRECT_PRESCRIPTION.type -> {
                tokoConsultationId?.let {
                    ePharmacyPrescriptionAttachmentViewModel.getConsultationDetails(it)
                }
            }
            PrescriptionActionType.REDIRECT_CHECK_PRESCRIPTION.type -> {
                groupId?.let { ePharmacyGroupId ->
                    startPhotoUpload(enablerName, ePharmacyGroupId, EPHARMACY_CEK_RESEP_REQUEST_CODE)
                }
            }
        }
    }

    private fun redirectActionPWA(
        consultationData: EG.ConsultationData?,
        chooserLogo: String?,
        groupId: String?,
        enablerName: String?,
        price: String?,
        operatingSchedule: EG.ConsultationSource.OperatingSchedule?,
        note: String?
    ) {
        consultationData?.let {
            if (consultationData.consultationStatus.orZero().isZero()) {
                startAttachmentChooser(chooserLogo, groupId, enablerName, price, operatingSchedule, note, true)
            } else {
                sendViewOngoingChatDokterEvent("$enablerName - $groupId - ${consultationData.tokoConsultationId}")
                startMiniConsultation(enablerName, groupId)
            }
        } ?: kotlin.run {
            startAttachmentChooser(chooserLogo, groupId, enablerName, price, operatingSchedule, note, true)
        }
    }

    private fun startAttachmentChooser(
        enablerImage: String?,
        ePharmacyGroupId: String?,
        enablerName: String?,
        price: String?,
        operatingSchedule: EG.ConsultationSource.OperatingSchedule?,
        note: String?,
        isOnlyConsult: Boolean = false
    ) {
        RouteManager.getIntent(
            context,
            EPharmacyNavigator.createChooserAppLink(
                enablerImage,
                ePharmacyGroupId,
                enablerName,
                price,
                operatingSchedule?.duration.toString(),
                note,
                operatingSchedule?.isClosingHour,
                isOnlyConsult
            )
        )?.also {
            it.putExtra(ENABLER_IMAGE_URL, enablerImage)
            startActivityForResult(it, EPHARMACY_CHOOSER_REQUEST_CODE)
        }
    }

    private fun startPhotoUpload(enablerName: String?, groupId: String?, requestCode: Int = EPHARMACY_UPLOAD_REQUEST_CODE) {
        EPharmacyMiniConsultationAnalytics.clickUploadResepDokter(enablerName, groupId.orEmpty())
        RouteManager.getIntent(activity, EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING, groupId)
            putExtra(EXTRA_SOURCE_STRING, UPLOAD_PAGE_SOURCE_PAP)
        }.also {
            startActivityForResult(it, requestCode)
        }
    }

    private fun startMiniConsultation(enablerName: String?, groupId: String?) {
        if (!groupId.isNullOrBlank()) {
            EPharmacyMiniConsultationAnalytics.clickChatDokter(enablerName, groupId)
            ePharmacyPrescriptionAttachmentViewModel.initiateConsultation(
                InitiateConsultationParam(
                    InitiateConsultationParam.InitiateConsultationParamInput(groupId, EPHARMACY_ANDROID_SOURCE)
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
                                groupId
                            )
                        }
                    }
                    EPHARMACY_UPLOAD_REQUEST_CODE -> {
                        data?.getStringExtra(EPHARMACY_GROUP_ID)?.let { groupId ->
                            startPhotoUpload(data.getStringExtra(EPHARMACY_ENABLER_NAME), groupId)
                        }
                    }
                }
            }
            EPHARMACY_MINI_CONSULTATION_REQUEST_CODE, EPHARMACY_UPLOAD_REQUEST_CODE -> {
                getData()
            }
        }
    }

    override fun getScreenName() = ATTACH_PRESCRIPTION_PAGE

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyPrescriptionAttachmentPageFragment {
            return EPharmacyPrescriptionAttachmentPageFragment().apply {
                arguments = bundle
            }
        }
    }

    fun sendViewOngoingChatDokterEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view ongoing chat dokter")
            .setEventCategory(ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.VIEW_ONGOING_CHAT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
