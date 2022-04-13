package com.tokopedia.play.view.dialog.interactive.giveaway

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
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.custom.interactive.follow.InteractiveFollowView
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.view.game.giveaway.GiveawayWidgetView
import com.tokopedia.play_common.view.game.setupGiveaway
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class InteractiveDialogFragment @Inject constructor() : DialogFragment() {

    private var mDataSource: DataSource? = null

    private lateinit var viewModel: PlayViewModel

    private val followViewListener = object : InteractiveFollowView.Listener {
        override fun onFollowClicked(view: InteractiveFollowView) {
            viewModel.submitAction(PlayViewerNewAction.FollowPartner)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vmProvider = mDataSource?.getViewModelProvider() ?: ViewModelProvider(this)
        viewModel = vmProvider.get(PlayViewModel::class.java)
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
            (WIDTH_PERCENTAGE * getScreenWidth()).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.submitAction(PlayViewerNewAction.StopPlayingInteractive)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    private fun setupObserve() {
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                if (!state.interactive.isPlaying) {
                    dismiss()
                    return@collectLatest
                }

                when (state.interactive.interactive) {
                    is InteractiveUiModel.Giveaway -> renderGiveawayDialog(
                        state.interactive.interactive,
                        state.partner
                    )
                }

            }
        }
    }

    private fun renderGiveawayDialog(
        giveaway: InteractiveUiModel.Giveaway,
        partner: PlayPartnerInfo,
    ) {
        val giveawayStatus = giveaway.status
        if (partner.status == PlayPartnerFollowStatus.Followable(false)) {
            setChildView { ctx ->
                val view = InteractiveFollowView(ctx)
                view.setListener(followViewListener)
                view
            }.apply {
                setBadgeUrl(partner.badgeUrl)
                setAvatarUrl(partner.iconUrl)
                setPartnerName(partner.name)
                setLoading(partner.isLoadingFollow)
                getHeader().setupGiveaway(giveaway.title)
            }
        } else if (giveawayStatus is InteractiveUiModel.Giveaway.Status.Ongoing) {
            setChildView { ctx ->
                GiveawayWidgetView(ctx)
            }.apply {
                setTitle(giveaway.title)
                setTargetTime(giveawayStatus.endTime) {
                    viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                }
            }
        }
    }

    private inline fun <reified V: View> setChildView(
        viewCreator: (Context) -> V
    ): V {
        val parent = view as ViewGroup
        val firstChild = parent.getChildAt(0)
        return if (firstChild !is V) {
            parent.removeAllViews()
            val view = viewCreator(parent.context)
            parent.addView(view)
            view
        } else firstChild
    }

    companion object {
        private const val TAG = "InteractiveDialogFragment"

        private const val WIDTH_PERCENTAGE = 0.6

        fun get(fragmentManager: FragmentManager): InteractiveDialogFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? InteractiveDialogFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): InteractiveDialogFragment {
            val oldInstance = get(fragmentManager)
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    InteractiveDialogFragment::class.java.name
                ) as InteractiveDialogFragment
            }
        }
    }

    interface DataSource {

        fun getViewModelProvider(): ViewModelProvider
    }
}