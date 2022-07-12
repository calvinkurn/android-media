package com.tokopedia.play.broadcaster.view.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSetupUiModel
import com.tokopedia.play.broadcaster.view.custom.interactive.giveaway.GiveawayFormView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 20/04/22
 */
class InteractiveSetupDialogFragment @Inject constructor(
    val analytic: PlayBroadcastInteractiveAnalytic,
) : DialogFragment() {

    private var mDataSource: DataSource? = null

    private lateinit var viewModel: PlayBroadcastViewModel

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(it.requireView(), it.viewLifecycleOwner) }
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), R.style.Dialog_Setup_Interactive) {
            override fun onBackPressed() {
                when (val child = getChildView()) {
                    is GiveawayFormView -> child.back()
                    else -> dismiss()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vmProvider = mDataSource?.getViewModelProvider() ?: ViewModelProvider(this)
        viewModel = vmProvider.get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(inflater.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserve()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.submitAction(PlayBroadcastAction.ClickGameOption(GameType.Unknown))
    }

    fun showNow(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    private fun setupObserve() {
        observeUiState()
        observeUiEvent()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                when (state.interactiveSetup.type) {
                    is GameType.Giveaway -> renderGiveawaySetup(
                        state.interactiveSetup, state.interactiveConfig
                    )
                    else -> {}
                }
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayBroadcastEvent.CreateInteractive.Error -> {
                        toaster.showError(
                            err = event.error,
                            customErrMessage = getString(
                                R.string.play_interactive_broadcast_create_fail
                            ),
                            duration = Toaster.LENGTH_SHORT,
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderGiveawaySetup(
        setup: InteractiveSetupUiModel,
        config: InteractiveConfigUiModel
    ) {
        setChildView {
            val formView = GiveawayFormView(it)
            formView.setListener(object : GiveawayFormView.Listener {
                override fun onExit(view: GiveawayFormView) {
                    analytic.onClickBackGiveaway(viewModel.channelId, viewModel.channelTitle)
                    dismiss()
                }

                override fun onDone(view: GiveawayFormView, data: GiveawayFormView.Data) {
                    viewModel.submitAction(
                        PlayBroadcastAction.CreateGiveaway(
                            title = data.title,
                            durationInMs = data.durationInMs,
                        )
                    )
                }

                override fun onClickBackSetTimer() {
                    analytic.onclickBackSetTimerGiveAway(viewModel.channelId, viewModel.channelTitle)
                }

                override fun onClickContinue() {
                    analytic.onClickContinueGiveaway(viewModel.channelId, viewModel.channelTitle)
                }

                override fun getRemainingTimeInMillis(): Long {
                    return viewModel.remainingDurationInMillis
                }
            })
            formView
        }.apply {
            setEligibleDurations(config.giveawayConfig.availableStartTimeInMs)
            setLoading(setup.isSubmitting)
        }
    }

    private inline fun <reified V: View> setChildView(
        viewCreator: (Context) -> V
    ): V {
        val parent = view as ViewGroup
        val firstChild = getChildView()
        return if (firstChild !is V) {
            parent.removeAllViews()
            val view = viewCreator(parent.context)
            parent.addView(view)
            view
        } else firstChild
    }

    private fun getChildView(): View? {
        return (view as ViewGroup).getChildAt(0)
    }

    companion object {
        private const val TAG = "InteractiveSetupDialogFragment"

        fun get(fragmentManager: FragmentManager): InteractiveSetupDialogFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? InteractiveSetupDialogFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): InteractiveSetupDialogFragment {
            val oldInstance = get(fragmentManager)
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    InteractiveSetupDialogFragment::class.java.name
                ) as InteractiveSetupDialogFragment
            }
        }
    }

    interface DataSource {

        fun getViewModelProvider(): ViewModelProvider
    }
}