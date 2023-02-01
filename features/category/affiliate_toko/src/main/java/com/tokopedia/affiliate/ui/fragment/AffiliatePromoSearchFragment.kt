package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.ON_REVIEWED
import com.tokopedia.affiliate.SYSTEM_DOWN
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextField
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextFieldInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionErrorCardItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate.utils.setBoldSpannedText
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromoSearchFragment : AffiliateBaseFragment<AffiliatePromoViewModel>(),
    AffiliateLinkTextFieldInterface, PromotionClickInterface {

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    private var affiliatePromoViewModel: AffiliatePromoViewModel? = null

    private val adapter: AffiliateAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(null, null, this),
            source = AffiliateAdapter.SOURCE_PROMOSIKAN,
            userId = userSessionInterface?.userId.orEmpty()
        )
    }

    companion object {
        private const val ZERO = 0
        private const val LINK_TITLE_BOLD_SPAN_LENGTH = 28
        private const val TWO_STEP_BOLD_SPAN_LENGTH = 12
        fun newInstance() = AffiliatePromoSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_affiliate_promo_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        view?.findViewById<NavToolbar>(R.id.promo_search_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(IconBuilder().addIcon(IconList.ID_INFORMATION) {
                AffiliateHowToPromoteBottomSheet.newInstance(
                    AffiliateHowToPromoteBottomSheet.STATE_HOW_TO_PROMOTE
                ).show(childFragmentManager, "")
            }.addIcon(IconList.ID_NAV_GLOBAL) {})
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(R.string.affiliate_promo)
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        view?.findViewById<RecyclerView>(R.id.promotion_recycler_view)?.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }

        view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.run {
            setDoneAction {
                editText.text.let {
                    if (it.isNotEmpty()) {
                        affiliatePromoViewModel?.getSearch(it.toString())
                    }
                }
            }
            setEventListener(this@AffiliatePromoSearchFragment)
        }
        view?.findViewById<UnifyButton>(R.id.search_button)?.setOnClickListener {
            view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.let {
                it.editingState(false)
                if (it.editText.text?.isNotEmpty() == true) {
                    affiliatePromoViewModel?.getSearch(it.editText.text.toString())
                }
            }
        }
        view?.findViewById<Typography>(R.id.promo_link_title)?.setBoldSpannedText(
            getString(R.string.affiliate_paste_product_link),
            ZERO,
            LINK_TITLE_BOLD_SPAN_LENGTH,
            Typography.DISPLAY_3
        )
        val twoStepDesc = getString(R.string.paste_info_step_two)
        val promosikanIndex = twoStepDesc.indexOf("Promosikan")
        view?.findViewById<Typography>(R.id.paste_info_step_two)?.setBoldSpannedText(
            twoStepDesc, promosikanIndex - 1, TWO_STEP_BOLD_SPAN_LENGTH, Typography.DISPLAY_3
        )

    }

    private fun setObservers() {
        affiliatePromoViewModel?.getErrorMessage()?.observe(this) { _ ->
            view?.rootView?.let {
                Toaster.build(
                    it,
                    getString(R.string.affiliate_product_link_invalid),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
            view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.editingState(true)
        }
        affiliatePromoViewModel?.getAffiliateSearchData()?.observe(this) { affiliateSearchData ->
            onGetAffiliateSearchData(affiliateSearchData)
        }
        affiliatePromoViewModel?.getAffiliateSearchData()?.observe(this) { affiliateSearchData ->
            onGetAffiliateSearchData(affiliateSearchData)
        }
    }

    private fun onGetAffiliateSearchData(affiliateSearchData: AffiliateSearchData) {
        resetAdapter()
        if (affiliateSearchData.searchAffiliate?.data?.status == 0) {
            if (affiliateSearchData.searchAffiliate?.data?.error?.errorType == 1) {
                view?.rootView?.let {
                    Toaster.build(
                        it,
                        getString(R.string.affiliate_product_link_invalid),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                sendSearchEvent(AffiliateAnalytics.LabelKeys.NOT_URL)
            } else {
                affiliateSearchData.searchAffiliate?.data?.error?.let {
                    adapter.addElement(AffiliatePromotionErrorCardModel(it))
                }
                val errorLabel =
                    when (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus) {
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_FOUND ->
                            AffiliateAnalytics.LabelKeys.PRDOUCT_URL_NOT_FOUND
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_ELIGIBLE ->
                            AffiliateAnalytics.LabelKeys.NON_WHITELISTED_CATEGORIES
                        AffiliatePromotionErrorCardItemVH.ERROR_NON_PM_OS -> AffiliateAnalytics.LabelKeys.NON_PM_OS_SHOP
                        else -> AffiliateAnalytics.LabelKeys.NOT_URL
                    }
                sendSearchEvent(errorLabel)
            }
        } else {
            affiliateSearchData.searchAffiliate?.data?.cards?.firstOrNull()?.let { cards ->
                view?.findViewById<Typography>(R.id.promotion_card_title)?.text = cards.title
                cards.items?.forEach {
                    it?.let {
                        it.type = cards.pageType
                        it.itemId = cards.itemID.toString()
                        if (cards.pageType == "pdp") {
                            adapter.addElement(AffiliatePromotionCardModel(it))
                        } else {
                            adapter.addElement(AffiliatePromotionShopModel(it))
                        }

                    }
                }

            }
        }
    }

    private fun resetAdapter() {
        adapter.clearAllElements()
        view?.findViewById<RecyclerView>(R.id.promotion_recycler_view)?.apply {
            adapter = null
            adapter = this@AffiliatePromoSearchFragment.adapter
        }
        view?.findViewById<Group>(R.id.view_initial_info)?.hide()
        view?.findViewById<RecyclerView>(R.id.promotion_recycler_view)?.show()
    }


    private fun sendSearchEvent(eventLabel: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            AffiliateAnalytics.ActionKeys.CLICK_SEARCH,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            eventLabel,
            userSessionInterface?.userId.orEmpty()
        )
    }

    override fun onEditState(state: Boolean) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            AffiliateAnalytics.ActionKeys.CLICK_SEARCH_BOX,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface?.userId.orEmpty()
        )
    }

    override fun onPromotionClick(
        itemID: String,
        itemName: String,
        itemImage: String,
        itemURL: String,
        position: Int,
        commison: String,
        status: String,
        type: String?,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo?
    ) {
        AffiliatePromotionBottomSheet.newInstance(
            AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
            null,
            null,
            itemID,
            itemName,
            itemImage,
            itemURL,
            "",
            AffiliatePromotionBottomSheet.ORIGIN_PROMOSIKAN,
            commission = commison,
            status = status,
            type = type
        ).show(childFragmentManager, "")
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) {
        if (errorCta?.ctaAction == AffiliatePromotionErrorCardItemVH.ACTION_REDIRECT) {
            errorCta.ctaLink?.androidUrl?.let {
                RouteManager.routeNoFallbackCheck(context, it, it)
            }
        } else {
            view?.findViewById<Group>(R.id.view_initial_info)?.show()
            view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.editingState(true)
        }
    }

    override fun onSystemDown() {
        view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.isEnabled = false
        affiliatePromoViewModel?.setValidateUserType(SYSTEM_DOWN)
        affiliatePromoViewModel?.getAnnouncementInformation()
    }

    override fun onReviewed() {
        affiliatePromoViewModel?.setValidateUserType(ON_REVIEWED)
        affiliatePromoViewModel?.getAnnouncementInformation()
    }

    override fun onUserNotRegistered() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onNotEligible() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onUserValidated() {
        affiliatePromoViewModel?.getAnnouncementInformation()
        affiliatePromoViewModel?.setValidateUserType(ON_REGISTERED)
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        DaggerAffiliateComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().injectPromoSearchFragment(this)
    }

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromoViewModel = viewModel as AffiliatePromoViewModel
    }

}
