package com.tokopedia.media.preview.ui.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.uiEagerComponent
import com.tokopedia.media.common.component.NavToolbarComponent
import com.tokopedia.media.common.component.ToolbarTheme
import com.tokopedia.media.common.intent.PreviewIntent
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.databinding.ActivityPreviewBinding
import com.tokopedia.media.preview.ui.activity.pagers.adapter.PreviewPagerAdapter
import com.tokopedia.utils.view.binding.viewBinding

class PickerPreviewActivity : BaseActivity(), NavToolbarComponent.Listener {

    private val binding: ActivityPreviewBinding? by viewBinding()
    private val uiModel = arrayListOf<MediaUiModel>()

    private val navToolbar by uiEagerComponent {
        NavToolbarComponent(
            listener = this,
            parent = it
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        restoreDataState(savedInstanceState)

        initView()
    }

    override fun onCloseClicked() {
        finish()
    }

    override fun onContinueClicked() {
        uiModel.forEach {
            println("MEDIAPICKER -> $it")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CACHE_LAST_SELECTION, uiModel)
    }

    private fun restoreDataState(savedInstanceState: Bundle?) {
        // get data from picker
        PreviewIntent.extractData(intent).also { elements ->
            setIntentData(elements)
        }

        // temporary cache of uiModel
        savedInstanceState?.let {
            it.getParcelableArrayList<MediaUiModel>(CACHE_LAST_SELECTION)
                ?.let { elements ->
                    setIntentData(elements)
                }
        }
    }

    private fun initView() {
        setupToolbar()
        setDataToPager()
    }

    private fun setupToolbar() {
        navToolbar.setTitle(getString(R.string.picker_toolbar_preview_title))
        navToolbar.showContinueButtonAs(true)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
    }

    private fun setDataToPager() {
        binding?.vpPreview?.adapter = PreviewPagerAdapter(
            context = this,
            elements = uiModel
        )
    }

    private fun setIntentData(elements: List<MediaUiModel>) {
        uiModel.clear()
        uiModel.addAll(elements)
    }

    companion object {
        private const val CACHE_LAST_SELECTION = "cache_last_selection"
    }

}