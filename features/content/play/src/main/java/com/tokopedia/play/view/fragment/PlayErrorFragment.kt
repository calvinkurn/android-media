package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.ERR_STATE_GLOBAL
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by mzennis on 2020-01-10.
 */
class PlayErrorFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory
): TkpdBaseV4Fragment(), PlayFragmentContract {

    private lateinit var playViewModel: PlayViewModel
    private lateinit var container: View
    private lateinit var globalError: GlobalError
    private lateinit var imgBack: ImageView

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    override fun getScreenName() = "Play Video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setupView(view)
        setupInsets(view)
        setupObserve()
    }

    override fun onStart() {
        super.onStart()
        imgBack.requestApplyInsetsWhenAttached()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        return false
    }

    private fun initView(view: View) {
        with (view) {
            container = findViewById(R.id.container_global_error)
            globalError = findViewById(R.id.global_error)
            imgBack = findViewById(R.id.img_back)
        }
    }

    private fun setupView(view: View) {
        imgBack.setOnClickListener { activity?.onBackPressed() }

        globalError.errorTitle.setTextColor(
                MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N0)
        )
        globalError.errorDescription.setTextColor(
                MethodChecker.getColor(requireContext(), R.color.play_error_text_color)
        )
    }

    private fun setupInsets(view: View) {
        imgBack.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams

            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupObserve() {
        observeErrorChannel()
    }

    /**
     * Observe
     */
    private fun observeErrorChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is Fail -> {
                    showGlobalError(it.throwable)
                }
                is Success -> {
                    container.hide()
                }
            }
        })
    }

    private fun showGlobalError(throwable: Throwable) {
        throwable.message?.let {
            when(GlobalErrorCodeWrapper.wrap(it)) {
                GlobalErrorCodeWrapper.Unknown -> {
                    return
                }
                GlobalErrorCodeWrapper.NotFound -> {
                    globalError.setType(GlobalError.PAGE_NOT_FOUND)
                    globalError.setActionClickListener {
                        activity?.let { activity ->
                            RouteManager.route(activity, ApplinkConst.HOME)
                        }
                    }
                }
                GlobalErrorCodeWrapper.PageFull -> {
                    globalError.setType(GlobalError.PAGE_FULL)
                    globalError.setActionClickListener {
                        playViewModel.getChannelInfo(channelId)
                    }
                }
                GlobalErrorCodeWrapper.ServerError -> {
                    globalError.setType(GlobalError.SERVER_ERROR)
                    globalError.setActionClickListener {
                        playViewModel.getChannelInfo(channelId)
                    }
                }
            }
            PlayAnalytics.errorState(channelId, "$ERR_STATE_GLOBAL: ${globalError.errorDescription.text}", playViewModel.channelType)
            container.show()
        }
    }
}