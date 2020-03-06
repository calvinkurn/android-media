package com.tokopedia.exploreCategory.ui.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.ECConstants.Companion.DEFAULT_SCREEN
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TITLE
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TAB
import com.tokopedia.exploreCategory.ECConstants.Companion.EXTRA_TYPE
import com.tokopedia.exploreCategory.ECConstants.Companion.LAYANAN_SCREEN
import com.tokopedia.exploreCategory.ECConstants.Companion.TYPE_LAYANAN
import com.tokopedia.exploreCategory.ECConstants.Companion.TYPE_BELANJA
import com.tokopedia.exploreCategory.ui.fragment.ECServiceFragment
import com.tokopedia.exploreCategory.viewmodel.ECHomeViewModel
import javax.inject.Inject

class ECHomeActivity : BaseECActivity<ECHomeViewModel>() {

    private var fragmentDigital: Fragment? = null

    private lateinit var ecHomeHomeViewModel: ECHomeViewModel
    private lateinit var title: String

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

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
            if ((data?.getQueryParameter(EXTRA_TYPE))?.toInt() == TYPE_BELANJA) {
                title = TITLE_BELANJA
                return openBelanjaActivity(this)
            } else if ((data?.getQueryParameter(EXTRA_TYPE))?.toInt() == TYPE_LAYANAN) {
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

    private fun openBelanjaActivity(context: Context) {
        RouteManager.route(context, ApplinkConst.CATEGORY_BELANJA)
        finish()
    }

    companion object {
        const val TITLE_BELANJA = "Belanja di Tokopedia"
        const val TITLE_LAYANAN = "Jelajah Tokopedia"
    }

}
