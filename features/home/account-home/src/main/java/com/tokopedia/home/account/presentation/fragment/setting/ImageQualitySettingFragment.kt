package com.tokopedia.home.account.presentation.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.adapter.setting.ImageQualitySettingAdapter
import com.tokopedia.home.account.presentation.listener.ImageQualitySettingListener
import com.tokopedia.home.account.presentation.uimodel.MediaQualityUIModel.Companion.settingsMenu
import com.tokopedia.home.account.presentation.util.MediaQualityCacheManager
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_image_quality_setting.*
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT as LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL as TYPE_NORMAL

class ImageQualitySettingFragment: BaseDaggerFragment(), ImageQualitySettingListener {

    @Inject lateinit var settings: MediaQualityCacheManager

    private val _adapter by lazy {
        ImageQualitySettingAdapter(settingsMenu(), this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_image_quality_setting,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerview?.adapter = _adapter
        _adapter.previousPosition = settings.indexQuality()
    }

    private fun toastMessage(quality: Int): Int {
        return when(quality) {
            0 -> R.string.image_quality_auto_toast
            1 -> R.string.image_quality_low_toast
            2 -> R.string.image_quality_high_toast
            else -> 0
        }
    }

    override fun onOptionClicked(quality: Int) {
        settings.setQualitySettings(quality)

        view?.let {
            val message = toastMessage(quality)
            Toaster.make(it, getString(message), LENGTH_SHORT, TYPE_NORMAL)
        }
    }

    override fun initInjector() {
        /*
        * val fcmComponent = DaggerFcmComponent.builder()
                .fcmModule(FcmModule(requireContext()))
                .build()

        DaggerTroubleshootComponent.builder()
                .fcmComponent(fcmComponent)
                .troubleshootModule(TroubleshootModule(requireContext()))
                .build()
                .inject(this)
                * */
    }

    override fun getScreenName(): String = SCREEN_NAME

    companion object {
        const val SCREEN_NAME = "Image Quality Setting"

        fun createInstance(bundle: Bundle): ImageQualitySettingFragment {
            val fragment = ImageQualitySettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}