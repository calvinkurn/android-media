package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.adapter.PlaySummaryInfosAdapter
import com.tokopedia.play.broadcaster.view.uimodel.SummaryUiModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import kotlinx.android.synthetic.main.fragment_play_broadcaster_summary.*
import javax.inject.Inject


/**
 * @author by jessica on 26/05/20
 */

class PlayBroadcastSummaryFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: PlayBroadcastSummaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcaster_summary, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() = DaggerPlayBroadcasterComponent.create().inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSummary()
    }

    private fun observeSummary() {
        viewModel.observableSummary.observe(viewLifecycleOwner, Observer {
            renderView(it)
        })
    }

    private fun renderView(summaryUiModel: SummaryUiModel) {

        tv_play_summary_live_title.text = summaryUiModel.liveTitle

        if (summaryUiModel.tickerContent.showTicker) {
            ticker_live_summary.show()
            ticker_live_summary.tickerTitle = summaryUiModel.tickerContent.tickerTitle
            ticker_live_summary.setHtmlDescription(summaryUiModel.tickerContent.tickerDescription)
        } else ticker_live_summary.hide()

        iv_play_summary_cover.loadImage(summaryUiModel.coverImage)

        tv_play_summary_live_duration.text = summaryUiModel.liveDuration

        rv_play_summary_live_information.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_play_summary_live_information.removeItemDecorations()
        rv_play_summary_live_information.adapter = PlaySummaryInfosAdapter(summaryUiModel.liveInfos)

        btn_play_summary_finish.setOnClickListener {
            //put action here
            RouteManager.route(requireContext(), summaryUiModel.finishRedirectUrl)
        }

        entranceAnimation(view)
    }

    private fun entranceAnimation(v: View?) {
        v?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    layout_live_summary_info.translationY = 100f
                    layout_live_summary_meta.translationY = -100f
                    layout_live_summary_info.animate().translationYBy(-100f).setDuration(300)
                    layout_live_summary_meta.animate().translationYBy(100f).setDuration(300)
                }
            })
        }
    }

    private fun <T : RecyclerView> T.removeItemDecorations() {
        while (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
    }

    companion object {
        fun newInstance(): PlayBroadcastSummaryFragment = PlayBroadcastSummaryFragment()
    }
}