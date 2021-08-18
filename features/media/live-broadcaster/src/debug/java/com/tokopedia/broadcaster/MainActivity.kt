package com.tokopedia.broadcaster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.databinding.ActivityMainBinding
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState

class MainActivity : AppCompatActivity() {

    private lateinit var broadcaster: LiveBroadcaster
    private lateinit var binding: ActivityMainBinding
    private var isStreamed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtRtmpUrl.setText("rtmp://live-ingest.tokopedia.net/stream/androidtesting")

        broadcaster = LiveBroadcasterManager().apply {
            init(applicationContext, Handler(mainLooper))
        }.also {
            setupView()
        }

        broadcaster.setListener(object : BroadcasterListener {
            override fun onNewLivePusherState(state: BroadcasterState) {
                binding.debugView.updateState(state)
            }

            override fun onUpdateLivePusherStatistic(log: BroadcasterLogger) {
                binding.debugView.updateStats(log)
            }
        })

        binding.btnStream.setOnClickListener {
            if (isStreamed) {
                isStreamed = false
                binding.btnStream.text = "START"
                broadcaster.stopPreview()
                broadcaster.stop()
            } else {
                isStreamed = true
                binding.btnStream.text = "STOP"
                binding.edtRtmpUrl.text?.let {
                    broadcaster.start(it.toString())
                    binding.debugView.setLiveInfo(it.toString(), broadcaster.config)
                }
            }
        }
    }

    private fun setupView() {
        binding.surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
                broadcaster.stopPreview()
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                broadcaster.startPreview(binding.surfaceView)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcaster.stop()
    }

    companion object {
        fun route(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}