package com.tokopedia.play.broadcaster.view.fragment.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.datetimepicker.DateTimePicker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.SetupBroadcastScheduleViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class SetupBroadcastScheduleBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var dateTimePicker: DateTimePicker

    private var btnSet: UnifyButton? = null

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: SetupBroadcastScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupBroadcastScheduleViewModel::class.java)

        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet_Setup_Pinned)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    private fun inject() {
        DaggerPlayBroadcastSetupComponent.builder()
                .setBroadcastComponent((requireActivity() as PlayBroadcastComponentProvider).getBroadcastComponent())
                .build()
                .inject(this)
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.play_broadcast_add_schedule_info))
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_broadcast_setup_schedule, null)
        setChild(view)
    }

    private fun initView(view: View) {
        with(view) {
            dateTimePicker = findViewById(R.id.date_time_picker)
            safeInitButton()
        }
    }

    private fun setupView(view: View) {
        btnSet?.text = getString(R.string.play_broadcast_set_schedule)
        btnSet?.setOnClickListener {

        }
    }

    /**
     * todo: required a safe init button before unify created an open function to set the button title
     *  and set a listener for it.
     */
    private fun safeInitButton() {
        try {
            btnSet = dateTimePicker.findViewById(com.tokopedia.datepicker.R.id.unify_datepicker_button)
        } catch (throwable: Throwable) {
            // ignore
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        const val TAG = "TagPlayBroadcastEditScheduleBottomSheet"
    }

}