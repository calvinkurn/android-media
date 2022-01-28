package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedFragment : TkpdBaseV4Fragment() {

    private val rvWidget by lazy { view?.findViewById<RecyclerView>(R.id.rv_widget_sample_feed) }

    override fun getScreenName(): String {
        return "PlayWidgetSampleFeedFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_widget_sample_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {

    }
}