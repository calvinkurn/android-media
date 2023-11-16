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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common_epharmacy.EPHARMACY_CEK_RESEP_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_PPG_QTY_CHANGE
import com.tokopedia.common_epharmacy.EPHARMACY_UPLOAD_REQUEST_CODE
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.databinding.EpharmacyQuantityChangeFragmentBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.EPHARMACY_APPLINK
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPharmacyAttachmentUiUpdater
import com.tokopedia.epharmacy.utils.EPharmacyButtonState
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationAnalytics
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_ID_STRING
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_PAGE_SOURCE
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_PAGE_SOURCE_EPHARMACY
import com.tokopedia.epharmacy.utils.EXTRA_SOURCE_STRING
import com.tokopedia.epharmacy.utils.PrescriptionActionType
import com.tokopedia.epharmacy.utils.UPLOAD_PAGE_SOURCE_PAP
import com.tokopedia.epharmacy.utils.openDocument
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EPharmacyQuantityChangeFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyRecyclerView: RecyclerView? = null
    private var ePharmacyGlobalError: GlobalError? = null
    private var qCTotalAmount: TotalAmount? = null
    private var tConsultationId = 0L
    private var totalAmount = 0.0
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val ePharmacyPrescriptionAttachmentViewModel: EPharmacyPrescriptionAttachmentViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[EPharmacyPrescriptionAttachmentViewModel::class.java]
    }

    private var ePharmacyAttachmentUiUpdater: EPharmacyAttachmentUiUpdater = EPharmacyAttachmentUiUpdater(
        linkedMapOf()
    )

    private var binding by autoClearedNullable<EpharmacyQuantityChangeFragmentBinding>()

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
        binding = EpharmacyQuantityChangeFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
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
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observerEPharmacyButtonData()
        observerPrescriptionError()
        observerConsultationDetails()
        observerUpdateEPharmacyCart()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
            qCTotalAmount = findViewById(R.id.qc_total_amount)
        }
    }

    private fun initData() {
        setupRecyclerView()
        qCTotalAmount?.labelTitleView?.setWeight(BOLD)
    }

    private fun getData() {
        addShimmer()
        ePharmacyPrescriptionAttachmentViewModel.getPrepareProductGroup(EPHARMACY_PPG_QTY_CHANGE, makeRequestParams())
    }

    private fun makeRequestParams(): MutableMap<String, Any?> {
        return mutableMapOf(
            EPharmacyPrepareProductsGroupUseCase.PARAM_SOURCE to EPHARMACY_PPG_QTY_CHANGE,
            EPharmacyPrepareProductsGroupUseCase.PARAM_TOKO_CONSULTATION_ID to tConsultationId
        )
    }

    private fun addShimmer() {
        ePharmacyRecyclerView?.show()
        ePharmacyAttachmentUiUpdater.addQuantityEditorShimmer()
        updateUi()
    }

    private fun removeShimmer() {
        ePharmacyRecyclerView?.hide()
        qCTotalAmount?.hide()
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
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

    private fun observerEPharmacyButtonData() {
        ePharmacyPrescriptionAttachmentViewModel.buttonLiveData.observe(viewLifecycleOwner) { papSecondaryCTA ->
            papSecondaryCTA?.let { cta ->
                qCTotalAmount?.amountCtaView?.text = cta.title
                when (cta.state) {
                    EPharmacyButtonState.ACTIVE.state -> {
                        qCTotalAmount?.amountCtaView?.isEnabled = true
                        qCTotalAmount?.amountCtaView?.setOnClickListener {
                            onDoneButtonClick()
                        }
                    }
                    EPharmacyButtonState.DISABLED.state -> {
                        qCTotalAmount?.amountCtaView?.isEnabled = false
                        qCTotalAmount?.amountCtaView?.setOnClickListener(null)
                    }
                }
            }
        }
    }

    private fun onDoneButtonClick() {
        ePharmacyPrescriptionAttachmentViewModel.updateEPharmacyCart(ePharmacyAttachmentUiUpdater)
    }

    private fun observerUpdateEPharmacyCart() {
        ePharmacyPrescriptionAttachmentViewModel.updateEPharmacyCart.observe(viewLifecycleOwner) { updateEPharmacyCart ->
            if (updateEPharmacyCart) {
                onSuccessUpdateEPharmacyCart()
            } else {
                showToast(
                    Toaster.TYPE_ERROR,
                    context?.resources?.getString(R.string.epharmacy_reminder_fail).orEmpty()
                )
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

    private fun onFailGetConsultationDetails(throwable: Throwable) {
        showToasterError(throwable)
    }

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(Toaster.TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error).orEmpty())
            else -> showToast(Toaster.TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail).orEmpty())
        }
    }

    private fun onSuccessUpdateEPharmacyCart() {
        RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT).apply {
            putExtra(EXTRA_CHECKOUT_PAGE_SOURCE, EXTRA_CHECKOUT_PAGE_SOURCE_EPHARMACY)
        }.also {
            startActivity(it)
        }
    }

    private fun observerPrescriptionError() {
        ePharmacyPrescriptionAttachmentViewModel.uploadError.observe(viewLifecycleOwner) { error ->

        }
    }

    private fun onSuccessGroupData(it: Success<EPharmacyDataModel>) {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        ePharmacyRecyclerView?.show()
        it.data.listOfComponents.forEach { component ->
            ePharmacyAttachmentUiUpdater.updateModel(component)
        }
        updateUi()
        setUpTotalAmount()
    }

    private fun setUpTotalAmount() {
        totalAmount = calculateTotalAmount()
        qCTotalAmount?.setAmount(EPharmacyUtils.getTotalAmountFmt(totalAmount))
    }

    private fun updateUi() {
        val updatedComponents = ePharmacyAttachmentUiUpdater.mapOfData.values.toList()
        ePharmacyAdapter.submitList(updatedComponents)
    }

    private fun onFailGroupData(it: Fail) {
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
            binding?.root?.rootView?.let { safeView ->
                Toaster.build(safeView, message, Toaster.LENGTH_LONG, type).show()
            }
        }
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

    private fun redirectAttachmentCTA(
        enablerName: String?,
        chooserLogo: String?,
        groupId: String?,
        prescriptionCTA: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionCTA?,
        tokoConsultationId: String?,
        consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?,
        price: String?,
        operatingSchedule: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource.OperatingSchedule?,
        note: String?
    ) {
        when (prescriptionCTA?.actionType) {
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

    private fun startPhotoUpload(enablerName: String?, groupId: String?, requestCode: Int = EPHARMACY_UPLOAD_REQUEST_CODE) {
        EPharmacyMiniConsultationAnalytics.clickUploadResepDokter(enablerName, groupId.orEmpty())
        RouteManager.getIntent(activity, EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING, groupId)
            putExtra(EXTRA_SOURCE_STRING, UPLOAD_PAGE_SOURCE_PAP)
        }.also {
            startActivityForResult(it, requestCode)
        }
    }

    override fun onToast(toasterType: Int, message: String) {
        super.onToast(toasterType, message)
        showToast(toasterType, message)
    }

    override fun onQuantityChanged(changeInValue: Double) {
        super.onQuantityChanged(changeInValue)
        totalAmount += changeInValue
        qCTotalAmount?.setAmount(EPharmacyUtils.getTotalAmountFmt(totalAmount))
    }

    private fun calculateTotalAmount(): Double {
       return ePharmacyAttachmentUiUpdater.getTotalAmount()
    }

    override fun getScreenName(): String = CategoryKeys.EPHARMACY_QUANTITY_CHANGE_BS

    override fun initInjector() {
        getComponent(EPharmacyComponent::class.java).inject(this)
    }

    companion object {

        @JvmStatic
        fun newInstance(tConsultationId: Long): EPharmacyQuantityChangeFragment {
            return EPharmacyQuantityChangeFragment().apply {
                arguments = Bundle().apply {
                    putLong(EPHARMACY_TOKO_CONSULTATION_ID, tConsultationId)
                }
            }
        }
    }
}
