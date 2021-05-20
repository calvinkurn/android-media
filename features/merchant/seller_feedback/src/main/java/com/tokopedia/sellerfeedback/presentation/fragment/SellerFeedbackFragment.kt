package com.tokopedia.sellerfeedback.presentation.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.common.*
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.SellerFeedbackTracking
import com.tokopedia.sellerfeedback.data.SubmitResult
import com.tokopedia.sellerfeedback.di.component.DaggerSellerFeedbackComponent
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.adapter.ImageFeedbackAdapter
import com.tokopedia.sellerfeedback.presentation.bottomsheet.SellerFeedbackPageChooserBottomSheet
import com.tokopedia.sellerfeedback.presentation.uimodel.BaseImageFeedbackUiModel
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.sellerfeedback.presentation.util.ScreenshotManager
import com.tokopedia.sellerfeedback.presentation.view.SellerFeedbackToolbar
import com.tokopedia.sellerfeedback.presentation.viewholder.BaseImageFeedbackViewHolder
import com.tokopedia.sellerfeedback.presentation.viewmodel.SellerFeedbackViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class SellerFeedbackFragment : BaseDaggerFragment(), BaseImageFeedbackViewHolder.ImageClickListener {

    companion object {
        const val REQUEST_CODE_IMAGE = 111
        const val EXTRA_URI_IMAGE = "uri_image"

        fun createInstance(uri: Uri?): SellerFeedbackFragment {
            return SellerFeedbackFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_URI_IMAGE, uri)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val requiredPermissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val toolbar by lazy { SellerFeedbackToolbar(requireActivity()) }
    private val feedbackDetailTextWatcher by lazy { getFeedbackDetailWatcher() }
    private val imageFeedbackAdapter by lazy { ImageFeedbackAdapter(this) }
    private val imagePickerBuilder by lazy { buildImagePicker() }
    private val imagePickerMultipleSelectionBuilder by lazy { buildImagePickerMultipleSelectionBuilder() }
    private val scoreBad by lazy { Score.Bad() }
    private val scoreNeutral by lazy { Score.Neutral() }
    private val scoreGood by lazy { Score.Good() }

    private var viewModel: SellerFeedbackViewModel? = null
    private var backgroundHeader: FrameLayout? = null
    private var buttonFeedbackBad: Typography? = null
    private var buttonFeedbackNeutral: Typography? = null
    private var buttonFeedbackGood: Typography? = null
    private var chipFeedback: ChipsUnify? = null
    private var chipReportError: ChipsUnify? = null
    private var textFieldFeedbackPage: EditText? = null
    private var textAreaFeedbackDetail: TextAreaUnify? = null
    private var buttonSend: UnifyButton? = null
    private var rvImageFeedback: RecyclerView? = null
    private var customToast: Toast? = null

    private var isValidFeedbackPage = false
    private var isValidFeedbackDetail = false
    private var activeScore: Score? = null
    private var feedbackTypeChips: List<ChipsUnify>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SellerFeedbackViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backgroundHeader = view.findViewById(R.id.background_header)
        buttonFeedbackBad = view.findViewById(R.id.button_feedback_bad)
        buttonFeedbackNeutral = view.findViewById(R.id.button_feedback_neutral)
        buttonFeedbackGood = view.findViewById(R.id.button_feedback_good)
        chipFeedback = view.findViewById(R.id.chip_feedback)
        chipReportError = view.findViewById(R.id.chip_report_error)
        textFieldFeedbackPage = view.findViewById(R.id.textfield_feedback_page)
        textAreaFeedbackDetail = view.findViewById(R.id.textfield_feedback_detail)
        buttonSend = view.findViewById(R.id.button_send)
        rvImageFeedback = view.findViewById(R.id.rv_image_feedback)

        val viewGroupToast = view.findViewById<ViewGroup>(R.id.custom_toast_root)
        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast, viewGroupToast)

        customToast = Toast(context?.applicationContext).apply {
            duration = Toast.LENGTH_LONG
            this.view = customToastLayout
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            val showBottomUp = AnimationUtils.loadAnimation(activity, R.anim.show_bottom_up)
            view.findViewById<ConstraintLayout>(R.id.layout_questions).animation = showBottomUp
        }

        if (!allPermissionsGranted()) {
            requestPermissions(requiredPermissions.toTypedArray(), 5111)
        }

        setupViewInteraction()
        observeViewModel()
        initData()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val component = activity?.run {
            DaggerSellerFeedbackComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
        component?.inject(this)
    }

    private fun setupViewInteraction() {
        buttonFeedbackBad?.setOnClickListener { onClickFeedbackBtn(scoreBad) }
        buttonFeedbackNeutral?.setOnClickListener { onClickFeedbackBtn(scoreNeutral) }
        buttonFeedbackGood?.setOnClickListener { onClickFeedbackBtn(scoreGood) }

        feedbackTypeChips = listOfNotNull(chipFeedback, chipReportError)
        feedbackTypeChips?.let { chips ->
            chips.forEach { chip ->
                chip.setOnClickListener {
                    chip.chipType = ChipsUnify.TYPE_SELECTED
                    toggleChipsToNormal(chips - chip)
                }
            }
        }


        textFieldFeedbackPage?.setOnClickListener {
            val currentValue = textFieldFeedbackPage?.text.toString()
            val bottomSheet = SellerFeedbackPageChooserBottomSheet.createInstance(currentValue)
            bottomSheet.setListener {
                textFieldFeedbackPage?.setText(it)
                isValidFeedbackPage = true
                checkButtonSend()
            }
            activity?.supportFragmentManager?.let { bottomSheet.show(it, null) }
        }

        textAreaFeedbackDetail?.textAreaInput?.addTextChangedListener(feedbackDetailTextWatcher)

        rvImageFeedback?.adapter = imageFeedbackAdapter

        buttonSend?.apply {
            setOnClickListener {
                isClickable = false
                isLoading = true
                SellerFeedbackTracking.Click.eventClickSubmit()
                val sellerFeedback = SellerFeedback(
                        feedbackScore = getFeedbackScore(),
                        feedbackType = getFeedbackType(),
                        feedbackPage = getFeedbackPage(),
                        feedbackDetail = getFeedbackDetail()
                )
                viewModel?.submitFeedback(sellerFeedback)
            }

        }
    }

    private fun onClickFeedbackBtn(score: Score) {
        activeScore?.let { toggleScoreToInactive(it) }
        toggleScoreToActive(score)
        changeHeaderColor(score.colorId)
        activeScore = score
        checkButtonSend()
    }

    private fun toggleScoreToActive(score: Score) {
        val button = when (score) {
            is Score.Bad -> buttonFeedbackBad
            is Score.Neutral -> buttonFeedbackNeutral
            is Score.Good -> buttonFeedbackGood
        }
        button?.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, score.drawableActiveId, 0, 0)
            setWeight(Typography.BOLD)
        }
    }

    private fun toggleScoreToInactive(score: Score) {
        val button = when (score) {
            is Score.Bad -> buttonFeedbackBad
            is Score.Neutral -> buttonFeedbackNeutral
            is Score.Good -> buttonFeedbackGood
        }
        button?.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, score.drawableInactiveId, 0, 0)
            setWeight(Typography.REGULAR)
        }
    }

    private fun changeHeaderColor(@ColorRes colorId: Int) {
        backgroundHeader?.apply {
            val color = ContextCompat.getColor(requireContext(), colorId)
            setBackgroundColor(color)
            toolbar.setupBackground(colorId)
        }
    }

    private fun toggleChipsToNormal(chips: List<ChipsUnify>) {
        chips.forEach { chip ->
            chip.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun getFeedbackDetailWatcher(): TextWatcher {
        return object : TextWatcher {

            private val MAX_CHAR = textAreaFeedbackDetail?.textAreaCounter

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val text = p0?.toString() ?: ""
                if (text.isBlank()) {
                    textAreaFeedbackDetail?.textAreaMessage = getString(R.string.feedback_form_detail_error_empty)
                    textAreaFeedbackDetail?.isError = true
                } else if (text.length == MAX_CHAR) {
                    textAreaFeedbackDetail?.textAreaMessage = ""
                    textAreaFeedbackDetail?.isError = true
                } else {
                    textAreaFeedbackDetail?.textAreaMessage = ""
                    textAreaFeedbackDetail?.isError = false
                }
                isValidFeedbackDetail = textAreaFeedbackDetail?.isError == false
                checkButtonSend()
            }
        }
    }

    private fun checkButtonSend() {
        buttonSend?.isEnabled = isValidFeedbackPage &&
                isValidFeedbackDetail &&
                activeScore != null &&
                imageFeedbackAdapter.itemCount > 1
    }

    private fun getFeedbackScore(): String {
        return activeScore?.let { getString(it.value) } ?: ""
    }

    private fun getFeedbackType(): String {
        val selectedChip = feedbackTypeChips?.first { it.chipType == ChipsUnify.TYPE_SELECTED }
        return selectedChip?.chipText ?: ""
    }

    private fun getFeedbackPage(): String {
        return textFieldFeedbackPage?.text.toString()
    }

    private fun getFeedbackDetail(): String {
        return textAreaFeedbackDetail?.textAreaInput?.text.toString()
    }

    private fun observeViewModel() {
        viewModel?.run {
            getFeedbackImages().observe(viewLifecycleOwner, observerFeedbackImages)
            getSubmitResult().observe(viewLifecycleOwner, observerSubmitResult)
        }
    }

    private val observerFeedbackImages = Observer<List<ImageFeedbackUiModel>> {
        imageFeedbackAdapter.setImageFeedbackData(it)
        checkButtonSend()
    }

    private val observerSubmitResult = Observer<SubmitResult> {
        when (it) {
            is SubmitResult.Success -> {
                customToast?.show()
                activity?.finish()
            }
            is SubmitResult.UploadFail -> showErrorToaster(getString(R.string.feedback_form_toaster_fail_upload))
            is SubmitResult.SubmitFail -> showErrorToaster(getString(R.string.feedback_form_toaster_fail_submit))
            is SubmitResult.NetworkFail -> showErrorToaster(getString(R.string.feedback_form_toaster_fail_network))
        }
        buttonSend?.apply {
            isLoading = false
            isClickable = true
        }
    }

    private fun showErrorToaster(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun initData() {
        val uriImage = arguments?.getParcelable<Uri>(EXTRA_URI_IMAGE)
        if (uriImage != null) {
            attachScreenshot(uriImage)
        }
    }

    private fun attachScreenshot(uri: Uri) {
        if (allPermissionsGranted()) {
            val screenshotManager = ScreenshotManager(requireContext())
            screenshotManager.getUiModel(uri)?.let {
                viewModel?.setImages(listOf(it))
            }
        }
    }


    private fun allPermissionsGranted(): Boolean {
        requiredPermissions.forEach { permission ->
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun buildImagePicker(): ImagePickerBuilder {
        return ImagePickerBuilder(
                title = getString(R.string.feedback_form_gallery_picker_title),
                galleryType = GalleryType.IMAGE_ONLY,
                imagePickerTab = arrayOf(ImagePickerTab.TYPE_GALLERY)
        )
    }

    private fun buildImagePickerMultipleSelectionBuilder(): ImagePickerMultipleSelectionBuilder {
        return ImagePickerMultipleSelectionBuilder(
                maximumNoPick = 3
        )
    }

    override fun onClickRemoveImage(item: BaseImageFeedbackUiModel) {
        SellerFeedbackTracking.Click.eventClickRemoveAttachment()
        imageFeedbackAdapter.removeImage(item)
    }

    override fun onClickAddImage() {
        SellerFeedbackTracking.Click.eventClickPutAttachment()
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.IMAGE_PICKER)
        val currentSelectedImages = getSelectedImageUrl(imageFeedbackAdapter.getImageFeedbackData())
        imagePickerMultipleSelectionBuilder.initialSelectedImagePathList = currentSelectedImages
        imagePickerBuilder.imagePickerMultipleSelectionBuilder = imagePickerMultipleSelectionBuilder
        intent.putImagePickerBuilder(imagePickerBuilder)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> handleImagePicker(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleImagePicker(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImages = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
            if (selectedImages.isNotEmpty()) {
                val imageFeedbackUiModels = selectedImages.map {
                    ImageFeedbackUiModel(it)
                }
                viewModel?.setImages(imageFeedbackUiModels)
            }
        }
    }

    private fun getSelectedImageUrl(imageFeedbackDataList: List<BaseImageFeedbackUiModel>): ArrayList<String> {
        return imageFeedbackDataList.filterIsInstance(ImageFeedbackUiModel::class.java)
                .map { it.imageUrl } as ArrayList<String>
    }

    sealed class Score {
        abstract val value: Int
        abstract val colorId: Int
        abstract val drawableActiveId: Int
        abstract val drawableInactiveId: Int

        data class Bad(
                override val value: Int = R.string.feedback_form_score_bad,
                override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_Y500,
                override val drawableActiveId: Int = R.drawable.ic_emoji_bad_active,
                override val drawableInactiveId: Int = R.drawable.ic_emoji_bad_inactive
        ) : Score()

        data class Neutral(
                override val value: Int = R.string.feedback_form_score_neutral,
                override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_Y400,
                override val drawableActiveId: Int = R.drawable.ic_emoji_neutral_active,
                override val drawableInactiveId: Int = R.drawable.ic_emoji_neutral_inactive
        ) : Score()

        data class Good(
                override val value: Int = R.string.feedback_form_score_good,
                override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                override val drawableActiveId: Int = R.drawable.ic_emoji_good_active,
                override val drawableInactiveId: Int = R.drawable.ic_emoji_good_inactive
        ) : Score()
    }


}