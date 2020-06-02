package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    override fun getTitle(): String = getString(R.string.play_prepare_cover_fragment_title)

    override fun isRootFragment(): Boolean = false

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveData()
    }

    private fun initView(view: View) {
        with(view) {
        }
    }

    private fun setupView(view: View) {
    }

    private fun observeLiveData() {
    }
}