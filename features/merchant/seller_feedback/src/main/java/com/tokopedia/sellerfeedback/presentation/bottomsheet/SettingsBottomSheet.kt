package com.tokopedia.sellerfeedback.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.databinding.BottomSheetFeedbackSettingsBinding
import com.tokopedia.sellerfeedback.presentation.util.ScreenshotPreferenceManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created By @ilhamsuaib on 05/10/21
 */

class SettingsBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "SettingsBottomSheet"
        fun createInstance() = SettingsBottomSheet().apply {
            showKnob = true
            showCloseIcon = false
            isDragable = true
            isHideable = true
        }
    }

    private var binding by autoClearedNullable<BottomSheetFeedbackSettingsBinding>()
    private val preferenceManager by lazy {
        context?.let {
            return@lazy ScreenshotPreferenceManager(it.applicationContext)
        }
        return@lazy null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetFeedbackSettingsBinding.inflate(
            inflater, container, false
        ).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(getString(R.string.feedback_form_settings_title))
        setupView()
    }

    private fun setupView() {
        binding?.run {
            switchGfScreenShootEnabled.isChecked =
                preferenceManager?.isScreenShootTriggerEnabled().orFalse()
            switchGfScreenShootEnabled.setOnCheckedChangeListener { _, isChecked ->
                preferenceManager?.setScreenShootTriggerEnabled(isChecked)
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}