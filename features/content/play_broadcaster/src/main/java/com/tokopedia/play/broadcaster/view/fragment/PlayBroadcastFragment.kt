package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alivc.live.pusher.SurfaceStatus
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
): TkpdBaseV4Fragment() {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var surfaceView: SurfaceView

    private var surfaceStatus = SurfaceStatus.UNINITED

    override fun getScreenName(): String = "Play Broadcast"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePermissionStateEvent()
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
                } else if (surfaceStatus == SurfaceStatus.DESTROYED) {
                    surfaceStatus = SurfaceStatus.RECREATED
                }
                startPreview()
            }
        })
    }

    private fun startPreview() {
        if (surfaceStatus != SurfaceStatus.UNINITED &&
                surfaceStatus != SurfaceStatus.DESTROYED) {
            parentViewModel.startPreview(surfaceView)
        }
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.resumePushStream()
    }

    override fun onPause() {
        super.onPause()
        parentViewModel.pausePushStream()
    }

    override fun onStop() {
        super.onStop()
        parentViewModel.destroyPushStream()
    }

    //region observe
    /**
     * Observe
     */
    private fun observePermissionStateEvent() {
        parentViewModel.observablePermissionState.observe(this, Observer {
            when(it) {
                is PlayPermissionState.Granted -> startPreview()
                is PlayPermissionState.Denied -> checkPermissionBeforeStartPreview(it.permissions)
            }
        })
    }
    //endregion

    private fun checkPermissionBeforeStartPreview(permissions: List<String>) {
        if (permissions.contains(Manifest.permission.CAMERA)) {
            startPreview()
        }
    }

    companion object {

        const val PARENT_FRAGMENT_TAG = "parent_fragment"
    }
}