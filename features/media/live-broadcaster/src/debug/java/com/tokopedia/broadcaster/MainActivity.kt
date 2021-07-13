package com.tokopedia.broadcaster

import android.os.Bundle
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var debugView: DebugView
    private lateinit var edtRtmpUrl: EditText
    private lateinit var btnStream: Button

    private var isStreamed = false

    private lateinit var broadcaster: LiveBroadcaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.surface_view)
        debugView = findViewById(R.id.debug_view)
        edtRtmpUrl = findViewById(R.id.edt_rtmp_url)
        btnStream = findViewById(R.id.btn_stream)

        broadcaster = LiveBroadcasterManager().apply {
            init(Handler(mainLooper))
        }.also {
            setupView()
        }

        broadcaster.setListener(object : BroadcasterListener {
            override fun onNewLivePusherState(state: BroadcasterState) {
                debugView.updateState(state)
            }

            override fun onUpdateLivePusherStatistic(log: BroadcasterLogger) {
                debugView.updateStats(log)
            }
        })

        btnStream.setOnClickListener {
            if (isStreamed) {
                isStreamed = false
                btnStream.text = "STOP"
                broadcaster.stopPreview()
                broadcaster.stop()
            } else {
                isStreamed = true
                btnStream.text = "START"
                edtRtmpUrl.text?.let {
                    broadcaster.start(it.toString())
                    debugView.setLiveInfo(it.toString(), broadcaster.config)
                }
            }
        }
    }

    private fun setupView() {
        surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
                broadcaster.startPreview(surfaceView)
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                broadcaster.startPreview(surfaceView)
            }
        })
    }

}