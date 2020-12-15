package com.tokopedia.play.widget.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleActivity : BaseSimpleActivity() {

    private val rvWidgetSample by lazy { findViewById<RecyclerView>(R.id.rv_widget_sample) }

    private lateinit var adapter: PlayWidgetSampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_widget_sample)
        setupView()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun setupView() {
        val sampleData = getSampleData()
        val coordinatorMap = List(sampleData.size) {
            PlayWidgetCoordinator(this).apply {
                setAnalyticListener(PlayWidgetSampleAnalytic(this@PlayWidgetSampleActivity))
            }
        }.associateWith { null }

        adapter = PlayWidgetSampleAdapter(coordinatorMap)

        rvWidgetSample.adapter = adapter
        adapter.setItemsAndAnimateChanges(getSampleData())
    }

    private fun getSampleData(): List<PlayWidgetUiModel> {
        return listOf(
                PlayWidgetUiModel.Placeholder,
                PlayWidgetUiMock.getPlayWidgetSmall(),
                PlayWidgetUiMock.getPlayWidgetMedium()
        )
    }
}