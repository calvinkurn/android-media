package com.tokopedia.epharmacy.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.ui.activity.EPharmacyActivity
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.utils.TrackerId.Companion.IMAGE_UPLOAD_FAILED_ID
import com.tokopedia.epharmacy.utils.TrackerId.Companion.IMAGE_UPLOAD_SUCCESS_ID
import com.tokopedia.epharmacy.utils.TrackerId.Companion.SUBMIT_PRESCRIPTION_ID
import com.tokopedia.epharmacy.utils.TrackerId.Companion.SUBMIT_SUCCESS_ID
import com.tokopedia.epharmacy.utils.TrackerId.Companion.UPLOAD_PRESCRIPTION_ID
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.track.builder.Tracker
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class UploadPrescriptionFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyToolTipText: Typography? = null
    private var ePharmacyToolTipHyperLinkText: Typography? = null
    private var ePharmacyToolTipGroup: Group? = null
    private var ePharmacyRecyclerView: RecyclerView? = null
    private var ePharmacyUploadPhotoButton: UnifyButton? = null
    private var ePharmacyDoneButton: UnifyButton? = null
    private var ePharmacyLoader: LoaderUnify? = null
    private var ePharmacyGlobalError: GlobalError? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val uploadPrescriptionViewModel: UploadPrescriptionViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(UploadPrescriptionViewModel::class.java)
    }

    private var ePharmacyUiUpdater: EPharmacyUiUpdater = EPharmacyUiUpdater(mutableMapOf())

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(this) }

    private var orderId = DEFAULT_ZERO_VALUE
    private var checkoutId = ""
    private var entryPoint = ""
    private var source = ""

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
        source = arguments?.getString(EXTRA_SOURCE_STRING, ENTRY_POINT_CHECKOUT.lowercase()) ?: (ENTRY_POINT_CHECKOUT.lowercase())
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observeButtonData()
        observePrescriptionImages()
        observeUploadPrescriptionIdsData()
        observerUploadPrescriptionError()
        observeUploadPhotoLiveData()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyToolTipText = findViewById(R.id.tooltip)
            ePharmacyToolTipHyperLinkText = findViewById(R.id.tooltip_hyperlink_text)
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyUploadPhotoButton = findViewById(R.id.foto_resep_button)
            ePharmacyDoneButton = findViewById(R.id.done_button)
            ePharmacyToolTipGroup = findViewById(R.id.tooltip_group)
            ePharmacyLoader = findViewById(R.id.epharmacy_loader)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
    }

    private fun initData() {
        setupRecyclerView()
        renderToolTip()
        renderButtons()
    }

    private fun getData() {
        ePharmacyLoader?.show()
        if(checkoutId.isNotBlank()) {
            uploadPrescriptionViewModel.getEPharmacyCheckoutDetail(checkoutId,source)
        } else if (orderId != DEFAULT_ZERO_VALUE) {
            uploadPrescriptionViewModel.getEPharmacyOrderDetail(orderId)
        }
    }

    private fun setupRecyclerView() {
        ePharmacyRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ePharmacyAdapter
        }
    }

    private fun renderToolTip() {
        ePharmacyToolTipText?.text = context?.resources?.getString(R.string.epharmacy_terms)
        ePharmacyToolTipHyperLinkText?.text = context?.resources?.getString(R.string.epharmacy_terms_hyper_text)
        ePharmacyToolTipHyperLinkText?.setOnClickListener {
            showTnC()
        }
    }

    private fun renderButtons() {
        setCameraDrawableOnButton()
        ePharmacyUploadPhotoButton?.setOnClickListener {
            onClickUploadPhotoButton()
        }
        ePharmacyDoneButton?.setOnClickListener {
            onDoneButtonClick()
        }
    }

    private fun setCameraDrawableOnButton() {
        MethodChecker.getDrawable(context, com.tokopedia.iconunify.R.drawable.iconunify_camera)?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(it),
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            )
            ePharmacyUploadPhotoButton?.setDrawable(it, UnifyButton.DrawablePosition.LEFT)
        }
    }

    private fun showTnC() {
        context?.let { safeContext ->
            startActivity(Intent(Intent(safeContext, BaseSimpleWebViewActivity::class.java)).putExtra(KEY_URL, EPHARMACY_TNC_LINK))
        }
    }

    private fun onClickUploadPhotoButton() {
        openMediaPicker(
            (MAX_MEDIA_ITEM) -
                (uploadPrescriptionViewModel.prescriptionImages.value?.size ?: 0)
        )
    }

    private fun onDoneButtonClick() {
        sendSubmitButtonClickEvent()
        ePharmacyLoader?.show()
        if (orderId != DEFAULT_ZERO_VALUE) {
            uploadPrescriptionViewModel.uploadPrescriptionIdsInOrder(orderId)
        } else if (checkoutId.isNotBlank()) {
            uploadPrescriptionViewModel.uploadPrescriptionIdsInCheckout(checkoutId)
        }
    }

    private fun openMediaPicker(withMaxMediaItems: Int) {
        context?.let { safeContext ->
            val intent = MediaPicker.intent(safeContext) {
                pageSource(PageSource.Epharmacy)
                modeType(ModeType.IMAGE_ONLY)
                multipleSelectionMode()
                maxVideoItem(0)
                maxMediaItem(withMaxMediaItems)
                maxImageFileSize(MAX_MEDIA_SIZE_PICKER)
            }
            startActivityForResult(intent, MEDIA_PICKER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MEDIA_PICKER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = MediaPicker.result(data)
                    sendUploadPrescriptionButtonClickFromPreview()
                    uploadPrescriptionViewModel.addSelectedPrescriptionImages(result.originalPaths)
                }
            } else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setNewPrescriptionData() {
        ePharmacyUiUpdater.updateModel(
            EPharmacyPrescriptionDataModel(
                PRESCRIPTION_COMPONENT,
                PRESCRIPTION_COMPONENT,
                (uploadPrescriptionViewModel.prescriptionImages.value),
                ePharmacyUiUpdater.prescriptionInfoMap?.isReUpload ?: true
            )
        )
        reloadPrescriptionUI()
    }

    private fun reloadPrescriptionUI() {
        updateUi()
    }

    private fun observerEPharmacyDetail() {
        uploadPrescriptionViewModel.productDetailLiveDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessEPharmacyData(it)
                }
                is Fail -> {
                    onFailEPharmacyData(it)
                }
            }
        }
    }

    private fun onFailEPharmacyData(it: Fail) {
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

    private fun observeButtonData() {
        uploadPrescriptionViewModel.buttonLiveData.observe(viewLifecycleOwner) {
            updateButtonUI(it)
        }
    }

    private fun observePrescriptionImages() {
        uploadPrescriptionViewModel.prescriptionImages.observe(viewLifecycleOwner) {
            setNewPrescriptionData()
        }
    }

    private fun observeUploadPrescriptionIdsData() {
        uploadPrescriptionViewModel.uploadPrescriptionIdsData.observe(viewLifecycleOwner, {
            ePharmacyLoader?.hide()
            when (it) {
                is Success -> {
                    sendSubmitSuccessEvent()
                    if (orderId != DEFAULT_ZERO_VALUE) {
                        openOrderPage(orderId)
                    } else if (checkoutId.isNotBlank()) {
                        sendResultToCheckout()
                    }
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException ||
                        it.throwable is SocketTimeoutException
                    ) {
                        showToast(context?.resources?.getString(R.string.epharmacy_internet_error) ?: "")
                    } else {
                        it.throwable.message?.let { errorMessage ->
                            showToast(errorMessage)
                        }
                    }
                }
            }
        })
    }

    private fun observerUploadPrescriptionError() {
        uploadPrescriptionViewModel.uploadError.observe(viewLifecycleOwner, { error ->
            when (error) {
                is EPharmacyNoInternetError -> showToast(context?.resources?.getString(R.string.epharmacy_upload_error) ?: "")
                is EPharmacyUploadBackendError -> showToast(error.errMsg)
                is EPharmacyUploadEmptyImageError -> {
                    if (error.showErrorToast) {
                        showToast(context?.resources?.getString(R.string.epharmacy_upload_error) ?: "")
                    }
                }

                is EPharmacyUploadNoPrescriptionIdError -> showToast(context?.resources?.getString(R.string.epharmacy_upload_error) ?: "")
            }
            sendUploadImageFailedEvent()
        })
    }

    private fun observeUploadPhotoLiveData() {
        uploadPrescriptionViewModel.successUploadPhoto.observe(viewLifecycleOwner, { isSuccessUpload ->
            if (isSuccessUpload) {
                sendUploadImageSuccessEvent()
            }
        })
    }

    private fun showToast(message: String) {
        view?.let { it ->
            Toaster.build(it, message, LENGTH_LONG, TYPE_ERROR).show()
        }
    }

    private fun sendResultToCheckout() {
        Intent().apply {
            val prescriptionIds = arrayListOf<String>()
            uploadPrescriptionViewModel.prescriptionImages.value?.let { presImages ->
                presImages.forEach { presImage ->
                    presImage?.prescriptionId?.let { presId ->
                        if (presId != DEFAULT_ZERO_VALUE) {
                            prescriptionIds.add(presId.toString())
                        }
                    }
                }
            }
            putStringArrayListExtra(EPHARMACY_PRESCRIPTION_IDS, prescriptionIds)
            activity?.setResult(EPHARMACY_REQUEST_CODE, this)
            activity?.finish()
        }
    }

    private fun openOrderPage(orderId: Long) {
        view?.let {
            context?.resources?.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_success_text)?.let { successMessage ->
                Toaster.build(it, successMessage, LENGTH_LONG, TYPE_NORMAL).show()
            }
        }
        Timer().schedule(DELAY_IN_MILLS_FOR_SNACKBAR_VIEW) { activity?.finish() }
    }

    private fun onSuccessEPharmacyData(it: Success<EPharmacyDataModel>) {
        ePharmacyLoader?.hide()
        it.data.listOfComponents.forEach { component ->
            if (component.name() == PRESCRIPTION_COMPONENT) {
                (component as? EPharmacyPrescriptionDataModel)?.let { presComponent ->
                    presComponent.prescriptions?.let {
                        uploadPrescriptionViewModel.onSuccessGetPrescriptionImages(it)
                    }
                }
            } else {
                ePharmacyUiUpdater.updateModel(component)
            }
        }
        updateUi()
    }

    private fun updateUi() {
        val newData = ePharmacyUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun updateButtonUI(buttonType: String?) {
        buttonType?.let {
            when (it) {
                EPharmacyButtonKey.RE_UPLOAD.key -> {
                    showUploadPhotoButtonState()
                }
                EPharmacyButtonKey.DONE_DISABLED.key -> {
                    showDoneButtonState()
                    ePharmacyDoneButton?.isEnabled = false
                }
                EPharmacyButtonKey.DONE.key -> {
                    showDoneButtonState()
                    ePharmacyDoneButton?.isEnabled = true
                }
                EPharmacyButtonKey.CHECK.key -> {
                    hideAllButtons()
                    ePharmacyUiUpdater.prescriptionInfoMap?.isReUpload = false
                    context?.resources?.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_title_view_only)?.let { title ->
                        (activity as? EPharmacyActivity)?.updateTitle(title)
                    }
                    reloadPrescriptionUI()
                }
            }
        }
    }

    private fun showDoneButtonState() {
        ePharmacyToolTipGroup?.hide()
        ePharmacyUploadPhotoButton?.hide()
        ePharmacyDoneButton?.show()
    }

    private fun showUploadPhotoButtonState() {
        ePharmacyToolTipGroup?.show()
        ePharmacyUploadPhotoButton?.show()
        ePharmacyDoneButton?.hide()
    }

    private fun hideAllButtons() {
        ePharmacyToolTipGroup?.hide()
        ePharmacyUploadPhotoButton?.hide()
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
        fun newInstance(bundle: Bundle): UploadPrescriptionFragment {
            val fragment = UploadPrescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCameraClick() {
        onClickUploadPhotoButton()
    }

    override fun onPrescriptionImageClick(adapterPosition: Int, image: PrescriptionImage) {
    }

    override fun onPrescriptionCrossImageClick(adapterPosition: Int) {
        removeImageFromPrescriptions(adapterPosition)
    }

    private fun removeImageFromPrescriptions(adapterPosition: Int) {
        uploadPrescriptionViewModel.removePrescriptionImageAt(adapterPosition)
    }

    override fun onPrescriptionReLoadButtonClick(adapterPosition: Int, image: PrescriptionImage) {
        uploadPrescriptionViewModel.reUploadPrescriptionImage((adapterPosition), image.localPath ?: "")
    }

    private fun sendUploadPrescriptionButtonClickFromPreview() {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.UPLOAD_PRESCRIPTION)
            .setEventCategory(CategoryKeys.UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: ${getCurrentId()}")
            .setCustomProperty(EventKeys.TRACKER_ID, UPLOAD_PRESCRIPTION_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendUploadImageSuccessEvent() {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_CONTENT_IRIS)
            .setEventAction(ActionKeys.IMAGE_UPLOAD_SUCCESS)
            .setEventCategory(CategoryKeys.UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: ${getCurrentId()}")
            .setCustomProperty(EventKeys.TRACKER_ID, IMAGE_UPLOAD_SUCCESS_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendSubmitButtonClickEvent() {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.SUBMIT_PRESCRIPTION)
            .setEventCategory(CategoryKeys.UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: ${getCurrentId()}")
            .setCustomProperty(EventKeys.TRACKER_ID, SUBMIT_PRESCRIPTION_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendSubmitSuccessEvent() {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_CONTENT_IRIS)
            .setEventAction(ActionKeys.SUBMIT_SUCCESS)
            .setEventCategory(CategoryKeys.UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: ${getCurrentId()}")
            .setCustomProperty(EventKeys.TRACKER_ID, SUBMIT_SUCCESS_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendUploadImageFailedEvent() {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_CONTENT_IRIS)
            .setEventAction(ActionKeys.IMAGE_UPLOAD_FAILED)
            .setEventCategory(CategoryKeys.UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: ${getCurrentId()}")
            .setCustomProperty(EventKeys.TRACKER_ID, IMAGE_UPLOAD_FAILED_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun getCurrentId(): String {
        return if (orderId == DEFAULT_ZERO_VALUE) {
            checkoutId
        } else {
            orderId.toString()
        }
    }
}
