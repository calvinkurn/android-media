package com.tokopedia.affiliate.ui.fragment.education

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AFFILIATE_HELP_URL_WEBVIEW
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_PROD_URL
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_STAGING_URL
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateEducationBannerClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationEventArticleClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationLearnClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationSocialCTAClickInterface
import com.tokopedia.affiliate.interfaces.AffiliateEducationTopicTutorialClickInterface
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateEducationSearchActivity
import com.tokopedia.affiliate.ui.activity.AffiliateEducationSeeAllActivity
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextField
import com.tokopedia.affiliate.viewmodel.AffiliateEducationLandingViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.url.TokopediaUrl
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AffiliateEducationLandingPage :
    BaseViewModelFragment<AffiliateEducationLandingViewModel>(),
    AffiliateEducationEventArticleClickInterface,
    AffiliateEducationLearnClickInterface,
    AffiliateEducationTopicTutorialClickInterface,
    AffiliateEducationSocialCTAClickInterface,
    AffiliateEducationBannerClickInterface {

    private var eduViewModel: AffiliateEducationLandingViewModel? = null
    private var searchBar: SearchBarUnify? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    override fun getViewModelType(): Class<AffiliateEducationLandingViewModel> {
        return AffiliateEducationLandingViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        eduViewModel = viewModel as AffiliateEducationLandingViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelFactory
    }

    override fun initInject() {
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().injectEducationLandingPage(this)
    }

    companion object {
        fun getFragmentInstance() = AffiliateEducationLandingPage()
        private val kamusSlug = if (TokopediaUrl.getInstance().GQL.contains("staging")) {
            "imagetest"
        } else {
            "kamus-affiliate"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.affiliate_education_landing_page, container, false)
        searchBar = view.findViewById(R.id.edukasi_navToolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchListener(searchBar)
        eduViewModel?.getEducationPageData()?.observe(viewLifecycleOwner) {
            val adapter =
                AffiliateAdapter(
                    AffiliateAdapterFactory(
                        affiliateEducationEventArticleClickInterface = this,
                        affiliateEducationLearnClickInterface = this,
                        affiliateEducationTopicTutorialClickInterface = this,
                        educationSocialCTAClickInterface = this,
                        educationBannerClickInterface = this
                    )
                )
            adapter.setVisitables(it)
            view.findViewById<RecyclerView>(R.id.rv_education_page).adapter = adapter
        }
        view.findViewById<SearchBarUnify>(R.id.edukasi_navToolbar)?.run {

        }
    }

    private fun setSearchListener(searchBar: SearchBarUnify?) {

        val searchTextField = searchBar?.searchBarTextField
        val searchClearButton = searchBar?.searchBarIcon

        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH && !searchTextField.text.toString().isNullOrEmpty()) {
                    context?.let {
                        startActivity(AffiliateEducationSearchActivity.createIntent(it, searchTextField.text.toString()))
                    }
                    return true
                }
                return false
            }
        })

        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
        }
    }

    override fun onSeeMoreClick(pageType: String, categoryId: String) {
        context?.let {
            startActivity(AffiliateEducationSeeAllActivity.createIntent(it, pageType, categoryId))
        }
    }

    override fun onDetailClick(pageType: String, slug: String) {
        context?.let {
            RouteManager.route(
                it,
                getArticleEventUrl(
                    slug,
                    if (pageType == PAGE_EDUCATION_EVENT) {
                        getString(R.string.affiliate_event)
                    } else {
                        getString(R.string.affiliate_artikel)
                    }
                )
            )
        }
    }

    override fun onKamusClick() {
        context?.let {
            RouteManager.route(
                it,
                getArticleEventUrl(
                    kamusSlug,
                    getString(R.string.affiliate_artikel)
                )
            )
        }
    }

    override fun onBantuanClick() {
        context?.let {
            RouteManager.route(it, AFFILIATE_HELP_URL_WEBVIEW)
        }
    }

    override fun onCardClick(pageType: String, categoryId: String) {
        context?.let {
            startActivity(AffiliateEducationSeeAllActivity.createIntent(it, pageType, categoryId))
        }
    }

    private fun getArticleEventUrl(slug: String, title: String): String {
        return String.format(
            Locale.getDefault(),
            "%s?title=%s&url=%s%s?navigation=hide",
            ApplinkConst.WEBVIEW,
            title.replace(" ", "+"),
            if (TokopediaUrl.getInstance().GQL.contains("staging")) {
                EDUCATION_ARTICLE_DETAIL_STAGING_URL
            } else {
                EDUCATION_ARTICLE_DETAIL_PROD_URL
            },
            slug
        )
    }

    override fun onSocialClick(channel: String, url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBannerClick(url: String) {
        context?.let {
            RouteManager.route(
                it,
                buildString {
                    append(ApplinkConst.WEBVIEW)
                    append("?titlebar=true")
                    append("&url=$url")
                    append("?navigation=hide")
                }
            )
        }
    }
}
