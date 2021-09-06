package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.view.viewmodel.PlayViewModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class PlayUpcomingFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val analytic: PlayAnalytic
): TkpdBaseV4Fragment() {

    private lateinit var playViewModel: PlayViewModel

    override fun getScreenName(): String = "Play Upcoming"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_upcoming, container, false)
    }
}