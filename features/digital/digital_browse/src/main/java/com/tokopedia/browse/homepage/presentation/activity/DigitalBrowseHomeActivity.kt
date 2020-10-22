package com.tokopedia.browse.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.Menu
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.browse.DigitalBrowseComponentInstance
import com.tokopedia.browse.R
import com.tokopedia.browse.common.di.utils.DigitalBrowseComponentUtils
import com.tokopedia.browse.common.presentation.DigitalBrowseBaseActivity
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics
import com.tokopedia.browse.homepage.di.DaggerDigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseServiceFragment
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

class DigitalBrowseHomeActivity : DigitalBrowseBaseActivity(), HasComponent<DigitalBrowseHomeComponent> {
    @Inject
    lateinit var digitalBrowseAnalytics: DigitalBrowseAnalytics

    private var fragmentDigital: Fragment? = null

    private var autocompleteParam = ""

    private val AUTOCOMPLETE_BELANJA = "belanja"

    private val AUTOCOMPLETE_LAYANAN = "homenav"

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
        val type = if (intent.hasExtra(EXTRA_TYPE) && intent.getStringExtra(EXTRA_TYPE)?.isNotEmpty() == true ) intent.getStringExtra(EXTRA_TYPE) else "1"
       if (Integer.parseInt(type) == TYPE_LAYANAN) {
            autocompleteParam = AUTOCOMPLETE_LAYANAN
            fragmentDigital = if (intent.hasExtra(EXTRA_TAB)) {
                val tab = if(intent.getStringExtra(EXTRA_TAB).isNotEmpty()) intent.getStringExtra(EXTRA_TAB) else "1"
                DigitalBrowseServiceFragment.getFragmentInstance(
                        Integer.parseInt(tab))
            } else {
                DigitalBrowseServiceFragment.fragmentInstance
            }
        }

        return fragmentDigital
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_digital_browse_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            onSearchClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onSearchClicked() {
        digitalBrowseAnalytics.eventClickOnSearchTopNav(screenName)
        RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?navsource={source}", autocompleteParam)
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
           if (fragmentDigital is DigitalBrowseServiceFragment) {
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
        val TITLE_LAYANAN = "Semua Kategori"

        val LAYANAN_SCREEN = "/digital"
        val DEFAULT_SCREEN = "/kategori-belanja"

        private var digitalBrowseHomeComponent: DigitalBrowseHomeComponent? = null
    }

    override fun getScreenName(): String =
            if (intent.hasExtra(EXTRA_TYPE) && Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN) {
                LAYANAN_SCREEN
            } else {
                DEFAULT_SCREEN
            }

    object DeepLinkIntents {
        lateinit var intent: Intent

        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            intent = Intent(context, DigitalBrowseHomeActivity::class.java)

            if (!extras.containsKey(EXTRA_TITLE)) {
                if (Integer.parseInt(extras.getString(EXTRA_TYPE, "")) == TYPE_BELANJA) {
                    extras.putString(EXTRA_TITLE, TITLE_BELANJA)
                    return openBelanjaActivity(context)
                } else if (Integer.parseInt(extras.getString(EXTRA_TYPE, "")) == TYPE_LAYANAN) {
                    intent = Intent(context, DigitalBrowseHomeActivity::class.java)
                    extras.putString(EXTRA_TITLE, TITLE_LAYANAN)
                }
            }

            return intent.setData(uri.build()).putExtras(extras)
        }

        private fun openBelanjaActivity(context: Context): Intent {
            return RouteManager.getIntent(context, INTERNAL_BELANJA_CATEGORY)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

    }


}
