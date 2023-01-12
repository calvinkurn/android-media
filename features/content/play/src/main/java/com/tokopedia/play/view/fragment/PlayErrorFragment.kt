package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.play_common.model.ui.ArchivedUiModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by mzennis on 2020-01-10.
 */
@Suppress("LateinitUsage")
class PlayErrorFragment @Inject constructor(
    private val analytic: PlayAnalytic,
    private val router: Router,
): TkpdBaseV4Fragment(), PlayFragmentContract {

    private lateinit var parentViewModel: PlayParentViewModel
    private lateinit var container: View
    private lateinit var globalError: GlobalError
    private lateinit var imgBack: IconUnify
    private lateinit var tvTitle: TextView

    private var mChannelId: String = ""
    private var mState: PageResultState = PageResultState.Loading

    override fun getScreenName() = "Play Video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val theActivity = requireActivity()
        if (theActivity is PlayActivity) {
            parentViewModel = ViewModelProvider(theActivity, theActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
        }
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

    private fun initView(view: View) {
        with (view) {
            container = findViewById(R.id.container_global_error)
            globalError = findViewById(R.id.global_error)
            imgBack = findViewById(R.id.img_back)
            tvTitle = findViewById(R.id.text_play_title)
        }
    }

    private fun setupView(view: View) {
        imgBack.setOnClickListener { activity?.onBackPressed() }

        globalError.errorTitle.setTextColor(
                MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        )
        globalError.errorDescription.setTextColor(
                MethodChecker.getColor(requireContext(), R.color.play_dms_error_text_color)
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
        parentViewModel.observableChannelIdsResult.observe(viewLifecycleOwner, DistinctObserver {
            mState = it.state

            when (val state = it.state) {
                is PageResultState.Fail -> showGlobalError(state.error)
                is PageResultState.Success -> container.hide()
                is PageResultState.Archived -> showArchived(state.config, it.currentValue.firstOrNull() ?: "")
            }
        })
    }

    private fun showGlobalError(throwable: Throwable) {
        if (throwable is MessageErrorException) handleKnownServerError(throwable)
        else handleUnknownError(throwable)

        analytic.trackGlobalError(globalError.errorDescription.text.toString())
        container.show()
    }

    private fun handleKnownServerError(exception: MessageErrorException) {
        when(GlobalErrorCodeWrapper.wrap(exception.message.orEmpty())) {
            GlobalErrorCodeWrapper.NotFound -> {
                globalError.setType(GlobalError.PAGE_NOT_FOUND)
                globalError.setActionClickListener {
                    activity?.let { activity ->
                        router.route(activity, ApplinkConst.HOME)
                    }
                }
            }
            GlobalErrorCodeWrapper.PageFull,
            GlobalErrorCodeWrapper.ServerError,
            GlobalErrorCodeWrapper.Unknown -> {
                globalError.setType(GlobalError.PAGE_FULL)
                globalError.setActionClickListener {
                    parentViewModel.loadNextPage()
                }
            }
        }
    }

    private fun handleUnknownError(error: Throwable) {
        when (error) {
            is ConnectException, is UnknownHostException -> {
                globalError.setType(GlobalError.NO_CONNECTION)
                globalError.setActionClickListener {
                    parentViewModel.loadNextPage()
                }
            }
            else -> {
                globalError.setType(GlobalError.PAGE_FULL)
                globalError.setActionClickListener {
                    parentViewModel.loadNextPage()
                }
            }
        }
    }

    private fun showArchived(config: ArchivedUiModel, channelId: String) {
        mChannelId = channelId

        analytic.sendScreenArchived(channelId)

        imgBack.setImage(newIconId = IconUnify.ARROW_BACK)
        tvTitle.text = ""

        globalError.apply {
            setType(GlobalError.PAGE_NOT_FOUND)
            errorTitle.text = config.title
            errorDescription.text = config.description
            errorAction.text = config.btnTitle
            setActionClickListener {
                activity?.let { activity ->
                    analytic.clickCtaArchived(channelId)
                    router.route(activity, config.appLink)
                }
            }
        }
        container.show()
    }

    fun onBackPressed() {
        if(mState is PageResultState.Archived) {
            analytic.clickExitArchived(mChannelId)
        }
    }
}
