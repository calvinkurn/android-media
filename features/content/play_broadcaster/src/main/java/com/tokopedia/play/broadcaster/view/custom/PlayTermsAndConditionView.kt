package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.NestedScrollView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroadcastTncBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.DefaultVerticalItemDecoration
import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.ui.model.tnc.TermsAndConditionBenefitUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.tnc.PlayTermsAndConditionAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.tnc.PlayTermsAndConditionBenefitAdapter

/**
 * Created by jegul on 04/10/21
 */
class PlayTermsAndConditionView : NestedScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: ViewPlayBroadcastTncBinding = ViewPlayBroadcastTncBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val tncAdapter = PlayTermsAndConditionAdapter()
    private val tncBenefitAdapter = PlayTermsAndConditionBenefitAdapter()

    private var mListener: Listener? = null

    init {
        setupView()
    }

    private fun setupView() {
        binding.rvTnc.adapter = tncAdapter
        binding.rvTncBenefit.adapter = tncBenefitAdapter

        binding.rvTnc.addItemDecoration(
            DefaultVerticalItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.play_dp_12)
            )
        )
        binding.rvTncBenefit.addItemDecoration(
            DefaultVerticalItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.play_dp_12)
            )
        )

        tncBenefitAdapter.setItemsAndAnimateChanges(getBenefits())

        binding.btnOk.setOnClickListener {
            mListener?.onOkButtonClicked(this)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTermsAndConditions(tncList: List<TermsAndConditionUiModel>) {
        tncAdapter.setItems(tncList)
        if (tncList.isEmpty()) {
            binding.rvTnc.visibility = View.GONE
            binding.tvTncConditionTitle.visibility = View.GONE
            binding.dividerTnc.visibility = View.GONE
        } else {
            binding.rvTnc.visibility = View.VISIBLE
            binding.tvTncConditionTitle.visibility = View.VISIBLE
            binding.dividerTnc.visibility = View.VISIBLE
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    private fun getBenefits(): List<TermsAndConditionBenefitUiModel> {
        return listOf(
            TermsAndConditionBenefitUiModel(
                IconUnify.SHOP_FAVORITE,
                R.string.play_bro_tnc_benefit_1,
            ),
            TermsAndConditionBenefitUiModel(
                IconUnify.USER_SUCCESS,
                R.string.play_bro_tnc_benefit_2,
            ),
            TermsAndConditionBenefitUiModel(
                IconUnify.GRAPH,
                R.string.play_bro_tnc_benefit_3,
            )
        )
    }

    interface Listener {

        fun onOkButtonClicked(view: PlayTermsAndConditionView)
    }
}