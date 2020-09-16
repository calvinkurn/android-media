package com.tokopedia.home.account.presentation.fragment.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.adapter.setting.ImageQualitySettingAdapter
import com.tokopedia.home.account.presentation.listener.ImageQualitySettingListener
import com.tokopedia.home.account.presentation.viewmodel.ImageQualitySettingItemViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_image_quality_setting.*

class ImageQualitySettingFragment: BaseDaggerFragment(), ImageQualitySettingListener {

    private lateinit var adapter: ImageQualitySettingAdapter
    private var sharedPreferences: SharedPreferences? = null

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_quality_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        adapter = ImageQualitySettingAdapter(this)
        recyclerview?.layoutManager = linearLayoutManager
        recyclerview?.adapter = adapter
        adapter.itemList = createOptionItems()
        adapter.previousPosition = sharedPreferences?.getInt(SHARED_KEY, 0)?: 0
    }

    private fun createOptionItems(): List<ImageQualitySettingItemViewModel> {
        val optionList = arrayListOf<ImageQualitySettingItemViewModel>()

        context?.let {
            val recommended = ImageQualitySettingItemViewModel(it.getString(R.string.image_quality_auto_title),
                    it.getString(R.string.image_quality_auto_subtitle))
            optionList.add(recommended)

            val low = ImageQualitySettingItemViewModel(it.getString(R.string.image_quality_low_title),
                    it.getString(R.string.image_quality_low_subtitle))
            optionList.add(low)

            val high = ImageQualitySettingItemViewModel(it.getString(R.string.image_quality_high_title),
                    it.getString(R.string.image_quality_high_subtitle))
            optionList.add(high)
        }

        return optionList
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun onOptionClicked(quality: Int) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putInt(SHARED_KEY, quality)
        editor?.apply()

        var textId = 0
        when(quality) {
            0 -> textId = R.string.image_quality_auto_toast
            1 -> textId = R.string.image_quality_low_toast
            2 -> textId = R.string.image_quality_high_toast
        }

        cl_image_quality?.let {
            context?.let {ctx ->
                Toaster.make(it, ctx.getString(textId), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            }
        }
    }

    companion object {
        const val SCREEN_NAME = "Image Quality Setting"
        const val SHARED_PREFERENCE = "image_quality_setting"
        const val SHARED_KEY = "quality"

        fun createInstance(bundle: Bundle): ImageQualitySettingFragment {
            val fragment = ImageQualitySettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}