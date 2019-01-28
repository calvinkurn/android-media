package com.tokopedia.browse.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.browse.DigitalBrowseComponentInstance
import com.tokopedia.browse.common.applink.ApplinkConstant
import com.tokopedia.browse.common.di.utils.DigitalBrowseComponentUtils
import com.tokopedia.browse.common.presentation.DigitalBrowseBaseActivity
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics
import com.tokopedia.browse.homepage.di.DaggerDigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseMarketplaceFragment
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseServiceFragment
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

class DigitalBrowseHomeActivity : DigitalBrowseBaseActivity(), HasComponent<DigitalBrowseHomeComponent> {

    @Inject lateinit var digitalBrowseAnalytics: DigitalBrowseAnalytics

    private var fragmentDigital: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EXTRA_TITLE)) {
            setupToolbar()
        }

        GraphqlClient.init(this)
        DaggerDigitalBrowseHomeComponent.builder()
                .digitalBrowseComponent(DigitalBrowseComponentInstance.getDigitalBrowseComponent(application))
                .build()
                .inject(this)
    }

    override fun getComponent(): DigitalBrowseHomeComponent {
        if (digitalBrowseHomeComponent == null) {
            digitalBrowseHomeComponent = DaggerDigitalBrowseHomeComponent.builder()
                    .digitalBrowseComponent(DigitalBrowseComponentUtils.getDigitalBrowseComponent(application))
                    .build()
        }
        return digitalBrowseHomeComponent as DigitalBrowseHomeComponent
    }

    override fun getNewFragment(): Fragment? {

        if (Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_BELANJA) {
            fragmentDigital = DigitalBrowseMarketplaceFragment.fragmentInstance
        } else if (Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN) {
            fragmentDigital = if (intent.hasExtra(EXTRA_TAB)) {
                DigitalBrowseServiceFragment.getFragmentInstance(
                        Integer.parseInt(intent.getStringExtra(EXTRA_TAB)))
            } else {
                DigitalBrowseServiceFragment.fragmentInstance
            }
        }

        return fragmentDigital
    }

    private fun setupToolbar() {
        toolbar.contentInsetStartWithNavigation = 0
        val title = intent.getStringExtra(EXTRA_TITLE)
        val titleStr = SpannableStringBuilder(title)
        titleStr.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = titleStr
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (fragmentDigital != null) {
            if (fragmentDigital is DigitalBrowseMarketplaceFragment) {
                digitalBrowseAnalytics.eventClickBackOnBelanjaPage()
            } else if (fragmentDigital is DigitalBrowseServiceFragment) {
                digitalBrowseAnalytics.eventClickBackOnLayananPage()
            }
        }
    }

    companion object {

        val EXTRA_TYPE = "type"
        private val EXTRA_TAB = "tab"
        val EXTRA_TITLE = "title"

        val TYPE_BELANJA = 1
        val TYPE_LAYANAN = 2

        val TITLE_BELANJA = "Belanja di Tokopedia"
        val TITLE_LAYANAN = "Lainnya"

        val LAYANAN_SCREEN = "/digital"

        private var digitalBrowseHomeComponent: DigitalBrowseHomeComponent? = null
    }

    override fun getScreenName(): String {
        if(Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN){
            return LAYANAN_SCREEN
        }

        return super.getScreenName()
    }
}

@DeepLink(ApplinkConstant.DIGITAL_BROWSE)
fun getCallingIntent(context: Context, extras: Bundle): Intent {
    val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
    val intent = Intent(context, DigitalBrowseHomeActivity::class.java)

    if (!extras.containsKey(DigitalBrowseHomeActivity.EXTRA_TITLE)) {
        if (Integer.parseInt(extras.getString(DigitalBrowseHomeActivity.EXTRA_TYPE)) == DigitalBrowseHomeActivity.TYPE_BELANJA) {
            extras.putString(DigitalBrowseHomeActivity.EXTRA_TITLE, DigitalBrowseHomeActivity.TITLE_BELANJA)
        } else if (Integer.parseInt(extras.getString(DigitalBrowseHomeActivity.EXTRA_TYPE)) == DigitalBrowseHomeActivity.TYPE_LAYANAN) {
            extras.putString(DigitalBrowseHomeActivity.EXTRA_TITLE, DigitalBrowseHomeActivity.TITLE_LAYANAN)
        }
    }

    return intent.setData(uri.build()).putExtras(extras)
}
