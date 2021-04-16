package com.tokopedia.imagepicker.videorecorder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.common.GalleryType
import com.tokopedia.imagepicker.common.listener.VideoPickerCallback
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment
import com.tokopedia.imagepicker.common.model.MediaItem
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import com.tokopedia.imagepicker.videorecorder.adapter.ViewPagerAdapter
import com.tokopedia.imagepicker.videorecorder.recorder.VideoRecorderFragment
import com.tokopedia.imagepicker.common.state.StateRecorder
import com.tokopedia.imagepicker.videorecorder.utils.*
import com.tokopedia.imagepicker.videorecorder.utils.hide
import com.tokopedia.imagepicker.videorecorder.utils.show
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
        private const val IMAGE_EXIST = "image_exist"

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
            permissionCheckerHelper.request(this, getPermissions(), {
                initView()
            }, {
                onBackPressed()
                finish()
            })
        } else {
            initView()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
            false -> supportActionBar?.title = "  ${getString(R.string.vidpick_title)}"
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
        videoPreview.stopPlayback()
        videoPreview.setVideoURI(null)

        if (!isVideoSourcePicker) {
            if (File(videoPath).exists()) {
                FileUtils.deleteCacheDir()
            }
        }

        videoPath = ""
        initViewPager()
        onVideoVisible()
        onVideoRecorder(StateRecorder.Stop)
    }

    private fun selectCurrentPage(index: Int) {
        tabPicker.getTabAt(index)?.select()
        vpVideoPicker.currentItem = index
    }

    private fun onVideoDoneClicked() {
        val isImageExist = intent?.getBooleanExtra(IMAGE_EXIST, false)?: false

        if (isImageExist) {
            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.ip_title_update_post))
            dialog.setDesc(
                    getString(R.string.ip_message_update_choosen_video))
            dialog.setBtnCancel(getString(R.string.ip_label_cancel))
            dialog.setBtnOk(getString(com.tokopedia.imagepicker.common.R.string.ip_continue))
            dialog.setOnOkClickListener{
                dialog.dismiss()
                onFinishPicked(videoPath)
            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
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

    /**
     * Callback from video recorder
     * @method(onVideoTaken(file))
     * @method(onPreviewVideoVisible)
     * @method(onVideoVisible)
     * @method(onVideoRecorder)
     */

    override fun onVideoTaken(filePath: String) {
        if (filePath.isNotEmpty()) {
            initViewPager()
            onPreviewVideoVisible()
            selectCurrentPage(currentSelectedTab)

            val uriFile = Uri.parse(filePath)
            isVideoSourcePicker = false
            videoPath = filePath

            videoPreview.setVideoURI(uriFile)
            videoPreview.setOnCompletionListener {
                videoPreview.stopPlayback()
                onVideoVisible()
            }
            videoPreview.setOnPreparedListener { mp ->
                mp.isLooping = true //loop
                playVideoPreview()
            }
        }
    }

    override fun onPreviewVideoVisible() {
        layoutPreview.bringToFront()
        sendViewToBack(containerPicker)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            layoutPreview.elevation = 90f
            containerPicker.elevation = 0f
        }
        showBackButton(false)
        layoutPreview.show()
        containerPicker.hide()
        btnDone.show()

        btnDeleteVideo.text = if (isVideoSourcePicker) {
            getString(R.string.vidpick_btn_back)
        } else {
            getString(R.string.vidpick_btn_delete)
        }
    }

    override fun onVideoVisible() {
        containerPicker.bringToFront()
        sendViewToBack(layoutPreview)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            containerPicker.elevation = 90f
            layoutPreview.elevation = 0f
        }
        showBackButton(true)
        containerPicker.show()
        layoutPreview.hide()
        btnDone.hide()
    }

    override fun onVideoRecorder(state: StateRecorder) {
        when (state) {
            StateRecorder.Start -> tabPicker.tabClickable(false)
            StateRecorder.Stop -> tabPicker.tabClickable(true)
        }
    }

    private fun sendViewToBack(child: View) {
        val parent = child.parent as ViewGroup
        parent.removeView(child)
        parent.addView(child, 0)
    }

    /**
     * Callback from video picker
     * @method(onAlbumItemClicked(item, isChecked))
     * @method(getImagePath)
     * @method(isMaxImageReached)
     * @method(getMaxFileSize)
     */

    override fun onAlbumItemClicked(item: MediaItem?, isChecked: Boolean) {
        if (item?.contentUri == null) {
            return
        }
        //get single image
        isVideoSourcePicker = true
        videoPath = item.path
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

}