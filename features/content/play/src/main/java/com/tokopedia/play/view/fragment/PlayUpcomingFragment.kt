package com.tokopedia.play.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewcomponent.ToolbarViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class PlayUpcomingFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val analytic: PlayAnalytic
): TkpdBaseV4Fragment(),
    ToolbarViewComponent.Listener
{

    private val toolbarView by viewComponent { ToolbarViewComponent(it, R.id.view_toolbar, this) }
    private lateinit var ivUpcomingCover: AppCompatImageView
    private lateinit var btnAction: UnifyButton

    private lateinit var playViewModel: PlayViewModel

    override fun getScreenName(): String = "Play Upcoming"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupObserver()
    }

    private fun initView(view: View) {
        ivUpcomingCover = view.findViewById(R.id.iv_upcoming_cover)
        btnAction = view.findViewById(R.id.btn_action)
    }

    private fun setupView(view: View) {
        setCover(view)
        setButtonAction()
        setToolbar()
    }

    private fun setCover(view: View) {
        val coverUrl = playViewModel.upcomingInfo?.coverUrl ?: ""
        if(coverUrl.isNotEmpty()) {
            Glide.with(view)
                .load(coverUrl)
                .into(ivUpcomingCover)
        }
    }

    private fun setButtonAction() {
        val isReminderSet = playViewModel.upcomingInfo?.isReminderSet ?: false
        if(!isReminderSet) {
            btnAction.text = getString(R.string.play_remind_me)
            btnAction.show()
        }
        else {
            btnAction.hide()
        }

        btnAction.setOnClickListener {
            btnAction.isLoading = true
        }
    }

    private fun setToolbar() {
        toolbarView.setIsShareable(true)
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                renderToolbarView(state.followStatus, state.partnerName)
            }
        }
    }

    private fun renderToolbarView(
        followStatus: PlayPartnerFollowStatus,
        partnerName: String
    ) {
        toolbarView.setFollowStatus(followStatus)
        toolbarView.setPartnerName(partnerName)
    }

    override fun onBackButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onMoreButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onFollowButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onPartnerNameClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onCartButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onCopyButtonClicked(view: ToolbarViewComponent, content: String) {
        copyToClipboard(content)
        showLinkCopiedToaster()

        analytic.clickCopyLink()
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-room", content))
    }

    private fun showLinkCopiedToaster() {
        doShowToaster(message = getString(R.string.play_link_copied))
    }

    private fun doShowToaster(
        toasterType: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        message: String,
    ) {
        Toaster.build(
            requireView(),
            message,
            type = toasterType,
            actionText = actionText
        ).show()
    }
}