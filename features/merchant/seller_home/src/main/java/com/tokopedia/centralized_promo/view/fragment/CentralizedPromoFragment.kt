package com.tokopedia.centralized_promo.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.centralized_promo.view.LayoutType
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.fragment.partialview.PartialCentralizedPromoOnGoingPromoView
import com.tokopedia.centralized_promo.view.fragment.partialview.PartialCentralizedPromoPostView
import com.tokopedia.centralized_promo.view.fragment.partialview.PartialCentralizedPromoRecommendationView
import com.tokopedia.centralized_promo.view.fragment.partialview.PartialView
import com.tokopedia.centralized_promo.view.model.BaseUiModel
import com.tokopedia.centralized_promo.view.viewmodel.CentralizedPromoViewModel
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_centralized_promo.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_post.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_recommendation.*
import java.util.ArrayList
import javax.inject.Inject

class CentralizedPromoFragment : BaseDaggerFragment(), PartialCentralizedPromoOnGoingPromoView.RefreshButtonClickListener {

    companion object {
        private const val TOAST_DURATION: Long = 5000

        private const val TAG_COACH_MARK = "CentralizedPromoCoachMark"

        private const val SHARED_PREF_COACH_MARK_ON_GOING_PROMOION = "onBoardingAdsAndPromotions"
        private const val SHARED_PREF_COACH_MARK_PROMOTION_CREATION = "onBoardingPromotionCreation"

        @JvmStatic
        fun createInstance(): CentralizedPromoFragment = CentralizedPromoFragment()
    }

    private val adapterTypeFactory by lazy { CentralizedPromoAdapterTypeFactory() }

    private val partialViews by lazy {
        return@lazy mapOf(
                LayoutType.ON_GOING_PROMO to PartialCentralizedPromoOnGoingPromoView(layoutCentralizedPromoOnGoingPromo, this, adapterTypeFactory),
                LayoutType.RECOMMENDED_PROMO to PartialCentralizedPromoRecommendationView(layoutCentralizedPromoRecommendation, adapterTypeFactory),
                LayoutType.POST to PartialCentralizedPromoPostView(layoutCentralizedPromoPostList, adapterTypeFactory)
        )
    }

    private var isErrorToastShown: Boolean = false
    private var sharedPref: SharedPreferences? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val centralizedPromoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CentralizedPromoViewModel::class.java)
    }

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

        context?.run {
            sharedPref = getSharedPreferences("${this.javaClass.simpleName}.pref", Context.MODE_PRIVATE)
            sharedPref?.run {
                edit().clear().apply()
            }
        }

        setupView()
        observeGetLayoutDataResult()
        refreshLayout()
    }

    override fun onRefreshButtonClicked() {
        getLayoutData(LayoutType.ON_GOING_PROMO)
    }

    private fun setupView() {
        swipeRefreshLayout.setOnRefreshListener {
            refreshLayout()
        }
    }

    private fun refreshLayout() {
        partialViews.forEach { it.value.onRefresh() }
        getLayoutData(
                LayoutType.ON_GOING_PROMO,
                LayoutType.RECOMMENDED_PROMO,
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
                    is Success -> value.onSuccessGetLayoutData<BaseUiModel, PartialView<BaseUiModel, BaseAdapterTypeFactory, Visitable<BaseAdapterTypeFactory>>>(entry.key)
                    is Fail -> value.onFailedGetLayoutData(entry.key)
                }
            }

            swipeRefreshLayout.isRefreshing = false
            view?.post {
                showCoachMark()
            }
        })
    }

    private inline fun <reified D : BaseUiModel, reified V : PartialView<D, BaseAdapterTypeFactory, Visitable<BaseAdapterTypeFactory>>> Success<BaseUiModel>.onSuccessGetLayoutData(layoutType: LayoutType) {
        partialViews.forEach {
            if (it.key == layoutType) {
                (it.value as V).renderData(data as D)
            }
        }
    }

    private fun Fail.onFailedGetLayoutData(layoutType: LayoutType) {
        partialViews[layoutType]?.renderError(this.throwable)
        showErrorToaster()
    }

    private fun showCoachMark() = sharedPref?.run {
        val coachMark = CoachMarkBuilder().build()
        val coachMarkItem = ArrayList<CoachMarkItem>()
        if (layoutCentralizedPromoOnGoingPromo.isShown && !getBoolean(SHARED_PREF_COACH_MARK_ON_GOING_PROMOION, false)) {
            coachMarkItem.add(CoachMarkItem(layoutCentralizedPromoOnGoingPromo,
                    getString(R.string.sh_coachmark_title_on_going_promo),
                    getString(R.string.sh_coachmark_desc_on_going_promo)))
            edit().putBoolean(SHARED_PREF_COACH_MARK_ON_GOING_PROMOION, true).apply()
        }
        if (layoutCentralizedPromoRecommendation.isShown && !getBoolean(SHARED_PREF_COACH_MARK_PROMOTION_CREATION, false)) {
            coachMarkItem.add(CoachMarkItem(layoutCentralizedPromoRecommendation,
                    getString(R.string.sh_coachmark_title_promo_creation),
                    getString(R.string.sh_coachmark_desc_promo_creation)))
            edit().putBoolean(SHARED_PREF_COACH_MARK_PROMOTION_CREATION, true).apply()
        }
        coachMark.show(activity, TAG_COACH_MARK, coachMarkItem)
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.make(this, context.getString(R.string.sah_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.sah_reload),
                View.OnClickListener {
                    refreshLayout()
                }
        )

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }
}