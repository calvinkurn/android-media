package com.tokopedia.centralizedpromo.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.common.errorhandler.CentralizedPromoErrorHandler
import com.tokopedia.centralizedpromo.databinding.FragmentCentralizedPromoBinding
import com.tokopedia.centralizedpromo.di.component.DaggerCentralizedPromoComponent
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.bottomSheet.DetailPromoBottomSheet
import com.tokopedia.centralizedpromo.view.fragment.partialview.BasePartialView
import com.tokopedia.centralizedpromo.view.fragment.partialview.PartialCentralizedPromoCreationView
import com.tokopedia.centralizedpromo.view.fragment.partialview.PartialCentralizedPromoOnGoingPromoView
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoViewModel
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class CentralizedPromoFragment : BaseDaggerFragment(),
    PartialCentralizedPromoOnGoingPromoView.RefreshButtonClickListener, CoachMarkListener,
    PartialCentralizedPromoCreationView.RefreshPromotionClickListener {

    companion object {
        private const val TOAST_DURATION: Long = 5000

        private const val TAG_COACH_MARK = "CentralizedPromoCoachMark"

        private const val SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION = "onBoardingAdsAndPromotions"
        private const val SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION =
            "onBoardingPromoRecommendation"
        private const val SHARED_PREF_SUFFIX =
            "centralizePromoFragmentSuffix"

        const val ERROR_GET_LAYOUT_DATA = "Error when get layout data for %s."

        const val WEBVIEW_APPLINK_FORMAT = "%s?url=%s"
        const val PLAY_PERFORMANCE_URL = "https://www.tokopedia.com/play/live"

        @JvmStatic
        fun createInstance(): CentralizedPromoFragment = CentralizedPromoFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val adapterTypeFactory by lazy {
        CentralizedPromoAdapterTypeFactory(
            ::onClickPromo,
            ::onImpressionPromoList
        )
    }

    private val partialViews by lazy {
        return@lazy mapOf(
            LayoutType.ON_GOING_PROMO to createOnGoingPromoView(),
            LayoutType.PROMO_CREATION to createPromoRecommendationView()
        )
    }

    private val centralizedPromoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CentralizedPromoViewModel::class.java)
    }

    private val sharedPref by lazy {
        requireContext().getSharedPreferences(
            "${this.javaClass.simpleName}.pref",
            Context.MODE_PRIVATE
        )
    }

    private val coachMark by lazy { createCoachMark() }
    private val coachMark2 by lazy {
        context?.let {
            CoachMark2(it)
        }
    }

    private var isErrorToastShown: Boolean = false
    private var isCoachMarkShowed: Boolean = false
    private var currentFilterTab = FilterPromoUiModel()

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerCentralizedPromoComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private var binding by autoClearedNullable<FragmentCentralizedPromoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCentralizedPromoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeGetLayoutDataResult()
        refreshLayout()
        CentralizedPromoTracking.sendOpenScreenEvent(userSession.isLoggedIn, userSession.userId)
    }

    override fun onRefreshButtonClicked() {
        getLayoutData(LayoutType.ON_GOING_PROMO)
    }

    override fun onRefreshPromotionListButtonClicked() {
        getLayoutData(LayoutType.PROMO_CREATION)
    }

    override fun onViewReadyForCoachMark() {
        partialViews.forEach {
            if (it.value?.showCoachMark.orFalse() && !it.value?.readyToShowCoachMark.orFalse()) return
        }

        view?.post {
            showCoachMark()
        }
    }

    private fun createOnGoingPromoView(): PartialCentralizedPromoOnGoingPromoView? {
        return binding?.let {
            PartialCentralizedPromoOnGoingPromoView(
                refreshButtonClickListener = this,
                binding = it,
                adapterTypeFactory = adapterTypeFactory,
                coachMarkListener = this,
                showCoachMark = sharedPref.getBoolean(
                    SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION,
                    true
                )
            )
        }
    }

    private fun createPromoRecommendationView(): PartialCentralizedPromoCreationView? {
        return binding?.let {
            PartialCentralizedPromoCreationView(
                refreshButtonClickListener = this,
                binding = it,
                adapterTypeFactory = adapterTypeFactory,
                coachMarkListener = this,
                showCoachMark = sharedPref.getBoolean(
                    SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION,
                    true
                ),
                onSelectedFilter = { filter ->
                    currentFilterTab = filter
                    CentralizedPromoTracking.sendClickFilter(filter.id)
                    partialViews[LayoutType.PROMO_CREATION]?.renderLoading(LoadingType.PROMO_LIST)
                    getLayoutData(
                        LayoutType.PROMO_CREATION
                    )
                }
            )
        }
    }

    private fun setupView() {
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            refreshLayout()
        }
    }

    private fun refreshLayout() {
        partialViews.forEach { it.value?.renderLoading() }
        getLayoutData(
            LayoutType.ON_GOING_PROMO,
            LayoutType.PROMO_CREATION,
        )
    }

    private fun getLayoutData(vararg layoutTypes: LayoutType) {
        centralizedPromoViewModel.getLayoutData(*layoutTypes, tabId = currentFilterTab.id)
    }

    private fun observeGetLayoutDataResult() {
        centralizedPromoViewModel.getLayoutResultLiveData.observe(viewLifecycleOwner, Observer {
            it.forEach { entry ->
                when (val value = entry.value) {
                    is Success -> value.onSuccessGetLayoutData<BaseUiModel, BasePartialView<BaseUiModel>>(
                        entry.key
                    )
                    is Fail -> value.onFailedGetLayoutData(entry.key)
                }
            }

            binding?.swipeRefreshLayout?.isRefreshing = false
        })
    }

    private inline fun <reified D : BaseUiModel, reified V : BasePartialView<D>> Success<BaseUiModel>.onSuccessGetLayoutData(
        layoutType: LayoutType
    ) {
        partialViews.forEach {
            if (it.key == layoutType) {
                (it.value as V).renderSuccess(data as D)
            }
        }
    }

    private fun Fail.onFailedGetLayoutData(layoutType: LayoutType) {
        CentralizedPromoErrorHandler.logException(
            throwable,
            String.format(ERROR_GET_LAYOUT_DATA, layoutType.name)
        )
        partialViews[layoutType]?.renderError(this.throwable)
        showErrorToaster()
    }

    private fun showCoachMark() {
        if (!isCoachMarkShowed) {
            isCoachMarkShowed = true
            val coachMarkItem = ArrayList(
                partialViews.filter {
                    it.value?.shouldShowCoachMark().orFalse()
                }.map { it.value?.getCoachMarkItem() }
                    .filterNotNull()
            )

            coachMark.show(activity, TAG_COACH_MARK, coachMarkItem)
        }
    }

    private fun showCoachMarkPromoCreationItem(pageId: String, view: View) {
        val key = "$pageId+$SHARED_PREF_SUFFIX"
        val coachMarkList = arrayListOf<CoachMark2Item>()

        if (pageId == PromoCreationUiModel.PAGE_ID_SHOP_COUPON) {
            val alreadyShow = sharedPref.getBoolean(key, false)

            if (!alreadyShow) {
                coachMarkList.add(
                    CoachMark2Item(
                        anchorView = view,
                        title = context?.getString(R.string.centralize_promo_flash_sale_title_coachmark)
                            ?: "",
                        description = context?.getString(R.string.centralize_promo_flash_sale_desc_coachmark)
                            ?: "",
                        position = CoachMark2.POSITION_BOTTOM
                    )
                )

                coachMark2?.showCoachMark(coachMarkList, null, 0)
                sharedPref.edit().putBoolean(key, true).apply()
            }
        }
    }

    private fun updateCoachMarkStatus(key: String) {
        sharedPref.edit().putBoolean(key, false).apply()
    }

    private fun createCoachMark(): CoachMark {
        val coachMark = CoachMarkBuilder().build()
        coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
            override fun onShowCaseGoTo(
                previousStep: Int,
                nextStep: Int,
                coachMarkItem: CoachMarkItem
            ): Boolean {
                when (coachMarkItem.title) {
                    getString(R.string.sh_coachmark_title_on_going_promo) -> updateCoachMarkStatus(
                        SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION
                    )
                    getString(R.string.sh_coachmark_title_promo_creation) -> updateCoachMarkStatus(
                        SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION
                    )
                }
                return false
            }
        })

        return coachMark
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.build(
            this, context.getString(R.string.centralized_promo_failed_to_get_information),
            TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.centralized_promo_reload)
        ) {
            refreshLayout()
        }.show()

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    private fun onClickPromo(promoCreationUiModel: PromoCreationUiModel) {

        if (promoCreationUiModel.isEligible()) {
            if (sharedPref.getBoolean(promoCreationUiModel.title, false)){
                RouteManager.route(requireContext(),promoCreationUiModel.ctaLink)
            } else {
                val detailPromoBottomSheet =
                    DetailPromoBottomSheet.createInstance(promoCreationUiModel)
                detailPromoBottomSheet.show(childFragmentManager)
                detailPromoBottomSheet.onCheckBoxListener { isDontShowBottomSheet ->
                    updateDontShowBottomSheet(
                        pageName = promoCreationUiModel.title,
                        isDontShowBottomSheet
                    )
                    CentralizedPromoTracking.sendClickCheckboxBottomSheet(
                        currentFilterTab.name,
                        promoCreationUiModel.title
                    )
                }
                detailPromoBottomSheet.onCreateCampaignTracking {
                    CentralizedPromoTracking.sendClickCreateCampaign(
                        currentFilterTab.name,
                        promoCreationUiModel.title
                    )
                }

                detailPromoBottomSheet.onClickPaywallTracking {
                    CentralizedPromoTracking.sendClickPaywall(
                        currentFilterTab.name,
                        promoCreationUiModel.title,
                        promoCreationUiModel.ctaText
                    )
                }


                detailPromoBottomSheet.onImpressionPaywallTracking {
                    CentralizedPromoTracking.sendImpressionBottomSheetPaywall(
                        currentFilterTab.name,
                        promoCreationUiModel.title,
                        promoCreationUiModel.ctaText
                    )
                }

                detailPromoBottomSheet.onClickPerformanceListener {
                    context?.let {
                        RouteManager.route(it, getPlayPerformanceApplink())
                    }
                }

                CentralizedPromoTracking.sendImpressionBottomSheetPromo(
                    currentFilterTab.name,
                    promoCreationUiModel.title
                )
            }

        } else {
            RouteManager.route(requireContext(), ApplinkConstInternalSellerapp.ADMIN_RESTRICTION)
        }

        CentralizedPromoTracking.sendClickCampaignCard(
            currentFilterTab.name,
            promoCreationUiModel.title
        )
    }

    private fun updateDontShowBottomSheet(pageName: String, isChecked: Boolean) {
        sharedPref.edit().putBoolean(pageName, isChecked).apply()
    }

    private fun onImpressionPromoList(promoName: String, pageId: String, targetView: View) {
        CentralizedPromoTracking.sendImpressionCard(promoName, currentFilterTab.name)
        view?.post {
            showCoachMarkPromoCreationItem(pageId, targetView)
        }
    }

    private fun getPlayPerformanceApplink(): String {
        return String.format(
            Locale.getDefault(),
            WEBVIEW_APPLINK_FORMAT,
            ApplinkConst.WEBVIEW,
            PLAY_PERFORMANCE_URL
        )
    }

}

interface CoachMarkListener {
    fun onViewReadyForCoachMark()
}
