package com.tokopedia.play.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils.getStatusBarHeight
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.util.keyboard.KeyboardWatcher
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val FULL_SCALE_FACTOR = 1.0f

        fun newInstance(channelId: String): PlayFragment {
            return PlayFragment().apply {
                arguments?.putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // TODO available channelId: 1543 > VOD, 1591, 1387
    private var channelId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel

    private lateinit var flVideo: FrameLayout
    private lateinit var flInteraction: FrameLayout

    private val onKeyboardShownAnimator = AnimatorSet()
    private val onKeyboardHiddenAnimator = AnimatorSet()

    override fun getScreenName(): String = "Play"

    override fun initInjector() {
        DaggerPlayComponent
                .builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID)?:"1865" // TODO remove default value, handle channel_id not found
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreen(view)

        flVideo = view.findViewById(R.id.fl_video)

        childFragmentManager.beginTransaction()
                .replace(flVideo.id, PlayVideoFragment.newInstance())
                .commit()

        flInteraction = view.findViewById(R.id.fl_interaction)

        childFragmentManager.beginTransaction()
                .replace(flInteraction.id, PlayInteractionFragment.newInstance(channelId))
                .commit()

        KeyboardWatcher().listen(view, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                playViewModel.showKeyboard(true)
            }

            override fun onKeyboardHidden() {
                playViewModel.showKeyboard(false)
                this@PlayFragment.onKeyboardHidden()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playViewModel.getChannelInfo(channelId)
        observeSocketInfo()
        observeEventUserInfo()
    }

    private fun setupScreen(view: View) {
        view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val interactionView = v.findViewById<View>(R.id.fl_interaction)
            interactionView.setPadding(v.paddingLeft, insets.systemWindowInsetTop, v.paddingRight, insets.systemWindowInsetBottom)

            insets
        }
    }

    private fun observeSocketInfo() {
        playViewModel.observableSocketInfo.observe(viewLifecycleOwner, Observer {
            view?.let { view ->
                if (it == PlaySocketInfo.ERROR) {
                    Toaster.showErrorWithAction(view, getString(R.string.play_message_socket_error), Snackbar.LENGTH_INDEFINITE, getString(R.string.play_try_again), View.OnClickListener {
                        playViewModel.getChannelInfo(channelId)
                    })
                } else if (it == PlaySocketInfo.RECONNECT) {
                    Toaster.showError(view, getString(R.string.play_message_socket_reconnect), Snackbar.LENGTH_LONG)
                }
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
            if (it.isBanned) {
                showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle, it.bannedButtonUrl)
            } else if (it.isFreeze) {
                showEventDialog(it.freezeTitle, it.freezeMessage, it.freezeButtonTitle, it.freezeButtonUrl)
            }
        })
    }

    private fun showEventDialog(title: String, message: String, buttonTitle: String, buttonUrl: String) {
        activity?.let {
            val dialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(title)
            dialog.setDescription(message)
            dialog.setPrimaryCTAText(buttonTitle)
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                it.finish()
                RouteManager.route(it, buttonUrl)
            }
            dialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playViewModel.destroy()

        onKeyboardShownAnimator.cancel()
        onKeyboardHiddenAnimator.cancel()
    }

    fun onKeyboardShown(chatListYPos: Int) {
        onKeyboardShownAnimator.cancel()

        val statusBarHeight = getStatusBarHeight(requireContext()).toFloat()
        val theTopSpace = 1.5f * statusBarHeight
        val currentHeight = flVideo.height
        val destHeight = chatListYPos.toFloat() - statusBarHeight
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y,FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        flVideo.pivotY = theTopSpace
        onKeyboardShownAnimator.apply {
            playTogether(animatorX, animatorY)
        }.start()
    }

    fun onKeyboardHidden() {
        onKeyboardHiddenAnimator.cancel()

        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, flVideo.scaleY, FULL_SCALE_FACTOR)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, flVideo.scaleX, FULL_SCALE_FACTOR)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        onKeyboardHiddenAnimator.apply {
            playTogether(animatorX, animatorY)
        }.start()
    }
}