package com.tokopedia.play.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
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
import com.tokopedia.play.util.PlayFullScreenHelper
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
        private const val MOCK_CHANNEL_ID = "1953"

        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()

        private const val VIDEO_FRAGMENT_TAG = "FRAGMENT_VIDEO"
        private const val INTERACTION_FRAGMENT_TAG = "FRAGMENT_INTERACTION"
        private const val ERROR_FRAGMENT_TAG = "FRAGMENT_ERROR"

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

    private val offset12 by lazy { resources.getDimensionPixelOffset(R.dimen.dp_12) }

    private val onKeyboardShownAnimator = AnimatorSet()
    private val onKeyboardHiddenAnimator = AnimatorSet()

    private lateinit var playViewModel: PlayViewModel

    private lateinit var ivClose: ImageView
    private lateinit var flVideo: FrameLayout
    private lateinit var flInteraction: FrameLayout
    private lateinit var flGlobalError: FrameLayout

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
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID) ?: MOCK_CHANNEL_ID
        PlayAnalytics.sendScreen(channelId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupScreen(view)

        if (childFragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flVideo.id, PlayVideoFragment.newInstance(channelId), VIDEO_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(INTERACTION_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flInteraction.id, PlayInteractionFragment.newInstance(channelId), INTERACTION_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flGlobalError.id, PlayErrorFragment.newInstance(channelId), ERROR_FRAGMENT_TAG)
                    .commit()
        }

        KeyboardWatcher().listen(view, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                playViewModel.showKeyboard(estimatedKeyboardHeight)
                ivClose.visible()
                flInteraction.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            override fun onKeyboardHidden() {
                playViewModel.hideKeyboard()
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

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    private fun initView(view: View) {
        with (view) {
            ivClose = findViewById(R.id.iv_close)
            flVideo = findViewById(R.id.fl_video)
            flInteraction = findViewById(R.id.fl_interaction)
            flGlobalError = findViewById(R.id.fl_global_error)
        }
    }

    private fun setupView(view: View) {
        ivClose.setOnClickListener {
            hideKeyboard()
        }
        flVideo.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setupScreen(view: View) {
        setFullScreen()
        setInsets(view)
    }

    private fun setFullScreen() {
        PlayFullScreenHelper.showSystemUi(requireActivity())
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
            ivClose.setMargin(closeLp.leftMargin, offset12 + insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)

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
            if (it.isFreeze)
                try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
            else if (it.isBanned)
                showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle)
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
            dialog.setOverlayClose(false)
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        val destHeight = bottomMostBounds.toFloat() - (MARGIN_CHAT_VIDEO + offset12) //offset12 for the range between video and status bar
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y,FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        val marginTop = (ivClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        val marginTopXt = marginTop * scaleFactor
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