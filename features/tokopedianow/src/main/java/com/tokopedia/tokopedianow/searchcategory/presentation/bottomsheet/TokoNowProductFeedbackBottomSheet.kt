package com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowProductFeedbackBinding
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTracker
import com.tokopedia.tokopedianow.searchcategory.di.ContextModule
import com.tokopedia.tokopedianow.searchcategory.di.DaggerSearchCategoryComponent
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.AddFeedbackViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class TokoNowProductFeedbackBottomSheet : BottomSheetUnify() {

    companion object{
        private const val INPUT_LIMIT = 70
        private const val MIN_TEXT_LEN = 4
        private const val TOAST_BOTTOM_MARGIN = 8
    }

    @Inject
    lateinit var userSession:UserSessionInterface

    private var binding:BottomsheetTokopedianowProductFeedbackBinding? = null

    private var callingParentView:View?=null
    private var trackerData:Triple<String,String,Boolean>?=null

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory).get(AddFeedbackViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        isKeyboardOverlap = false
        super.onCreate(savedInstanceState)
        DaggerSearchCategoryComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .contextModule(ContextModule(requireContext()))
            .build()
            .inject(this)
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
        trackerData?.let {
            ProductFeedbackLoopTracker.sendImpressionFeedbackSheet(
                it.first,
                it.second,
                it.third
            )
            setCloseClickListener { _ ->
                ProductFeedbackLoopTracker.sendCloseFeedbackSheet(
                    it.first,
                    it.second,
                    it.third
                )
                dismiss()
            }
        }
        observeViewModel()
    }

    private fun initView(inflater: LayoutInflater,container: ViewGroup?){
        binding = BottomsheetTokopedianowProductFeedbackBinding.inflate(inflater,container,false)
        setupInputArea()
        setupCta()
        val title = context?.resources?.getString(R.string.tokopedianow_feedback_bottomsheet_title).orEmpty()
        setTitle(title)
        setChild(binding?.root)
    }

    private fun setupInputArea(){
        binding?.feedbackBmTextfield?.apply {
            setMessage(context?.resources?.getString(R.string.tokopedianow_feedback_bottomsheet_input_message).orEmpty())
            minLine = 3
            setCounter(INPUT_LIMIT)
            editText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(binding?.feedbackBmCta?.isEnabled.orTrue()){
                        if(text==null || text.length<= MIN_TEXT_LEN)
                            disableCta()
                    }
                    else if(text!=null && text.length> MIN_TEXT_LEN)
                        enableCta()
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            val hintColor = ResourcesCompat.getColor(resources,com.tokopedia.unifyprinciples.R.color.Unify_NN400,null)
            textInputLayout.isHintEnabled = false
            textInputLayout.isHintAnimationEnabled = false
            editText.hint = context?.resources?.getString(R.string.tokopedianow_feedback_bottomsheet_input_hint)
            editText.setHintTextColor(hintColor)
            setupInputFocusListener(editText)
        }
    }

    private fun setupInputFocusListener(inputField:AutoCompleteTextView){
        inputField.setOnFocusChangeListener { _ , focus ->
            if(focus){
                trackerData?.let {
                    ProductFeedbackLoopTracker.sendClickInputFeedbackSheet(
                        it.first,
                        it.second,
                        it.third
                    )
                }
            }
        }
    }

    private fun observeViewModel(){
        viewModel.addFeedbackResult.observe(viewLifecycleOwner){
            when(it){
                is Success -> {
                    dismiss()
                    setSuccessToast()
                    trackerData?.let { it1 ->
                        ProductFeedbackLoopTracker.sendImpressionSuccessToastFeedbackSheet(
                            it1.first,
                            it1.second,
                            getUserInput(),
                            it1.third
                        )
                    }
                }
                is Fail -> {
                    setErrorToast()
                    trackerData?.let {it1 ->
                        ProductFeedbackLoopTracker.sendImpressionFailureToastFeedbackSheet(
                            it1.first,
                            it1.second,
                            getUserInput(),
                            it1.third
                        )
                    }
                }
            }
        }
    }

    private fun setupCta(){
        disableCta()
        binding?.feedbackBmCta?.setOnClickListener {
            trackerData?.let {
                ProductFeedbackLoopTracker.sendClickCtaFeedbackSheet(
                    it.first,
                    it.second,
                    getUserInput(),
                    it.third
                )
            }
            isLoggedIn {
                binding?.feedbackBmCta?.isLoading = true
                viewModel.addProductFeedback(getUserInput())
            }
        }
    }

    private fun disableCta(){
        binding?.feedbackBmCta?.isEnabled = false
    }

    private fun enableCta(){
        binding?.feedbackBmCta?.isEnabled = true
    }

    fun showBottomSheet(manager: FragmentManager?,view:View?,trackerData:Triple<String,String,Boolean>? = null){
        callingParentView = view
        this.trackerData = trackerData
        manager?.let { show(it, TokoNowProductFeedbackBottomSheet::class.simpleName) }
    }

    private fun setSuccessToast(){
        binding?.feedbackBmCta?.isLoading = false
        callingParentView?.let {
            val successText = context?.resources?.getString(R.string.tokopedianow_add_feedback_success_text).orEmpty()
            val actionText = context?.resources?.getString(R.string.tokopedianow_on_boarding_step_button_text_last_child).orEmpty()
            Toaster.build(
                view = it,
                text = successText,
                duration = Toaster.LENGTH_LONG,
                actionText = actionText,
                clickListener = {sendToasterClickEvent(true)}
                ).show()
        }
    }

    private fun setErrorToast(){
        binding?.feedbackBmCta?.isLoading = false
        binding?.feedbackBmCta?.let {
            val root = view?.rootView
            val errorText = context?.resources?.getString(R.string.tokopedianow_add_feedback_failure_text).orEmpty()
            val actionText = context?.resources?.getString(R.string.tokopedianow_on_boarding_step_button_text_last_child).orEmpty()
            Toaster.toasterCustomBottomHeight = getNavigationBarHeight() - TOAST_BOTTOM_MARGIN.toPx()
            root?.let { it1 ->
               Toaster.build(
                    view = it1,
                    text = errorText,
                    duration = Toaster.LENGTH_LONG,
                    actionText = actionText,
                    type = Toaster.TYPE_ERROR,
                    clickListener = {sendToasterClickEvent(false)}
                ).show()
            }
        }
    }

    private fun sendToasterClickEvent(success:Boolean){
        trackerData?.let {
            if(success){
                ProductFeedbackLoopTracker.sendClickSuccessToastOkFeedbackSheet(
                    it.first,
                    it.second,
                    getUserInput(),
                    it.third
                )
            }
            else{
                ProductFeedbackLoopTracker.sendClickFailureToastOkFeedbackSheet(
                    it.first,
                    it.second,
                    getUserInput(),
                    it.third
                )
            }
        }
    }

    private fun getUserInput() : String = binding?.feedbackBmTextfield?.editText?.text.toString()

    private fun getNavigationBarHeight() : Int{
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        val resources = context?.resources
        val resourceId:Int = resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
        return if (resourceId > 0) {
            resources?.getDimensionPixelSize(resourceId) ?: 0
        } else 0
    }

    private fun isLoggedIn(block:() -> Unit){
        if(userSession.isLoggedIn){
            block.invoke()
        }
        else{
            activity?.finish()
            RouteManager.route(context, ApplinkConstInternalUserPlatform.LOGIN)
        }
    }

}
