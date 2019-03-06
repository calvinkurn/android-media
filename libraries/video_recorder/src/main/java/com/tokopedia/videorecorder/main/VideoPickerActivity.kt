package com.tokopedia.videorecorder.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.Toast
import com.tokopedia.videorecorder.R
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
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
class VideoPickerActivity: BaseSimpleActivity(),
        VideoPickerCallback,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener {

    companion object {
        const val VIDEOS_RESULT = "video_result"
        const val VIDEO_MAX_SIZE = 50000L
    }

    override fun getNewFragment(): Fragment? = null

    //vpager adapter
    private lateinit var adapter: ViewPagerAdapter

    //catch videoPath uri
    private var videoGalleryPaths = arrayListOf<String>()
    private lateinit var videoPath: String

    //saved state of tab layout
    private var currentSelectedTab: Int = 0

    //runtime permission handle
    private var permissionList = arrayListOf<String>()

    override fun getLayoutRes(): Int = R.layout.activity_video_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //support actionbar
        setSupportActionBar(toolbar)

        //initial of adapter for viewPager and tabPicker
        setupViewPager()
        setupTabLayout()

        //remove recording result
        btnDeleteVideo.setOnClickListener { cancelVideo() }

        btnDone.setOnClickListener {
            //testing purpose
            val videos = arrayListOf<String>()
            videos.add(videoPath)

            val intent = Intent()
            intent.putStringArrayListExtra(VIDEOS_RESULT, videos)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter = viewPagerAdapter()
        vpVideoPicker.adapter = adapter

        vpVideoPicker.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                currentSelectedTab = position
            }
        })
    }

    private fun setupTabLayout() {
        tabPicker.setupWithViewPager(vpVideoPicker)
    }

    private fun refreshViewPager() {
        adapter.destroyAllView()
        adapter = viewPagerAdapter()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("MissingPermission")
    private fun viewPagerAdapter(): ViewPagerAdapter {
        val videoPickerGallery = ImagePickerGalleryFragment.newInstance(
                GalleryType.VIDEO_ONLY,
                false,
                0)

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
        Toast.makeText(this, videoPath, Toast.LENGTH_LONG).show()
    }

    override fun isMaxImageReached(): Boolean = false

    override fun getImagePath(): ArrayList<String> {
        return videoGalleryPaths
    }

    override fun getMaxFileSize(): Long = VIDEO_MAX_SIZE

}