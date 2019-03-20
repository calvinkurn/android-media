package com.tokopedia.videorecorder.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.tokopedia.videorecorder.R
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.videorecorder.main.adapter.ViewPagerAdapter
import com.tokopedia.videorecorder.main.recorder.VideoRecorderFragment
import com.tokopedia.videorecorder.utils.*
import kotlinx.android.synthetic.main.activity_video_picker.*
import java.io.File
import java.util.ArrayList

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 * imagePickerGallery: henry
 */
open class VideoPickerActivity: BaseSimpleActivity(),
        VideoPickerCallback,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener {

    companion object {
        //video recorder const
        const val VIDEOS_RESULT = "video_result"
        const val VIDEO_MAX_SIZE = 50000L //50 mb
        const val VIDEO_MAX_DURATION_MS = 60000 //ms = 1 minute

        //flag
        var isVideoSourcePicker = false

        //image/video picker configuration
        const val supportMultipleSelection = false
        const val minImageResolution = 0 //unnecessary but needed
    }

    override fun getNewFragment(): Fragment? = null

    //viewpager adapter
    private lateinit var adapter: ViewPagerAdapter

    //catch videoPath uri
    private var videoGalleryPaths = arrayListOf<String>()
    private lateinit var videoPath: String

    //saved state of tab layout
    private var currentSelectedTab: Int = 0

    //runtime permission handle
    private lateinit var runtimePermission: RuntimePermission

    //ffmpeg
    private lateinit var ffmpeg: FFmpeg

    private lateinit var progressDialog: ProgressDialog

    override fun getLayoutRes(): Int = R.layout.activity_video_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init runtime permission
        runtimePermission = RuntimePermission(this)
        runtimePermission.requestPermissionForRecord()

        //init progress dialog
        progressDialog = ProgressDialog(this)

        //init ffmpeg instance
        ffmpeg = FFmpeg.getInstance(this)
        ffmpeg.loadBinary(object : LoadBinaryResponseHandler() {})

        //support actionbar
        setSupportActionBar(toolbarVideoPicker)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.vidpick_title)

        //initial of adapter for viewPager and tabPicker
        setupViewPager()
        setupTabLayout()

        //remove recording result
        btnDeleteVideo.setOnClickListener { cancelVideo() }

        //video picked
        btnDone.setOnClickListener { onVideoDoneClicked() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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

    @SuppressLint("MissingPermission")
    private fun viewPagerAdapter(): ViewPagerAdapter {
        val videoPickerGallery = ImagePickerGalleryFragment.newInstance(
                GalleryType.VIDEO_ONLY,
                supportMultipleSelection,
                minImageResolution)

        adapter.addFragment(videoPickerGallery, getString(R.string.vidpick_menu_video_picker))
        adapter.addFragment(VideoRecorderFragment(), getString(R.string.vidpick_menu_recorder))
        return adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FileUtils.deleteCacheDir()
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
        if (!isVideoSourcePicker) {
            if (File(videoPath).exists()) {
                FileUtils.deleteCacheDir()
            }
        }
    }

    protected open fun onVideoDoneClicked() {
        if (isVideoSourcePicker && videoPreview.duration > VIDEO_MAX_DURATION_MS) {
            //prepared
            val resultFile = FileUtils.videoPath(FileUtils.RESULT_DIR).absolutePath
            val trimQuery = VideoUtils.ffmegCommand(videoPath, resultFile)

            exceptionHandler {
                ffmpeg.execute(trimQuery, object : ExecuteBinaryResponseHandler() {
                    override fun onSuccess(message: String?) {
                        super.onSuccess(message)
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        onFinishPicked(resultFile)
                    }

                    override fun onFailure(message: String?) {
                        super.onFailure(message)
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        showToast(applicationContext, getString(R.string.vidpick_error_message))
                    }

                    override fun onProgress(message: String?) {
                        super.onProgress(message)
                        //@TODO(showing progress dialog)
                        progressDialog.setMessage(getString(R.string.vidpick_progress_loader))
                        progressDialog.show()
                    }
                })
            }
        } else {
            onFinishPicked(videoPath)
        }
    }

    private fun onFinishPicked(file: String) {
        val videos = arrayListOf<String>()
        videos.add(file)

        val intent = Intent()
        intent.putStringArrayListExtra(VIDEOS_RESULT, videos)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    //video recorder callback

    override fun onVideoTaken(filePath: String) {
        if (filePath.isNotEmpty()) {
            val uriFile = Uri.parse(filePath)
            videoPath = filePath
            onPreviewVideoVisible()
            videoPreview.setVideoURI(uriFile)
            videoPreview.setOnPreparedListener { mp ->
                mp.isLooping = true //loop
                playVideoPreview()
            }
        }
    }

    override fun onPreviewVideoVisible() {
        layoutPreview.show()
        vpVideoPicker.hide()
        tabPicker.hide()
        btnDone.show()
        if (isVideoSourcePicker) {
            btnDeleteVideo.text = getString(R.string.vidpick_btn_back)
        } else {
            btnDeleteVideo.text = getString(R.string.vidpick_btn_delete)
        }
    }

    override fun onVideoVisible() {
        layoutPreview.hide()
        vpVideoPicker.show()
        tabPicker.show()
        btnDone.hide()
    }

    //video picker callback

    override fun onAlbumItemClicked(item: MediaItem?, isChecked: Boolean) {
        //get single image
        isVideoSourcePicker = true
        videoPath = item?.realPath.toString()
        onVideoTaken(videoPath)
    }

    override fun isMaxImageReached(): Boolean = false

    override fun getImagePath(): ArrayList<String> {
        return videoGalleryPaths
    }

    override fun getMaxFileSize(): Long = VIDEO_MAX_SIZE

}