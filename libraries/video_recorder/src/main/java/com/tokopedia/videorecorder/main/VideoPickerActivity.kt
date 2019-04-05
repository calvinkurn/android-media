package com.tokopedia.videorecorder.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.permissionchecker.request
import com.tokopedia.videorecorder.R
import com.tokopedia.videorecorder.main.adapter.ViewPagerAdapter
import com.tokopedia.videorecorder.main.recorder.VideoRecorderFragment
import com.tokopedia.videorecorder.utils.*
import kotlinx.android.synthetic.main.activity_video_picker.*
import java.io.File
import java.util.*

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 * imagePickerGallery: henry
 */
open class VideoPickerActivity : BaseSimpleActivity(),
        VideoPickerCallback,
        ImagePickerGalleryFragment.OnImagePickerGalleryFragmentListener {

    companion object {
        //video recorder const
        const val VIDEOS_RESULT = "video_result"
        const val VIDEO_MAX_SIZE = 100000L //100 mb

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
    private var videoPath: String = ""

    //saved state of tab layout
    private var currentSelectedTab: Int = 0

    //runtime permission handle
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper

    override fun getLayoutRes(): Int = R.layout.activity_video_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init runtime permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper = PermissionCheckerHelper()
            permissionCheckerHelper.request(this, getPermissions()) {
                initView()
            }
        } else {
            initView()
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                PermissionCheckerHelper.Companion.PERMISSION_RECORD_AUDIO)
    }

    private fun initView() {
        //support actionbar
        setSupportActionBar(toolbarVideoPicker)
        showBackButton(true)
        supportActionBar?.title = getString(R.string.vidpick_title)

        //initial of adapter for viewPager and tabPicker
        initViewPager()

        //remove recording result
        btnDeleteVideo.setOnClickListener { cancelVideo() }

        //video picked
        btnDone.setOnClickListener { onVideoDoneClicked() }
    }

    private fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
        when (show) {
            true -> supportActionBar?.title = getString(R.string.vidpick_title)
            false -> supportActionBar?.title = " ${getString(R.string.vidpick_title)}"
        }
    }

    private fun initViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        setupViewPager()
    }

    private fun setupViewPager() {
        adapter.destroyAllView()
        adapter.notifyDataSetChanged()
        adapter = viewPagerAdapter()
        vpVideoPicker.adapter = adapter
        vpVideoPicker.addOnPageChangeListener(PageChangeCallback { position ->
            currentSelectedTab = position
        })
        setupTabLayout()
    }

    private fun setupTabLayout() {
        tabPicker.setupWithViewPager(vpVideoPicker)
        tabPicker.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // there is no way to change style to BOLD in XML and programmatically. we use this trick.
                // source: henry (imagepicker)
                exceptionHandler {
                    val textView = ((tabPicker.getChildAt(0) as ViewGroup).getChildAt(tab.position) as ViewGroup).getChildAt(1) as TextView
                    textView.setTypeface(textView.typeface, Typeface.BOLD)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                exceptionHandler {
                    val textView = ((tabPicker.getChildAt(0) as ViewGroup).getChildAt(tab.position) as ViewGroup).getChildAt(1) as TextView
                    textView.typeface = Typeface.create(textView.typeface, Typeface.NORMAL)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        //select first tab for startup
        selectCurrentPage(currentSelectedTab)
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
        if (videoPath.isNotEmpty()) {
            cancelVideo()
        } else {
            super.onBackPressed()
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
        videoPath = ""
        videoPreview.stopPlayback()
        videoPreview.setVideoURI(null)

        if (!isVideoSourcePicker) {
            if (File(videoPath).exists()) {
                FileUtils.deleteCacheDir()
            }
        }
    }

    private fun selectCurrentPage(index: Int) {
        tabPicker.getTabAt(index)?.select()
        vpVideoPicker.currentItem = index
    }

    protected open fun onVideoDoneClicked() {
        onFinishPicked(videoPath)
    }

    private fun onFinishPicked(file: String) {
        val videos = arrayListOf<String>()
        videos.add(file)

        val intent = Intent()
        intent.putStringArrayListExtra(VIDEOS_RESULT, videos)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * Callback from video recorder
     * @method(onVideoTaken(file))
     * @method(onPreviewVideoVisible)
     * @method(onVideoVisible)
     */

    override fun onVideoTaken(filePath: String) {
        if (filePath.isNotEmpty()) {
            selectCurrentPage(currentSelectedTab)
            onPreviewVideoVisible()

            //preventing crash
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                initViewPager()
            }

            val uriFile = Uri.parse(filePath)
            isVideoSourcePicker = false
            videoPath = filePath

            videoPreview.setVideoURI(uriFile)
            videoPreview.setOnPreparedListener { mp ->
                mp.isLooping = true //loop
                playVideoPreview()
            }
        }
    }

    override fun onPreviewVideoVisible() {
        layoutPreview.show()
        layoutPreview.bringToFront()
        layoutPreview.invalidate()
        showBackButton(false)
        containerPicker.hide()
        btnDone.show()

        btnDeleteVideo.text = if (isVideoSourcePicker) {
            getString(R.string.vidpick_btn_back)
        } else {
            getString(R.string.vidpick_btn_delete)
        }
    }

    override fun onVideoVisible() {
        containerPicker.show()
        containerPicker.bringToFront()
        containerPicker.invalidate()
        showBackButton(true)
        layoutPreview.hide()
        btnDone.hide()
    }

    /**
     * Callback from video picker
     * @method(onAlbumItemClicked(item, isChecked))
     * @method(getImagePath)
     * @method(isMaxImageReached)
     * @method(getMaxFileSize)
     */

    override fun onAlbumItemClicked(item: MediaItem?, isChecked: Boolean) {
        //get single image
        isVideoSourcePicker = true
        videoPath = item?.realPath.toString()
        onVideoTaken(videoPath)
    }

    override fun getImagePath(): ArrayList<String> {
        return videoGalleryPaths
    }

    override fun isMaxImageReached(): Boolean = false

    override fun getMaxFileSize(): Long = VIDEO_MAX_SIZE

    /**
     * Don't allow Shake" while picking video
     * @method(initShake)
     * @method(registerShake)
     * @method(unregisterShake)
     */
    override fun initShake() {}
    override fun registerShake() {}
    override fun unregisterShake() {}

}
