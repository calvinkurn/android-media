package com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.di.DaggerSearchCategoryComponent
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.AddFeedbackViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowProductFeedbackBottomSheet : BottomSheetUnify() {

    private var inputArea:TextAreaUnify2?=null
    private var feedbackCta:UnifyButton?=null

    private var callingParentView:View?=null

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory).get(AddFeedbackViewModel::class.java)
    }

    private var coordinatorLayout:CoordinatorLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        isKeyboardOverlap = false
        super.onCreate(savedInstanceState)
        DaggerSearchCategoryComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun initView(inflater: LayoutInflater,container: ViewGroup?){
        val view = inflater.inflate(R.layout.bottomsheet_tokopedianow_product_feedback,container)
        coordinatorLayout = view.findViewById(R.id.feedback_bottomsheet_cl)
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

    private fun observeViewModel(){
        viewModel.addFeedbackResult.observe(viewLifecycleOwner){
            when(it){
                is Success -> {
                    dismiss()
                    setSuccessToast()
                }
                is Fail -> {
                    setErrorToast()
                }
            }
        }
    }

    private fun setupCta(){
        disableCta()
        feedbackCta?.setOnClickListener {
            val feedback = inputArea?.editText?.text
            feedback?.let { it1 ->
                feedbackCta?.isLoading = true
                viewModel.addProductFeedback(it1.toString())
            }
        }
    }

    private fun disableCta(){
        feedbackCta?.isEnabled = false
    }

    private fun enableCta(){
        feedbackCta?.isEnabled = true
    }

    fun showBottomSheet(manager: FragmentManager?,view:View?){
        callingParentView = view
        manager?.let { show(it, TokoNowProductFeedbackBottomSheet::class.simpleName) }
    }

    private fun setSuccessToast(){
        feedbackCta?.isLoading = false
        callingParentView?.let {
            val successText = context?.resources?.getString(R.string.tokopedianow_add_feedback_success_text).orEmpty()
            val actionText = context?.resources?.getString(R.string.tokopedianow_on_boarding_step_button_text_last_child).orEmpty()
            Toaster.build(
                view = it,
                text = successText,
                duration = Toaster.LENGTH_LONG,
                actionText = actionText
                ).show()
        }
    }

    private fun setErrorToast(){
        feedbackCta?.isLoading = false
        coordinatorLayout?.let {
            val successText = context?.resources?.getString(R.string.tokopedianow_add_feedback_failure_text).orEmpty()
            val actionText = context?.resources?.getString(R.string.tokopedianow_on_boarding_step_button_text_last_child).orEmpty()
            Toaster.build(
                view = it,
                text = successText,
                duration = Toaster.LENGTH_LONG,
                actionText = actionText,
                type = Toaster.TYPE_ERROR
            ).show()
        }
    }

    companion object{
        private const val INPUT_LIMIT = 144
    }
}
