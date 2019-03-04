package com.tokopedia.videorecorder.main

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.videorecorder.R
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.videorecorder.main.adapter.ViewPagerAdapter
import com.tokopedia.videorecorder.main.recorder.VideoRecorderFragment
import com.tokopedia.videorecorder.utils.FileUtils
import com.tokopedia.videorecorder.utils.hide
import com.tokopedia.videorecorder.utils.show
import kotlinx.android.synthetic.main.activity_video_picker.*
import java.io.File
import java.util.ArrayList

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 * imagePickerGallery: henry
 */
class VideoPickerActivity: BaseSimpleActivity(), VideoPickerCallback,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener {

    override fun getNewFragment(): Fragment? = null

    private lateinit var videoPath: String
    private var videoGalleryPaths = arrayListOf<String>()

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

    @SuppressLint("MissingPermission")
    private fun viewPagerAdapter(): ViewPagerAdapter {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val videoPickerGallery = ImagePickerGalleryFragment.newInstance(
                GalleryType.VIDEO_ONLY,
                false,
                720)

        adapter.addFragment(videoPickerGallery, getString(R.string.menu_video_picker))
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

    override fun onAlbumItemClicked(item: MediaItem?, isChecked: Boolean) {
        //get single image
        videoPath = item?.realPath.toString()
    }

    override fun isMaxImageReached(): Boolean = false

    override fun getImagePath(): ArrayList<String> {
        return videoGalleryPaths
    }

    override fun getMaxFileSize(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}