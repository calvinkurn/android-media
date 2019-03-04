package com.tokopedia.videorecorder.main

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.videorecorder.R
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.videorecorder.main.adapter.ViewPagerAdapter
import com.tokopedia.videorecorder.main.recorder.VideoRecorderFragment
import com.tokopedia.videorecorder.utils.FileUtils
import com.tokopedia.videorecorder.utils.hide
import com.tokopedia.videorecorder.utils.show
import kotlinx.android.synthetic.main.activity_video_picker.*
import java.io.File

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */
class VideoPickerActivity: BaseSimpleActivity(), VideoPickerCallback {

    override fun getNewFragment(): Fragment? = null

    private lateinit var videoPath: String

    override fun getLayoutRes(): Int {
        return R.layout.activity_video_picker
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initial of adapter for viewPager and tabPicker
        val adapter = viewPagerAdapter()
        vpVideoPicker.adapter = adapter
        tabPicker.setupWithViewPager(vpVideoPicker)

        //remove recording result
        btnDeleteVideo.setOnClickListener { cancelVideo() }
    }

    private fun viewPagerAdapter(): ViewPagerAdapter {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VideoRecorderFragment(), getString(R.string.menu_video_picker))
        adapter.addFragment(VideoRecorderFragment(), getString(R.string.menu_recorder))
        return adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FileUtils.deleteCacheDir()
    }


    override fun onVideoTaken(filePath: String) {
        val uriFile = Uri.parse(filePath)
        videoPath = filePath
        onPreviewVideoVisible()
        videoPreview.setVideoURI(uriFile)
        videoPreview.setOnPreparedListener { mp ->
            mp.isLooping = true //loop
            playVideoPreview()
        }
    }

    private fun playVideoPreview() {
        if (videoPreview.isPlaying) return
        if (File(videoPath).exists()) {
            videoPreview.start()
        }
    }

    private fun cancelVideo() {
        onVideoVisible()
        videoPreview.stopPlayback()
        if (File(videoPath).exists()) {
            FileUtils.deleteCacheDir()
        }
    }

    override fun onPreviewVideoVisible() {
        layoutPreview.show()
        vpVideoPicker.hide()
        tabPicker.hide()
    }

    override fun onVideoVisible() {
        layoutPreview.hide()
        vpVideoPicker.show()
        tabPicker.show()
    }

}