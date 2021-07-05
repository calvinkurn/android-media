package com.tokopedia.exploreCategory.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.ECConstants.Companion.DEFAULT_SCREEN
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TITLE
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TAB
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TYPE
import com.tokopedia.exploreCategory.ECConstants.Companion.LAYANAN_SCREEN
import com.tokopedia.exploreCategory.ECConstants.Companion.TYPE_LAYANAN
import com.tokopedia.exploreCategory.ui.fragment.ECServiceFragment
import com.tokopedia.exploreCategory.viewmodel.ECHomeViewModel

class ECHomeActivity : BaseViewModelActivity<ECHomeViewModel>() {

    private var fragmentDigital: Fragment? = null

    private lateinit var ecHomeHomeViewModel: ECHomeViewModel
    private lateinit var title: String

    override fun getViewModelType(): Class<ECHomeViewModel> {
        return ECHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        ecHomeHomeViewModel = viewModel as ECHomeViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntentFromDeeplink()
    }

    private fun handleIntentFromDeeplink() {
        val data = intent.data

        if (data?.getQueryParameter(EXTRA_TITLE) == null) {
           if (data?.getQueryParameter(EXTRA_TYPE)?.toInt() == TYPE_LAYANAN) {
                title = TITLE_LAYANAN
            }
        }


        if (::title.isInitialized) {
            setupToolbar()
        }
    }

    override fun getNewFragment(): Fragment? {
        val type = when {
            intent.hasExtra(EXTRA_TYPE) && intent.getStringExtra(EXTRA_TYPE)?.isNotEmpty() == true -> intent.getStringExtra(EXTRA_TYPE)
            else -> "1"
        }

        if (TYPE_LAYANAN == type.toInt()) {
            fragmentDigital = if (intent.hasExtra(EXTRA_TAB)) {
                val tab = when {
                    intent.getStringExtra(EXTRA_TAB).isNotEmpty() -> intent.getStringExtra(EXTRA_TAB)
                    else -> "1"
                }
                ECServiceFragment.getFragmentInstance(tab.toInt())
            } else {
                ECServiceFragment.fragmentInstance
            }
        }

        return fragmentDigital
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        toolbar.contentInsetStartWithNavigation = 0
        val titleStr = SpannableStringBuilder(title)
        titleStr.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = titleStr
    }

    override fun getScreenName(): String =
            if (intent.hasExtra(EXTRA_TYPE) && intent.getStringExtra(EXTRA_TYPE) != null && (intent.getStringExtra(EXTRA_TYPE)).toInt() == TYPE_LAYANAN) {
                LAYANAN_SCREEN
            } else {
                DEFAULT_SCREEN
            }

    override fun onBackPressed() {
        ecHomeHomeViewModel.fireBackEvent()
        super.onBackPressed()
    }

    companion object {
        const val TITLE_LAYANAN = "Jelajah Tokopedia"
    }

}
