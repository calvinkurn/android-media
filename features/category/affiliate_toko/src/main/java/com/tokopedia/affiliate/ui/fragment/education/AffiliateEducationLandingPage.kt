package com.tokopedia.affiliate.ui.fragment.education

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AFFILIATE_HELP_URL_WEBVIEW
import com.tokopedia.affiliate.AFFILIATE_PRIVACY_POLICY_URL_WEBVIEW
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_PROD_URL
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_STAGING_URL
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
import com.tokopedia.affiliate.ui.activity.AffiliateEducationSeeAllActivity
import com.tokopedia.affiliate.viewmodel.AffiliateEducationLandingViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
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

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @JvmField
    @Inject
    var userSessionInterface: UserSessionInterface? = null

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

        private const val IS_APP_LINK = "isAppLink"
        private const val IS_HELP_APP_LINK = "isHelpAppLink"

        fun getFragmentInstance(
            fromAppLink: Boolean = false,
            fromHelpAppLink: Boolean = false
        ) = AffiliateEducationLandingPage().apply {
            arguments = bundleOf(
                IS_APP_LINK to fromAppLink,
                IS_HELP_APP_LINK to fromHelpAppLink
            )
        }

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
        return inflater.inflate(R.layout.affiliate_education_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eduViewModel?.getEducationLandingPageData()
        view.findViewById<NavToolbar>(R.id.edukasi_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(IconBuilder().addIcon(IconList.ID_NAV_GLOBAL) {})
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(R.string.affiliate_edukasi)
            setOnBackButtonClickListener {
                handleBack()
            }
        }
        eduViewModel?.getEducationPageData()?.observe(viewLifecycleOwner) {
            val adapter =
                AffiliateAdapter(
                    AffiliateAdapterFactory(
                        affiliateEducationEventArticleClickInterface = this,
                        affiliateEducationLearnClickInterface = this,
                        affiliateEducationTopicTutorialClickInterface = this,
                        educationSocialCTAClickInterface = this,
                        educationBannerClickInterface = this
                    ),
                    source = AffiliateAdapter.PageSource.SOURCE_EDU_LANDING,
                    userId = userSessionInterface?.userId.orEmpty()
                )
            adapter.setVisitables(it)
            view.findViewById<RecyclerView>(R.id.rv_education_page).adapter = adapter
        }
        if (arguments?.getBoolean(IS_HELP_APP_LINK, false) == true) {
            onBantuanClick()
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

    override fun onKebijakanPrivasiClick() {
        context?.let {
            RouteManager.route(it, AFFILIATE_PRIVACY_POLICY_URL_WEBVIEW)
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

    fun handleBack() {
        if (arguments?.getBoolean(IS_APP_LINK, false) == true) {
            if (activity?.isTaskRoot == true) {
                context?.let {
                    startActivity(
                        RouteManager.getIntent(
                            it,
                            ApplinkConst.HOME
                        )
                    ).also {
                        activity?.finish()
                    }
                }
            } else {
                activity?.finish()
            }
        } else {
            (activity as? AffiliateActivity)?.handleBackButton(false)
        }
    }
}
