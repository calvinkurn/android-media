package com.tokopedia.play.broadcaster.view.fragment.interactive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveDurationInfoUiModel
import com.tokopedia.play.broadcaster.view.partial.InteractiveSetupViewComponent
import com.tokopedia.play.broadcaster.view.partial.InteractiveTimePickerViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent


/**
 * Created by mzennis on 07/07/21.
 */
class PlayInteractiveSetupFragment : TkpdBaseV4Fragment(), InteractiveTimePickerViewComponent.Listener {

    private val setupView by viewComponent { InteractiveSetupViewComponent(it) }
    private val timePickerView by viewComponent { InteractiveTimePickerViewComponent(it, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_interactive_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
    }

    private fun setupView(view: View) {

    }

    override fun getScreenName(): String = TAG

    /**
     * TimePicker View Component Listener
     */
    override fun onCloseButtonClicked(view: InteractiveTimePickerViewComponent) {

    }

    override fun onValuePickerChanged(
        view: InteractiveTimePickerViewComponent,
        selectedDuration: InteractiveDurationInfoUiModel
    ) {

    }

    override fun onApplyButtonClicked(
        view: InteractiveTimePickerViewComponent,
        selectedDuration: InteractiveDurationInfoUiModel
    ) {

    }

    companion object {
        private const val TAG = "PlayInteractiveSetupFragment"
    }
}