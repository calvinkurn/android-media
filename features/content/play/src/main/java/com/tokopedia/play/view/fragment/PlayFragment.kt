package com.tokopedia.play.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
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

        launch {
            delay(2000)
            playViewModel.showKeyboard(true)
            flInteraction.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

            delay(5000)
            playViewModel.showKeyboard(false)
            flInteraction.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            onKeyboardHidden()
        }
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
    }

    fun onKeyboardShown(chatListYPos: Int) {
        val currentHeight = flVideo.height
        val destHeight = chatListYPos.toFloat()
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y,1.0f , destHeight / currentHeight);
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,1.0f , 0.5f);
        animatorY.duration = 300
        animatorX.duration = 300

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        flVideo.pivotY = 0f
        AnimatorSet().apply {
            playTogether(animatorX, animatorY)
        }.start()
    }

    fun onKeyboardHidden() {
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, flVideo.scaleY , 1.0f);
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,flVideo.scaleX , 1.0f);
        animatorY.duration = 300
        animatorX.duration = 300

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        flVideo.pivotY = 0f
        AnimatorSet().apply {
            playTogether(animatorX, animatorY)
        }.start()
    }
}