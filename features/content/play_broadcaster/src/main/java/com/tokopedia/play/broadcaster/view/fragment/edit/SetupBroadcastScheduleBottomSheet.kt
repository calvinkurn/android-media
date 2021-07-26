package com.tokopedia.play.broadcaster.view.fragment.edit

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.datetimepicker.DateTimePicker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.extension.toCalendar
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.viewmodel.BroadcastScheduleViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by mzennis on 01/12/20.
 */
class SetupBroadcastScheduleBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    private lateinit var container: CoordinatorLayout
    private lateinit var dateTimePicker: DateTimePicker

    private var btnSet: UnifyButton? = null

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: BroadcastScheduleViewModel

    private var mListener: SetupResultListener? = null

    private val job = SupervisorJob()

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = dispatcher.main + job
    }

    private var toasterBottomMargin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this, viewModelFactory).get(DataStoreViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BroadcastScheduleViewModel::class.java)

        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet_Setup_Pinned)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: SetupResultListener) {
        mListener = listener
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
            container = findViewById(R.id.container)
            dateTimePicker = findViewById(R.id.date_time_picker)
            safeInitButton()
        }
    }

    private fun setupView(view: View) {
        setOnDismissListener { mListener?.onSetupCanceled() }
        dataStoreViewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        dateTimePicker.init(
                defaultDate = viewModel.defaultScheduleDate.toCalendar(),
                minDate = viewModel.minScheduleDate.toCalendar(),
                maxDate = viewModel.maxScheduleDate.toCalendar(),
                onDateChangedListener = null
        )

        btnSet?.text = getString(R.string.play_broadcast_set_schedule)
        btnSet?.setOnClickListener {
            viewModel.setBroadcastSchedule(dateTimePicker.selectedDate)
            analytic.clickSaveScheduleOnFinalSetupPage()
        }
    }

    private fun setupObserve() {
        observeSetBroadcastSchedule()
    }

    /**
     * todo: required a safe init button until unify created an open function to set the button title
     *  and set a listener for it.
     */
    private fun safeInitButton() {
        try {
            btnSet = dateTimePicker.findViewById(com.tokopedia.datepicker.R.id.unify_datepicker_button)
        } catch (throwable: Throwable) {
            // ignore
        }
    }

    private fun onUpdateSuccess() {
        scope.launch {
            val error = mListener?.onSetupCompletedWithData(this@SetupBroadcastScheduleBottomSheet, dataStoreViewModel.getDataStore())
            if (error != null) {
                yield()
                onUpdateFail(error)
            } else {
                dismiss()
            }
        }
    }

    private fun onUpdateFail(error: Throwable) {
        btnSet?.isLoading = false
        showToasterError(
                message = error.localizedMessage
        )
    }

    private fun showToasterError(
            message: String
    ) {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = btnSet?.height.orZero() + offset8
        }

        container.showToaster(
                message = message,
                type = Toaster.TYPE_ERROR,
                bottomMargin = toasterBottomMargin
        )
    }

    //region observe
    /**
     * Observe
     */
    private fun observeSetBroadcastSchedule() {
        viewModel.observableSetBroadcastSchedule.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> onUpdateSuccess()
                is NetworkResult.Fail -> onUpdateFail(it.error)
                NetworkResult.Loading -> {
                    btnSet?.isLoading = true
                }
            }
        })
    }
    //endregion

    companion object {
        const val TAG = "TagPlayBroadcastEditScheduleBottomSheet"
    }
}