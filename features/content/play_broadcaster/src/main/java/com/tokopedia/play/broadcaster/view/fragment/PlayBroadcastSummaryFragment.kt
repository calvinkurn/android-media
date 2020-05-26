package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlaySummaryInfosAdapter
import com.tokopedia.play.broadcaster.view.uimodel.SummaryUiModel
import kotlinx.android.synthetic.main.fragment_play_broadcaster_summary.*

/**
 * @author by jessica on 26/05/20
 */

class PlayBroadcastSummaryFragment : BaseDaggerFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcaster_summary, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView(SummaryUiModel())
    }

    private fun renderView(summaryUiModel: SummaryUiModel) {
        tv_play_summary_title.text = summaryUiModel.liveTitle
        iv_play_summary_bg.loadImage(summaryUiModel.coverImage)
        tv_play_summary_live_duration.text = summaryUiModel.liveDuration

        rv_play_summary_live_information.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_play_summary_live_information.removeItemDecorations()
        rv_play_summary_live_information.adapter = PlaySummaryInfosAdapter(summaryUiModel.liveInfos)

        btn_play_summary_finish.setOnClickListener {
            //put action here
            RouteManager.route(requireContext(), summaryUiModel.finishRedirectUrl)
        }
    }

    private fun <T : RecyclerView> T.removeItemDecorations() {
        while (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
    }
}