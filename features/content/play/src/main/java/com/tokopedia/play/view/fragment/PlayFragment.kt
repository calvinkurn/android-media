package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment() {

    companion object {

        // TODO retrieve channelId
        fun newInstance(): PlayFragment {
            return PlayFragment()
        }
    }

    // TODO remove this hardcoded channelId (1543, 1591, 1387)
    private var channelId = "1591"

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
                .replace(R.id.fl_video, PlayVideoFragment.newInstance())
                .commit()

        childFragmentManager.beginTransaction()
                .replace(R.id.fl_interaction, PlayInteractionFragment.newInstance())
                .commit()

        playViewModel.initVideo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playViewModel.getChannelInfo(channelId)

        playViewModel.observeChannel.observe(this, Observer {
            when(it) {
                is Success -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }
                is Fail -> {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}