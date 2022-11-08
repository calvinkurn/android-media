package com.tokopedia.play.broadcaster.shorts.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play.broadcaster.databinding.ActivityPlayShortsBinding
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsModule
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
@Suppress("LateinitUsage")
class PlayShortsActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityPlayShortsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        binding = ActivityPlayShortsBinding.inflate(
            LayoutInflater.from(this),
            null,
            false
        )
        setContentView(binding.root)

        openMediaPicker()
    }

    private fun inject() {
        DaggerPlayShortsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .playShortsModule(PlayShortsModule(this))
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun openMediaPicker() {
        val intent = MediaPicker.intent(this) {
            pageSource(PageSource.Unknown)
            minVideoDuration(1000)
            maxVideoDuration(90000)
            pageType(PageType.GALLERY)
            modeType(ModeType.VIDEO_ONLY)
            singleSelectionMode()
        }

        startActivityForResult(intent, MEDIA_PICKER_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MEDIA_PICKER_REQ) {
            if (resultCode == RESULT_OK) {
                val data = MediaPicker.result(data)
            } else {
                finish()
            }
        }
    }

    companion object {
        private const val MEDIA_PICKER_REQ = 123
    }
}
