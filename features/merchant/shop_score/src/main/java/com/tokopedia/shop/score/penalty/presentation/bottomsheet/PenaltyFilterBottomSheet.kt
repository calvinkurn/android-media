package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class PenaltyFilterBottomSheet: BaseBottomSheetShopScore() {

    @Inject
    lateinit var shopPenaltyViewModel: ShopPenaltyViewModel

    private var isApplyFilter = false
    private var rvPenaltyFilter: RecyclerView? = null
    private var btnShowPenalty: UnifyButton? = null

    override fun getLayoutResId(): Int = R.layout.bottomsheet_filter_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_filter_bottom_sheet)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PENALTY_FILTER_BOTTOM_SHEET_TAG)
            }
        }
    }

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFullpage = true
        showKnob = true
        isDragable = true
        initView(view)
    }

    private fun clickBtnApplied() {
        btnShowPenalty?.apply {

            dismissAllowingStateLoss()
        }
    }

    private fun initView(view: View) {
        rvPenaltyFilter = view.findViewById(R.id.rvPenaltyFilter)
        btnShowPenalty = view.findViewById(R.id.btnShowPenalty)
    }

    companion object {
        const val PENALTY_FILTER_BOTTOM_SHEET_TAG = "PenaltyFilterBottomSheetTag"
    }

}