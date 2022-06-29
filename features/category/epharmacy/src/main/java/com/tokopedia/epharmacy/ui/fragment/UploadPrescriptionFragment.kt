package com.tokopedia.epharmacy.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.epharmacy.network.response.EpharmacyButton
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.utils.ImageCompressor
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UploadPrescriptionFragment : BaseDaggerFragment() , EPharmacyListener {

    private var ePharmacyToolTipText : Typography? = null
    private var ePharmacyRecyclerView : RecyclerView? = null
    private var ePharmacyUploadPhotoButton : UnifyButton? = null
    private var ePharmacyDoneButton : UnifyButton? = null

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

    private var orderId = ""
    private var checkoutId = ""

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
        orderId = arguments?.getString(EXTRA_ORDER_ID,"") ?: ""
        checkoutId = arguments?.getString(EXTRA_CHECKOUT_ID,"") ?: ""
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
        observeButtonData()
        observePrescriptionImages()
        observeUploadPrescriptionIdsData()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyToolTipText = findViewById(R.id.tooltip)
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyUploadPhotoButton = findViewById(R.id.foto_resep_button)
            ePharmacyDoneButton = findViewById(R.id.done_button)
        }
    }

    private fun initData(){
        setupRecyclerView()
        renderToolTip()
        renderButtons()
    }

    private fun getData() {
        if(checkoutId.isNotBlank()) {
            uploadPrescriptionViewModel.getEPharmacyCheckoutDetail(checkoutId)
        }else if(orderId.isNotBlank()){
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
        val terms = getString(R.string.epharmacy_terms)
        val spannableString = SpannableString(terms)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTnC(EPHARMACY_TNC_LINK)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        spannableString.setSpan(clickableSpan, 44, 65, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        ePharmacyToolTipText?.text = spannableString
        ePharmacyToolTipText?.movementMethod = LinkMovementMethod.getInstance()
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

    private fun setCameraDrawableOnButton(){
        MethodChecker.getDrawable(context,com.tokopedia.iconunify.R.drawable.iconunify_camera)?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(it),
                MethodChecker.getColor(context,com.tokopedia.unifyprinciples.R.color.Green_G500)
            )
            ePharmacyUploadPhotoButton?.setDrawable(it, UnifyButton.DrawablePosition.LEFT)
        }
    }

    private fun showTnC(linkUrl : String) {
        EPharmacyWebViewBottomSheet.newInstance("", linkUrl).show(childFragmentManager,EPharmacyWebViewBottomSheet.TAG)
    }

    private fun onClickUploadPhotoButton() {
        uploadPrescriptionViewModel.removeRejectedImages()
        openMediaPicker((MAX_MEDIA_ITEM)
                - (uploadPrescriptionViewModel.prescriptionImages.value?.size ?: 0))
    }

    private fun onDoneButtonClick(){
        if(orderId.isNotBlank()){
            uploadPrescriptionViewModel.uploadPrescriptionIds(UPLOAD_ORDER_ID_KEY,orderId)
        }else if(checkoutId.isNotBlank()){
            uploadPrescriptionViewModel.uploadPrescriptionIds(UPLOAD_CHECKOUT_ID_KEY,orderId)
        }
    }

    private fun openMediaPicker(withMaxMediaItems: Int) {
        context?.let { safeContext ->
            val intent = MediaPicker.intent(safeContext) {
                pageSource(PageSource.EPharmacy)
                modeType(ModeType.IMAGE_ONLY)
                multipleSelectionMode()
                maxMediaItem(withMaxMediaItems - 2)
                maxImageFileSize(4_000_000)
            }
            startActivityForResult(intent, MEDIA_PICKER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            MEDIA_PICKER_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val result = MediaPicker.result(data)
                    uploadPrescriptionViewModel.addSelectedPrescriptionImages(result.originalPaths)
                }
            }else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setNewPrescriptionData() {
        ePharmacyUiUpdater.updateModel(EPharmacyPrescriptionDataModel(PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT,(uploadPrescriptionViewModel.prescriptionImages.value),
            ePharmacyUiUpdater.prescriptionInfoMap?.showCamera ?: true))
        reloadPrescriptionUI()
    }

    private fun reloadPrescriptionUI() {
        updateUi()
    }

    private fun observerEPharmacyDetail(){
        uploadPrescriptionViewModel.productDetailLiveDataResponse.observe(viewLifecycleOwner){
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
        when (it.throwable) {

        }
    }

    private fun observeButtonData() {
        uploadPrescriptionViewModel.buttonLiveData.observe(viewLifecycleOwner){
            updateButtonUI(it)
        }
    }

    private fun observePrescriptionImages() {
        uploadPrescriptionViewModel.prescriptionImages.observe(viewLifecycleOwner){
            setNewPrescriptionData()
        }
    }

    private fun observeUploadPrescriptionIdsData() {
        uploadPrescriptionViewModel.uploadPrescriptionIdsData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    if(orderId.isNotBlank()){
                        openOrderPage()
                    }else if (checkoutId.isNotBlank()) {
                        sendResultToCheckout()
                    }
                }
                is Fail -> {

                }
            }
        })
    }

    private fun sendResultToCheckout() {
        Intent().apply {
            val prescriptionIds = arrayListOf<Long>()
            uploadPrescriptionViewModel.prescriptionImages.value?.let {  presImages ->
                presImages.forEach { presImage ->
                    presImage?.prescriptionId?.let { presId ->
                        if(presId != DEFAULT_ZERO_VALUE){
                            prescriptionIds.add(presId)
                        }
                    }
                }
            }
            putExtra(EPHARMACY_PRESCRIPTION_IDS,prescriptionIds)
            activity?.setResult(EPHARMACY_REQUEST_CODE,this)
        }
    }

    private fun openOrderPage() {
        activity?.finish()
    }

    private fun onSuccessEPharmacyData(it: Success<EPharmacyDataModel>) {
        it.data.listOfComponents.forEach { component ->
            if(component.name() == PRESCRIPTION_COMPONENT){
                (component as? EPharmacyPrescriptionDataModel)?.let { presComponent ->
                    presComponent.prescriptions?.let {
                        uploadPrescriptionViewModel.onSuccessGetPrescriptionImages(it)
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
                    ePharmacyUiUpdater.prescriptionInfoMap?.showCamera = false
                    reloadPrescriptionUI()
                }
            }
        }
    }

    private fun showDoneButtonState(){
        ePharmacyToolTipText?.hide()
        ePharmacyUploadPhotoButton?.hide()
        ePharmacyDoneButton?.show()
    }

    private fun showUploadPhotoButtonState(){
        ePharmacyToolTipText?.show()
        ePharmacyUploadPhotoButton?.show()
        ePharmacyDoneButton?.hide()
    }

    private fun hideAllButtons(){
        ePharmacyToolTipText?.hide()
        ePharmacyUploadPhotoButton?.hide()
        ePharmacyDoneButton?.hide()
    }

    private fun submitList(visitableList: List<BaseEPharmacyDataModel>) {
        ePharmacyAdapter.submitList(visitableList)
    }

    override fun getScreenName() = EPHARMACY_SCREEN_NAME

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
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
        uploadPrescriptionViewModel.reUploadPrescriptionImage((adapterPosition) ,image.localPath ?: "")
    }

}