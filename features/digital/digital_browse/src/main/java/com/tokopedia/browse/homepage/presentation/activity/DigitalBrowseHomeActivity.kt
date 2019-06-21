package com.tokopedia.browse.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.Menu
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
import com.tokopedia.browse.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.categoryNavigation.view.BaseCategoryBrowseActivity
import com.tokopedia.browse.categoryNavigation.view.CategoryBrowseActivity
import javax.inject.Inject

class DigitalBrowseHomeActivity : DigitalBrowseBaseActivity(), HasComponent<DigitalBrowseHomeComponent> {

    @Inject lateinit var digitalBrowseAnalytics: DigitalBrowseAnalytics

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
        if (Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_BELANJA) {
            autocompleteParam = AUTOCOMPLETE_BELANJA
            fragmentDigital = DigitalBrowseMarketplaceFragment.fragmentInstance
        } else if (Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN) {
            autocompleteParam = AUTOCOMPLETE_LAYANAN
            fragmentDigital = if (intent.hasExtra(EXTRA_TAB)) {
                DigitalBrowseServiceFragment.getFragmentInstance(
                        Integer.parseInt(intent.getStringExtra(EXTRA_TAB)))
            } else {
                DigitalBrowseServiceFragment.fragmentInstance
            }
        }

        return fragmentDigital
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_search, menu)
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
        RouteManager.route(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE_WITH_NAVSOURCE, autocompleteParam)
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
        val TITLE_LAYANAN = "Semua Kategori"

        val LAYANAN_SCREEN = "/digital"
        val DEFAULT_SCREEN = "/kategori-belanja"

        private var digitalBrowseHomeComponent: DigitalBrowseHomeComponent? = null
    }

    override fun getScreenName(): String =
        if(Integer.parseInt(intent.getStringExtra(EXTRA_TYPE)) == TYPE_LAYANAN){
             LAYANAN_SCREEN
        } else {
            DEFAULT_SCREEN
        }

}


@DeepLink(ApplinkConstant.DIGITAL_BROWSE)
fun getCallingIntent(context: Context, extras: Bundle): Intent {
    val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
    var intent = Intent(context, DigitalBrowseHomeActivity::class.java)

    if (!extras.containsKey(DigitalBrowseHomeActivity.EXTRA_TITLE)) {
        if (Integer.parseInt(extras.getString(DigitalBrowseHomeActivity.EXTRA_TYPE)) == DigitalBrowseHomeActivity.TYPE_BELANJA) {
            if(BaseCategoryBrowseActivity.isNewCategoryEnabled(context)) {
                intent = BaseCategoryBrowseActivity.newIntent(context)
            }else {
                extras.putString(DigitalBrowseHomeActivity.EXTRA_TITLE, DigitalBrowseHomeActivity.TITLE_BELANJA)
            }
        } else if (Integer.parseInt(extras.getString(DigitalBrowseHomeActivity.EXTRA_TYPE)) == DigitalBrowseHomeActivity.TYPE_LAYANAN) {
            intent = Intent(context, DigitalBrowseHomeActivity::class.java)
            extras.putString(DigitalBrowseHomeActivity.EXTRA_TITLE, DigitalBrowseHomeActivity.TITLE_LAYANAN)
        }
    }

    return intent.setData(uri.build()).putExtras(extras)
}
