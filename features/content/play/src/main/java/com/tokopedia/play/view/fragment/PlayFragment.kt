package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(channelId: String): PlayFragment {
            return PlayFragment().apply {
                arguments?.putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    // TODO available channelId: 1543 > VOD, 1591, 1387
    private var channelId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel

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

        childFragmentManager.beginTransaction()
                .replace(R.id.fl_video, PlayVideoFragment.newInstance())
                .commit()

        childFragmentManager.beginTransaction()
                .replace(R.id.fl_interaction, PlayInteractionFragment.newInstance(channelId))
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playViewModel.getChannelInfo(channelId)
        observeSocketInfo()
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

    override fun onDestroy() {
        super.onDestroy()
        playViewModel.destroy()
    }
}