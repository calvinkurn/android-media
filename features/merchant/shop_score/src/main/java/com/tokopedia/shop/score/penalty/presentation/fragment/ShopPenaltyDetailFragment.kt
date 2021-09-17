package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.detail.PenaltyDetailStepperAdapter
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyStatusBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyDetailViewModel
import kotlinx.android.synthetic.main.fragment_penalty_detail.*
import kotlinx.android.synthetic.main.fragment_penalty_page.*
import javax.inject.Inject

class ShopPenaltyDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var shopPenaltyDetailViewModel: ShopPenaltyDetailViewModel

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    private val penaltyDetailStepperAdapter by lazy { PenaltyDetailStepperAdapter() }

    private var itemPenalty: ItemPenaltyUiModel? = null
    private var keyCacheManagerId = ""
    private var cacheManager: SaveInstanceCacheManager? = null

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            keyCacheManagerId = it.getStringExtra(KEY_CACHE_MANAGE_ID) ?: ""
        }
    }

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_penalty_detail, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        cacheManager?.put(KEY_ITEM_PENALTY_DETAIL, itemPenalty)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            )
            cacheManager = SaveInstanceCacheManager(it, keyCacheManagerId)
        }
        setupActionBar()
        if (itemPenalty == null) {
            itemPenalty = cacheManager?.get(KEY_ITEM_PENALTY_DETAIL, ItemPenaltyUiModel::class.java)
        }
        itemPenalty?.let { getPenaltyDetail(it) }
        observePenaltyDetailData()
    }

    private fun observePenaltyDetailData() {
        observe(shopPenaltyDetailViewModel.penaltyDetailData) {
            initDataView(it)
        }
    }

    private fun getPenaltyDetail(itemPenaltyUiModel: ItemPenaltyUiModel) {
        shopPenaltyDetailViewModel.getPenaltyDetailData(itemPenaltyUiModel)
    }

    private fun initDataView(shopPenaltyDetailUiModel: ShopPenaltyDetailUiModel) {
        tvTitleDetailPenalty?.text = shopPenaltyDetailUiModel.titleDetail
        tvStartDateDetailPenalty?.text =
            getString(R.string.date_penalty_detail, shopPenaltyDetailUiModel.startDateDetail)
        tvSummaryDetailPenalty?.text = shopPenaltyDetailUiModel.summaryDetail
        tv_total_deduction_point_penalty?.text = MethodChecker.fromHtml(
            getString(
                R.string.total_deduction_point_performance,
                shopPenaltyDetailUiModel.deductionPointPenalty
            )
        )
        tvEndDateDetailPenalty?.text = MethodChecker.fromHtml(
            getString(
                R.string.point_deduction_date_result_detail_penalty,
                shopPenaltyDetailUiModel.prefixDateDetail,
                shopPenaltyDetailUiModel.endDateDetail
            )
        )
        tvDescResultDetailPenalty?.text = shopPenaltyDetailUiModel.descStatusPenalty
        setupRvStepper(shopPenaltyDetailUiModel.stepperPenaltyDetailList)

        ic_info_status_penalty?.setOnClickListener {
            showStatusPenaltyBottomSheet()
        }

        btnCallHelpCenter?.setOnClickListener {
            RouteManager.route(
                context,
                ApplinkConstInternalGlobal.WEBVIEW,
                ShopScoreConstant.HELP_URL
            )
            shopScorePenaltyTracking.clickLearMoreHelpCenterPenaltyDetail()
        }

        if (btnCallHelpCenter?.isVisible == true) {
            shopScorePenaltyTracking.impressHelpCenterPenaltyDetail()
        }
    }

    private fun setupRvStepper(stepperList: List<ShopPenaltyDetailUiModel.StepperPenaltyDetail>) {
        val gridLayoutManager = GridLayoutManager(context, MAX_SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (stepperList.size == position + 1) {
                    SPAN_WIDTH_LAST_ITEM
                } else SPAN_WIDTH_DEFAULT
            }
        }
        rv_timeline_status_penalty?.apply {
            layoutManager = gridLayoutManager
            adapter = penaltyDetailStepperAdapter
        }
        penaltyDetailStepperAdapter.setStepperPenaltyDetail(stepperList)
    }

    private fun showStatusPenaltyBottomSheet() {
        val bottomSheet = PenaltyStatusBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager)
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(penalty_detail_toolbar)
            supportActionBar?.apply {
                title = getString(R.string.title_penalty_detail_shop_score)
            }
        }
    }

    companion object {
        const val KEY_ITEM_PENALTY_DETAIL = "key_item_penalty_detail"
        const val KEY_CACHE_MANAGE_ID = "extra_cache_manager_id"
        const val SPAN_WIDTH_DEFAULT = 2
        const val SPAN_WIDTH_LAST_ITEM = 1
        const val MAX_SPAN_COUNT = 5

        fun newInstance(): ShopPenaltyDetailFragment {
            return ShopPenaltyDetailFragment()
        }
    }
}