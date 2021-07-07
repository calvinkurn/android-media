package com.tokopedia.play.broadcaster.view.fragment.interactive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.partial.InteractiveSetupViewComponent
import com.tokopedia.play.broadcaster.view.partial.InteractiveTimePickerViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent


/**
 * Created by mzennis on 07/07/21.
 */
class PlayInteractiveSetupFragment : TkpdBaseV4Fragment(),
    InteractiveSetupViewComponent.Listener,
    InteractiveTimePickerViewComponent.Listener
{

    private val setupView by viewComponent { InteractiveSetupViewComponent(it, this) }
    private val timePickerView by viewComponent { InteractiveTimePickerViewComponent(it, this) }

    /**
     * TODO: please delete
     */
    private val mockAvailableDuration = List(5) { ((it+1)*(if (it > 0) 60 else 30)).toLong() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_interactive_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getScreenName(): String = TAG

    private fun setupView(view: View) {
        setAvailableDuration(mockAvailableDuration)
    }

    fun setActiveDuration(duration: Long) {
        timePickerView.setActiveDuration(duration)
    }

    fun setAvailableDuration(durations: List<Long>) {
        timePickerView.setAvailableDuration(durations)
    }

    /**
     * Setup View Component Listener
     */
    override fun onNextSoftKeyboardClicked(view: InteractiveSetupViewComponent, title: String) {
        timePickerView.show()
    }

    /**
     * TimePicker View Component Listener
     */
    override fun onBackButtonClicked(view: InteractiveTimePickerViewComponent) {
        timePickerView.hide()
        setupView.focusOnEditTitle()
    }

    override fun onValuePickerChanged(
        view: InteractiveTimePickerViewComponent,
        selectedDuration: Long
    ) {
        setupView.setActiveDuration(selectedDuration)
    }

    override fun onApplyButtonClicked(
        view: InteractiveTimePickerViewComponent,
        selectedDuration: Long
    ) {

    }

    companion object {
        private const val TAG = "PlayInteractiveSetupFragment"
    }
}