package com.tokopedia.videorecorder.main.recorder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.cameraview.*
import com.tokopedia.videorecorder.R
import com.tokopedia.videorecorder.main.VideoPickerCallback
import com.tokopedia.videorecorder.utils.*
import kotlinx.android.synthetic.main.fragment_recorder.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */
class VideoRecorderFragment: TkpdBaseV4Fragment() {

    companion object {
        const val SAVED_FLASH_INDEX = "saved_flash_index"
        const val DURATION_MAX = 60000 //1 minute
    }

    //flash collection
    private var flashList = arrayListOf<Flash>()

    //flash index
    private var flashIndex = 0

    //callback handler
    private lateinit var videoCallback: VideoPickerCallback

    //for progress loader
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            flashIndex = savedInstanceState.getInt(SAVED_FLASH_INDEX, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recorder, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        videoCallback = context as VideoPickerCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraView.mode = Mode.VIDEO
        cameraView.addCameraListener(cameraListener())
        cameraView.open()

        //set max progress value
        progressBar.max = DURATION_MAX

        //flip button
        btnFlip.setOnClickListener { cameraSwitchFacing() }
        //video recording handler
        btnRecord.setOnClickListener { recording() }

        //flash button
        btnFlash.setOnClickListener {
            if (flashList.size > 0) {
                flashIndex = (flashIndex + 1) % flashList.size
                setCameraFlash()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        exceptionHandler {
            cameraView.open()
        }
    }

    override fun onPause() {
        super.onPause()
        exceptionHandler {
            cameraView.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exceptionHandler {
            cameraView.destroy()
        }
    }

    private fun cameraListener(): CameraListener {
        return object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
                initCameraFlash()
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                videoCallback.onVideoTaken(result.file.absolutePath)
            }
        }
    }

    private fun recording() {
        //set default value
        progressBar.progress = 0
        var countDownMills = DURATION_MAX.toLong()
        txtDuration.text = getString(R.string.vidpick_duration_default)

        if (cameraView.isTakingVideo) {
            vwRecord.hide()
            btnFlip.show()
            cameraView.stopVideo()
            timer.cancel()
        } else {
            btnFlip.hide()
            vwRecord.show()
            val file = FileUtils.videoPath(FileUtils.RESULT_DIR)
            cameraView.takeVideoSnapshot(file, DURATION_MAX)
            //progress and duration countdown
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (cameraView != null) {
                        if (cameraView.isTakingVideo) {
                            activity?.runOnUiThread {
                                val minutes = TimeUnit.MILLISECONDS.toMinutes(countDownMills)
                                val seconds = TimeUnit.MILLISECONDS.toSeconds(countDownMills) - TimeUnit.MINUTES.toSeconds(minutes)
                                txtDuration.text = getString(R.string.vidpick_duration_format, formatter(minutes), formatter(seconds))
                                progressBar.progress += 1000
                                countDownMills -= 1000
                            }
                        }
                    }
                }
            },1, 1000)
        }
    }

    private fun initCameraFlash() {
        if (cameraView == null || cameraView.cameraOptions == null) return

        val supportedFlashes = cameraView.cameraOptions!!.supportedFlash
        for (flash: Flash in supportedFlashes) {
            if (flash != Flash.TORCH) {
                flashList.add(flash)
            }
        }

        btnFlash.visible(flashList.size > 0) {
            setCameraFlash()
        }
    }

    private fun setCameraFlash() {
        if (flashIndex < 0 || flashList.size <= flashIndex) return

        var flash = flashList[flashIndex]
        if (flash.ordinal == Flash.TORCH.ordinal) {
            flashIndex = (flashIndex + 1) % flashList.size
            flash = flashList[flashIndex]
        }

        cameraView.set(flash)
        setUIFlashCamera(flash.ordinal)
    }

    private fun cameraSwitchFacing() {
        if (cameraView.isTakingVideo) return
        cameraView.toggleFacing()
    }

    private fun setUIFlashCamera(flashEnum: Int) {
        when (flashEnum) {
            Flash.AUTO.ordinal -> btnFlash.setImageResource(R.drawable.ic_auto_flash)
            Flash.ON.ordinal -> btnFlash.setImageResource(R.drawable.ic_on_flash)
            Flash.OFF.ordinal -> btnFlash.setImageResource(R.drawable.ic_off_flash)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_FLASH_INDEX, flashIndex)
    }

    override fun getScreenName(): String = getString(R.string.app_name)

}