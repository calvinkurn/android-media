package com.tokopedia.topads.auto.view.sheet

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.FEEDBACK_FORM_PARAM
import com.tokopedia.topads.auto.databinding.TopadsAutopsFeedbackFormBinding
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val eventCategory = "feedback auto ps"
private const val eventAction = "click - simpan"
private const val trackerId = "47795"

class AutoPsFeedbackBottomSheet : BottomSheetUnify(), View.OnClickListener {

    private var binding: TopadsAutopsFeedbackFormBinding? = null
    private var rating = Int.ZERO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout(inflater: LayoutInflater, container: ViewGroup?) {
        binding = TopadsAutopsFeedbackFormBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        showCloseIcon = true
        isFullpage = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        binding?.feedback?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {updateSumbitBtnState()}
        })
    }

    private fun setOnClickListeners() {
        binding?.rating1?.setOnClickListener(this)
        binding?.rating2?.setOnClickListener(this)
        binding?.rating3?.setOnClickListener(this)
        binding?.rating4?.setOnClickListener(this)
        binding?.rating5?.setOnClickListener(this)
        binding?.checkbox1?.setOnClickListener(this)
        binding?.checkbox2?.setOnClickListener(this)
        binding?.checkbox3?.setOnClickListener(this)
        binding?.checkbox4?.setOnClickListener(this)
        binding?.checkbox5?.setOnClickListener(this)
        binding?.submitCta?.setOnClickListener(this)
        binding?.cancelCta?.setOnClickListener(this)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, FEEDBACK_FORM_PARAM)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding?.rating5?.id -> setRating(5)
            binding?.rating4?.id -> setRating(4)
            binding?.rating3?.id -> setRating(3)
            binding?.rating2?.id -> setRating(2)
            binding?.rating1?.id -> setRating(1)

            binding?.checkbox1?.id,
            binding?.checkbox2?.id,
            binding?.checkbox3?.id,
            binding?.checkbox4?.id,
            binding?.checkbox5?.id,
            -> updateSumbitBtnState()

            binding?.submitCta?.id -> {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCustomEvent(
                    eventCategory,
                    eventAction,
                    getEventLabel(),
                    trackerId
                )
                dismiss()
            }

            binding?.cancelCta?.id -> {
                dismiss()
            }
        }
    }

    private fun updateSumbitBtnState() {
        binding?.submitCta?.isEnabled =
            getCheckList().isNotEmpty() || rating > 0 || !(binding?.feedback?.editText?.text.isNullOrEmpty())
    }

    private fun getRatingDrawableList(): List<Drawable?> {
        val list = mutableListOf<Drawable?>()
        binding?.root?.context?.let {
            val starEmptyIcon = getIconUnifyDrawable(it, IconUnify.STAR)
            val starFilledIcon = getIconUnifyDrawable(it, IconUnify.STAR_FILLED)
            for (i in 1..5) {
                if (i <= rating)
                    list.add(starFilledIcon)
                else
                    list.add(starEmptyIcon)
            }
        }
        return list
    }

    private fun getRatingColorCodeList(): List<Int> {
        val list = mutableListOf<Int>()
        binding?.root?.context?.let {
            val colorFilled = ContextCompat.getColor(it, unifyprinciplesR.color.Unify_YN200)
            val emptyColor = ContextCompat.getColor(it, unifyprinciplesR.color.Unify_NN700)
            for (i in 1..5) {
                if (i <= rating)
                    list.add(colorFilled)
                else
                    list.add(emptyColor)
            }
        }
        return list
    }

    private fun getEventLabel(): String {
        return "checklist: ${getCheckList()}, rating: ${rating}, saran: ${binding?.feedback?.editText?.text}"
    }

    private fun getCheckList(): String {
        var checklist = String.EMPTY
        if (binding?.checkbox1?.isChecked == true)
            checklist += "checklist1, "
        if (binding?.checkbox2?.isChecked == true)
            checklist += "checklist2, "
        if (binding?.checkbox3?.isChecked == true)
            checklist += "checklist3, "
        if (binding?.checkbox4?.isChecked == true)
            checklist += "checklist4, "
        if (binding?.checkbox5?.isChecked == true)
            checklist += "checklist5"
        return checklist
    }

    private fun setRating(rating: Int) {
        this.rating = rating
        val drawableList = getRatingDrawableList()
        val colorList = getRatingColorCodeList()
        updateRatingDrawble(binding?.rating1, drawableList, colorList)
        updateRatingDrawble(binding?.rating2, drawableList, colorList)
        updateRatingDrawble(binding?.rating3, drawableList, colorList)
        updateRatingDrawble(binding?.rating4, drawableList, colorList)
        updateRatingDrawble(binding?.rating5, drawableList, colorList)
        updateSumbitBtnState()
    }

    private fun updateRatingDrawble(
        iconUnify: IconUnify?,
        drawableList: List<Drawable?>,
        colorList: List<Int>
    ) {
        iconUnify?.apply {
            val drawable = drawableList.getOrNull(tag.toString().toIntOrZero() - 1)
            val color = colorList.getOrNull(tag.toString().toIntOrZero() - 1)
            if (drawable != null && color != null) {
                setImageDrawable(drawable)
                applyIconUnifyColor(drawable, color)
            }
        }
    }
}
