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
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.partialview.BasePartialView
import com.tokopedia.centralizedpromo.view.fragment.partialview.PartialCentralizedPromoCreationView
import com.tokopedia.centralizedpromo.view.fragment.partialview.PartialCentralizedPromoOnGoingPromoView
import com.tokopedia.centralizedpromo.view.fragment.partialview.PartialCentralizedPromoPostView
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoViewModel
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.centralized_promo_partial_post.*
import kotlinx.android.synthetic.main.centralized_promo_partial_promo_creation.*
import kotlinx.android.synthetic.main.fragment_centralized_promo.*
import java.util.*
import javax.inject.Inject

class CentralizedPromoFragment : BaseDaggerFragment(), PartialCentralizedPromoOnGoingPromoView.RefreshButtonClickListener, CoachMarkListener {

    companion object {
        private const val TOAST_DURATION: Long = 5000

        private const val TAG_COACH_MARK = "CentralizedPromoCoachMark"

        private const val SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION = "onBoardingAdsAndPromotions"
        private const val SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION = "onBoardingPromoRecommendation"

        private const val ERROR_GET_LAYOUT_DATA = "Error when get layout data for %s."

        @JvmStatic
        fun createInstance(): CentralizedPromoFragment = CentralizedPromoFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val adapterTypeFactory by lazy {
        CentralizedPromoAdapterTypeFactory(
                { trackFreeShippingImpression() },
                { trackFreeShippingClick() }
        )
    }

    private val partialViews by lazy {
        return@lazy mapOf(
                LayoutType.ON_GOING_PROMO to createOnGoingPromoView(),
                LayoutType.PROMO_CREATION to createPromoRecommendationView(),
                LayoutType.POST to createPromoPostView()
        )
    }

    private val centralizedPromoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CentralizedPromoViewModel::class.java)
    }

    private val sharedPref by lazy { requireContext().getSharedPreferences("${this.javaClass.simpleName}.pref", Context.MODE_PRIVATE) }

    private val coachMark by lazy { createCoachMark() }

    private var isErrorToastShown: Boolean = false
    private var isCoachMarkShowed: Boolean = false

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_centralized_promo, container, false)
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

    override fun onViewReadyForCoachMark() {
        partialViews.forEach { if (it.value.showCoachMark && !it.value.readyToShowCoachMark) return }

        view?.post {
            showCoachMark()
        }
    }

    private fun createOnGoingPromoView() = PartialCentralizedPromoOnGoingPromoView(
            refreshButtonClickListener = this,
            view = container,
            adapterTypeFactory = adapterTypeFactory,
            coachMarkListener = this,
            showCoachMark = sharedPref.getBoolean(SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION, true))

    private fun createPromoRecommendationView() = PartialCentralizedPromoCreationView(
            view = layoutCentralizedPromoCreation,
            adapterTypeFactory = adapterTypeFactory,
            coachMarkListener = this,
            showCoachMark = sharedPref.getBoolean(SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION, true))

    private fun createPromoPostView() = PartialCentralizedPromoPostView(
            view = layoutCentralizedPromoPostList,
            adapterTypeFactory = adapterTypeFactory,
            coachMarkListener = this,
            showCoachMark = false)

    private fun setupView() {
        swipeRefreshLayout.setOnRefreshListener {
            refreshLayout()
        }
    }

    private fun refreshLayout() {
        partialViews.forEach { it.value.renderLoading() }
        getLayoutData(
                LayoutType.ON_GOING_PROMO,
                LayoutType.PROMO_CREATION,
                LayoutType.POST
        )
    }

    private fun getLayoutData(vararg layoutTypes: LayoutType) {
        centralizedPromoViewModel.getLayoutData(*layoutTypes)
    }

    private fun observeGetLayoutDataResult() {
        centralizedPromoViewModel.getLayoutResultLiveData.observe(viewLifecycleOwner, Observer {
            it.forEach { entry ->
                when (val value = entry.value) {
                    is Success -> value.onSuccessGetLayoutData<BaseUiModel, BasePartialView<BaseUiModel>>(entry.key)
                    is Fail -> value.onFailedGetLayoutData(entry.key)
                }
            }

            swipeRefreshLayout.isRefreshing = false
        })
    }

    private inline fun <reified D : BaseUiModel, reified V : BasePartialView<D>> Success<BaseUiModel>.onSuccessGetLayoutData(layoutType: LayoutType) {
        partialViews.forEach {
            if (it.key == layoutType) {
                (it.value as V).renderSuccess(data as D)
            }
        }
    }

    private fun Fail.onFailedGetLayoutData(layoutType: LayoutType) {
        SellerHomeErrorHandler.logExceptionToCrashlytics(throwable, String.format(ERROR_GET_LAYOUT_DATA, layoutType.name))
        partialViews[layoutType]?.renderError(this.throwable)
        showErrorToaster()
    }

    private fun showCoachMark() {
        if (!isCoachMarkShowed) {
            isCoachMarkShowed = true
            val coachMarkItem = ArrayList(partialViews.filter { it.value.shouldShowCoachMark() }
                    .map { it.value.getCoachMarkItem() }
                    .filterNotNull())

            coachMark.show(activity, TAG_COACH_MARK, coachMarkItem)
        }
    }

    private fun updateCoachMarkStatus(key: String) {
        sharedPref.edit().putBoolean(key, false).apply()
    }

    private fun createCoachMark(): CoachMark {
        val coachMark = CoachMarkBuilder().build()
        coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
            override fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean {
                when (coachMarkItem.title) {
                    getString(R.string.sh_coachmark_title_on_going_promo) -> updateCoachMarkStatus(SHARED_PREF_COACH_MARK_ON_GOING_PROMOTION)
                    getString(R.string.sh_coachmark_title_promo_creation) -> updateCoachMarkStatus(SHARED_PREF_COACH_MARK_PROMO_RECOMMENDATION)
                }
                return false
            }
        })

        return coachMark
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.build(this, context.getString(R.string.sah_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.sah_reload)
        ) {
            refreshLayout()
        }.show()

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    private fun trackFreeShippingImpression() {
        centralizedPromoViewModel.trackFreeShippingImpression()
    }

    private fun trackFreeShippingClick() {
        centralizedPromoViewModel.trackFreeShippingClick()
    }
}

interface CoachMarkListener {
    fun onViewReadyForCoachMark()
}