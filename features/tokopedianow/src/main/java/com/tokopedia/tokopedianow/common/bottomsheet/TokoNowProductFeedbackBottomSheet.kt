package com.tokopedia.tokopedianow.common.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.tokopedianow.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.UnifyButton

class TokoNowProductFeedbackBottomSheet : BottomSheetUnify() {

    private var inputArea:TextAreaUnify2?=null
    private var feedbackCta:UnifyButton?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(inflater: LayoutInflater,container: ViewGroup?){
        val view = inflater.inflate(R.layout.bottomsheet_tokopedianow_product_feedback,container)
        inputArea = view.findViewById(R.id.feedback_bm_textfield)
        feedbackCta = view.findViewById(R.id.feedback_bm_cta)
        setupInputArea()
        setupCta()
        val title = context?.resources?.getString(R.string.tokopedianow_feedback_bottomsheet_title).orEmpty()
        setTitle(title)
        setChild(view)
    }

    private fun setupInputArea(){
        inputArea?.apply {
            setMessage(context?.resources?.getString(R.string.tokopedianow_feedback_bottomsheet_input_message).orEmpty())
            minLine = 3
            setCounter(INPUT_LIMIT)
            editText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(feedbackCta?.isEnabled.orTrue()){
                        if(text==null || text.length<=4)
                            disableCta()
                    }
                    else if(text!=null && text.length>4)
                        enableCta()
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            val hintColor = ResourcesCompat.getColor(resources,com.tokopedia.unifyprinciples.R.color.Unify_NN400,null)
            editText.setHintTextColor(hintColor)
        }
    }

    private fun setupCta(){
        disableCta()
        feedbackCta?.setOnClickListener {}
    }

    private fun disableCta(){
        feedbackCta?.isEnabled = false
    }

    private fun enableCta(){
        feedbackCta?.isEnabled = true
    }

    fun showBottomSheet(manager: FragmentManager?){
        manager?.let { show(it,TokoNowProductFeedbackBottomSheet::class.simpleName) }
    }

    companion object{
        private const val INPUT_LIMIT = 144
    }
}
