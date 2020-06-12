package com.tokopedia.play.broadcaster.view.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.ui.model.CoverStarterEnum
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastCoverFromGalleryBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverTitleViewModel
import com.tokopedia.play.broadcaster.view.widget.PlayCropImageView
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.util.RectUtils
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_play_cover_title_setup.*
import kotlinx.android.synthetic.main.layout_play_cover_crop.*
import kotlinx.android.synthetic.main.layout_play_cover_title_setup.*
import java.io.File
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(private val viewModelFactory: ViewModelFactory)
    : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener,
        PlayBroadcastCoverFromGalleryBottomSheet.Listener {

    private lateinit var viewModel: PlayBroadcastCoverTitleViewModel

    private var selectedImageUrlList = arrayListOf<Pair<Long, String>>()
    private var liveTitle: String = ""
    private var selectedCoverUri: Uri? = null
    private var coverSource: CoverSourceEnum = CoverSourceEnum.NONE
    private var starterState: CoverStarterEnum = CoverStarterEnum.NORMAL

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean = false

    override fun refresh() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            selectedImageUrlList = it.getSerializable(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST) as ArrayList<Pair<Long, String>>?
                    ?: arrayListOf()
            liveTitle = it.getString(EXTRA_LIVE_TITLE, "") ?: ""
            starterState = if (it.getInt(EXTRA_STARTER_STATE) == CoverStarterEnum.CROP_ONLY.value)
                CoverStarterEnum.CROP_ONLY else CoverStarterEnum.NORMAL
            coverSource = when (it.getInt(EXTRA_COVER_SOURCE)) {
                CoverSourceEnum.CAMERA.value -> CoverSourceEnum.CAMERA
                CoverSourceEnum.PRODUCT.value -> CoverSourceEnum.PRODUCT
                CoverSourceEnum.GALLERY.value -> CoverSourceEnum.GALLERY
                else -> CoverSourceEnum.NONE
            }
        } ?: arguments?.let {
            selectedImageUrlList = it.getSerializable(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST) as ArrayList<Pair<Long, String>>?
                    ?: arrayListOf()
            starterState = if (it.getInt(EXTRA_STARTER_STATE) == CoverStarterEnum.CROP_ONLY.value)
                CoverStarterEnum.CROP_ONLY else CoverStarterEnum.NORMAL
            coverSource = when (it.getInt(EXTRA_COVER_SOURCE)) {
                CoverSourceEnum.CAMERA.value -> CoverSourceEnum.CAMERA
                CoverSourceEnum.PRODUCT.value -> CoverSourceEnum.PRODUCT
                CoverSourceEnum.GALLERY.value -> CoverSourceEnum.GALLERY
                else -> CoverSourceEnum.NONE
            }

            // if crop mode and from gallery/camera, there will be Image Uri provided
            if (it.containsKey(EXTRA_COVER_URI)) {
                selectedCoverUri = it.getParcelable(EXTRA_COVER_URI)
            }
        }
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory)
                .get(PlayBroadcastCoverTitleViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.imageEcsLink.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                if (liveTitle.isNotEmpty()) {
                    bottomSheetCoordinator.saveCoverAndTitle(it, liveTitle)
                }
            }
        })

        viewModel.originalImageUri.observe(viewLifecycleOwner, Observer {
            if (coverSource == CoverSourceEnum.PRODUCT) {
                renderCoverCropLayout(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST, selectedImageUrlList)
        outState.putString(EXTRA_LIVE_TITLE, liveTitle)
        outState.putInt(EXTRA_STARTER_STATE, starterState.value)
        outState.putInt(EXTRA_COVER_SOURCE, coverSource.value)
    }

    override fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.CAMERA
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    override fun onGetCoverFromProduct(position: Int) {
        coverSource = CoverSourceEnum.PRODUCT
        viewModel.getOriginalImageUrl(requireContext(),
                selectedImageUrlList[position].first,
                selectedImageUrlList[position].second)
        showCoverCropLayout()
    }

    override fun onChooseFromGalleryClicked() {
        val coverFromGalleryBottomSheet = PlayBroadcastCoverFromGalleryBottomSheet()
        coverFromGalleryBottomSheet.listener = this
        coverFromGalleryBottomSheet.setShowListener { coverFromGalleryBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        coverFromGalleryBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.GALLERY
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))
        bottomSheetCoordinator.showBottomAction(false)

        containerChangeCover.setOnClickListener {
            onChangeCoverClicked()
        }
        ivPlayCoverImage.setOnClickListener {
            onChangeCoverClicked()
        }
        btnPlayPrepareBroadcastNext.setOnClickListener {
            onFinishCoverTitleSetup()
        }

        etPlayCoverTitleText.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        etPlayCoverTitleText.setHintTextColor(resources.getColor(R.color.play_white_68))
        etPlayCoverTitleText.setSingleLine(false)
        etPlayCoverTitleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                liveTitle = text.toString()
                setupTitleCounter()
                setupNextButton()
            }
        })
        tvPlayCoverTitleLabel.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_cover_title_default_title_label),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
        etPlayCoverTitleText.hint = getString(R.string.play_prepare_cover_title_default_title_placeholder)
        etPlayCoverTitleText.filters = arrayOf(InputFilter.LengthFilter(MAX_CHARS))

        setupNextButton()
        setupCoverLabelText()

        if (starterState == CoverStarterEnum.CROP_ONLY) {
            showCoverCropLayout()
            if (coverSource == CoverSourceEnum.GALLERY || coverSource == CoverSourceEnum.CAMERA) {
                selectedCoverUri?.let {
                    renderCoverCropLayout(it)
                }
            } else {
                viewModel.getOriginalImageUrl(requireContext(),
                        selectedImageUrlList[0].first,
                        selectedImageUrlList[0].second)
            }
        }
    }

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance(selectedImageUrlList)
        changeCoverBottomSheet.listener = this
        changeCoverBottomSheet.setShowListener { changeCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        changeCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    private fun showCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.GONE
        containerPlayCoverCropSetup.visibility = View.VISIBLE
    }

    private fun hideCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.VISIBLE
        containerPlayCoverCropSetup.visibility = View.GONE
    }

    private fun renderCoverTitleLayout(resultImageUri: Uri?) {
        resultImageUri?.let {
            selectedCoverUri = resultImageUri
            if (liveTitle.isNotEmpty()) etPlayCoverTitleText.setText(liveTitle)
            ivPlayCoverImage.setImageURI(selectedCoverUri)
        }
        setupCoverLabelText()
        setupNextButton()
    }

    private fun renderCoverCropLayout(imageUri: Uri) {
        containerPlayCoverCropImage.removeAllViews()
        val ivPlayCoverCropImage = PlayCropImageView(requireContext())
        ivPlayCoverCropImage.setImageUri(imageUri, null)
        ivPlayCoverCropImage.isScaleEnabled = true
        ivPlayCoverCropImage.isRotateEnabled = false

        // need delay until onDraw for overlay is called to get the crop points
        ivPlayCoverCropImage.post {
            ivPlayCoverCropImage.layoutParams.height = resources.getDimensionPixelSize(R.dimen.play_cover_height)
        }
        Handler().postDelayed({
            ivPlayCoverCropImage.setCropRect(ivPlayCoverCropOverlay.getCropRect())
        }, SECONDS)

        containerPlayCoverCropImage.addView(ivPlayCoverCropImage)

        btnPlayCoverCropNext.setOnClickListener {
            context?.let { context ->
                ivPlayCoverCropImage.viewBitmap?.let {
                    viewModel.cropImage(context,
                            ivPlayCoverCropImage.imageInputPath,
                            ivPlayCoverCropOverlay.getCropRect(),
                            RectUtils.trapToRect(ivPlayCoverCropImage.getCurrentImageCorners()),
                            ivPlayCoverCropImage.currentScale,
                            ivPlayCoverCropImage.currentAngle,
                            ivPlayCoverCropImage.exifInfo,
                            it, object : BitmapCropCallback {
                        override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                            val finalResultUri = viewModel.validateImageMinSize(resultUri)
                            onImageCropped(finalResultUri)
                        }

                        override fun onCropFailure(t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }
        }
        btnPlayCoverCropChange.setOnClickListener {
            containerPlayCoverCropImage.removeAllViews()
            onCancelCropImage()
        }
    }

    private fun onCancelCropImage() {
        hideCoverCropLayout()
        renderCoverTitleLayout(null)
        when (coverSource) {
            CoverSourceEnum.GALLERY -> onChooseFromGalleryClicked()
            else -> onChangeCoverClicked()
        }
    }

    private fun onImageCropped(resultImageUri: Uri) {
        if (starterState == CoverStarterEnum.CROP_ONLY) {
            onFinishCoverTitleSetup()
        } else {
            hideCoverCropLayout()
            renderCoverTitleLayout(resultImageUri)
        }
    }

    private fun setupCoverLabelText() {
        if (selectedCoverUri != null) {
            tvPlayChangeCoverLabel.text = getString(R.string.play_prepare_cover_title_change_cover_label)
        } else {
            tvPlayChangeCoverLabel.text = getString(R.string.play_prepare_cover_title_add_cover_label)
        }
    }

    private fun setupTitleCounter() {
        tvPlayTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                liveTitle.length, MAX_CHARS)
    }

    private fun setupNextButton() {
        btnPlayPrepareBroadcastNext.isEnabled = liveTitle.isNotEmpty() && selectedCoverUri != null
    }

    private fun onFinishCoverTitleSetup() {
        if (::viewModel.isInitialized && selectedCoverUri != null) {
            viewModel.uploadCover(File(selectedCoverUri?.path).absolutePath)
        }
    }

    companion object {
        const val EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST = "EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST"
        const val EXTRA_STARTER_STATE = "EXTRA_STARTER_STATE"
        const val EXTRA_COVER_SOURCE = "EXTRA_COVER_SOURCE"
        const val EXTRA_COVER_URI = "EXTRA_COVER_URI"

        private const val EXTRA_LIVE_TITLE = "EXTRA_LIVE_TITLE"
        private const val MAX_CHARS = 38
        private const val SECONDS: Long = 1000
    }
}
