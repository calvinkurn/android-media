package com.tokopedia.play.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.util.keyboard.KeyboardWatcher
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.dpToPx
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment() {

    companion object {
        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()

        private const val ANIMATION_DURATION = 300L
        private const val FULL_SCALE_FACTOR = 1.0f

        fun newInstance(channelId: String?): PlayFragment {
            return PlayFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    // TODO available channelId: 1543 > VOD, 1591, 1387
    private var channelId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel

    private lateinit var ivClose: ImageView
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
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID) ?: "2" // TODO remove default value, handle channel_id=1865, 80 staging live not found
        PlayAnalytics.sendScreen(channelId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupScreen(view)

        childFragmentManager.beginTransaction()
                .replace(flVideo.id, PlayVideoFragment.newInstance(channelId))
                .commit()

        childFragmentManager.beginTransaction()
                .replace(flInteraction.id, PlayInteractionFragment.newInstance(channelId))
                .commit()

        KeyboardWatcher().listen(view, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                playViewModel.showKeyboard(true)
                ivClose.visible()
                flInteraction.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            override fun onKeyboardHidden() {
                playViewModel.showKeyboard(false)
                ivClose.invisible()
                flInteraction.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
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

    private fun initView(view: View) {
        with (view) {
            ivClose = findViewById(R.id.iv_close)
            flVideo = findViewById(R.id.fl_video)
            flInteraction = findViewById(R.id.fl_interaction)
        }
    }

    private fun setupView(view: View) {
        ivClose.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setupScreen(view: View) {
        view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            flInteraction.setPadding(v.paddingLeft, insets.systemWindowInsetTop, v.paddingRight, insets.systemWindowInsetBottom)

            val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
            ivClose.setMargin(closeLp.leftMargin, insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)

            insets
        }
    }

    private fun observeSocketInfo() {
        playViewModel.observableSocketInfo.observe(viewLifecycleOwner, Observer {
            view?.let { view ->
                if (it == PlaySocketInfo.ERROR) {
                    PlayAnalytics.errorState(channelId, getString(R.string.play_message_socket_error), playViewModel.isLive)
                    Toaster.make(
                            view,
                            getString(R.string.play_message_socket_error),
                            type = Toaster.TYPE_ERROR,
                            duration = Snackbar.LENGTH_INDEFINITE,
                            actionText = getString(R.string.play_try_again),
                            clickListener = View.OnClickListener {
                                playViewModel.getChannelInfo(channelId)
                            }
                    )
                } else if (it == PlaySocketInfo.RECONNECT) {
                    Toaster.make(
                            view,
                            getString(R.string.play_message_socket_reconnect),
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_LONG
                    )
                }
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
            if (it.isBanned) {
                showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle)
            } else if (it.isFreeze) {
                showEventDialog(it.freezeTitle, it.freezeMessage, it.freezeButtonTitle, it.freezeButtonUrl)
            }
        })
    }

    private fun showEventDialog(title: String, message: String, buttonTitle: String, buttonUrl: String = "") {
        activity?.let {
            val dialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(title)
            dialog.setDescription(message)
            dialog.setPrimaryCTAText(buttonTitle)
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                it.finish()
                if (buttonUrl.isNotEmpty()) RouteManager.route(it, buttonUrl)
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

    fun onKeyboardShown(bottomMostBounds: Int) {
        onKeyboardShownAnimator.cancel()

        val currentHeight = flVideo.height
        val destHeight = bottomMostBounds.toFloat() - MARGIN_CHAT_VIDEO
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y,FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        val marginTopX = (ivClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        val marginTopXt = marginTopX * scaleFactor
        flVideo.pivotY = ivClose.y + (ivClose.y * scaleFactor) + marginTopXt
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

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}