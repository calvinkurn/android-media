package com.tokopedia.play.broadcaster.view.fragment

import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_play_cover_title_setup.*
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener {

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun refresh() {}

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

    override fun onGetCoverFromCamera(imageUri: Uri?) {
//        ivPlayCoverTitleImage.setImageURI(imageUri)
        bottomSheetCoordinator.navigateToFragment(PlayCoverCropperFragment::class.java, Bundle().apply {
            putString(PlayCoverCropperFragment.EXTRA_IMAGE_URI, imageUri.toString())
        })
    }

    private fun initView() {
        containerChangeCover.setOnClickListener {
            onChangeCoverClicked()
        }
    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))

        tfPlayCoverTitleText.textFieldInput.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        tfPlayCoverTitleText.textFieldInput.setHintTextColor(resources.getColor(R.color.play_title_text_hint_color))
        tfPlayCoverTitleText.textFieldInput.setSingleLine(false)
        context?.let {
            tfPlayCoverTitleText.textFieldInput.layoutParams.height = it.resources.getDimensionPixelSize(R.dimen.layout_lvl5)
        }
        tvPlayCoverTitleLabel.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_cover_title_default_title_label),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
        tfPlayCoverTitleText.textFieldInput.hint = getString(R.string.play_prepare_cover_title_default_title_placeholder)
        tfPlayCoverTitleText.textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAX_CHARS))
    }

    private fun observeLiveData() {
    }

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance()
        changeCoverBottomSheet.listener = this
        changeCoverBottomSheet.setShowListener { changeCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        changeCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    companion object {
        private const val MAX_CHARS = 38
    }
}