package com.tokopedia.imagepicker.videorecorder.recorder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.common.listener.VideoPickerCallback
import com.tokopedia.imagepicker.common.state.StateRecorder
import com.tokopedia.imagepicker.videorecorder.DURATION_MAX
import com.tokopedia.imagepicker.videorecorder.utils.*
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner.cleanUpInternalStorageIfNeeded
import kotlinx.android.synthetic.main.fragment_recorder.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */
class VideoRecorderFragment : TkpdBaseV4Fragment() {

    companion object {
        const val SAVED_FLASH_INDEX = "saved_flash_index"
        const val VIDEO_DIR = "video/"
        const val VIDEO_EXT = ".mp4"
    }

    //flash collection
    private var flashList = arrayListOf<Flash>()

    //cameraView lazy configuration
    private var flashIndex = 0

    //callback handler
    private lateinit var videoCallback: VideoPickerCallback

    //for progress loader
    private lateinit var timer: Timer

    fun getTokopediaVideoPath(relativePathDirectory: String? = VIDEO_DIR): File {
        return File(
            FileUtil.getTokopediaInternalDirectory(relativePathDirectory).absolutePath,
            FileUtil.generateUniqueFileName() + VIDEO_EXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            flashIndex = savedInstanceState.getInt(SAVED_FLASH_INDEX, 0)
        }
        cleanUpInternalStorageIfNeeded(requireContext(), VIDEO_DIR)
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

        cameraPrepared()
        //set max progress value
        progress.max = DURATION_MAX

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

    private fun cameraPrepared() {
        cameraView.mode = Mode.VIDEO
        cameraView.audio = Audio.ON
        cameraView.clearCameraListeners()
        cameraView.addCameraListener(cameraListener())
        cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
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
            timer.cancel()
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
        //init state
        videoCallback.onVideoRecorder(StateRecorder.Start)

        //set default value
        progress.progress = 0
        var countDownMills = DURATION_MAX.toLong()
        txtDuration.text = getString(R.string.vidpick_duration_default)

        if (cameraView.isTakingVideo) {
            vwRecord.hide()
            progress.hide()
            btnFlash.show()
            btnFlip.show()
            cameraView.stopVideo()
            timer.cancel()
        } else {
            vwRecord.show()
            progress.show()
            btnFlip.hide()
            btnFlash.hide()
            val file = getTokopediaVideoPath()
            cameraView.takeVideo(file, DURATION_MAX)
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
                                progress.progress += 1000
                                countDownMills -= 1000
                            }
                        }
                    }
                }
            }, 1, 1000)
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
        val colorWhite = context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Static_White) }
        when (flashEnum) {
            Flash.AUTO.ordinal -> btnFlash.setImageDrawable(com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable(activity, com.tokopedia.imagepicker.common.R.drawable.ic_auto_flash))
            Flash.ON.ordinal -> btnFlash.setImage(IconUnify.FLASH_ON, colorWhite, colorWhite, colorWhite, colorWhite)
            Flash.OFF.ordinal -> btnFlash.setImage(IconUnify.FLASH_OFF, colorWhite, colorWhite, colorWhite, colorWhite)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_FLASH_INDEX, flashIndex)
    }

    override fun getScreenName(): String = getString(R.string.app_name)

}
