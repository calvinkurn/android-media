package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.alivc.live.pusher.SurfaceStatus
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.pusher.DeviceInfoUtil
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.PlayPusherImpl
import com.tokopedia.play.broadcaster.pusher.PlayPusherImplNoop


/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastFragment: BaseDaggerFragment() {

    private var playPusher: PlayPusher? = null

    private lateinit var surfaceView: SurfaceView
    private lateinit var containerPermission: View
    private lateinit var textSwitchCamera: TextView
    private lateinit var textClose: TextView

    private var surfaceStatus = SurfaceStatus.UNINITED

    override fun getScreenName(): String = "Play Broadcast"

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPusher()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        containerPermission = view.findViewById(R.id.container_permission)
        setupViewPermission()

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
                    if (isPermissionGranted(Manifest.permission.CAMERA)) {
                        startPreview()
                    }
                } else if (surfaceStatus == SurfaceStatus.DESTROYED) {
                    surfaceStatus = SurfaceStatus.RECREATED
                }
            }
        })

        textSwitchCamera = view.findViewById(R.id.tv_switch)
        textSwitchCamera.setOnClickListener {
            playPusher?.switchCamera()
        }

        textClose = view.findViewById(R.id.tv_close)
        textClose.setOnClickListener {
            // TODO("action close page")
        }

        if (!isAllPermissionGranted()) {
            requestPermission(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO))
        }
    }

    private fun setupViewPermission() {
        if (isAllPermissionGranted()) {
            containerPermission.gone()
            return
        }

        containerPermission.visible()

        val textAllowCamera = containerPermission.findViewById<TextView>(R.id.text_allow_camera)
        val textAllowMic = containerPermission.findViewById<TextView>(R.id.text_allow_mic)

        setupActionViewPermission(Manifest.permission.CAMERA, textAllowCamera)
        setupActionViewPermission(Manifest.permission.RECORD_AUDIO, textAllowMic)
    }

    private fun setupActionViewPermission(permission: String, textAction: TextView) {
        if (isPermissionGranted(permission)) {
            textAction.setTextColor(ContextCompat.getColor(requireContext(), R.color.play_granted_permission))
            textAction.isEnabled = false
        } else {
            textAction.setTextColor(ContextCompat.getColor(requireContext(), R.color.play_denied_permission))
            textAction.isEnabled = true
            textAction.setOnClickListener {
                requestPermission(arrayOf(permission))
            }
        }
    }

    private fun startPreview() {
        if (surfaceStatus != SurfaceStatus.UNINITED &&
                surfaceStatus != SurfaceStatus.DESTROYED) {
            playPusher?.startPreview(surfaceView)
        }
    }

    private fun setupPusher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            playPusher = getPlayPusher()
            playPusher?.create()
        } else {
            // TODO ("handle user with android version < 18")
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getPlayPusher(): PlayPusher = if (DeviceInfoUtil.isSupportedAbi()) {
        PlayPusherImpl.Builder(requireContext()).build()
    } else {
        PlayPusherImplNoop()
    }

    override fun onResume() {
        super.onResume()
        playPusher?.resume()
    }

    override fun onPause() {
        super.onPause()
        playPusher?.pause()
    }

    override fun onDestroy() {
        playPusher?.destroy()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissions[0] == Manifest.permission.CAMERA) {
                    startPreview()
                }
                setupViewPermission()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAllPermissionGranted(): Boolean {
        return isPermissionGranted(Manifest.permission.CAMERA) &&
                isPermissionGranted(Manifest.permission.RECORD_AUDIO)
    }

    private fun requestPermission(arrayOfPermission: Array<String>) {
        requestPermissions(arrayOfPermission,
                PERMISSION_CODE)
    }

    companion object {

        const val PERMISSION_CODE = 9276

        fun newInstance(): PlayBroadcastFragment {
            return PlayBroadcastFragment()
        }
    }
}