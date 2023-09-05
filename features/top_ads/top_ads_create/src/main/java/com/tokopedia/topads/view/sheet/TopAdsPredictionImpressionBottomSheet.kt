package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topads.create.databinding.TopadsImpressionPredictionBottomSheetLayoutBinding

class TopAdsPredictionImpressionBottomSheet : BottomSheetUnify() {

    private var descriptionTypography: Typography? = null
    private var description: String = ""

    companion object {
        fun newInstance(searchPrediction: Int, recomPrediction: Int, totalPrediction: Int): TopAdsPredictionImpressionBottomSheet {
            val bundle = Bundle()
            bundle.putInt("searchPrediction", searchPrediction)
            bundle.putInt("recomPrediction", recomPrediction)
            bundle.putInt("totalPrediction", totalPrediction)
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
//        descriptionTypography = contentView?.findViewById(R.id.toolTipDescription)
        descriptionTypography?.text = description
        this.setTitle("Potensi tampil")
    }


    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, "")
    }

    fun setDescription(description: String) {
        this.description = description
    }
}
