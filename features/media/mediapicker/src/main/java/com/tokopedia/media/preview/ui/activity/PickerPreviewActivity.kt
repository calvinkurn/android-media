package com.tokopedia.media.preview.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.uiComponent
import com.tokopedia.media.common.component.NavToolbarComponent
import com.tokopedia.media.common.component.ToolbarTheme
import com.tokopedia.media.common.intent.PreviewIntent
import com.tokopedia.media.common.types.PickerSelectionType
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.databinding.ActivityPreviewBinding
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.observer.EventFlowFactory
import com.tokopedia.media.picker.ui.observer.EventState
import com.tokopedia.media.preview.ui.component.PreviewPagerComponent
import com.tokopedia.utils.view.binding.viewBinding

class PickerPreviewActivity : BaseActivity()
    , NavToolbarComponent.Listener
    , DrawerSelectionWidget.Listener {

    private val binding: ActivityPreviewBinding? by viewBinding()
    private val uiModel = arrayListOf<MediaUiModel>()

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it
        )
    }

    private val pickerPager by uiComponent {
        PreviewPagerComponent(
            parent = it
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        restoreDataState(savedInstanceState)

        initView()
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onCloseClicked() {
        finishIntent()
    }

    override fun onBackPressed() {
        finishIntent()
    }

    override fun onContinueClicked() {
        uiModel.forEach {
            println("MEDIAPICKER -> $it")
        }
    }

    override fun onItemClicked(media: MediaUiModel) {
        pickerPager.moveToOf(media)
    }

    override fun onDataSetChanged(action: DrawerActionType) {
        when (action) {
            is DrawerActionType.Remove -> {
                pickerPager.removeData(action.mediaToRemove)
                setIntentData(action.data)
            }
            is DrawerActionType.Add -> {
                setIntentData(action.data)
            }
            is DrawerActionType.Reorder -> {
                setIntentData(action.data)
            }
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
        pickerPager.setupView(uiModel)
        setupSelectionDrawerOrActionButton()
    }

    private fun setupToolbar() = with(navToolbar) {
        setTitle(getString(R.string.picker_toolbar_preview_title))
        showContinueButtonAs(true)
        onToolbarThemeChanged(ToolbarTheme.Solid)
    }

    private fun setupSelectionDrawerOrActionButton() {
        val isMultipleSelectionType = PickerUiConfig.paramType == PickerSelectionType.MULTIPLE

        if (isMultipleSelectionType) {
            binding?.drawerSelector?.setMaxAdapterSize(5) // TODO
            binding?.drawerSelector?.addAllData(uiModel)
            binding?.drawerSelector?.show()
        } else {
//            binding?.btnAction?.show()
        }
    }

    private fun setIntentData(elements: List<MediaUiModel>) {
        uiModel.clear()
        uiModel.addAll(elements)
    }

    private fun finishIntent() {
        val intent = Intent()
        intent.putExtra(MEDIA_ELEMENT_RESULT, uiModel)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        private const val CACHE_LAST_SELECTION = "cache_last_selection"

        const val MEDIA_ELEMENT_RESULT = "media_element_result"
    }

}