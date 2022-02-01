package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.sample.adapter.common.PlayWidgetSampleCommonAdapter
import com.tokopedia.play.widget.sample.analytic.PlayWidgetSampleAnalytic
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PlayWidgetType

/**
 * Created by meyta.taliti on 27/01/22.
 */
class PlayWidgetSampleCommonFragment : TkpdBaseV4Fragment() {

    private val rvWidgetSample by lazy { view?.findViewById<RecyclerView>(R.id.rv_widget_sample) }

    private lateinit var adapter: PlayWidgetSampleCommonAdapter

    override fun getScreenName(): String {
        return "PlayWidgetSampleCommonFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_widget_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {
        val sampleData = getSampleData()
        val coordinatorMap = List(sampleData.size) {
            PlayWidgetCoordinator(this).apply {
                setAnalyticListener(PlayWidgetSampleAnalytic(requireContext()))
            }
        }.associateWith { null }

        adapter = PlayWidgetSampleCommonAdapter(coordinatorMap)

        rvWidgetSample?.adapter = adapter
        adapter.setItemsAndAnimateChanges(getSampleData())
    }

    private fun getSampleData(): List<PlayWidgetState> {
        return listOf(
            PlayWidgetState(
                model = PlayWidgetUiMock.getSamplePlayWidget(),
                widgetType = PlayWidgetType.Small,
                isLoading = false,
            ),
            PlayWidgetState(
                model = PlayWidgetUiMock.getSamplePlayWidget(),
                widgetType = PlayWidgetType.Medium,
                isLoading = false
            )
        )
    }
}