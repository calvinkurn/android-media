package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.constants.ConstantTopAdsCreate.RECOM_PREDICTION
import com.tokopedia.topads.constants.ConstantTopAdsCreate.SEARCH_PREDICTION
import com.tokopedia.topads.constants.ConstantTopAdsCreate.TOTAL_PREDICTION
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topads.create.databinding.TopadsImpressionPredictionBottomSheetLayoutBinding
import com.tokopedia.topads.create.R

class TopAdsPredictionImpressionBottomSheet : BottomSheetUnify() {

    private var descriptionTypography: Typography? = null
    private var description: String = String.EMPTY

    companion object {
        fun newInstance(searchPrediction: Int, recomPrediction: Int, totalPrediction: Int): TopAdsPredictionImpressionBottomSheet {
            val bundle = Bundle()
            bundle.putInt(SEARCH_PREDICTION, searchPrediction)
            bundle.putInt(RECOM_PREDICTION, recomPrediction)
            bundle.putInt(TOTAL_PREDICTION, totalPrediction)
            return TopAdsPredictionImpressionBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val childView = TopadsImpressionPredictionBottomSheetLayoutBinding.inflate(LayoutInflater.from(activity))
        setChild(childView.root)
        showCloseIcon = true
        descriptionTypography?.text = description
        this.setTitle(getString(R.string.topads_ads_performance_item_title))
    }


    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, String.EMPTY)
    }

    fun setDescription(description: String) {
        this.description = description
    }
}
