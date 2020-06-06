package com.tokopedia.play.broadcaster.view.fragment

import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverTitleSetupViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_play_cover_title_setup.*
import kotlinx.android.synthetic.main.layout_play_cover_crop.*
import kotlinx.android.synthetic.main.layout_play_cover_title_setup.*
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener {

    private lateinit var viewModel: PlayCoverTitleSetupViewModel

    private var imageLeft: Float = 0f
    private var imageTop: Float = 0f
    private var imageRight: Float = 0f
    private var imageBottom: Float = 0f

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun refresh() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory)
                .get(PlayCoverTitleSetupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onGetCoverFromCamera(imagePath: String?) {
        imagePath?.let {
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    private fun initView() {
        containerChangeCover.setOnClickListener {
            onChangeCoverClicked()
        }
    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))

        etPlayCoverTitleText.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        etPlayCoverTitleText.setHintTextColor(resources.getColor(R.color.play_white_68))
        etPlayCoverTitleText.setSingleLine(false)
        tvPlayCoverTitleLabel.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_cover_title_default_title_label),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
        etPlayCoverTitleText.hint = getString(R.string.play_prepare_cover_title_default_title_placeholder)
        etPlayCoverTitleText.filters = arrayOf(InputFilter.LengthFilter(MAX_CHARS))
    }

    private fun observeLiveData() {
    }

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance()
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

    private fun renderCoverCropLayout(imagePath: String) {
        ivPlayCoverCropImage.setImageBitmap(ImageUtils.getBitmapFromPath(imagePath,
                ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT, true))
        imageDragDrop()

        imageLeft = ivPlayCoverCropImage.left.toFloat()
        imageTop = ivPlayCoverCropImage.top.toFloat()
        imageRight = ivPlayCoverCropImage.right.toFloat()
        imageBottom = ivPlayCoverCropImage.bottom.toFloat()

        btnPlayCoverCropNext.setOnClickListener {
            val croppedImage = viewModel.cropImage(imagePath, ivPlayCoverCropImage,
                    ivPlayCoverCropOverlay.leftPosition,
                    resources.getDimensionPixelSize(R.dimen.play_cover_width))
            ivPlayCoverCropImage.setImageURI(Uri.parse(croppedImage))
        }
    }

    private fun imageDragDrop() {
        var isDragging = false
        var xSpaceFromLeftImage = 0f

        ivPlayCoverCropImage.setOnTouchListener { view, motionEvent ->
            val xPosition = motionEvent.rawX

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isDragging) {
                        xSpaceFromLeftImage = xPosition - ivPlayCoverCropImage.left
                        isDragging = true
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (ivPlayCoverCropImage.left < ivPlayCoverCropOverlay.leftPosition) {
                        ivPlayCoverCropImage.x = xPosition - xSpaceFromLeftImage
                    } else {
                        ivPlayCoverCropImage.x = ivPlayCoverCropOverlay.leftPosition
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    xSpaceFromLeftImage = 0f
                    isDragging = false
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val MAX_CHARS = 38
    }
}