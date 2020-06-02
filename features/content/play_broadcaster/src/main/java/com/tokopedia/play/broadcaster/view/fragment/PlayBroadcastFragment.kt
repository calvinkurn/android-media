package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import com.alivc.live.pusher.SurfaceStatus
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var surfaceView: SurfaceView

    private var surfaceStatus = SurfaceStatus.UNINITED

    override fun getScreenName(): String = "Play Broadcast"

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.builder()
                .playBroadcasterModule(PlayBroadcasterModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        surfaceView = view.findViewById(R.id.surface_view)
        surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                surfaceStatus = SurfaceStatus.CHANGED
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
                surfaceStatus = SurfaceStatus.DESTROYED
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                if (surfaceStatus == SurfaceStatus.UNINITED) {
                    surfaceStatus = SurfaceStatus.CREATED
//                    if (isPermissionGranted(Manifest.permission.CAMERA)) {
//                        startPreview()
//                    }
                } else if (surfaceStatus == SurfaceStatus.DESTROYED) {
                    surfaceStatus = SurfaceStatus.RECREATED
                }
            }
        })
    }

    private fun startPreview() {
        if (surfaceStatus != SurfaceStatus.UNINITED &&
                surfaceStatus != SurfaceStatus.DESTROYED) {
            parentViewModel.getPlayPusher().startPreview(surfaceView)
        }
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.getPlayPusher().resume()
    }

    override fun onPause() {
        super.onPause()
        parentViewModel.getPlayPusher().pause()
    }

    override fun onDestroy() {
        parentViewModel.getPlayPusher().destroy()
        super.onDestroy()
    }

    companion object {

        const val PARENT_FRAGMENT_TAG = "parent_fragment"

        fun newInstance(): PlayBroadcastFragment {
            return PlayBroadcastFragment()
        }
    }
}