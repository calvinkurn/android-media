package com.tokopedia.updateinactivephone.features.submitnewphone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneDataUploadBinding
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

abstract class BaseInactivePhoneSubmitDataFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneDataUploadViewModel::class.java) }

    @Inject
    lateinit var trackerRegular: InactivePhoneTracker

    @Inject
    lateinit var trackerWithPin: InactivePhoneWithPinTracker

    var viewBinding by autoClearedNullable<FragmentInactivePhoneDataUploadBinding>()
    var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    protected abstract fun initObserver()
    protected abstract fun onSubmit()
    protected abstract fun dialogOnBackPressed()
    protected abstract fun initView()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.application?.let {
            InactivePhoneComponentBuilder
                .getComponent(it)
                .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentInactivePhoneDataUploadBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inactivePhoneUserDataModel = arguments?.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()

        viewBinding?.buttonNext?.setOnClickListener {
            onSubmit()
        }
    }

    open fun gotoSuccessPage(source: String) {
        activity?.let {
            val intent = InactivePhoneSuccessPageActivity.createIntent(it, source, inactivePhoneUserDataModel)
            startActivity(intent)
            it.finish()
        }
    }

    fun isPhoneValid(): Boolean {
        val phoneNumber = viewBinding?.textPhoneNumber?.text.toString()
        when {
            phoneNumber.isEmpty() -> {
                viewBinding?.textPhoneNumber?.error = getString(R.string.text_form_error_empty)
                return false
            }
            phoneNumber.length < 9 -> {
                viewBinding?.textPhoneNumber?.error = getString(R.string.text_form_error_min_9_digit)
                return false
            }
        }

        viewBinding?.textPhoneNumber?.clearErrorMessage()
        return true
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(viewBinding?.textPhoneNumber?.windowToken, 0)
    }

    fun showThumbnailLayout() {
        viewBinding?.layoutThumbnailDataUpload?.show()
    }

    fun hideThumbnailLayout() {
        viewBinding?.layoutThumbnailDataUpload?.hide()
    }

    open fun showLoading() {
        viewBinding?.loader?.show()
    }

    open fun hideLoading() {
        viewBinding?.loader?.hide()
    }
}