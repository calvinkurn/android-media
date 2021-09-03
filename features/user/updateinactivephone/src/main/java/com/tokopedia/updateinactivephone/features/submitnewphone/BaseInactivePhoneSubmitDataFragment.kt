package com.tokopedia.updateinactivephone.features.submitnewphone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.view.PhoneNumberView
import com.tokopedia.updateinactivephone.common.view.ThumbnailFileView
import com.tokopedia.updateinactivephone.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity
import javax.inject.Inject

abstract class BaseInactivePhoneSubmitDataFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneDataUploadViewModel::class.java) }

    var tracker = InactivePhoneTracker()
    var thumbnailIdCard: ThumbnailFileView? = null
    var thumbnailSelfie: ThumbnailFileView? = null
    var thumbnailSavingBook: ThumbnailFileView? = null
    var textPhoneNumber: PhoneNumberView? = null
    var buttonSubmit: UnifyButton? = null
    var layoutThumbnail: ConstraintLayout? = null
    var loader: ConstraintLayout? = null

    var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    protected abstract fun initObserver()
    protected abstract fun onSubmit()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerInactivePhoneComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .inactivePhoneModule(InactivePhoneModule(requireContext()))
            .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_data_upload, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inactivePhoneUserDataModel = arguments?.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    open fun initView() {
        thumbnailIdCard = view?.findViewById(R.id.imgIdCard)
        thumbnailSelfie = view?.findViewById(R.id.imgSelfie)
        thumbnailSavingBook = view?.findViewById(R.id.imgSavingBook)
        textPhoneNumber = view?.findViewById(R.id.textPhoneNumber)
        buttonSubmit = view?.findViewById(R.id.btnNext)
        layoutThumbnail = view?.findViewById(R.id.layoutThumbnailDataUpload)
        loader = view?.findViewById(R.id.loader)

        buttonSubmit?.setOnClickListener {
            onSubmit()
        }
    }

    fun gotoSuccessPage() {
        activity?.let {
            val intent = InactivePhoneSuccessPageActivity.createIntent(it)
            intent.putExtras(Bundle().apply {
                putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
            })
            startActivity(intent)
            it.finish()
        }
    }

    fun isPhoneValid(): Boolean {
        val phoneNumber = textPhoneNumber?.text.toString()
        when {
            phoneNumber.isEmpty() -> {
                textPhoneNumber?.error = getString(R.string.text_form_error_empty)
                return false
            }
            phoneNumber.length < 9 -> {
                textPhoneNumber?.error = getString(R.string.text_form_error_min_9_digit)
                return false
            }
        }

        return true
    }

     fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(textPhoneNumber?.windowToken, 0)
    }

    fun showThumbnailLayout() {
        layoutThumbnail?.show()
    }

    fun hideThumbnailLayout() {
        layoutThumbnail?.hide()
    }

    fun showLoading() {
        loader?.show()
    }

    fun hideLoading() {
        loader?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}