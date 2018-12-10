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
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseMarketplaceFragment
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseServiceFragment
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

class DigitalBrowseHomeActivity : DigitalBrowseBaseActivity(), HasComponent<DigitalBrowseHomeComponent> {

    @Inject
    internal var digitalBrowseAnalytics: DigitalBrowseAnalytics? = null

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
            fragmentDigital = DigitalBrowseMarketplaceFragment.getFragmentInstance()
        } else if (Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN) {
            if (intent.hasExtra(EXTRA_TAB)) {
                fragmentDigital = DigitalBrowseServiceFragment.getFragmentInstance(
                        Integer.parseInt(intent.getStringExtra(EXTRA_TAB)))
            } else {
                fragmentDigital = DigitalBrowseServiceFragment.getFragmentInstance()
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
                digitalBrowseAnalytics!!.eventClickBackOnBelanjaPage()
            } else if (fragmentDigital is DigitalBrowseServiceFragment) {
                digitalBrowseAnalytics!!.eventClickBackOnLayananPage()
            }
        }
    }

    companion object {

        private val EXTRA_TYPE = "type"
        private val EXTRA_TAB = "tab"
        private val EXTRA_TITLE = "title"

        private val TYPE_BELANJA = 1
        private val TYPE_LAYANAN = 2

        private val TITLE_BELANJA = "Belanja di Tokopedia"
        private val TITLE_LAYANAN = "Lainnya"

        private var digitalBrowseHomeComponent: DigitalBrowseHomeComponent? = null

        @DeepLink(ApplinkConstant.DIGITAL_BROWSE)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val intent = Intent(context, DigitalBrowseHomeActivity::class.java)

            if (!extras.containsKey(EXTRA_TITLE)) {
                if (Integer.parseInt(extras.getString(EXTRA_TYPE)) == TYPE_BELANJA) {
                    extras.putString(EXTRA_TITLE, TITLE_BELANJA)
                } else if (Integer.parseInt(extras.getString(EXTRA_TYPE)) == TYPE_LAYANAN) {
                    extras.putString(EXTRA_TITLE, TITLE_LAYANAN)
                }
            }

            return intent.setData(uri.build()).putExtras(extras)
        }
    }
}

